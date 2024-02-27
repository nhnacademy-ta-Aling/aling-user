package kr.aling.user.common.feign;

import kr.aling.user.post.dto.response.IsExistsPostResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "aling-post", url = "${aling.openfeign.url}")
public interface PostFeignClient {

    @GetMapping("/post/api/v1/check-post/{postNo}")
    ResponseEntity<IsExistsPostResponseDto> isExistsPost(
            @RequestHeader("Authorization") String authorization,
            @PathVariable("postNo") Long postNo);
}