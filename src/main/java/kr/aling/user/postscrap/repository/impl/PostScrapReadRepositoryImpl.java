package kr.aling.user.postscrap.repository.impl;

import com.querydsl.jpa.JPQLQuery;
import java.util.List;
import kr.aling.user.postscrap.entity.PostScrap;
import kr.aling.user.postscrap.entity.QPostScrap;
import kr.aling.user.postscrap.repository.PostScrapReadRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

/**
 * QueryDsl 게시물 스크랩 조회를 위한 repository.
 *
 * @author 이수정
 * @since 1.0
 */
@Repository
public class PostScrapReadRepositoryImpl extends QuerydslRepositorySupport implements PostScrapReadRepositoryCustom {

    public PostScrapReadRepositoryImpl() {
        super(PostScrap.class);
    }

    /**
     * {@inheritDoc}
     *
     * @param userNo   회원 번호
     * @param pageable 페이징 정보를 담은 객체
     * @return 페이징 조회된 스크랩한 게시물 번호 리스트
     */
    @Override
    public Page<Long> findPostNoByUserNo(Long userNo, Pageable pageable) {
        QPostScrap postScrap = QPostScrap.postScrap;

        List<Long> postNos = from(postScrap)
                .where(postScrap.pk.userNo.eq(userNo))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .select(postScrap.pk.postNo)
                .fetch();

        JPQLQuery<Long> countQuery = from(postScrap)
                .where(postScrap.pk.userNo.eq(userNo))
                .select(postScrap.count());

        return PageableExecutionUtils.getPage(postNos, pageable, countQuery::fetchFirst);
    }
}
