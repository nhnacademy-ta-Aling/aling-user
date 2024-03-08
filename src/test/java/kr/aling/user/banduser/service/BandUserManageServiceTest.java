package kr.aling.user.banduser.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import kr.aling.user.band.dummy.BandDummy;
import kr.aling.user.band.entity.Band;
import kr.aling.user.band.exception.BandDeniedException;
import kr.aling.user.band.exception.BandNotFoundException;
import kr.aling.user.band.repository.BandReadRepository;
import kr.aling.user.banduser.dto.request.ModifyRoleOfBandUserRequestDto;
import kr.aling.user.banduser.dummy.BandUserDummy;
import kr.aling.user.banduser.entity.BandUser;
import kr.aling.user.banduser.exception.BandUserAlreadyExistsException;
import kr.aling.user.banduser.exception.BandUserNotFoundException;
import kr.aling.user.banduser.exception.BandUserRoleDeniedException;
import kr.aling.user.banduser.repository.BandUserManageRepository;
import kr.aling.user.banduser.repository.BandUserReadRepository;
import kr.aling.user.banduser.service.impl.BandUserManageServiceImpl;
import kr.aling.user.banduserrole.dummy.BandUserRoleDummy;
import kr.aling.user.banduserrole.entity.BandUserRole;
import kr.aling.user.banduserrole.exception.BandUserRoleNotFoundException;
import kr.aling.user.banduserrole.repository.BandUserRoleReadRepository;
import kr.aling.user.common.enums.BandUserRoleEnum;
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
 * 그룹 회원 관리 Service 테스트.
 *
 * @author 정유진
 * @since 1.0
 **/
@ExtendWith(SpringExtension.class)
class BandUserManageServiceTest {

    BandUserRole bandUserRole;
    BandUser bandUser;
    @InjectMocks
    private BandUserManageServiceImpl bandUserManageService;
    @Mock
    private BandReadRepository bandReadRepository;
    @Mock
    private UserReadRepository userReadRepository;
    @Mock
    private BandUserManageRepository bandUserManageRepository;
    @Mock
    private BandUserReadRepository bandUserReadRepository;
    @Mock
    private BandUserRoleReadRepository bandUserRoleReadRepository;

    @BeforeEach
    void setUp() {
        bandUserRole = BandUserRoleDummy.dummyUser();
        bandUser = BandUserDummy.dummy(bandUserRole, BandDummy.dummyPublicBand(),
                UserDummy.dummy());

        ReflectionTestUtils.setField(bandUserRole, "bandUserRoleNo", 1);
    }

    @Test
    @DisplayName("그룹 회원 생성 성공 테스트")
    void makeBandUser_successTest() {
        // given
        String bandName = "bandName";
        Long userNo = 1L;

        // when
        when(bandReadRepository.getByName(anyString())).thenReturn(Optional.ofNullable(mock(Band.class)));
        when(userReadRepository.getByUserNo(anyLong())).thenReturn(Optional.ofNullable(mock(AlingUser.class)));
        when(bandUserRoleReadRepository.findByRoleName(anyString())).thenReturn(
                Optional.ofNullable(mock(BandUserRole.class)));
        when(bandUserReadRepository.findBandUserByBandNameAndUserNo(anyString(), anyLong()))
                .thenReturn(Optional.empty());
        when(bandUserReadRepository.getIsBlockBandUser(anyString(), anyLong())).thenReturn(false);
        when(bandUserManageRepository.save(any(BandUser.class))).thenReturn(mock(BandUser.class));

        // then
        assertDoesNotThrow(() -> bandUserManageService.makeBandUser(bandName, userNo));

        verify(bandReadRepository, times(1)).getByName(anyString());
        verify(userReadRepository, times(1)).getByUserNo(anyLong());
        verify(bandUserRoleReadRepository, times(1)).findByRoleName(anyString());
        verify(bandUserReadRepository, times(1)).findBandUserByBandNameAndUserNo(anyString(), anyLong());
        verify(bandUserReadRepository, times(1)).getIsBlockBandUser(anyString(), anyLong());
        verify(bandUserManageRepository, times(1)).save(any(BandUser.class));
    }

    @Test
    @DisplayName("그룹 회원 생성 실패 테스트_그룹이 존재 하지 않을 경우")
    void makeBandUser_failTest_bandNotFoundException() {
        // given
        String bandName = "bandName";
        Long userNo = 1L;

        // when
        when(bandReadRepository.getByName(anyString())).thenReturn(Optional.empty());
        when(userReadRepository.getByUserNo(anyLong())).thenReturn(Optional.ofNullable(mock(AlingUser.class)));
        when(bandUserRoleReadRepository.findByRoleName(anyString())).thenReturn(
                Optional.ofNullable(mock(BandUserRole.class)));
        when(bandUserReadRepository.findBandUserByBandNameAndUserNo(anyString(), anyLong()))
                .thenReturn(Optional.empty());
        when(bandUserReadRepository.getIsBlockBandUser(anyString(), anyLong())).thenReturn(false);
        when(bandUserManageRepository.save(any(BandUser.class))).thenReturn(mock(BandUser.class));

        // then
        assertThatThrownBy(() -> bandUserManageService.makeBandUser(bandName, userNo))
                .isInstanceOf(BandNotFoundException.class)
                .hasMessageContaining(BandNotFoundException.MESSAGE);

        verify(bandReadRepository, times(1)).getByName(anyString());
        verify(userReadRepository, times(0)).getByUserNo(anyLong());
        verify(bandUserRoleReadRepository, times(0)).findByRoleName(anyString());
        verify(bandUserReadRepository, times(0)).findBandUserByBandNameAndUserNo(anyString(), anyLong());
        verify(bandUserReadRepository, times(0)).getIsBlockBandUser(anyString(), anyLong());
        verify(bandUserManageRepository, times(0)).save(any(BandUser.class));
    }

    @Test
    @DisplayName("그룹 회원 생성 실패 테스트_회원이 존재 하지 않을 경우")
    void makeBandUser_failTest_userNotFoundException() {
        // given
        String bandName = "bandName";
        Long userNo = 1L;

        // when
        when(bandReadRepository.getByName(anyString())).thenReturn(Optional.ofNullable(mock(Band.class)));
        when(userReadRepository.getByUserNo(anyLong())).thenReturn(Optional.empty());
        when(bandUserRoleReadRepository.findByRoleName(anyString())).thenReturn(
                Optional.ofNullable(mock(BandUserRole.class)));
        when(bandUserReadRepository.findBandUserByBandNameAndUserNo(anyString(), anyLong()))
                .thenReturn(Optional.empty());
        when(bandUserReadRepository.getIsBlockBandUser(anyString(), anyLong())).thenReturn(false);
        when(bandUserManageRepository.save(any(BandUser.class))).thenReturn(mock(BandUser.class));

        // then
        assertThatThrownBy(() -> bandUserManageService.makeBandUser(bandName, userNo))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining(UserNotFoundException.MESSAGE);

        verify(bandReadRepository, times(1)).getByName(anyString());
        verify(userReadRepository, times(1)).getByUserNo(anyLong());
        verify(bandUserRoleReadRepository, times(0)).findByRoleName(anyString());
        verify(bandUserReadRepository, times(0)).findBandUserByBandNameAndUserNo(anyString(), anyLong());
        verify(bandUserReadRepository, times(0)).getIsBlockBandUser(anyString(), anyLong());
        verify(bandUserManageRepository, times(0)).save(any(BandUser.class));
    }

    @Test
    @DisplayName("그룹 회원 생성 실패 테스트_그룹 회원 권한이 존재 하지 않을 경우")
    void makeBandUser_failTest_bandUserRoleNotFoundException() {
        // given
        String bandName = "bandName";
        Long userNo = 1L;

        // when
        when(bandReadRepository.getByName(anyString())).thenReturn(Optional.ofNullable(mock(Band.class)));
        when(userReadRepository.getByUserNo(anyLong())).thenReturn(Optional.ofNullable(mock(AlingUser.class)));
        when(bandUserRoleReadRepository.findByRoleName(anyString())).thenReturn(Optional.empty());
        when(bandUserReadRepository.findBandUserByBandNameAndUserNo(anyString(), anyLong()))
                .thenReturn(Optional.empty());
        when(bandUserReadRepository.getIsBlockBandUser(anyString(), anyLong())).thenReturn(false);
        when(bandUserManageRepository.save(any(BandUser.class))).thenReturn(mock(BandUser.class));

        // then
        assertThatThrownBy(() -> bandUserManageService.makeBandUser(bandName, userNo))
                .isInstanceOf(BandUserRoleNotFoundException.class)
                .hasMessageContaining(BandUserRoleNotFoundException.MESSAGE);

        verify(bandReadRepository, times(1)).getByName(anyString());
        verify(userReadRepository, times(1)).getByUserNo(anyLong());
        verify(bandUserRoleReadRepository, times(1)).findByRoleName(anyString());
        verify(bandUserReadRepository, times(0)).findBandUserByBandNameAndUserNo(anyString(), anyLong());
        verify(bandUserReadRepository, times(0)).getIsBlockBandUser(anyString(), anyLong());
        verify(bandUserManageRepository, times(0)).save(any(BandUser.class));
    }

    @Test
    @DisplayName("그룹 회원 생성 실패 테스트_그룹 회원이 이미 존재 하는 경우")
    void makeBandUser_failTest_bandUserAlreadyExistsExceptionn() {
        // given
        String bandName = "bandName";
        Long userNo = 1L;

        // when
        when(bandReadRepository.getByName(anyString())).thenReturn(Optional.ofNullable(mock(Band.class)));
        when(userReadRepository.getByUserNo(anyLong())).thenReturn(Optional.ofNullable(mock(AlingUser.class)));
        when(bandUserRoleReadRepository.findByRoleName(anyString())).thenReturn(
                Optional.ofNullable(mock(BandUserRole.class)));
        when(bandUserReadRepository.findBandUserByBandNameAndUserNo(anyString(), anyLong()))
                .thenReturn(Optional.of(mock(BandUser.class)));
        when(bandUserReadRepository.getIsBlockBandUser(anyString(), anyLong())).thenReturn(false);
        when(bandUserManageRepository.save(any(BandUser.class))).thenReturn(mock(BandUser.class));

        // then
        assertThatThrownBy(() -> bandUserManageService.makeBandUser(bandName, userNo))
                .isInstanceOf(BandUserAlreadyExistsException.class)
                .hasMessageContaining(BandUserAlreadyExistsException.MESSAGE);

        verify(bandReadRepository, times(1)).getByName(anyString());
        verify(userReadRepository, times(1)).getByUserNo(anyLong());
        verify(bandUserRoleReadRepository, times(1)).findByRoleName(anyString());
        verify(bandUserReadRepository, times(1)).findBandUserByBandNameAndUserNo(anyString(), anyLong());
        verify(bandUserReadRepository, times(0)).getIsBlockBandUser(anyString(), anyLong());
        verify(bandUserManageRepository, times(0)).save(any(BandUser.class));
    }

    @Test
    @DisplayName("그룹 회원 생성 실패 테스트_추방된 회원이 가입 하려고 하는 경우")
    void makeBandUser_failTest_bandDeniedException() {
        // given
        String bandName = "bandName";
        Long userNo = 1L;

        // when
        when(bandReadRepository.getByName(anyString())).thenReturn(Optional.ofNullable(mock(Band.class)));
        when(userReadRepository.getByUserNo(anyLong())).thenReturn(Optional.ofNullable(mock(AlingUser.class)));
        when(bandUserRoleReadRepository.findByRoleName(anyString())).thenReturn(
                Optional.ofNullable(mock(BandUserRole.class)));
        when(bandUserReadRepository.findBandUserByBandNameAndUserNo(anyString(), anyLong()))
                .thenReturn(Optional.empty());
        when(bandUserReadRepository.getIsBlockBandUser(anyString(), anyLong())).thenReturn(true);
        when(bandUserManageRepository.save(any(BandUser.class))).thenReturn(null);

        // then
        assertThatThrownBy(() -> bandUserManageService.makeBandUser(bandName, userNo))
                .isInstanceOf(BandDeniedException.class)
                .hasMessageContaining(BandDeniedException.MESSAGE);

        verify(bandReadRepository, times(1)).getByName(anyString());
        verify(userReadRepository, times(1)).getByUserNo(anyLong());
        verify(bandUserRoleReadRepository, times(1)).findByRoleName(anyString());
        verify(bandUserReadRepository, times(1)).findBandUserByBandNameAndUserNo(anyString(), anyLong());
        verify(bandUserReadRepository, times(1)).getIsBlockBandUser(anyString(), anyLong());
        verify(bandUserManageRepository, times(0)).save(any(BandUser.class));
    }

    @Test
    @DisplayName("그룹 회원 삭제 성공 테스트")
    void removeBandUser_successTest() {
        // given

        // when
        when(bandUserReadRepository.findBandUserByBandNameAndUserNo(anyString(), anyLong())).thenReturn(
                Optional.of(bandUser));

        // then
        bandUserManageService.removeBandUser(anyString(), anyLong());

        verify(bandUserReadRepository, times(1)).findBandUserByBandNameAndUserNo(anyString(), anyLong());
    }

    @Test
    @DisplayName("그룹 회원 삭제 실패 테스트 - bandUser not Found")
    void removeBandUser_fail_test_bandUser_notFound() {
        // given

        // when
        when(bandUserReadRepository.findBandUserByBandNameAndUserNo(anyString(), anyLong())).thenReturn(
                Optional.empty());

        // then
        assertThatThrownBy(() -> bandUserManageService.removeBandUser("band name", 1L))
                .isInstanceOf(BandUserNotFoundException.class)
                .hasMessage(BandUserNotFoundException.MESSAGE);

        verify(bandUserReadRepository, times(1)).findBandUserByBandNameAndUserNo(anyString(), anyLong());
    }

    @Test
    @DisplayName("그룹 회원 삭제 실패 테스트 - 그룹의 creator를 지우려고 시도 할 경우")
    void removeBandUser_failTest_bandUserRoleDeniedException() {
        // given
        BandUserRole creator = BandUserRoleDummy.dummyCreator();
        BandUser bandCreator = BandUserDummy.dummy(creator, BandDummy.dummyPublicBand(), UserDummy.dummy());

        // when
        when(bandUserReadRepository.findBandUserByBandNameAndUserNo(anyString(), anyLong())).thenReturn(
                Optional.ofNullable(bandCreator));

        // then
        assertThatThrownBy(() -> bandUserManageService.removeBandUser("band name", 1L))
                .isInstanceOf(BandUserRoleDeniedException.class)
                .hasMessage(BandUserRoleDeniedException.MESSAGE);

        verify(bandUserReadRepository, times(1)).findBandUserByBandNameAndUserNo(anyString(), anyLong());
    }

    @Test
    @DisplayName("그룹 회원 권한 수정 메서드 성공 테스트")
    void modifyRoleOfBandUser_success_test() {
        // given
        ModifyRoleOfBandUserRequestDto modifyRoleOfBandUserRequestDto = new ModifyRoleOfBandUserRequestDto();
        ReflectionTestUtils.setField(modifyRoleOfBandUserRequestDto, "bandUserRoleNo", 1);

        // when
        when(bandUserReadRepository.findBandUserByBandNameAndUserNo(anyString(), anyLong())).thenReturn(
                Optional.of(bandUser));
        when(bandUserRoleReadRepository.findById(anyInt())).thenReturn(Optional.of(bandUserRole));

        // then
        bandUserManageService.modifyRoleOfBandUser(anyString(), anyLong(), modifyRoleOfBandUserRequestDto);

        verify(bandUserReadRepository, times(1)).findBandUserByBandNameAndUserNo(anyString(), anyLong());
        verify(bandUserRoleReadRepository, times(1)).findById(anyInt());
    }

    @Test
    @DisplayName("그룹 회원 권한 수정 메서드 실패 테스트 - bandUser not found")
    void modifyRoleOfBandUser_fail_test_bandUser_notFound() {
        // given

        // when
        when(bandUserReadRepository.findBandUserByBandNameAndUserNo(anyString(), anyLong())).thenReturn(
                Optional.empty());

        // then
        assertThatThrownBy(
                () -> bandUserManageService.modifyRoleOfBandUser("name", 1L, new ModifyRoleOfBandUserRequestDto()))
                .isInstanceOf(BandUserNotFoundException.class)
                .hasMessage(BandUserNotFoundException.MESSAGE);

        verify(bandUserReadRepository, times(1)).findBandUserByBandNameAndUserNo(anyString(), anyLong());
    }

    @Test
    @DisplayName("그룹 회원 권한 수정 메서드 실패 테스트 - bandUserRole not found")
    void modifyRoleOfBandUser_fail_test_bandUserRole_notFound() {
        // given

        // when
        when(bandUserReadRepository.findBandUserByBandNameAndUserNo(anyString(), anyLong())).thenReturn(
                Optional.of(bandUser));
        when(bandUserRoleReadRepository.findById(anyInt())).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(
                () -> bandUserManageService.modifyRoleOfBandUser("name", 1L, new ModifyRoleOfBandUserRequestDto()))
                .isInstanceOf(BandUserRoleNotFoundException.class)
                .hasMessage(BandUserRoleNotFoundException.MESSAGE);

        verify(bandUserReadRepository, times(1)).findBandUserByBandNameAndUserNo(anyString(), anyLong());
    }

    @Test
    @DisplayName("그룹 회원 추방 상태 변경 성공 테스트")
    void modifyBlockOfBandUser_success_test() {
        // given
        ReflectionTestUtils.setField(bandUser, "isBlock", false);
        ReflectionTestUtils.setField(bandUser, "isDelete", false);

        // when
        when(bandUserReadRepository.findBandUserByBandNameAndUserNo(anyString(), anyLong())).thenReturn(
                Optional.of(bandUser));

        // then
        bandUserManageService.modifyBlockOfBandUser(anyString(), anyLong());

        verify(bandUserReadRepository, times(1)).findBandUserByBandNameAndUserNo(anyString(), anyLong());
    }

    @Test
    @DisplayName("그룹 회원 추방 상태 변경 실패 테스트 - bandUser Not Found")
    void modifyBlockOfBandUser_fail_test_bandUser_notFound() {
        // given

        // when
        when(bandUserReadRepository.findBandUserByBandNameAndUserNo(anyString(), anyLong())).thenReturn(
                Optional.empty());

        // then
        assertThatThrownBy(() -> bandUserManageService.modifyBlockOfBandUser(anyString(), anyLong()))
                .isInstanceOf(BandUserNotFoundException.class)
                .hasMessage(BandUserNotFoundException.MESSAGE);

        verify(bandUserReadRepository, times(1)).findBandUserByBandNameAndUserNo(anyString(), anyLong());
    }

    @Test
    @DisplayName("그룹 creator 권한 위임 성공 테스트")
    void modifyCreatorRoleOfBandUser_successTest() {
        // given
        String bandName = "bandName";
        Long targetUserNo = 2L;
        Long userNo = 1L;

        BandUserRole bandUserRoleCreator = BandUserRoleDummy.dummyCreator();
        BandUserRole bandUSerRoleAdmin = BandUserRoleDummy.dummyAdmin();
        BandUser bandUserCreator = BandUserDummy.dummy(bandUserRoleCreator, BandDummy.dummyPublicBand(),
                UserDummy.dummy());

        // when
        when(bandUserReadRepository.findBandUserByBandNameAndUserNo(anyString(), eq(targetUserNo)))
                .thenReturn(Optional.of(bandUser));
        when(bandUserRoleReadRepository.findByRoleName(BandUserRoleEnum.CREATOR.getRoleName())).thenReturn(
                Optional.of(bandUserRoleCreator));
        when(bandUserReadRepository.findBandUserByBandNameAndUserNo(anyString(), eq(userNo)))
                .thenReturn(Optional.of(bandUserCreator));
        when(bandUserRoleReadRepository.findByRoleName(BandUserRoleEnum.ADMIN.getRoleName())).thenReturn(
                Optional.of(bandUSerRoleAdmin));

        // then
        bandUserManageService.modifyCreatorRoleOfBandUser(bandName, targetUserNo, userNo);

        verify(bandUserReadRepository, times(1)).findBandUserByBandNameAndUserNo(anyString(), eq(targetUserNo));
        verify(bandUserRoleReadRepository, times(1)).findByRoleName(BandUserRoleEnum.CREATOR.getRoleName());
        verify(bandUserReadRepository, times(1)).findBandUserByBandNameAndUserNo(anyString(), eq(userNo));
        verify(bandUserRoleReadRepository, times(1)).findByRoleName(BandUserRoleEnum.ADMIN.getRoleName());
    }

    @Test
    @DisplayName("그룹 creator 권한 위임 실패 테스트_위임할 그룹 회원이 존재하지 않는 경우")
    void modifyCreatorRoleOfBandUser_failTest_targetUser_bandUserNotFoundException() {
        // given
        String bandName = "bandName";
        Long targetUserNo = 2L;
        Long userNo = 1L;

        BandUserRole bandUserRoleCreator = BandUserRoleDummy.dummyCreator();
        BandUserRole bandUSerRoleAdmin = BandUserRoleDummy.dummyAdmin();
        BandUser bandUserCreator = BandUserDummy.dummy(bandUserRoleCreator, BandDummy.dummyPublicBand(),
                UserDummy.dummy());

        // when
        when(bandUserReadRepository.findBandUserByBandNameAndUserNo(anyString(), eq(targetUserNo)))
                .thenReturn(Optional.empty());
        when(bandUserRoleReadRepository.findByRoleName(BandUserRoleEnum.CREATOR.getRoleName())).thenReturn(
                Optional.of(bandUserRoleCreator));
        when(bandUserReadRepository.findBandUserByBandNameAndUserNo(anyString(), eq(userNo)))
                .thenReturn(Optional.of(bandUserCreator));
        when(bandUserRoleReadRepository.findByRoleName(BandUserRoleEnum.ADMIN.getRoleName())).thenReturn(
                Optional.of(bandUSerRoleAdmin));

        // then
        assertThatThrownBy(() -> bandUserManageService.modifyCreatorRoleOfBandUser(bandName, targetUserNo, userNo))
                .isInstanceOf(BandUserNotFoundException.class)
                .hasMessage(BandUserNotFoundException.MESSAGE);

        verify(bandUserReadRepository, times(1)).findBandUserByBandNameAndUserNo(anyString(), eq(targetUserNo));
        verify(bandUserRoleReadRepository, times(0)).findByRoleName(BandUserRoleEnum.CREATOR.getRoleName());
        verify(bandUserReadRepository, times(0)).findBandUserByBandNameAndUserNo(anyString(), eq(userNo));
        verify(bandUserRoleReadRepository, times(0)).findByRoleName(BandUserRoleEnum.ADMIN.getRoleName());
    }

    @Test
    @DisplayName("그룹 creator 권한 위임 실패 테스트_그룹 회원 권한 CREATOR가 존재하지 않는 경우")
    void modifyCreatorRoleOfBandUser_failTest_creator_bandUserRoleNotFoundException() {
        // given
        String bandName = "bandName";
        Long targetUserNo = 2L;
        Long userNo = 1L;

        BandUserRole bandUserRoleCreator = BandUserRoleDummy.dummyCreator();
        BandUserRole bandUSerRoleAdmin = BandUserRoleDummy.dummyAdmin();
        BandUser bandUserCreator = BandUserDummy.dummy(bandUserRoleCreator, BandDummy.dummyPublicBand(),
                UserDummy.dummy());

        // when
        when(bandUserReadRepository.findBandUserByBandNameAndUserNo(anyString(), eq(targetUserNo)))
                .thenReturn(Optional.of(bandUser));
        when(bandUserRoleReadRepository.findByRoleName(BandUserRoleEnum.CREATOR.getRoleName())).thenReturn(
                Optional.empty());
        when(bandUserReadRepository.findBandUserByBandNameAndUserNo(anyString(), eq(userNo)))
                .thenReturn(Optional.of(bandUserCreator));
        when(bandUserRoleReadRepository.findByRoleName(BandUserRoleEnum.ADMIN.getRoleName())).thenReturn(
                Optional.of(bandUSerRoleAdmin));

        // then
        assertThatThrownBy(() -> bandUserManageService.modifyCreatorRoleOfBandUser(bandName, targetUserNo, userNo))
                .isInstanceOf(BandUserRoleNotFoundException.class)
                .hasMessage(BandUserRoleNotFoundException.MESSAGE);

        verify(bandUserReadRepository, times(1)).findBandUserByBandNameAndUserNo(anyString(), eq(targetUserNo));
        verify(bandUserRoleReadRepository, times(1)).findByRoleName(BandUserRoleEnum.CREATOR.getRoleName());
        verify(bandUserReadRepository, times(0)).findBandUserByBandNameAndUserNo(anyString(), eq(userNo));
        verify(bandUserRoleReadRepository, times(0)).findByRoleName(BandUserRoleEnum.ADMIN.getRoleName());
    }

    @Test
    @DisplayName("그룹 creator 권한 위임 실패 테스트_그룹 회원이 존재하지 않는 경우")
    void modifyCreatorRoleOfBandUser_failTest_bandUserRoleNotFoundException() {
        // given
        String bandName = "bandName";
        Long targetUserNo = 2L;
        Long userNo = 1L;

        BandUserRole bandUserRoleCreator = BandUserRoleDummy.dummyCreator();
        BandUserRole bandUSerRoleAdmin = BandUserRoleDummy.dummyAdmin();

        // when
        when(bandUserReadRepository.findBandUserByBandNameAndUserNo(anyString(), eq(targetUserNo)))
                .thenReturn(Optional.of(bandUser));
        when(bandUserRoleReadRepository.findByRoleName(BandUserRoleEnum.CREATOR.getRoleName())).thenReturn(
                Optional.of(bandUserRoleCreator));
        when(bandUserReadRepository.findBandUserByBandNameAndUserNo(anyString(), eq(userNo)))
                .thenReturn(Optional.empty());
        when(bandUserRoleReadRepository.findByRoleName(BandUserRoleEnum.ADMIN.getRoleName())).thenReturn(
                Optional.of(bandUSerRoleAdmin));

        // then
        assertThatThrownBy(() -> bandUserManageService.modifyCreatorRoleOfBandUser(bandName, targetUserNo, userNo))
                .isInstanceOf(BandUserNotFoundException.class)
                .hasMessage(BandUserNotFoundException.MESSAGE);

        verify(bandUserReadRepository, times(1)).findBandUserByBandNameAndUserNo(anyString(), eq(targetUserNo));
        verify(bandUserRoleReadRepository, times(1)).findByRoleName(BandUserRoleEnum.CREATOR.getRoleName());
        verify(bandUserReadRepository, times(1)).findBandUserByBandNameAndUserNo(anyString(), eq(userNo));
        verify(bandUserRoleReadRepository, times(0)).findByRoleName(BandUserRoleEnum.ADMIN.getRoleName());
    }

    @Test
    @DisplayName("그룹 creator 권한 위임 실패 테스트_그룹 회원 권한 ADMIN이 존재하지 않는 경우")
    void modifyCreatorRoleOfBandUser_failTest_admin_bandUserRoleNotFoundException() {
        // given
        String bandName = "bandName";
        Long targetUserNo = 2L;
        Long userNo = 1L;

        BandUserRole bandUserRoleCreator = BandUserRoleDummy.dummyCreator();
        BandUser bandUserCreator = BandUserDummy.dummy(bandUserRoleCreator, BandDummy.dummyPublicBand(),
                UserDummy.dummy());

        // when
        when(bandUserReadRepository.findBandUserByBandNameAndUserNo(anyString(), eq(targetUserNo)))
                .thenReturn(Optional.of(bandUser));
        when(bandUserRoleReadRepository.findByRoleName(BandUserRoleEnum.CREATOR.getRoleName())).thenReturn(
                Optional.of(bandUserRoleCreator));
        when(bandUserReadRepository.findBandUserByBandNameAndUserNo(anyString(), eq(userNo)))
                .thenReturn(Optional.of(bandUserCreator));
        when(bandUserRoleReadRepository.findByRoleName(BandUserRoleEnum.ADMIN.getRoleName())).thenReturn(
                Optional.empty());

        // then
        assertThatThrownBy(() -> bandUserManageService.modifyCreatorRoleOfBandUser(bandName, targetUserNo, userNo))
                .isInstanceOf(BandUserRoleNotFoundException.class)
                .hasMessage(BandUserRoleNotFoundException.MESSAGE);

        verify(bandUserReadRepository, times(1)).findBandUserByBandNameAndUserNo(anyString(), eq(targetUserNo));
        verify(bandUserRoleReadRepository, times(1)).findByRoleName(BandUserRoleEnum.CREATOR.getRoleName());
        verify(bandUserReadRepository, times(1)).findBandUserByBandNameAndUserNo(anyString(), eq(userNo));
        verify(bandUserRoleReadRepository, times(1)).findByRoleName(BandUserRoleEnum.ADMIN.getRoleName());
    }

}

