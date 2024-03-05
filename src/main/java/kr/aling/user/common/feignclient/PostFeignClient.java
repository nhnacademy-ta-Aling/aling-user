package kr.aling.user.common.feignclient;

import java.util.List;
import kr.aling.user.band.dto.request.external.CreateBandPostTypeRequestExternalDto;
import kr.aling.user.band.dto.response.external.GetBandPostTypeResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Some description here.
 *
 * @author 정유진
 * @since 1.0
 **/
@FeignClient("aling-post")
public interface PostFeignClient {
    @PostMapping("/api/v1/band-post-types")
    void requestMakeBandPostType(CreateBandPostTypeRequestExternalDto requestDto);

    @GetMapping("/api/v1/bands/{bandNo}/band-post-types")
    List<GetBandPostTypeResponseDto> requestGetBandPostTypeList(@PathVariable("bandNo") Long bandNo);
}
