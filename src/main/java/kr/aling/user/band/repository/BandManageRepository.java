package kr.aling.user.band.repository;

import kr.aling.user.band.entity.Band;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 그룹(Band)을 관리 하는 Repository.
 *
 * @author : 정유진
 * @since : 1.0
 **/
public interface BandManageRepository extends JpaRepository<Band, Long> {
}
