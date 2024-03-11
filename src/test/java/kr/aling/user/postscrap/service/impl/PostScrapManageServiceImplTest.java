package kr.aling.user.postscrap.service.impl;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import kr.aling.user.common.feignclient.PostFeignClient;
import kr.aling.user.post.dto.response.IsExistsPostResponseDto;
import kr.aling.user.post.exception.PostNotFoundException;
import kr.aling.user.postscrap.entity.PostScrap;
import kr.aling.user.postscrap.entity.PostScrap.Pk;
import kr.aling.user.postscrap.repository.PostScrapManageRepository;
import kr.aling.user.postscrap.repository.PostScrapReadRepository;
import kr.aling.user.postscrap.service.PostScrapManageService;
import kr.aling.user.user.dummy.UserDummy;
import kr.aling.user.user.exception.UserNotFoundException;
import kr.aling.user.user.service.UserReadService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

class PostScrapManageServiceImplTest {

    private PostScrapManageService postScrapManageService;

    private PostScrapManageRepository postScrapManageRepository;
    private PostScrapReadRepository postScrapReadRepository;

    private UserReadService userReadService;

    private PostFeignClient postFeignClient;

    @BeforeEach
    void setUp() {
        postScrapManageRepository = mock(PostScrapManageRepository.class);
        postScrapReadRepository = mock(PostScrapReadRepository.class);
        userReadService = mock(UserReadService.class);
        postFeignClient = mock(PostFeignClient.class);

        postScrapManageService = new PostScrapManageServiceImpl(
                postScrapManageRepository,
                postScrapReadRepository,
                userReadService,
                postFeignClient
        );
    }

    @Test
    @DisplayName("게시물 스크랩 성공 - 이미 존재하는 경우")
    void postScrap_exists() {
        // given
        Long postNo = 1L;
        Long userNo = 1L;

        ResponseEntity<IsExistsPostResponseDto> responseEntity = mock(ResponseEntity.class);
        when(postFeignClient.isExistsPost(anyLong())).thenReturn(responseEntity);

        IsExistsPostResponseDto responseDto = new IsExistsPostResponseDto();
        ReflectionTestUtils.setField(responseDto, "isExists", Boolean.TRUE);
        when(responseEntity.getBody()).thenReturn(responseDto);

        PostScrap postScrap = new PostScrap(new Pk(userNo, postNo), UserDummy.dummy());
        when(postScrapReadRepository.findById(any())).thenReturn(Optional.of(postScrap));

        // when
        postScrapManageService.postScrap(postNo, userNo);

        // then
        verify(userReadService, times(1)).getAlingUserByUserNo(anyLong());
        verify(postFeignClient, times(1)).isExistsPost(anyLong());
        verify(postScrapReadRepository, times(1)).findById(any());
        verify(postScrapManageRepository, times(1)).delete(any());
        verify(postScrapManageRepository, never()).save(any());
    }

    @Test
    @DisplayName("게시물 스크랩 성공 - 존재하지 않는 경우")
    void postScrap_notExists() {
        // given
        Long postNo = 1L;
        Long userNo = 1L;

        ResponseEntity<IsExistsPostResponseDto> responseEntity = mock(ResponseEntity.class);
        when(postFeignClient.isExistsPost(anyLong())).thenReturn(responseEntity);

        IsExistsPostResponseDto responseDto = new IsExistsPostResponseDto();
        ReflectionTestUtils.setField(responseDto, "isExists", Boolean.TRUE);
        when(responseEntity.getBody()).thenReturn(responseDto);

        PostScrap postScrap = new PostScrap(new Pk(userNo, postNo), UserDummy.dummy());
        when(postScrapReadRepository.findById(any())).thenReturn(Optional.empty());

        // when
        postScrapManageService.postScrap(postNo, userNo);

        // then
        verify(userReadService, times(1)).getAlingUserByUserNo(anyLong());
        verify(postFeignClient, times(1)).isExistsPost(anyLong());
        verify(postScrapReadRepository, times(1)).findById(any());
        verify(postScrapManageRepository, never()).delete(any());
        verify(postScrapManageRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("게시물 스크랩 실패 - 회원이 존재하지 않는 경우")
    void postScrap_userNotFound() {
        // given
        Long postNo = 1L;
        Long userNo = 1L;

        when(userReadService.getAlingUserByUserNo(anyLong())).thenThrow(UserNotFoundException.class);

        // when
        assertThatThrownBy(() -> postScrapManageService.postScrap(postNo, userNo))
                .isInstanceOf(UserNotFoundException.class);

        // then
        verify(userReadService, times(1)).getAlingUserByUserNo(anyLong());
        verify(postFeignClient, never()).isExistsPost(anyLong());
        verify(postScrapReadRepository, never()).existsById(any());
        verify(postScrapManageRepository, never()).deleteById(any());
        verify(postScrapManageRepository, never()).save(any());
    }

    @Test
    @DisplayName("게시물 스크랩 실패 - 게시물이 존재하지 않는 경우")
    void postScrap_postNotFound() {
        // given
        Long postNo = 1L;
        Long userNo = 1L;

        ResponseEntity<IsExistsPostResponseDto> responseEntity = mock(ResponseEntity.class);
        when(postFeignClient.isExistsPost(anyLong())).thenReturn(responseEntity);

        IsExistsPostResponseDto responseDto = new IsExistsPostResponseDto();
        ReflectionTestUtils.setField(responseDto, "isExists", Boolean.FALSE);
        when(responseEntity.getBody()).thenReturn(responseDto);

        // when
        assertThatThrownBy(() -> postScrapManageService.postScrap(postNo, userNo))
                .isInstanceOf(PostNotFoundException.class);

        // then
        verify(userReadService, times(1)).getAlingUserByUserNo(anyLong());
        verify(postFeignClient, times(1)).isExistsPost(anyLong());
        verify(postScrapReadRepository, never()).existsById(any());
        verify(postScrapManageRepository, never()).deleteById(any());
        verify(postScrapManageRepository, never()).save(any());
    }
}