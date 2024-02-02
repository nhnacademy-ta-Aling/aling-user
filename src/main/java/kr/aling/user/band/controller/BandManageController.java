package kr.aling.user.band.controller;

import javax.validation.Valid;
import kr.aling.user.band.dto.request.CreateBandRequestDto;
import kr.aling.user.band.service.BandManageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 그룹(Band)을 관리 하는 Controller.
 *
 * @author 정유진
 * @since 1.0
 **/
@RestController
@RequiredArgsConstructor
@RequestMapping("/bands")
public class BandManageController {
    private final BandManageService bandManageService;

    /**
     * 그룹 생성을 위한 메서드.
     *
     * @param userNo 그룹 생성 요청 유저 번호
     * @param createBandRequestDto 그룹 생성 요청 dto
     * @return ResponseEntity
     */
    @PostMapping
    public ResponseEntity<Void> makeBand(@RequestHeader("X-TEMP-USER-NO") Long userNo,
                                     @Valid @RequestBody CreateBandRequestDto createBandRequestDto) {
        bandManageService.makeBand(userNo, createBandRequestDto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

}

