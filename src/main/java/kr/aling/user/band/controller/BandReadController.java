package kr.aling.user.band.controller;

import java.util.List;
import kr.aling.user.band.dto.response.ExistsBandNameResponseDto;
import kr.aling.user.band.dto.response.GetBandDetailInfoResponseDto;
import kr.aling.user.band.dto.response.GetBandInfoResponseDto;
import kr.aling.user.band.service.BandReadService;
import kr.aling.user.banduser.dto.response.GetBandUserInfoResponseDto;
import kr.aling.user.banduser.service.BandUserReadService;
import kr.aling.user.common.dto.PageResponseDto;
import kr.aling.user.common.utils.ConstantUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
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
@RequestMapping("/api/v1/bands")
public class BandReadController {
    private final BandReadService bandReadService;
    private final BandUserReadService bandUserReadService;

    /**
     * 그룹명 중복 검사를 위한 메서드.
     *
     * @param bandName 그룹명
     * @return ResponseEntity 중복 여부를 담은 dto
     */
    @GetMapping
    @RequestMapping(value = "/check-duplicate", params = "bandName")
    public ResponseEntity<ExistsBandNameResponseDto> bandNameExists(@RequestParam("bandName") String bandName) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(bandReadService.existBandName(bandName));
    }

    /**
     * 그룹 명을 통해 그룹 상세 정보 조회 메서드.
     *
     * @param bandName 그룹 명
     * @return 200 OK. ResponseEntity 그룹 상세 정보를 담은 dto
     */
    @GetMapping("/{bandName}")
    public ResponseEntity<GetBandDetailInfoResponseDto> bandDetailInfo(@PathVariable("bandName") String bandName,
                                                                       @RequestHeader(ConstantUtil.X_TEMP_USER_NO)
                                                                       Long userNo) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(bandReadService.getBandDetailInfo(bandName, userNo));
    }

    /**
     * 그룹 명을 통해 그룹 리스트 조회 메서드.
     *
     * @param bandName 그룹 명
     * @return 200 OK. ResponseEntity 그룹 정보를 담은 dto list
     */
    @GetMapping
    @RequestMapping(value = "/search", params = "bandName")
    public ResponseEntity<List<GetBandInfoResponseDto>> searchBandBasicInfoList(
            @RequestParam("bandName") String bandName) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(bandReadService.getSearchBandInfoList(bandName));
    }

    @GetMapping
    @RequestMapping("{bandNo}/users")
    public ResponseEntity<PageResponseDto<GetBandUserInfoResponseDto>> joinBandUserList(
            @PathVariable("bandNo") Long bandNo,
            Pageable pageable) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(bandUserReadService.getBandUserList(bandNo, pageable));
    }
}
