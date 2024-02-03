package kr.aling.user.wantjobtype.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import kr.aling.user.wantjobtype.dto.response.ReadWantJobTypeResponseDto;
import kr.aling.user.wantjobtype.dummy.WantJobTypeDummy;
import kr.aling.user.wantjobtype.entity.WantJobType;
import kr.aling.user.wantjobtype.exception.WantJobTypeNotFoundException;
import kr.aling.user.wantjobtype.repository.WantJobTypeReadRepository;
import kr.aling.user.wantjobtype.service.WantJobTypeReadService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class WantJobTypeReadServiceImplTest {

    private WantJobTypeReadService wantJobTypeReadService;

    private WantJobTypeReadRepository wantJobTypeReadRepository;

    @BeforeEach
    void setUp() {
        wantJobTypeReadRepository = mock(WantJobTypeReadRepository.class);

        wantJobTypeReadService = new WantJobTypeReadServiceImpl(wantJobTypeReadRepository);
    }

    @Test
    @DisplayName("구직희망타입 ID로 조회 성공")
    void findByWantJobTypeNo() {
        // given
        WantJobType wantJobType = WantJobTypeDummy.dummy();

        when(wantJobTypeReadRepository.findById(any())).thenReturn(Optional.of(wantJobType));

        // when
        ReadWantJobTypeResponseDto responseDto =
                wantJobTypeReadService.findByWantJobTypeNo(wantJobType.getWantJobTypeNo());

        // then
        assertThat(responseDto).isNotNull();
        assertThat(responseDto.getWantJobType()).isNotNull();
        assertThat(responseDto.getWantJobType().getWantJobTypeNo()).isEqualTo(wantJobType.getWantJobTypeNo());
        assertThat(responseDto.getWantJobType().getName()).isEqualTo(wantJobType.getName());

        verify(wantJobTypeReadRepository, times(1)).findById(any());
    }


    @Test
    @DisplayName("구직희망타입 ID로 조회 실패 - 존재하지 않는 구직희망타입 ID")
    void findByWantJobTypeNo_idNotFound() {
        // given
        when(wantJobTypeReadRepository.findById(any())).thenReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> wantJobTypeReadService.findByWantJobTypeNo(-1)).isInstanceOf(
                WantJobTypeNotFoundException.class);

        verify(wantJobTypeReadRepository, times(1)).findById(any());
    }
}