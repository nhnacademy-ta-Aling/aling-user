package kr.aling.user.banduser.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import kr.aling.user.banduser.dto.response.BandPostUerQueryDto;
import kr.aling.user.banduser.dto.response.GetBandUserAndUserInfoResponseDto;
import kr.aling.user.banduser.dto.response.GetBandUserAuthResponseDto;
import kr.aling.user.banduser.dto.response.GetBandUserInfoResponseDto;
import kr.aling.user.banduser.dto.response.external.GetFileInfoResponseDto;
import kr.aling.user.banduser.exception.BandUserNotFoundException;
import kr.aling.user.banduser.repository.BandUserReadRepository;
import kr.aling.user.banduser.service.impl.BandUserReadServiceImpl;
import kr.aling.user.common.dto.PageResponseDto;
import kr.aling.user.common.feignclient.FileFeignClient;
import kr.aling.user.user.dto.response.GetUserSimpleInfoResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * 그룹 회원 조회 서비스 테스트.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@ExtendWith(SpringExtension.class)
class BandUserReadServiceTest {

    @InjectMocks
    BandUserReadServiceImpl bandUserReadService;

    @Mock
    BandUserReadRepository bandUserReadRepository;

    @Mock
    FileFeignClient fileFeignClient;

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("그룹 명으로 그룹 회원 리스트 조회 성공 테스트")
    void getBandUserList_successTest() {
        // given
        String bandName = "bandName";
        Long totalElement = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        GetBandUserInfoResponseDto getBandUserInfoResponseDto =
                new GetBandUserInfoResponseDto(1L, 1);
        GetUserSimpleInfoResponseDto getUserSimpleInfoResponseDto =
                new GetUserSimpleInfoResponseDto(1L, 1L, "name");
        GetBandUserAndUserInfoResponseDto getBandUserAndUserInfoResponseDto =
                new GetBandUserAndUserInfoResponseDto(getBandUserInfoResponseDto, getUserSimpleInfoResponseDto);
        Page page = new PageImpl(List.of(getBandUserAndUserInfoResponseDto), pageable, totalElement);

        // when
        when(bandUserReadRepository.getBandUserListByBandName(anyString(), any(Pageable.class)))
                .thenReturn(page);

        // then
        PageResponseDto<GetBandUserAndUserInfoResponseDto> bandUserList =
                bandUserReadService.getBandUserList(bandName, pageable);

        assertThat(bandUserList.getPageNumber()).isEqualTo(pageable.getPageNumber());
        assertThat(bandUserList.getTotalPages()).isEqualTo(1);
        assertThat(bandUserList.getTotalElements()).isEqualTo(totalElement);
        assertThat(bandUserList.getContent()).isNotEmpty();
        assertThat(bandUserList.getContent().get(0).getBandUserInfo().getBandUserRoleNo()).isEqualTo(
                getBandUserInfoResponseDto.getBandUserRoleNo());
        assertThat(bandUserList.getContent().get(0).getBandUserInfo().getBandUserNo()).isEqualTo(
                getBandUserInfoResponseDto.getBandUserNo());
        assertThat(bandUserList.getContent().get(0).getUserInfo().getUserNo()).isEqualTo(
                getUserSimpleInfoResponseDto.getUserNo());
        assertThat(bandUserList.getContent().get(0).getUserInfo().getName()).isEqualTo(
                getUserSimpleInfoResponseDto.getName());
        assertThat(bandUserList.getContent().get(0).getUserInfo().getFileNo()).isEqualTo(
                getUserSimpleInfoResponseDto.getFileNo());

        verify(bandUserReadRepository, times(1)).getBandUserListByBandName(anyString(), any(Pageable.class));
    }

    @Test
    @DisplayName("그룹 번호와 회원 번호를 통해 그룹 회원 정보 조회 성공 테스트")
    void getBandUserInfo_successTest() {
        // given
        Long bandNo = 1L;
        Long userNo = 1L;

        GetBandUserAuthResponseDto getBandUserAuthResponseDto = new GetBandUserAuthResponseDto("TEST_ROLE_NAME");

        // when
        when(bandUserReadRepository.getBandUserInfoByBandNoAndUserNo(anyLong(), anyLong()))
                .thenReturn(Optional.of(getBandUserAuthResponseDto));

        // then
        assertDoesNotThrow(() -> bandUserReadService.getBandUserInfo(bandNo, userNo));

        verify(bandUserReadRepository, times(1)).getBandUserInfoByBandNoAndUserNo(anyLong(), anyLong());
    }

    @Test
    @DisplayName("그룹 번호와 회원 번호를 통해 그룹 회원 정보 조회 실패 테스트_그룹 회원을 찾을 수 없을 경우")
    void getBandUserInfo_failTest_bandUserNotFoundException() {
        // given
        Long bandNo = 1L;
        Long userNo = 1L;

        // when
        when(bandUserReadRepository.getBandUserInfoByBandNoAndUserNo(anyLong(), anyLong()))
                .thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> bandUserReadService.getBandUserInfo(bandNo, userNo))
                .isInstanceOf(BandUserNotFoundException.class)
                .hasMessageContaining(BandUserNotFoundException.MESSAGE);

        verify(bandUserReadRepository, times(1)).getBandUserInfoByBandNoAndUserNo(anyLong(), anyLong());
    }

    @Test
    @DisplayName("게시글 작성자 조회 서비스 메서드 실패 테스트 - 그룹 회원 번호가 없음")
    void getPostWriterInfo_fail_service_test() {
        // given

        // when
        when(bandUserReadRepository.existsById(anyLong())).thenReturn(false);

        // then
        assertThatThrownBy(() -> bandUserReadService.getPostWriterInfo(1L))
                .isInstanceOf(BandUserNotFoundException.class)
                .hasMessage(BandUserNotFoundException.MESSAGE);

        verify(bandUserReadRepository, times(1)).existsById(anyLong());
        verify(bandUserReadRepository, times(0)).getBandUserForPost(anyLong());
        verify(fileFeignClient, times(0)).requestFileInfo(anyLong());
    }

    @Test
    @DisplayName("게시글 작성자 조회 서비스 성공 테스트 (작성자 프로필 없는 경우)")
    void getPostWriterInfo_success_test_without_fileNo() {
        // given
        BandPostUerQueryDto bandPostUerQueryDto = new BandPostUerQueryDto(1L, "username", null);

        // when
        when(bandUserReadRepository.existsById(anyLong())).thenReturn(true);
        when(bandUserReadRepository.getBandUserForPost(anyLong())).thenReturn(bandPostUerQueryDto);

        // then
        bandUserReadService.getPostWriterInfo(anyLong());

        verify(bandUserReadRepository, times(1)).existsById(anyLong());
        verify(bandUserReadRepository, times(1)).getBandUserForPost(anyLong());
        verify(fileFeignClient, times(0)).requestFileInfo(anyLong());
    }

    @Test
    @DisplayName("게시슬 작성자 조회 서비스 성공 테스트 (작성자 프로필 있는 경우)")
    void getPostWriterInfo_success_test_with_fileNo() {
        // given
        BandPostUerQueryDto bandPostUerQueryDto = new BandPostUerQueryDto(1L, "username", 1L);
        GetFileInfoResponseDto getFileInfoResponseDto = new GetFileInfoResponseDto();
        ReflectionTestUtils.setField(getFileInfoResponseDto, "path", "path");

        // when
        when(bandUserReadRepository.existsById(anyLong())).thenReturn(true);
        when(bandUserReadRepository.getBandUserForPost(anyLong())).thenReturn(bandPostUerQueryDto);
        when(fileFeignClient.requestFileInfo(anyLong())).thenReturn(getFileInfoResponseDto);

        // then
        bandUserReadService.getPostWriterInfo(anyLong());

        verify(bandUserReadRepository, times(1)).existsById(anyLong());
        verify(bandUserReadRepository, times(1)).getBandUserForPost(anyLong());
        verify(fileFeignClient, times(1)).requestFileInfo(anyLong());
    }

}