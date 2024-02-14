package kr.aling.user.normaluser.repository;

import static org.assertj.core.api.Assertions.assertThat;

import kr.aling.user.normaluser.dummy.NormalUserDummy;
import kr.aling.user.normaluser.entity.NormalUser;
import kr.aling.user.normaluser.type.NormalUserOpen;
import kr.aling.user.user.dummy.UserDummy;
import kr.aling.user.user.entity.AlingUser;
import kr.aling.user.wantjobtype.dummy.WantJobTypeDummy;
import kr.aling.user.wantjobtype.entity.WantJobType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@DataJpaTest
class NormalUserManageRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private NormalUserManageRepository normalUserManageRepository;

    private AlingUser alingUser;
    private WantJobType wantJobType;
    private NormalUser normalUser;

    @BeforeEach
    void setUp() {
        alingUser = UserDummy.dummyEncoder(new BCryptPasswordEncoder());
        wantJobType = WantJobTypeDummy.dummy();

        testEntityManager.persist(alingUser);
        testEntityManager.persist(wantJobType);

        normalUser = NormalUserDummy.dummy(alingUser, wantJobType);

        testEntityManager.persist(normalUser);
    }

    @Test
    @DisplayName("일반회원 저장")
    void save() {
        // when
        NormalUser savedNormalUser = normalUserManageRepository.save(normalUser);

        // then
        assertThat(savedNormalUser).isNotNull();
        assertThat(savedNormalUser.getAlingUser()).isEqualTo(alingUser);
        assertThat(savedNormalUser.getWantJobType()).isEqualTo(wantJobType);
        assertThat(savedNormalUser.getPhoneNo()).isEqualTo(normalUser.getPhoneNo());
        assertThat(savedNormalUser.getBirth()).isEqualTo(normalUser.getBirth());
        assertThat(savedNormalUser.getEduIsOpen()).isEqualTo(NormalUserOpen.ALL.name());
        assertThat(savedNormalUser.getCareerIsOpen()).isEqualTo(NormalUserOpen.ALL.name());
        assertThat(savedNormalUser.getCertIsOpen()).isEqualTo(NormalUserOpen.ALL.name());
        assertThat(savedNormalUser.getResumeIsOpen()).isEqualTo(NormalUserOpen.ALL.name());
        assertThat(savedNormalUser.getCoverLetterIsOpen()).isEqualTo(NormalUserOpen.ALL.name());
        assertThat(savedNormalUser.getTechSkillIsOpen()).isEqualTo(NormalUserOpen.ALL.name());
    }
}
