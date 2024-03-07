package kr.aling.user.postscrap.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import kr.aling.user.common.dto.PageResponseDto;
import kr.aling.user.common.feignclient.PostFeignClient;
import kr.aling.user.post.dto.response.IsExistsPostResponseDto;
import kr.aling.user.post.dto.response.ReadPostsForScrapResponseDto;
import kr.aling.user.post.exception.PostNotFoundException;
import kr.aling.user.postscrap.dto.response.IsExistsPostScrapResponseDto;
import kr.aling.user.postscrap.dto.response.NumberOfPostScrapResponseDto;
import kr.aling.user.postscrap.dto.response.ReadPostScrapsPostResponseDto;
import kr.aling.user.postscrap.dto.response.ReadPostScrapsUserResponseDto;
import kr.aling.user.postscrap.repository.PostScrapReadRepository;
import kr.aling.user.postscrap.service.PostScrapReadService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

class PostScrapReadServiceImplTest {

    private PostScrapReadService postScrapReadService;

    private PostScrapReadRepository postScrapReadRepository;

    private PostFeignClient postFeignClient;

    @BeforeEach
    void setUp() {
        postScrapReadRepository = mock(PostScrapReadRepository.class);
        postFeignClient = mock(PostFeignClient.class);

        postScrapReadService = new PostScrapReadServiceImpl(
                postScrapReadRepository,
                postFeignClient
        );
    }

    @Test
    @DisplayName("스크랩한 게시물인지 확인 성공")
    void isExistsPostScrap() {
        // given
        Long postNo = 1L;
        Long userNo = 1L;

        when(postScrapReadRepository.existsById(any())).thenReturn(Boolean.TRUE);

        // when
        IsExistsPostScrapResponseDto response = postScrapReadService.isExistsPostScrap(postNo, userNo);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getIsScrap()).isTrue();

        verify(postScrapReadRepository, times(1)).existsById(any());
    }

    @Test
    @DisplayName("스크랩 횟수 조회 성공")
    void getNumberOfPostScrap() {
        // given
        Long postNo = 1L;
        Long number = 100L;

        when(postScrapReadRepository.countByPk_PostNo(anyLong())).thenReturn(number);

        // when
        NumberOfPostScrapResponseDto response = postScrapReadService.getNumberOfPostScrap(postNo);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getNumber()).isEqualTo(number);

        verify(postScrapReadRepository, times(1)).countByPk_PostNo(anyLong());
    }

    @Test
    @DisplayName("스크랩용 게시물 페이징 조회 성공")
    void getPostScraps() {
        // given
        Long userNo = 1L;
        Pageable pageable = PageRequest.of(0, 3);

        Page<Long> page = mock(Page.class);
        when(postScrapReadRepository.findPostNoByUserNo(anyLong(), any())).thenReturn(page);
        when(page.getPageable()).thenReturn(pageable);
        when(page.getTotalPages()).thenReturn(1);
        when(page.getTotalElements()).thenReturn(1L);

        ResponseEntity<ReadPostsForScrapResponseDto> responseEntity = mock(ResponseEntity.class);
        when(postFeignClient.getPostsForScrap(any())).thenReturn(responseEntity);

        ReadPostScrapsPostResponseDto readPostScrapsResponseDto = new ReadPostScrapsPostResponseDto();
        ReflectionTestUtils.setField(readPostScrapsResponseDto, "postNo", 1L);
        ReflectionTestUtils.setField(readPostScrapsResponseDto, "content", "1");
        ReflectionTestUtils.setField(readPostScrapsResponseDto, "isDelete", false);
        ReflectionTestUtils.setField(readPostScrapsResponseDto, "isOpen", true);

        ReadPostsForScrapResponseDto readPostsForScrapResponseDto = new ReadPostsForScrapResponseDto();
        ReflectionTestUtils.setField(readPostsForScrapResponseDto, "infos", List.of(readPostScrapsResponseDto));

        when(responseEntity.getBody()).thenReturn(readPostsForScrapResponseDto);

        // when
        PageResponseDto<ReadPostScrapsPostResponseDto> response =
                postScrapReadService.getPostScrapsPost(userNo, pageable);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getPageNumber()).isZero();
        assertThat(response.getTotalPages()).isEqualTo(1);
        assertThat(response.getTotalElements()).isEqualTo(1);
        assertThat(response.getContent().get(0).getPostNo()).isEqualTo(1L);
        assertThat(response.getContent().get(0).getContent()).isEqualTo("1");
        assertThat(response.getContent().get(0).getIsDelete()).isFalse();
        assertThat(response.getContent().get(0).getIsOpen()).isTrue();

        verify(postScrapReadRepository, times(1)).findPostNoByUserNo(anyLong(), any());
        verify(postFeignClient, times(1)).getPostsForScrap(any());
    }

    @Test
    @DisplayName("게시물을 스크랩한 회원 목록 조회 성공")
    void getPostScrapsUser() {
        // given
        Long postNo = 1L;

        Long userNo = 1L;
        String name = "Aling";
        Long fileNo = 1L;

        ResponseEntity<IsExistsPostResponseDto> responseEntity = mock(ResponseEntity.class);
        when(postFeignClient.isExistsPost(anyLong())).thenReturn(responseEntity);

        IsExistsPostResponseDto isExistsPostResponseDto = new IsExistsPostResponseDto();
        ReflectionTestUtils.setField(isExistsPostResponseDto, "isExists", Boolean.TRUE);
        when(responseEntity.getBody()).thenReturn(isExistsPostResponseDto);

        ReadPostScrapsUserResponseDto responseDto = new ReadPostScrapsUserResponseDto(userNo, name, fileNo);
        when(postScrapReadRepository.getUsersByPostNo(anyLong())).thenReturn(List.of(responseDto));

        // when
        List<ReadPostScrapsUserResponseDto> list = postScrapReadService.getPostScrapsUser(postNo);

        // then
        assertThat(list).isNotNull();
        assertThat(list.get(0).getUserNo()).isEqualTo(userNo.intValue());
        assertThat(list.get(0).getName()).isEqualTo(name);
        assertThat(list.get(0).getFileNo()).isEqualTo(fileNo.intValue());

        verify(postFeignClient, times(1)).isExistsPost(anyLong());
        verify(postScrapReadRepository, times(1)).getUsersByPostNo(anyLong());
    }

    @Test
    @DisplayName("게시물을 스크랩한 회원 목록 조회 실패 - 존재하지 않는 게시물인 ㄱ여우")
    void getPostScrapsUser_postNotFound() {
        // given
        Long postNo = 1L;

        ResponseEntity<IsExistsPostResponseDto> responseEntity = mock(ResponseEntity.class);
        when(postFeignClient.isExistsPost(anyLong())).thenReturn(responseEntity);

        IsExistsPostResponseDto isExistsPostResponseDto = new IsExistsPostResponseDto();
        ReflectionTestUtils.setField(isExistsPostResponseDto, "isExists", Boolean.FALSE);
        when(responseEntity.getBody()).thenReturn(isExistsPostResponseDto);

        // when
        // then
        assertThatThrownBy(() -> postScrapReadService.getPostScrapsUser(postNo))
                .isInstanceOf(PostNotFoundException.class);

        verify(postFeignClient, times(1)).isExistsPost(anyLong());
        verify(postScrapReadRepository, never()).getUsersByPostNo(anyLong());
    }
}