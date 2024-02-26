package kr.aling.user.band.controller;

import java.util.List;
import kr.aling.user.band.dto.response.ExistsBandNameResponseDto;
import kr.aling.user.band.dto.response.GetBandInfoResponseDto;
import kr.aling.user.band.dto.response.GetBandInfoWithBandUserResponseDto;
import kr.aling.user.band.dto.response.external.GetBandPostTypeResponseDto;
import kr.aling.user.band.service.BandReadService;
import kr.aling.user.banduser.dto.response.GetBandUserAndUserInfoResponseDto;
import kr.aling.user.banduser.dto.response.GetBandUserAuthResponseDto;
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
    public ResponseEntity<GetBandInfoWithBandUserResponseDto> bandDetailInfo(@PathVariable("bandName") String bandName,
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
    public ResponseEntity<PageResponseDto<GetBandInfoResponseDto>> searchBandBasicInfoList(
            @RequestParam("bandName") String bandName,
            Pageable pageable) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(bandReadService.getSearchBandInfoList(bandName, pageable));
    }

    /**
     * 그룹에 가입된 유저 리스트 조회 메서드.
     *
     * @param bandName 조회할 그룹 이름
     * @param pageable 페이징
     * @return 200 OK. 그룹에 가입된 유저 리스트 페이지 dto
     */
    @GetMapping
    @RequestMapping("/{bandName}/users")
    public ResponseEntity<PageResponseDto<GetBandUserAndUserInfoResponseDto>> joinBandUserList(
            @PathVariable("bandName") String bandName,
            Pageable pageable) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(bandUserReadService.getBandUserList(bandName, pageable));
    }

    /**
     * 특정 그룹의 그룹 게시글 분류 리스트 조회 메서드.
     *
     * @param bandName 그룹 명
     * @return 200 ok. 그룹 게시글 분류 dto 리스트
     */
    @GetMapping
    @RequestMapping("/{bandName}/band-post-types")
    public ResponseEntity<List<GetBandPostTypeResponseDto>> bandPostTypeList(
            @PathVariable("bandName") String bandName) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(bandReadService.getBandPostTypeList(bandName));
    }

    /**
     * 그룹 번호와 회원 번호를 이용해 그룹 회원 권한 이름을 조회 하는 메서드. <br /> gateway 에서 공통적인 그룹 회원 권한 처리를 하기 위해 사용합니다.
     *
     * @param bandNo 그룹 번호
     * @param userNo 회원 번호
     * @return 그룹 회원 권한 정보 dto
     */
    @GetMapping
    @RequestMapping("/{bandNo}/users/{userNo}/role")
    public ResponseEntity<GetBandUserAuthResponseDto> bandUserRoleInfoForAuth(
            @PathVariable("bandNo") Long bandNo,
            @PathVariable("userNo") Long userNo) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(bandUserReadService.getBandUserInfo(bandNo, userNo));
    }
}
