package kr.aling.user.postscrap.repository;

import kr.aling.user.postscrap.entity.PostScrap;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 게시물 스크랩 CUD JpaRepository.
 *
 * @author 이수정
 * @since 1.0
 */
public interface PostScrapManageRepository extends JpaRepository<PostScrap, PostScrap.Pk> {

}
