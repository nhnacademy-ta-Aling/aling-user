package kr.aling.user.band.repository;

import kr.aling.user.band.entity.Band;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 그룹(Band)을 조회 하는 Repository.
 *
 * @author : 정유진
 * @since : 1.0
 **/
public interface BandReadRepository extends JpaRepository<Band, Long>, BandReadRepositoryCustom {

    /**
     * 그룹 명을 통해 그룹 존재 여부를 확인 하는 메서드입니다.
     *
     * @param bandName 그룹 명
     * @return 존재 여부 boolean
     */
    boolean existsBandByName(String bandName);
}
