package kr.aling.user.band.controller;

import kr.aling.user.band.dto.response.ExistsBandNameResponseDto;
import kr.aling.user.band.service.BandReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 그룹(Band) 조회 하기 위한 Controller.
 *
 * @author 정유진
 * @since 1.0
 **/
@RestController
@RequiredArgsConstructor
@RequestMapping("/bands")
public class BandReadController {
    private final BandReadService bandReadService;

    /**
     * 그룹명 중복 검사를 위한 메서드.
     *
     * @param bandName 그룹명
     * @return ResponseEntity 중복 여부를 담은 dto
     */
    @GetMapping
    @RequestMapping(value = "/check-duplicate", params = "bandName")
    public ResponseEntity<ExistsBandNameResponseDto> groupNameExists(@RequestParam("bandName") String bandName) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(bandReadService.existBandName(bandName));
    }
}
