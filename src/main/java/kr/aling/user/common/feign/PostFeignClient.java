package kr.aling.user.common.feign;

import kr.aling.user.post.dto.response.IsExistsPostResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("aling-post")
public interface PostFeignClient {

    @GetMapping("/api/v1/check-post/{postNo}")
    ResponseEntity<IsExistsPostResponseDto> isExistsPost(@PathVariable("postNo") Long postNo);
}