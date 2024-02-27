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
public interface PostScrapReadRepository extends JpaRepository<PostScrap, Pk> {

}
