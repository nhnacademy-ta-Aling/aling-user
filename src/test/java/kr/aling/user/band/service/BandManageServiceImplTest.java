package kr.aling.user.band.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import kr.aling.user.band.dto.request.CreateBandPostTypeRequestDto;
import kr.aling.user.band.dto.request.CreateBandRequestDto;
import kr.aling.user.band.dto.request.ModifyBandRequestDto;
import kr.aling.user.band.dto.request.external.CreateBandPostTypeRequestExternalDto;
import kr.aling.user.band.entity.Band;
import kr.aling.user.band.exception.BandAlreadyExistsException;
import kr.aling.user.band.exception.BandLimitExceededException;
import kr.aling.user.band.exception.BandNotFoundException;
import kr.aling.user.band.repository.BandManageRepository;
import kr.aling.user.band.repository.BandReadRepository;
import kr.aling.user.band.service.impl.BandManageServiceImpl;
import kr.aling.user.banduser.entity.BandUser;
import kr.aling.user.banduser.repository.BandUserManageRepository;
import kr.aling.user.banduser.repository.BandUserReadRepository;
import kr.aling.user.banduserrole.entity.BandUserRole;
import kr.aling.user.banduserrole.exception.BandUserRoleNotFoundException;
import kr.aling.user.banduserrole.repository.BandUserRoleReadRepository;
import kr.aling.user.common.feignclient.PostFeignClient;
import kr.aling.user.user.entity.AlingUser;
import kr.aling.user.user.exception.UserNotFoundException;
import kr.aling.user.user.repository.UserReadRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * 그룹 관리 서비스 테스트.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@ExtendWith(SpringExtension.class)
class BandManageServiceImplTest {

    @InjectMocks
    private BandManageServiceImpl bandManageService;
    @Mock
    private BandReadRepository bandReadRepository;
    @Mock
    private BandManageRepository bandManageRepository;
    @Mock
    private UserReadRepository userReadRepository;
    @Mock
    private BandUserRoleReadRepository bandUserRoleReadRepository;
    @Mock
    private BandUserReadRepository bandUserReadRepository;
    @Mock
    private BandUserManageRepository bandUserManageRepository;

    @Mock
    private PostFeignClient postFeignClient;

    private Long userNo;
    private CreateBandRequestDto createBandRequestDto;

    @BeforeEach
    void setUp() {
        userNo = 1L;

        createBandRequestDto = new CreateBandRequestDto();
        ReflectionTestUtils.setField(createBandRequestDto, "bandName", "testBandName");
        ReflectionTestUtils.setField(createBandRequestDto, "isEnter", true);
        ReflectionTestUtils.setField(createBandRequestDto, "isViewContent", true);
        ReflectionTestUtils.setField(createBandRequestDto, "bandInfo", "I'm band!");
        ReflectionTestUtils.setField(createBandRequestDto, "fileNo", 1L);

    }

    @Test
    @DisplayName("그룹 생성 성공")
    void makeBand_successTest() {
        // when
        when(bandUserRoleReadRepository.findByRoleName(anyString())).thenReturn(
                Optional.of(mock(BandUserRole.class)));
        when(bandUserReadRepository.countByUserNoAndBandUserRoleName(anyLong(), any())).thenReturn(
                0L);
        when(bandReadRepository.existsBandByName(anyString())).thenReturn(false);
        when(userReadRepository.findById(anyLong())).thenReturn(Optional.of(mock(AlingUser.class)));
        when(bandManageRepository.save(any(Band.class))).thenReturn(mock(Band.class));
        when(bandUserManageRepository.save(any(BandUser.class))).thenReturn(mock(BandUser.class));
        doNothing().when(postFeignClient).requestMakeBandPostType(any(CreateBandPostTypeRequestExternalDto.class));

        // then
        assertDoesNotThrow(() -> bandManageService.makeBand(userNo, createBandRequestDto));

        verify(bandUserRoleReadRepository, times(1)).findByRoleName(anyString());
        verify(bandUserReadRepository, times(1)).countByUserNoAndBandUserRoleName(anyLong(), any());
        verify(bandReadRepository, times(1)).existsBandByName(anyString());
        verify(userReadRepository, times(1)).findById(anyLong());
        verify(bandManageRepository, times(1)).save(any(Band.class));
        verify(bandUserManageRepository, times(1)).save(any(BandUser.class));
        verify(postFeignClient, times(1)).requestMakeBandPostType(any(CreateBandPostTypeRequestExternalDto.class));
    }

    @Test
    @DisplayName("그룹 생성 실패_그룹 회원 권한을 찾을 수 없음")
    void makeBand_failTest_BandUserRoleNotFoundException() {
        // when
        when(bandUserRoleReadRepository.findByRoleName(anyString())).thenReturn(
                Optional.ofNullable(null));
        when(bandUserReadRepository.countByUserNoAndBandUserRoleName(anyLong(), any())).thenReturn(
                Long.MIN_VALUE);
        when(bandReadRepository.existsBandByName(anyString())).thenReturn(false);
        when(userReadRepository.findById(anyLong())).thenReturn(Optional.of(mock(AlingUser.class)));
        when(bandManageRepository.save(any(Band.class))).thenReturn(mock(Band.class));
        when(bandUserManageRepository.save(any(BandUser.class))).thenReturn(mock(BandUser.class));

        // then
        assertThatThrownBy(() -> bandManageService.makeBand(userNo, createBandRequestDto))
                .isInstanceOf(BandUserRoleNotFoundException.class)
                .hasMessage(BandUserRoleNotFoundException.MESSAGE);

        verify(bandUserRoleReadRepository, times(1)).findByRoleName(anyString());
        verify(bandUserReadRepository, times(0)).countByUserNoAndBandUserRoleName(anyLong(), anyString());
        verify(bandReadRepository, times(0)).existsBandByName(anyString());
        verify(userReadRepository, times(0)).findById(anyLong());
        verify(bandManageRepository, times(0)).save(any());
        verify(bandUserManageRepository, times(0)).save(any());
    }

    @Test
    @DisplayName("그룹 생성 실패_그룹 소유 개수 초과")
    void makeBand_failTest_BandLimitExceededException() {
        // when
        when(bandUserRoleReadRepository.findByRoleName(anyString())).thenReturn(
                Optional.ofNullable(mock(BandUserRole.class)));
        when(bandUserReadRepository.countByUserNoAndBandUserRoleName(anyLong(), any())).thenReturn(
                Long.MAX_VALUE);
        when(bandReadRepository.existsBandByName(anyString())).thenReturn(false);
        when(userReadRepository.findById(anyLong())).thenReturn(Optional.of(mock(AlingUser.class)));
        when(bandManageRepository.save(any(Band.class))).thenReturn(mock(Band.class));
        when(bandUserManageRepository.save(any(BandUser.class))).thenReturn(mock(BandUser.class));

        // then
        assertThatThrownBy(() -> bandManageService.makeBand(userNo, createBandRequestDto))
                .isInstanceOf(BandLimitExceededException.class)
                .hasMessage(BandLimitExceededException.MESSAGE);

        verify(bandUserRoleReadRepository, times(1)).findByRoleName(anyString());
        verify(bandUserReadRepository, times(1)).countByUserNoAndBandUserRoleName(anyLong(), any());
        verify(bandReadRepository, times(0)).existsBandByName(anyString());
        verify(userReadRepository, times(0)).findById(anyLong());
        verify(bandManageRepository, times(0)).save(any(Band.class));
        verify(bandUserManageRepository, times(0)).save(any(BandUser.class));
    }

    @Test
    @DisplayName("그룹 생성 실패_그룹명 중복")
    void makeBand_failTest_BandAlreadyExistsException() {
        // when
        when(bandUserRoleReadRepository.findByRoleName(anyString())).thenReturn(
                Optional.ofNullable(mock(BandUserRole.class)));
        when(bandUserReadRepository.countByUserNoAndBandUserRoleName(anyLong(), any())).thenReturn(
                Long.MIN_VALUE);
        when(bandReadRepository.existsBandByName(anyString())).thenReturn(true);
        when(userReadRepository.findById(anyLong())).thenReturn(Optional.of(mock(AlingUser.class)));
        when(bandManageRepository.save(any(Band.class))).thenReturn(mock(Band.class));
        when(bandUserManageRepository.save(any(BandUser.class))).thenReturn(mock(BandUser.class));

        // then
        assertThatThrownBy(() -> bandManageService.makeBand(userNo, createBandRequestDto))
                .isInstanceOf(BandAlreadyExistsException.class)
                .hasMessage(BandAlreadyExistsException.MESSAGE);

        verify(bandUserRoleReadRepository, times(1)).findByRoleName(anyString());
        verify(bandUserReadRepository, times(1)).countByUserNoAndBandUserRoleName(anyLong(), any());
        verify(bandReadRepository, times(1)).existsBandByName(anyString());
        verify(userReadRepository, times(0)).findById(anyLong());
        verify(bandManageRepository, times(0)).save(any(Band.class));
        verify(bandUserManageRepository, times(0)).save(any(BandUser.class));
    }

    @Test
    @DisplayName("그룹 생성 실패_회원을 찾을 수 없음")
    void makeBand_failTest_UserNotFoundException() {
        // when
        when(bandUserRoleReadRepository.findByRoleName(anyString())).thenReturn(
                Optional.ofNullable(mock(BandUserRole.class)));
        when(bandUserReadRepository.countByUserNoAndBandUserRoleName(anyLong(), any())).thenReturn(
                Long.MIN_VALUE);
        when(bandReadRepository.existsBandByName(anyString())).thenReturn(false);
        when(userReadRepository.findById(anyLong())).thenReturn(Optional.ofNullable(null));
        when(bandManageRepository.save(any(Band.class))).thenReturn(mock(Band.class));
        when(bandUserManageRepository.save(any(BandUser.class))).thenReturn(mock(BandUser.class));

        // then
        assertThatThrownBy(() -> bandManageService.makeBand(userNo, createBandRequestDto))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage(UserNotFoundException.MESSAGE);

        verify(bandUserRoleReadRepository, times(1)).findByRoleName(anyString());
        verify(bandUserReadRepository, times(1)).countByUserNoAndBandUserRoleName(anyLong(), any());
        verify(bandReadRepository, times(1)).existsBandByName(anyString());
        verify(userReadRepository, times(1)).findById(anyLong());
        verify(bandManageRepository, times(0)).save(any(Band.class));
        verify(bandUserManageRepository, times(0)).save(any(BandUser.class));
    }

    @Test
    @DisplayName("그룹 수정 성공")
    void updateBandInfo_successTest() {
        // given
        String bandName = "bandName";
        ModifyBandRequestDto modifyBandRequestDto = new ModifyBandRequestDto();

        // when
        when(bandReadRepository.existsBandByName(any())).thenReturn(false);
        when(bandReadRepository.findByName(any())).thenReturn(Optional.of(mock(Band.class)));

        // then
        assertDoesNotThrow(() -> bandManageService.updateBandInfo(bandName, modifyBandRequestDto));

        verify(bandReadRepository, times(1)).existsBandByName(any());
        verify(bandReadRepository, times(1)).findByName(any());
    }

    @Test
    @DisplayName("그룹 수정 실패_그룹 명이 이미 존재 하는 경우")
    void updateBandInfo_failTest_bandAlreadyExistsException() {
        // given
        String bandName = "bandName";
        ModifyBandRequestDto modifyBandRequestDto = new ModifyBandRequestDto();

        // when
        when(bandReadRepository.existsBandByName(any())).thenReturn(true);
        when(bandReadRepository.findByName(any())).thenReturn(Optional.of(mock(Band.class)));

        // then
        assertThatThrownBy(() -> bandManageService.updateBandInfo(bandName, modifyBandRequestDto))
                .isInstanceOf(BandAlreadyExistsException.class)
                .hasMessage(BandAlreadyExistsException.MESSAGE);

        verify(bandReadRepository, times(1)).existsBandByName(any());
        verify(bandReadRepository, times(0)).findByName(any());
    }

    @Test
    @DisplayName("그룹 수정 실패_그룹을 찾을 수 없는 경우")
    void updateBandInfo_failTest_bandNotFoundException() {
        // given
        String bandName = "bandName";
        ModifyBandRequestDto modifyBandRequestDto = new ModifyBandRequestDto();

        // when
        when(bandReadRepository.existsBandByName(any())).thenReturn(false);
        when(bandReadRepository.findByName(any())).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> bandManageService.updateBandInfo(bandName, modifyBandRequestDto))
                .isInstanceOf(BandNotFoundException.class)
                .hasMessage(BandNotFoundException.MESSAGE);

        verify(bandReadRepository, times(1)).existsBandByName(any());
        verify(bandReadRepository, times(1)).findByName(any());
    }

    @Test
    @DisplayName("그룹 삭제 성공")
    void removeBand_successTest() {
        // given
        String bandName = "bandName";

        // when
        when(bandReadRepository.findByName(anyString())).thenReturn(Optional.of(mock(Band.class)));

        // then
        assertDoesNotThrow(() -> bandManageService.removeBand(bandName));

        verify(bandReadRepository, times(1)).findByName(anyString());
    }

    @Test
    @DisplayName("그룹 삭제 실패_그룹을 찾을 수 없는 경우")
    void removeBand_failTest_bandNotFoundException() {
        // given
        String bandName = "bandName";

        // when
        when(bandReadRepository.findByName(anyString())).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> bandManageService.removeBand(bandName))
                .isInstanceOf(BandNotFoundException.class)
                .hasMessage(BandNotFoundException.MESSAGE);

        verify(bandReadRepository, times(1)).findByName(anyString());
    }

    @Test
    @DisplayName("그룹 게시글 분류 생성 성공")
    void makeBandCategory_successTest() {
        // given
        String bandName = "bandName";
        CreateBandPostTypeRequestDto createBandPostTypeRequestDto = new CreateBandPostTypeRequestDto();

        // when
        when(bandReadRepository.findByName(anyString())).thenReturn(Optional.of(mock(Band.class)));

        // then
        assertDoesNotThrow(() -> bandManageService.makeBandCategory(bandName, createBandPostTypeRequestDto));

        verify(bandReadRepository, times(1)).findByName(anyString());
    }

    @Test
    @DisplayName("그룹 게시글 분류 생성 실패_그룹을 찾을 수 없는 경우")
    void makeBandCategory_failTest_bandNotFoundException() {
        // given
        String bandName = "bandName";
        CreateBandPostTypeRequestDto createBandPostTypeRequestDto = new CreateBandPostTypeRequestDto();

        // when
        when(bandReadRepository.findByName(anyString())).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() ->  bandManageService.makeBandCategory(bandName, createBandPostTypeRequestDto))
                .isInstanceOf(BandNotFoundException.class)
                .hasMessage(BandNotFoundException.MESSAGE);

        verify(bandReadRepository, times(1)).findByName(anyString());
    }
}