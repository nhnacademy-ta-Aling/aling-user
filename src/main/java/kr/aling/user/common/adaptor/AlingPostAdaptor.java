package kr.aling.user.common.adaptor;

import java.util.List;
import kr.aling.user.band.dto.request.external.CreateBandPostTypeDefaultRequestDto;
import kr.aling.user.band.dto.request.external.CreateBandPostTypeRequestExternalDto;
import kr.aling.user.band.dto.response.external.GetBandPostTypeResponseDto;
import kr.aling.user.common.properties.AlingUrlProperties;
import kr.aling.user.common.utils.HeaderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Post 서버와 통신 하기 위한 Adaptor.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@Component
@RequiredArgsConstructor
public class AlingPostAdaptor {

    private final AlingUrlProperties alingUrlProperties;
    private final RestTemplate restTemplate;

    /**
     * 기본 그룹 게시글 분류를 생성 하기 위한 메서드 입니다.
     *
     * @param requestDto 기본 그룹 게시글 분류 생성을 위한 dto
     */
    public void makeDefaultBandPostType(CreateBandPostTypeDefaultRequestDto requestDto) {
        restTemplate.exchange(alingUrlProperties.getGatewayUrl() + "/post/api/v1/band-post-types/default",
                HttpMethod.POST,
                new HttpEntity<>(requestDto, HeaderUtil.makeHeader()),
                Void.class);
    }

    /**
     * 기본 그룹 게시글 분류를 수정 하기 위한 메서드 입니다.
     *
     * @param requestDto 기본 그룹 게시글 분류 수정을 위한 dto
     */
    public void makeBandPostType(CreateBandPostTypeRequestExternalDto requestDto) {
        restTemplate.exchange(alingUrlProperties.getGatewayUrl() + "/post/api/v1/band-post-types",
                HttpMethod.POST,
                new HttpEntity<>(requestDto, HeaderUtil.makeHeader()),
                Void.class);
    }

    /**
     * 특정 그룹의 그룹 게시글 분류 리스트를 조회하기 위한 메서드 입니다.
     *
     * @param bandNo 그룹 번호
     * @return 그룹 게시글 분류 dto 리스트
     */
    public List<GetBandPostTypeResponseDto> getBandPostTypeList(Long bandNo) {
        return restTemplate.exchange(
                alingUrlProperties.getGatewayUrl() + "/post/api/v1/bands/" + bandNo + "/band-post-types",
                HttpMethod.GET,
                new HttpEntity<>(HeaderUtil.makeHeader()),
                new ParameterizedTypeReference<List<GetBandPostTypeResponseDto>>() {
                }).getBody();
    }
}
