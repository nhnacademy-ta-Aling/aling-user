package kr.aling.user.banduser.repository;

import static org.assertj.core.api.Assertions.assertThat;

import kr.aling.user.band.dummy.BandDummy;
import kr.aling.user.band.entity.Band;
import kr.aling.user.banduser.dto.response.BandPostUerQueryDto;
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
import org.springframework.test.util.ReflectionTestUtils;

@DataJpaTest
class BandUserReadRepositoryTest {

    @Autowired
    TestEntityManager testEntityManager;

    @Autowired
    BandUserReadRepository bandUserReadRepository;

    BandUserRole bandUserRole;
    Band band;
    AlingUser alingUser;

    @BeforeEach
    void setUp() {
        bandUserRole = BandUserRoleDummy.dummyDefaultBandUserRole();
        band = BandDummy.dummyPublicBand();
        alingUser = UserDummy.dummy();
        ReflectionTestUtils.setField(alingUser, "fileNo", 1L);

        testEntityManager.persist(bandUserRole);
        testEntityManager.persist(band);
        testEntityManager.persist(alingUser);
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