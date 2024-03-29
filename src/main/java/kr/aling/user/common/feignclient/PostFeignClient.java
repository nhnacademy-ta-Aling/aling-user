package kr.aling.user.common.feignclient;

import java.util.List;
import kr.aling.user.band.dto.request.external.CreateBandPostTypeRequestExternalDto;
import kr.aling.user.band.dto.request.external.ModifyBandPostTypeRequestExternalDto;
import kr.aling.user.band.dto.response.external.GetBandPostTypeResponseDto;
import kr.aling.user.post.dto.request.ReadPostsForScrapRequestDto;
import kr.aling.user.post.dto.response.IsExistsPostResponseDto;
import kr.aling.user.post.dto.response.ReadPostsForScrapResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * post 서버와의 통신을 위한 Feign Client.
 *
 * @author 정유진
 * @since 1.0
 **/
@FeignClient("aling-post")
public interface PostFeignClient {

    @PostMapping("/api/v1/band-post-types")
    void requestMakeBandPostType(CreateBandPostTypeRequestExternalDto requestDto);

    @PutMapping("/api/v1/band-post-types/{postTypeNo}")
    void requestUpdateBandPostType(@PathVariable("postTypeNo") Long postTypeNo,
            ModifyBandPostTypeRequestExternalDto requestDto);

    @DeleteMapping("/api/v1/band-post-types/{postTypeNo}")
    void requestDeleteBandPostType(@PathVariable("postTypeNo") Long postTypeNo);

    @GetMapping("/api/v1/band-post-types")
    List<GetBandPostTypeResponseDto> requestGetBandPostTypeList(@RequestParam("bandNo") Long bandNo);

    @GetMapping("/api/v1/check-post/{postNo}")
    ResponseEntity<IsExistsPostResponseDto> isExistsPost(@PathVariable("postNo") Long postNo);

    @GetMapping("/api/v1/posts-for-scrap")
    ResponseEntity<ReadPostsForScrapResponseDto> getPostsForScrap(
            @SpringQueryMap ReadPostsForScrapRequestDto requestDto);
}
