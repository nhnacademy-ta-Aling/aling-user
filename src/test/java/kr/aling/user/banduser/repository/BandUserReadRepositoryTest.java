package kr.aling.user.banduser.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import kr.aling.user.band.dummy.BandDummy;
import kr.aling.user.band.entity.Band;
import kr.aling.user.banduser.dto.response.BandPostUerQueryDto;
import kr.aling.user.banduser.dto.response.GetBandUserAndUserInfoResponseDto;
import kr.aling.user.banduser.dto.response.GetBandUserAuthResponseDto;
import kr.aling.user.banduser.dto.response.GetBandUserInfoResponseDto;
import kr.aling.user.banduser.dummy.BandUserDummy;
import kr.aling.user.banduser.entity.BandUser;
import kr.aling.user.banduserrole.dummy.BandUserRoleDummy;
import kr.aling.user.banduserrole.entity.BandUserRole;
import kr.aling.user.user.dummy.UserDummy;
import kr.aling.user.user.entity.AlingUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * 그룹 회원 조회 repository 테스트.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@DataJpaTest
class BandUserReadRepositoryTest {

    @Autowired
    TestEntityManager testEntityManager;

    @Autowired
    BandUserReadRepository bandUserReadRepository;

    BandUserRole bandUserRole;
    Band band;
    AlingUser alingUser;
    BandUser bandUser;

    @BeforeEach
    void setUp() {
        bandUserRole = BandUserRoleDummy.dummyCreator();
        band = BandDummy.dummyPublicBand();
        alingUser = UserDummy.dummy();
        bandUser = BandUserDummy.dummy(bandUserRole, band, alingUser);
        ReflectionTestUtils.setField(alingUser, "fileNo", 1L);

        testEntityManager.persist(bandUserRole);
        testEntityManager.persist(band);
        testEntityManager.persist(alingUser);
        testEntityManager.persist(bandUser);
    }

    @Test
    @DisplayName("그룹 명을 통해 그룹 회원 정보 페이징 조회 테스트")
    void getBandUserListByBandName_successTest() {
        // given
        String bandName = band.getName();
        Pageable pageable = PageRequest.of(0, 10);

        // when

        // then
        Page<GetBandUserAndUserInfoResponseDto> bandUserPage =
                bandUserReadRepository.getBandUserListByBandName(bandName, pageable);

        assertThat(bandUserPage.getTotalPages()).isEqualTo(1L);
        assertThat(bandUserPage.getTotalElements()).isEqualTo(1L);
        assertThat(bandUserPage.getContent()).isNotNull();
        assertThat(bandUserPage.getContent().get(0).getBandUserInfo().getBandUserRoleNo()).isEqualTo(
                bandUserRole.getBandUserRoleNo());
        assertThat(bandUserPage.getContent().get(0).getBandUserInfo().getBandUserNo()).isEqualTo(
                bandUser.getBandUserNo());
        assertThat(bandUserPage.getContent().get(0).getUserInfo().getUserNo()).isEqualTo(alingUser.getUserNo());
        assertThat(bandUserPage.getContent().get(0).getUserInfo().getName()).isEqualTo(alingUser.getName());
        assertThat(bandUserPage.getContent().get(0).getUserInfo().getFileNo()).isEqualTo(alingUser.getFileNo());
    }

    @Test
    @DisplayName("그룹 명과 회원 번호로 그룹 회원 정보 조회 테스트")
    void getBandUserInfoByUserNoAndBandName_successTest() {
        // given
        String bandName = band.getName();
        Long userNo = alingUser.getUserNo();

        // when

        // then
        Optional<GetBandUserInfoResponseDto> bandUserInfo =
                bandUserReadRepository.getBandUserInfoByUserNoAndBandName(bandName, userNo);

        assertThat(bandUserInfo).isPresent();
        assertThat(bandUserInfo.get().getBandUserNo()).isEqualTo(bandUser.getBandUserNo());
        assertThat(bandUserInfo.get().getBandUserRoleNo()).isEqualTo(bandUserRole.getBandUserRoleNo());
    }

    @Test
    @DisplayName("그룹 번호와 회원 번호로 그룹 회원 정보 조회 테스트")
    void getBandUserInfoByBandNoAndUserNo_successTest() {
        // given
        Long bandNo = band.getBandNo();
        Long userNo = alingUser.getUserNo();

        // when

        // then
        Optional<GetBandUserAuthResponseDto> bandUserInfo =
                bandUserReadRepository.getBandUserInfoByBandNoAndUserNo(bandNo, userNo);

        assertThat(bandUserInfo).isPresent();
        assertThat(bandUserInfo.get().getBandUserRoleName()).isEqualTo(bandUserRole.getRoleName());
    }

    @Test
    @DisplayName("그룹 명과 회원 번호로 그룹 회원 조회 테스트")
    void findBandUserByBandNameAndUserNo_successTest() {
        // given
        String bandName = band.getName();
        Long userNo = alingUser.getUserNo();

        // when

        // then
        Optional<BandUser> bandUserResult =
                bandUserReadRepository.findBandUserByBandNameAndUserNo(bandName, userNo);

        assertThat(bandUserResult).isPresent();
        assertThat(bandUserResult.get().getBandUserNo()).isEqualTo(bandUser.getBandUserNo());
        assertThat(bandUserResult.get().getBandUserRole()).isEqualTo(bandUserRole);
        assertThat(bandUserResult.get().getBand()).isEqualTo(band);
        assertThat(bandUserResult.get().getAlingUser()).isEqualTo(alingUser);
        assertThat(bandUserResult.get().getEnterAt()).isEqualTo(bandUser.getEnterAt());
        assertThat(bandUserResult.get().getIsBlock()).isEqualTo(bandUser.getIsBlock());
        assertThat(bandUserResult.get().getBlockReason()).isEqualTo(bandUser.getBlockReason());
        assertThat(bandUserResult.get().getIsDelete()).isEqualTo(bandUser.getIsDelete());
    }

    @Test
    @DisplayName("회원의 그룹 권한 개수 조회 메서드 테스트")
    void countByUserNoAndBandUserRoleName() {
        // given
        Long userNo = alingUser.getUserNo();
        String roleName = bandUserRole.getRoleName();

        // when

        // then
        long count = bandUserReadRepository.countByUserNoAndBandUserRoleName(userNo, roleName);

        assertThat(count).isEqualTo(1L);
    }


    @Test
    @DisplayName("그룹 게시글 작성자 정보 조회 테스트")
    void bandPost_writer_user_info_test() {
        // given
        BandUser bandUser = BandUserDummy.dummy(bandUserRole, band, alingUser);

        BandUser save = bandUserReadRepository.save(bandUser);

        // when
        BandPostUerQueryDto result = bandUserReadRepository.getBandUserForPost(save.getBandUserNo());

        // then
        assertThat(result).isNotNull();
        assertThat(result.getUserNo()).isEqualTo(alingUser.getUserNo());
        assertThat(result.getUsername()).isEqualTo(alingUser.getName());
        assertThat(result.getFileNo()).isEqualTo(alingUser.getFileNo());
    }
}