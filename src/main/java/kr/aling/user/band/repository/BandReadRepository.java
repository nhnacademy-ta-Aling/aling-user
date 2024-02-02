package kr.aling.user.band.repository;

import kr.aling.user.band.entity.Band;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Some description here.
 *
 * @author 정유진
 * @since 1.0
 **/
public interface BandReadRepository extends JpaRepository<Band, Long> {
}
