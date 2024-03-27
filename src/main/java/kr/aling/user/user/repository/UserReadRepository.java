package kr.aling.user.user.repository;

import java.util.Optional;
import kr.aling.user.user.entity.AlingUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 유저 읽기 전용 레포지토리.
 *
 * @author : 여운석
 * @since : 1.0
 **/
public interface UserReadRepository extends JpaRepository<AlingUser, Long>, UserReadRepositoryCustom {

    Optional<AlingUser> findByEmail(String email);
}
