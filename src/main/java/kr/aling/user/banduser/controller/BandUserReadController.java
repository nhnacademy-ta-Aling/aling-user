package kr.aling.user.banduser.controller;

import kr.aling.user.banduser.dto.response.GetPostWriterResponseDto;
import kr.aling.user.banduser.service.BandUserReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 그룹 회원 정보 Read RestController.
 *
 * @author 박경서
 * @since 1.0
 **/
@RestController
@RequestMapping("/api/v1/band-users")
@RequiredArgsConstructor
public class BandUserReadController {

    private final BandUserReadService bandUserReadService;

    /**
     * 그룹 회원(그룹 게시글 작성자) 정보 조회 API.
     *
     * @param bandUserNo 그룹 회원 번호
     * @return 회원(게시글 작성자) 정보 Dto.
     */
    @GetMapping("/{bandUserNo}")
    public ResponseEntity<GetPostWriterResponseDto> getBandUserInfo(@PathVariable("bandUserNo") Long bandUserNo) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(bandUserReadService.getPostWriterInfo(bandUserNo));
    }
}
