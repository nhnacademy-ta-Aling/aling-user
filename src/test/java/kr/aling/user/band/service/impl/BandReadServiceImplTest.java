package kr.aling.user.band.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import kr.aling.user.band.dto.response.ExistsBandNameResponseDto;
import kr.aling.user.band.repository.BandReadRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * 그룹 조회 서비스 테스트.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@ExtendWith(SpringExtension.class)
class BandReadServiceImplTest {

    @InjectMocks
    private BandReadServiceImpl bandReadService;

    @Mock
    private BandReadRepository bandReadRepository;

    @Test
    @DisplayName("그룹명 중복 검사 성공")
    void existBandName_successTest() {
        // given
        String bandName = "testBandName";
        boolean isExists = false;

        // when
        when(bandReadRepository.existsBandByName(any())).thenReturn(isExists);

        // then
        ExistsBandNameResponseDto existsBandNameResponseDto = bandReadService.existBandName(bandName);

        assertThat(existsBandNameResponseDto).isNotNull();
        assertThat(existsBandNameResponseDto.getIsExist()).isEqualTo(isExists);

        verify(bandReadRepository, times(1)).existsBandByName(any());
    }
}