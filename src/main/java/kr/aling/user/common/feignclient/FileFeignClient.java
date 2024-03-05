package kr.aling.user.common.feignclient;

import kr.aling.user.banduser.dto.response.external.GetFileInfoResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Some description here.
 *
 * @author 정유진
 * @since 1.0
 **/
@FeignClient("aling-file")
public interface FileFeignClient {
    @GetMapping("/api/v2/files/{fileNo}")
    GetFileInfoResponseDto requestFileInfo(@PathVariable("fileNo") Long fileNo);
}
