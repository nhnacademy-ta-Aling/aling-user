package kr.aling.user.band.service.impl;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import kr.aling.user.band.dto.request.CreateBandRequestDto;
import kr.aling.user.band.dummy.BandDummy;
import kr.aling.user.band.entity.Band;
import kr.aling.user.band.exception.BandAlreadyExistsException;
import kr.aling.user.band.exception.BandLimitExceededException;
import kr.aling.user.band.repository.BandManageRepository;
import kr.aling.user.band.repository.BandReadRepository;
import kr.aling.user.banduser.dummy.BandUserDummy;
import kr.aling.user.banduser.entity.BandUser;
import kr.aling.user.banduser.repository.BandUserManageRepository;
import kr.aling.user.banduser.repository.BandUserReadRepository;
import kr.aling.user.banduserrole.dummy.BandUserRoleDummy;
import kr.aling.user.banduserrole.entity.BandUserRole;
import kr.aling.user.banduserrole.exception.BandUserRoleNotFoundException;
import kr.aling.user.banduserrole.repository.BandUserRoleReadRepository;
import kr.aling.user.user.dummy.UserDummy;
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
    private BandManageServiceImpl bandManageServiceImpl;
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

    private BandUserRole bandUserRoleCreator;
    private AlingUser alingUser;
    private Band band;
    private BandUser bandUserCreator;
    private Long userNo;
    private CreateBandRequestDto createBandRequestDto;

    @BeforeEach
    void setUp() {
        bandUserRoleCreator = BandUserRoleDummy.dummyCreator();
        alingUser = UserDummy.dummy();
        band = BandDummy.dummyPublicBand();
        bandUserCreator = BandUserDummy.dummy(bandUserRoleCreator, band, alingUser);

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
        when(bandUserRoleReadRepository.findByRoleName(any())).thenReturn(
                Optional.of(bandUserRoleCreator));
        when(bandUserReadRepository.countByAlingUser_UserNoAndBandUserRole_RoleName(any(), any())).thenReturn(
                Long.MIN_VALUE);
        when(bandReadRepository.existsBandByName(any())).thenReturn(false);
        when(userReadRepository.findById(any())).thenReturn(Optional.of(alingUser));
        when(bandManageRepository.save(any())).thenReturn(band);
        when(bandUserManageRepository.save(any())).thenReturn(bandUserCreator);

        // then
        assertDoesNotThrow(() -> bandManageServiceImpl.makeBand(userNo, createBandRequestDto));

        verify(bandUserRoleReadRepository, times(1)).findByRoleName(anyString());
        verify(bandUserReadRepository, times(1)).countByAlingUser_UserNoAndBandUserRole_RoleName(anyLong(), anyString());
        verify(bandReadRepository, times(1)).existsBandByName(anyString());
        verify(userReadRepository, times(1)).findById(anyLong());
        verify(bandManageRepository, times(1)).save(any());
        verify(bandUserManageRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("그룹 생성 실패_그룹 회원 권한을 찾을 수 없음")
    void makeBand_failTest_BandUserRoleNotFoundException() {
        // when
        when(bandUserRoleReadRepository.findByRoleName(any())).thenReturn(
                Optional.ofNullable(null));
        when(bandUserReadRepository.countByAlingUser_UserNoAndBandUserRole_RoleName(any(), any())).thenReturn(
                Long.MIN_VALUE);
        when(bandReadRepository.existsBandByName(any())).thenReturn(false);
        when(userReadRepository.findById(any())).thenReturn(Optional.of(alingUser));
        when(bandManageRepository.save(any())).thenReturn(band);
        when(bandUserManageRepository.save(any())).thenReturn(bandUserCreator);

        // then
        assertThatThrownBy(() -> bandManageServiceImpl.makeBand(userNo, createBandRequestDto))
                .isInstanceOf(BandUserRoleNotFoundException.class)
                .hasMessage(BandUserRoleNotFoundException.MESSAGE);

        verify(bandUserRoleReadRepository, times(1)).findByRoleName(anyString());
        verify(bandUserReadRepository, times(0)).countByAlingUser_UserNoAndBandUserRole_RoleName(anyLong(), anyString());
        verify(bandReadRepository, times(0)).existsBandByName(anyString());
        verify(userReadRepository, times(0)).findById(anyLong());
        verify(bandManageRepository, times(0)).save(any());
        verify(bandUserManageRepository, times(0)).save(any());
    }

    @Test
    @DisplayName("그룹 생성 실패_그룹 소유 개수 초과")
    void makeBand_failTest_BandLimitExceededException() {
        // when
        when(bandUserRoleReadRepository.findByRoleName(any())).thenReturn(
                Optional.of(bandUserRoleCreator));
        when(bandUserReadRepository.countByAlingUser_UserNoAndBandUserRole_RoleName(any(), any())).thenReturn(
                Long.MAX_VALUE);
        when(bandReadRepository.existsBandByName(any())).thenReturn(false);
        when(userReadRepository.findById(any())).thenReturn(Optional.of(alingUser));
        when(bandManageRepository.save(any())).thenReturn(band);
        when(bandUserManageRepository.save(any())).thenReturn(bandUserCreator);

        // then
        assertThatThrownBy(() -> bandManageServiceImpl.makeBand(userNo, createBandRequestDto))
                .isInstanceOf(BandLimitExceededException.class)
                .hasMessage(BandLimitExceededException.MESSAGE);

        verify(bandUserRoleReadRepository, times(1)).findByRoleName(anyString());
        verify(bandUserReadRepository, times(1)).countByAlingUser_UserNoAndBandUserRole_RoleName(anyLong(), anyString());
        verify(bandReadRepository, times(0)).existsBandByName(anyString());
        verify(userReadRepository, times(0)).findById(anyLong());
        verify(bandManageRepository, times(0)).save(any());
        verify(bandUserManageRepository, times(0)).save(any());
    }

    @Test
    @DisplayName("그룹 생성 실패_그룹명 중복")
    void makeBand_failTest_BandAlreadyExistsException() {
        // when
        when(bandUserRoleReadRepository.findByRoleName(any())).thenReturn(
                Optional.of(bandUserRoleCreator));
        when(bandUserReadRepository.countByAlingUser_UserNoAndBandUserRole_RoleName(any(), any())).thenReturn(
                Long.MIN_VALUE);
        when(bandReadRepository.existsBandByName(any())).thenReturn(true);
        when(userReadRepository.findById(any())).thenReturn(Optional.of(alingUser));
        when(bandManageRepository.save(any())).thenReturn(band);
        when(bandUserManageRepository.save(any())).thenReturn(bandUserCreator);

        // then
        assertThatThrownBy(() -> bandManageServiceImpl.makeBand(userNo, createBandRequestDto))
                .isInstanceOf(BandAlreadyExistsException.class)
                .hasMessage(BandAlreadyExistsException.MESSAGE);

        verify(bandUserRoleReadRepository, times(1)).findByRoleName(anyString());
        verify(bandUserReadRepository, times(1)).countByAlingUser_UserNoAndBandUserRole_RoleName(anyLong(), anyString());
        verify(bandReadRepository, times(1)).existsBandByName(anyString());
        verify(userReadRepository, times(0)).findById(anyLong());
        verify(bandManageRepository, times(0)).save(any());
        verify(bandUserManageRepository, times(0)).save(any());
    }

    @Test
    @DisplayName("그룹 생성 실패_회원을 찾을 수 없음")
    void makeBand_failTest_UserNotFoundException() {
        // when
        when(bandUserRoleReadRepository.findByRoleName(any())).thenReturn(
                Optional.of(bandUserRoleCreator));
        when(bandUserReadRepository.countByAlingUser_UserNoAndBandUserRole_RoleName(any(), any())).thenReturn(
                Long.MIN_VALUE);
        when(bandReadRepository.existsBandByName(any())).thenReturn(false);
        when(userReadRepository.findById(any())).thenReturn(Optional.ofNullable(null));
        when(bandManageRepository.save(any())).thenReturn(band);
        when(bandUserManageRepository.save(any())).thenReturn(bandUserCreator);

        // then
        assertThatThrownBy(() -> bandManageServiceImpl.makeBand(userNo, createBandRequestDto))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage(UserNotFoundException.MESSAGE);

        verify(bandUserRoleReadRepository, times(1)).findByRoleName(anyString());
        verify(bandUserReadRepository, times(1)).countByAlingUser_UserNoAndBandUserRole_RoleName(anyLong(), anyString());
        verify(bandReadRepository, times(1)).existsBandByName(anyString());
        verify(userReadRepository, times(1)).findById(anyLong());
        verify(bandManageRepository, times(0)).save(any());
        verify(bandUserManageRepository, times(0)).save(any());
    }
}