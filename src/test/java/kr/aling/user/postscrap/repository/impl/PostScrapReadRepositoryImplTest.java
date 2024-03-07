package kr.aling.user.postscrap.repository.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kr.aling.user.postscrap.dto.response.ReadPostScrapsUserResponseDto;
import kr.aling.user.postscrap.entity.PostScrap;
import kr.aling.user.postscrap.repository.PostScrapReadRepository;
import kr.aling.user.user.dummy.UserDummy;
import kr.aling.user.user.entity.AlingUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@DataJpaTest
class PostScrapReadRepositoryImplTest {

    @Autowired
    private PostScrapReadRepository postScrapReadRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    @DisplayName("회원 번호 기준 스크랩한 게시물 페이징 조회 성공")
    void findPostNoByUserNo() {
        // given
        Pageable pageable = PageRequest.of(0, 3);
        AlingUser alingUser = testEntityManager.persist(UserDummy.dummy());
        testEntityManager.persist(
                new PostScrap(PostScrap.Pk.builder().userNo(alingUser.getUserNo()).postNo(1L).build(), alingUser));

        // when
        Page<Long> page = postScrapReadRepository.findPostNoByUserNo(alingUser.getUserNo(), pageable);

        // then
        assertThat(page).isNotNull();
        assertThat(page.getContent().get(0).longValue()).isEqualTo(1L);
    }

    @Test
    @DisplayName("게시물을 스크랩한 회원의 간단 정보 목록 조회")
    void getUsersByPostNo() {
        // given
        Long postNo = 1L;
        AlingUser alingUser = testEntityManager.persist(UserDummy.dummy());
        testEntityManager.persist(
                new PostScrap(PostScrap.Pk.builder().userNo(alingUser.getUserNo()).postNo(postNo).build(), alingUser));

        // when
        List<ReadPostScrapsUserResponseDto> list = postScrapReadRepository.getUsersByPostNo(postNo);

        // then
        assertThat(list).isNotNull();
        assertThat(list.get(0).getUserNo()).isEqualTo(alingUser.getUserNo());
        assertThat(list.get(0).getName()).isEqualTo(alingUser.getName());
        assertThat(list.get(0).getFileNo()).isEqualTo(alingUser.getFileNo());
    }
}