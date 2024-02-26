package kr.aling.user.common.adaptor;

import static kr.aling.user.common.utils.HeaderUtil.makeHeader;

import kr.aling.user.banduser.dto.response.external.GetFileInfoResponseDto;
import kr.aling.user.common.properties.AlingUrlProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * File 서버에 파일 정보를 요청을 위한 Adaptor.
 *
 * @author 박경서
 * @since 1.0
 **/
@Component
@RequiredArgsConstructor
public class AlingFileAdaptor {

    private final RestTemplate restTemplate;
    private final AlingUrlProperties alingUrlProperties;

    /**
     * 파일 번호를 가지고 파일 단건 정보 조회 요청 메서드.
     *
     * @param fileNo 파일 번호
     * @return 파일 정보 Dto
     */
    public GetFileInfoResponseDto requestFileInfo(Long fileNo) {
        String url = UriComponentsBuilder.fromHttpUrl(
                        alingUrlProperties.getFileGatewayUrl() + "/file/api/v1/files/" + fileNo)
                .encode()
                .toUriString();

        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(makeHeader()),
                GetFileInfoResponseDto.class
        ).getBody();
    }
}
