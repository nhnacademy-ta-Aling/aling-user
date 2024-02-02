package kr.aling.user.wantjobtype.repository;

import kr.aling.user.wantjobtype.entity.WantJobType;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 구직희망타입 조회 JpaRepository.
 *
 * @author : 이수정
 * @since : 1.0
 */
public interface WantJobTypeReadRepository extends JpaRepository<WantJobType, Integer> {}
