package kr.aling.user.band.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import kr.aling.user.band.dto.response.ExistsBandNameResponseDto;
import kr.aling.user.band.dto.response.GetBandDetailInfoResponseDto;
import kr.aling.user.band.dto.response.GetBandInfoResponseDto;
import kr.aling.user.band.dto.response.GetBandInfoWithBandUserResponseDto;
import kr.aling.user.band.dto.response.external.GetBandPostTypeResponseDto;
import kr.aling.user.band.entity.Band;
import kr.aling.user.band.exception.BandNotFoundException;
import kr.aling.user.band.repository.BandReadRepository;
import kr.aling.user.band.service.impl.BandReadServiceImpl;
import kr.aling.user.banduser.dto.response.GetBandUserInfoResponseDto;
import kr.aling.user.banduser.repository.BandUserReadRepository;
import kr.aling.user.common.dto.PageResponseDto;
import kr.aling.user.common.feignclient.PostFeignClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * 그룹 조회 서비스 테스트.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@ExtendWith(SpringExtension.class)
class BandReadServiceImplTest {

    GetBandInfoResponseDto getBandInfoResponseDto;
    GetBandUserInfoResponseDto getBandUserInfoResponseDto;
    GetBandDetailInfoResponseDto getBandDetailInfoResponseDto;
    GetBandPostTypeResponseDto getBandPostTypeResponseDto;
    @InjectMocks
    private BandReadServiceImpl bandReadService;
    @Mock
    private BandReadRepository bandReadRepository;
    @Mock
    private BandUserReadRepository bandUserReadRepository;
    @Mock
    private PostFeignClient postFeignClient;

    @BeforeEach
    void setUp() {
        getBandInfoResponseDto = new GetBandInfoResponseDto(1L, "band name", 1L, "band information", true, true, false);
        getBandUserInfoResponseDto = new GetBandUserInfoResponseDto(1L, 1);
        getBandDetailInfoResponseDto =
                new GetBandDetailInfoResponseDto(1L, "band name", 1L, "information", true, true, true, 1L, 1);
        getBandPostTypeResponseDto = new GetBandPostTypeResponseDto();
    }

    @Test
    @DisplayName("그룹명 중복 검사 성공")
    void existBandName_successTest() {
        // given
        String bandName = "testBandName";
        boolean isExists = false;

        // when
        when(bandReadRepository.existsBandByName(any())).thenReturn(isExists);

        // then
        ExistsBandNameResponseDto existsBandNameResponseDto = bandReadService.existBandName(bandName);

        assertThat(existsBandNameResponseDto).isNotNull();
        assertThat(existsBandNameResponseDto.getIsExist()).isEqualTo(isExists);

        verify(bandReadRepository, times(1)).existsBandByName(any());
    }

    @Test
    @DisplayName("그룹명으로 상세 정보 조회 서비스 성공 테스트")
    void bandDetailInfo_success_test() {
        // given
        String bandName = "bandName";
        Long userNo = 1L;

        // when
        when(bandReadRepository.getBandDetailInfoByBandName(anyString())).thenReturn(
                Optional.of(getBandInfoResponseDto));
        when(bandUserReadRepository.getBandUserInfoByUserNoAndBandName(anyString(), anyLong())).thenReturn(
                Optional.of(getBandUserInfoResponseDto));

        // then
        GetBandInfoWithBandUserResponseDto bandDetailInfo = bandReadService.getBandDetailInfo(bandName, userNo);

        assertThat(bandDetailInfo).isNotNull();
        assertThat(bandDetailInfo.getBandInfo().getBandNo()).isEqualTo(getBandInfoResponseDto.getBandNo());
        assertThat(bandDetailInfo.getBandInfo().getName()).isEqualTo(getBandInfoResponseDto.getName());
        assertThat(bandDetailInfo.getBandInfo().getInfo()).isEqualTo(getBandInfoResponseDto.getInfo());
        assertThat(bandDetailInfo.getBandInfo().getFileNo()).isEqualTo(getBandInfoResponseDto.getFileNo());
        assertThat(bandDetailInfo.getBandInfo().getIsEnter()).isEqualTo(getBandInfoResponseDto.getIsEnter());
        assertThat(bandDetailInfo.getBandInfo().getIsViewContent()).isEqualTo(
                getBandInfoResponseDto.getIsViewContent());
        assertThat(bandDetailInfo.getBandInfo().getIsUpload()).isEqualTo(getBandInfoResponseDto.getIsUpload());
        assertThat(bandDetailInfo.getBandUserInfo().getBandUserNo()).isEqualTo(
                getBandUserInfoResponseDto.getBandUserNo());
        assertThat(bandDetailInfo.getBandUserInfo().getBandUserRoleNo()).isEqualTo(
                getBandUserInfoResponseDto.getBandUserRoleNo());

        verify(bandReadRepository, times(1)).getBandDetailInfoByBandName(anyString());
        verify(bandUserReadRepository, times(1)).getBandUserInfoByUserNoAndBandName(anyString(), anyLong());
    }

    @Test
    @DisplayName("그룹명으로 상세 정보 조회 서비스 실패 테스트 - 없는 그룹 이름")
    void bandDetailInfo_fail_test_notFound_bandName() {
        // given
        String bandName = "noBand";
        Long userNo = 1L;

        // when
        when(bandReadRepository.getBandDetailInfoByBandName(anyString())).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> bandReadService.getBandDetailInfo(bandName, userNo))
                .isInstanceOf(BandNotFoundException.class)
                .hasMessage(BandNotFoundException.MESSAGE);

        verify(bandReadRepository, times(1)).getBandDetailInfoByBandName(anyString());
    }

    @Test
    @DisplayName("그룹 명으로 그룹 검색 성공 테스트")
    void getSearchBandInfoList_successTest() {
        // given
        String bandName = "bandName";
        Pageable pageable = PageRequest.of(0, 10);
        List<GetBandInfoResponseDto> list = List.of(getBandInfoResponseDto);

        Page<GetBandInfoResponseDto> page = PageableExecutionUtils.getPage(list, pageable, list::size);

        // when
        when(bandReadRepository.getSearchBandInfoListByBandName(bandName, pageable)).thenReturn(page);

        // then
        PageResponseDto<GetBandInfoResponseDto> result = bandReadService.getSearchBandInfoList(bandName, pageable);

        assertThat(result).isNotNull();
        assertThat(result.getPageNumber()).isEqualTo(pageable.getPageNumber());
        assertThat(result.getTotalPages()).isEqualTo(list.size());
        assertThat(result.getTotalElements()).isEqualTo(list.size());
        assertThat(result.getContent().get(0).getBandNo()).isEqualTo(list.get(0).getBandNo());
        assertThat(result.getContent().get(0).getName()).isEqualTo(list.get(0).getName());
        assertThat(result.getContent().get(0).getFileNo()).isEqualTo(list.get(0).getFileNo());
        assertThat(result.getContent().get(0).getInfo()).isEqualTo(list.get(0).getInfo());
        assertThat(result.getContent().get(0).getIsEnter()).isEqualTo(list.get(0).getIsEnter());
        assertThat(result.getContent().get(0).getIsViewContent()).isEqualTo(list.get(0).getIsViewContent());
        assertThat(result.getContent().get(0).getIsUpload()).isEqualTo(list.get(0).getIsUpload());

        verify(bandReadRepository, times(1)).getSearchBandInfoListByBandName(bandName, pageable);
    }

    @Test
    @DisplayName("회원 별 가입한 그룹 정보 목록 조회 성공 테스트")
    void getJoinedBandInfoList_success_test() {
        // given
        List<GetBandDetailInfoResponseDto> list = List.of(getBandDetailInfoResponseDto);

        // when
        when(bandReadRepository.getJoinedBandInfoListByUserNo(anyLong())).thenReturn(list);

        // then
        List<GetBandDetailInfoResponseDto> result = bandReadService.getJoinedBandInfoList(anyLong());

        assertThat(result).isNotNull();
        assertThat(result.get(0).getBandNo()).isEqualTo(list.get(0).getBandNo());
        assertThat(result.get(0).getName()).isEqualTo(list.get(0).getName());
        assertThat(result.get(0).getFileNo()).isEqualTo(list.get(0).getFileNo());
        assertThat(result.get(0).getInfo()).isEqualTo(list.get(0).getInfo());
        assertThat(result.get(0).getIsEnter()).isEqualTo(list.get(0).getIsEnter());
        assertThat(result.get(0).getIsViewContent()).isEqualTo(list.get(0).getIsViewContent());
        assertThat(result.get(0).getIsUpload()).isEqualTo(list.get(0).getIsUpload());
        assertThat(result.get(0).getBandUserNo()).isEqualTo(list.get(0).getBandUserNo());
        assertThat(result.get(0).getBandUserRoleNo()).isEqualTo(list.get(0).getBandUserRoleNo());

        verify(bandReadRepository, times(1)).getJoinedBandInfoListByUserNo(anyLong());
    }

    @Test
    @DisplayName("특정 그룹의 그룹 게시글 분류 조회 성공 테스트")
    void getBandPostTypeList_success_test() {
        // given
        String bandName = "bandName";

        ReflectionTestUtils.setField(getBandPostTypeResponseDto, "name", "typeName");

        // when
        when(bandReadRepository.findByName(anyString())).thenReturn(Optional.ofNullable(mock(Band.class)));
        when(postFeignClient.requestGetBandPostTypeList(anyLong())).thenReturn(List.of(getBandPostTypeResponseDto));

        // then
        List<GetBandPostTypeResponseDto> bandPostTypeList = bandReadService.getBandPostTypeList(bandName);

        assertThat(bandPostTypeList.get(0).getName()).isEqualTo(getBandPostTypeResponseDto.getName());

        verify(bandReadRepository, times(1)).findByName(anyString());
        verify(postFeignClient, times(1)).requestGetBandPostTypeList(anyLong());
    }

    @Test
    @DisplayName("특정 그룹의 그룹 게시글 분류 조회 실패 테스트")
    void getBandPostTypeList_fail_test() {
        // given
        String bandName = "bandName";

        // when
        when(bandReadRepository.findByName(anyString())).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> bandReadService.getBandPostTypeList(bandName))
                .isInstanceOf(BandNotFoundException.class)
                .hasMessage(BandNotFoundException.MESSAGE);
    }

}