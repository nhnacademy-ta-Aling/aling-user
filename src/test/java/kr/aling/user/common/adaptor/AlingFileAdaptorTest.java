package kr.aling.user.common.adaptor;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import kr.aling.user.banduser.dto.response.external.GetFileInfoResponseDto;
import kr.aling.user.common.properties.AlingUrlProperties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

@ExtendWith(SpringExtension.class)
class AlingFileAdaptorTest {

    @InjectMocks
    AlingFileAdaptor alingFileAdaptor;

    @Mock
    RestTemplate restTemplate;

    @Mock
    AlingUrlProperties alingUrlProperties;

    @Test
    @DisplayName("파일 번호로 파일 정보 조회 API RestTemplate 테스트")
    void requestFileInfo_restTemplate_test() {
        // given
        GetFileInfoResponseDto getFileInfoResponseDto = new GetFileInfoResponseDto();
        ReflectionTestUtils.setField(getFileInfoResponseDto, "fileNo", 1L);
        ReflectionTestUtils.setField(getFileInfoResponseDto, "categoryNo", 1);
        ReflectionTestUtils.setField(getFileInfoResponseDto, "categoryName", "category name");
        ReflectionTestUtils.setField(getFileInfoResponseDto, "path", "path");
        ReflectionTestUtils.setField(getFileInfoResponseDto, "originName", "origin name");
        ReflectionTestUtils.setField(getFileInfoResponseDto, "fileSize", "1KB");

        ResponseEntity<GetFileInfoResponseDto> responseEntity =
                new ResponseEntity<>(getFileInfoResponseDto, HttpStatus.OK);

        // when
        when(alingUrlProperties.getFileGatewayUrl()).thenReturn("http://localhost:8090");
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(GetFileInfoResponseDto.class)))
                .thenReturn(responseEntity);

        // then
        alingFileAdaptor.requestFileInfo(1L);

        verify(alingUrlProperties, times(1)).getFileGatewayUrl();
        verify(restTemplate, times(1)).exchange(anyString(), eq(HttpMethod.GET), any(),
                eq(GetFileInfoResponseDto.class));
    }

}