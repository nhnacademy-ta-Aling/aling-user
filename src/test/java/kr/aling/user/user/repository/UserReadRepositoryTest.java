package kr.aling.user.user.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import kr.aling.user.role.dummy.RoleDummy;
import kr.aling.user.role.entity.Role;
import kr.aling.user.role.repository.RoleReadRepository;
import kr.aling.user.user.dto.response.LoginInfoResponseDto;
import kr.aling.user.user.dummy.UserDummy;
import kr.aling.user.user.entity.AlingUser;
import kr.aling.user.userrole.entity.UserRole;
import kr.aling.user.userrole.repository.UserRoleManageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

/**
 * 회원 조회 Repository 테스트
 *
 * @author 정유진
 * @since 1.0
 **/
@DataJpaTest
class UserReadRepositoryTest {

    @Autowired
    private UserReadRepository userReadRepository;
    @Autowired
    private RoleReadRepository roleReadRepository;
    @Autowired
    private UserRoleManageRepository userRoleManageRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    AlingUser alingUser;

    @BeforeEach
    void setUp() {
        alingUser = UserDummy.dummy();

        testEntityManager.persist(alingUser);
    }

    @Test
    @DisplayName("회원 번호로 회원 엔티티 조회 테스트")
    void getByUserNo_successTest() {
        // given
        Long userNo = alingUser.getUserNo();

        // when

        // then
        Optional<AlingUser> result = userReadRepository.getByUserNo(userNo);
        assertThat(result).isPresent();
        assertThat(result.get().getUserNo()).isEqualTo(alingUser.getUserNo());
        assertThat(result.get().getEmail()).isEqualTo(alingUser.getEmail());
        assertThat(result.get().getPassword()).isEqualTo(alingUser.getPassword());
        assertThat(result.get().getName()).isEqualTo(alingUser.getName());
        assertThat(result.get().getIsDelete()).isEqualTo(alingUser.getIsDelete());
        assertThat(result.get().getDeleteAt()).isEqualTo(alingUser.getDeleteAt());
        assertThat(result.get().getFileNo()).isEqualTo(alingUser.getFileNo());
        assertThat(result.get().getIsBlock()).isEqualTo(alingUser.getIsBlock());
        assertThat(result.get().getDescription()).isEqualTo(alingUser.getDescription());
        assertThat(result.get().getAddress()).isEqualTo(alingUser.getAddress());
        assertThat(result.get().getFixPostNo()).isEqualTo(alingUser.getFixPostNo());
    }

    @Test
    @DisplayName("이메일 중복 여부 테스트")
    void userExistsByEmailTest() {
        String email = alingUser.getEmail();

        boolean result = userReadRepository.existsByEmail(email);
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("이메일로 회원번호, 비밀번호 조회 테스트")
    void findByEmailForLoginTest() {
        String email = alingUser.getEmail();

        Optional<LoginInfoResponseDto> optionalResponseDto =
                userReadRepository.findByEmailForLogin(email);

        assertThat(optionalResponseDto).isPresent();
        LoginInfoResponseDto responseDto = optionalResponseDto.get();
        assertThat(responseDto.getUserNo()).isEqualTo(alingUser.getUserNo());
        assertThat(responseDto.getPassword()).isEqualTo(alingUser.getPassword());
    }

    @Test
    @DisplayName("회원번호로 권한 조회 테스트")
    void findRolesByUserNoTest() {
        Long userNo = alingUser.getUserNo();
        Role role = RoleDummy.dummy();
        UserRole userRole = new UserRole(
                new UserRole.Pk(userNo, role.getRoleNo()), alingUser, role);

        roleReadRepository.save(role);
        userRoleManageRepository.save(userRole);

        List<String> results = userReadRepository.findRolesByUserNo(userNo);

        assertThat(results.size()).isEqualTo(1);
        assertThat(results.get(0)).isEqualTo(role.getName());
    }
}
