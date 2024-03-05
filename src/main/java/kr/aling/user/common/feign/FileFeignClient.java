package kr.aling.user.common.feignclient;

import kr.aling.user.banduser.dto.response.external.GetFileInfoResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 파일 서버와의 통신을 위한 Feign Client 입니다.
 *
 * @author 정유진
 * @since 1.0
 **/
@FeignClient("aling-file")
public interface FileFeignClient {
    @GetMapping("/api/v2/files/{fileNo}")
    GetFileInfoResponseDto requestFileInfo(@PathVariable("fileNo") Long fileNo);
}
