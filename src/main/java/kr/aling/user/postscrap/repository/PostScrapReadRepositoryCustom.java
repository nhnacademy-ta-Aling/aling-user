package kr.aling.user.postscrap.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * QueryDsl 게시물 스크랩 조회를 위한 interface.
 *
 * @author 이수정
 * @since 1.0
 */
@NoRepositoryBean
public interface PostScrapReadRepositoryCustom {

    /**
     * 특정 회원이 스크랩한 게시물을 페이징 조회합니다.
     *
     * @param userNo   회원 번호
     * @param pageable 페이징 정보를 담은 객체
     * @return 페이징 조회된 스크랩한 게시물 번호 리스트
     * @author 이수정
     * @since 1.0
     */
    Page<Long> findPostNoByUserNo(Long userNo, Pageable pageable);
}
