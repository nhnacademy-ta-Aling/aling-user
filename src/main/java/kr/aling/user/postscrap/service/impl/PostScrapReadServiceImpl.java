package kr.aling.user.postscrap.service.impl;

import java.util.List;
import java.util.Objects;
import kr.aling.user.common.annotation.ReadService;
import kr.aling.user.common.dto.PageResponseDto;
import kr.aling.user.common.feign.PostFeignClient;
import kr.aling.user.post.dto.request.ReadPostsForScrapRequestDto;
import kr.aling.user.post.exception.PostNotFoundException;
import kr.aling.user.postscrap.dto.response.IsExistsPostScrapResponseDto;
import kr.aling.user.postscrap.dto.response.NumberOfPostScrapResponseDto;
import kr.aling.user.postscrap.dto.response.ReadPostScrapsPostResponseDto;
import kr.aling.user.postscrap.dto.response.ReadPostScrapsUserResponseDto;
import kr.aling.user.postscrap.entity.PostScrap;
import kr.aling.user.postscrap.repository.PostScrapReadRepository;
import kr.aling.user.postscrap.service.PostScrapReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 게시물 스크랩 조회 Service 구현체.
 *
 * @author 이수정
 * @since 1.0
 */
@RequiredArgsConstructor
@ReadService
public class PostScrapReadServiceImpl implements PostScrapReadService {

    private final PostScrapReadRepository postScrapReadRepository;

    private final PostFeignClient postFeignClient;

    /**
     * {@inheritDoc}
     *
     * @param postNo 확인할 게시물 번호
     * @param userNo 확인하는 회원의 번호
     * @return 게시물 스크랩 여부
     */
    @Override
    public IsExistsPostScrapResponseDto isExistsPostScrap(Long postNo, Long userNo) {
        return new IsExistsPostScrapResponseDto(
                postScrapReadRepository.existsById(PostScrap.Pk.builder().postNo(postNo).userNo(userNo).build()));
    }

    /**
     * {@inheritDoc}
     *
     * @param postNo 조회할 게시물 번호
     * @return 게시물 스크랩 횟수
     */
    @Override
    public NumberOfPostScrapResponseDto getNumberOfPostScrap(Long postNo) {
        return new NumberOfPostScrapResponseDto(postScrapReadRepository.countByPk_PostNo(postNo));
    }

    /**
     * {@inheritDoc}
     *
     * @param userNo   조회하는 회원의 번호
     * @param pageable 페이징 정보를 담는 Pageable 객체
     * @return 페이징 조회된 게시물 스크랩의 게시물 목록
     */
    @Override
    public PageResponseDto<ReadPostScrapsPostResponseDto> getPostScrapsPost(Long userNo, Pageable pageable) {
        Page<Long> postNos = postScrapReadRepository.findPostNoByUserNo(userNo, pageable);

        List<ReadPostScrapsPostResponseDto> postScraps = Objects.requireNonNull(postFeignClient.getPostsForScrap(
                new ReadPostsForScrapRequestDto(postNos.getContent())).getBody()).getInfos();

        return new PageResponseDto<>(
                postNos.getPageable().getPageNumber(),
                postNos.getTotalPages(),
                postNos.getTotalElements(),
                postScraps
        );
    }

    /**
     * {@inheritDoc}
     *
     * @param postNo 게시물 번호
     * @return 게시물 번호로 조회된 스크랩한 회원 리스트
     */
    @Override
    public List<ReadPostScrapsUserResponseDto> getPostScrapsUser(Long postNo) {
        if (Boolean.FALSE.equals(
                Objects.requireNonNull(postFeignClient.isExistsPost(postNo).getBody()).getIsExists())) {
            throw new PostNotFoundException(postNo);
        }
        return postScrapReadRepository.getUsersByPostNo(postNo);
    }
}
