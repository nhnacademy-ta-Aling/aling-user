package kr.aling.user.common.feign;

import kr.aling.user.banduser.dto.response.external.GetFileInfoResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 파일 API 에 대한 Open Feign Client 입니다.
 *
 * @author : 이성준
 * @since : 1.0
 */
@FeignClient("aling-file")
public interface FileFeignClient {

    @GetMapping("/api/v1/files/{fileNo}")
    GetFileInfoResponseDto requestFileInfo(@PathVariable("fileNo") Long fileNo);
}
