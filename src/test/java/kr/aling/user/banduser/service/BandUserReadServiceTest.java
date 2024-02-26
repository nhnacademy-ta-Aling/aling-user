package kr.aling.user.banduser.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import kr.aling.user.banduser.dto.response.BandPostUerQueryDto;
import kr.aling.user.banduser.dto.response.external.GetFileInfoResponseDto;
import kr.aling.user.banduser.exception.BandUserNotFoundException;
import kr.aling.user.banduser.repository.BandUserReadRepository;
import kr.aling.user.banduser.service.impl.BandUserReadServiceImpl;
import kr.aling.user.common.adaptor.AlingFileAdaptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(SpringExtension.class)
class BandUserReadServiceTest {

    @InjectMocks
    BandUserReadServiceImpl bandUserReadService;

    @Mock
    BandUserReadRepository bandUserReadRepository;

    @Mock
    AlingFileAdaptor alingFileAdaptor;

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("게시글 작성자 조회 서비스 메서드 실패 테스트 - 그룹 회원 번호가 없음")
    void getPostWriterInfo_fail_service_test() {
        // given

        // when
        when(bandUserReadRepository.existsById(anyLong())).thenReturn(false);

        // then
        assertThatThrownBy(() -> bandUserReadService.getPostWriterInfo(1L))
                .isInstanceOf(BandUserNotFoundException.class)
                .hasMessage(BandUserNotFoundException.MESSAGE);

        verify(bandUserReadRepository, times(1)).existsById(anyLong());
        verify(bandUserReadRepository, times(0)).getBandUserForPost(anyLong());
        verify(alingFileAdaptor, times(0)).requestFileInfo(anyLong());
    }

    @Test
    @DisplayName("게시글 작성자 조회 서비스 성공 테스트 (작성자 프로필 없는 경우)")
    void getPostWriterInfo_success_test_without_fileNo() {
        // given
        BandPostUerQueryDto bandPostUerQueryDto = new BandPostUerQueryDto(1L, "username", null);

        // when
        when(bandUserReadRepository.existsById(anyLong())).thenReturn(true);
        when(bandUserReadRepository.getBandUserForPost(anyLong())).thenReturn(bandPostUerQueryDto);

        // then
        bandUserReadService.getPostWriterInfo(anyLong());

        verify(bandUserReadRepository, times(1)).existsById(anyLong());
        verify(bandUserReadRepository, times(1)).getBandUserForPost(anyLong());
        verify(alingFileAdaptor, times(0)).requestFileInfo(anyLong());
    }

    @Test
    @DisplayName("게시슬 작성자 조회 서비스 성공 테스트 (작성자 프로필 있는 경우)")
    void getPostWriterInfo_success_test_with_fileNo() {
        // given
        BandPostUerQueryDto bandPostUerQueryDto = new BandPostUerQueryDto(1L, "username", 1L);
        GetFileInfoResponseDto getFileInfoResponseDto = new GetFileInfoResponseDto();
        ReflectionTestUtils.setField(getFileInfoResponseDto, "path", "path");

        // when
        when(bandUserReadRepository.existsById(anyLong())).thenReturn(true);
        when(bandUserReadRepository.getBandUserForPost(anyLong())).thenReturn(bandPostUerQueryDto);
        when(alingFileAdaptor.requestFileInfo(anyLong())).thenReturn(getFileInfoResponseDto);

        // then
        bandUserReadService.getPostWriterInfo(anyLong());

        verify(bandUserReadRepository, times(1)).existsById(anyLong());
        verify(bandUserReadRepository, times(1)).getBandUserForPost(anyLong());
        verify(alingFileAdaptor, times(1)).requestFileInfo(anyLong());
    }

}