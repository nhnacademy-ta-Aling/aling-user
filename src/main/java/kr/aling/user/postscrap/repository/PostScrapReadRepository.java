package kr.aling.user.postscrap.repository;

import kr.aling.user.postscrap.entity.PostScrap;
import kr.aling.user.postscrap.entity.PostScrap.Pk;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 게시물 스크랩 조회 JpaRepository.
 *
 * @author 이수정
 * @since 1.0
 */
public interface PostScrapReadRepository extends JpaRepository<PostScrap, Pk>, PostScrapReadRepositoryCustom {

    /**
     * 게시물 번호로 게시물의 스크랩된 횟수를 얻습니다.
     *
     * @param postNo 조회할 게시물 번호
     * @return 게시물의 스크랩 횟수
     * @author 이수정
     * @since 1.0
     */
    Long countByPk_PostNo(Long postNo);
}
