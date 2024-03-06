package kr.aling.user.band.controller;

import javax.validation.Valid;
import kr.aling.user.band.dto.request.CreateBandPostTypeRequestDto;
import kr.aling.user.band.dto.request.CreateBandRequestDto;
import kr.aling.user.band.dto.request.ModifyBandRequestDto;
import kr.aling.user.band.service.BandManageService;
import kr.aling.user.banduser.dto.request.ModifyRoleOfBandUserRequestDto;
import kr.aling.user.banduser.service.BandUserManageService;
import kr.aling.user.common.annotation.BandAdminAuth;
import kr.aling.user.common.annotation.BandCreatorAuth;
import kr.aling.user.common.annotation.BandUserAuth;
import kr.aling.user.common.utils.ConstantUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
@RequestMapping("/api/v1/bands")
public class BandManageController {

    private final BandManageService bandManageService;
    private final BandUserManageService bandUserManageService;

    /**
     * 그룹 생성을 위한 메서드.
     *
     * @param userNo               그룹 생성 요청 유저 번호
     * @param createBandRequestDto 그룹 생성 요청 dto
     * @return ResponseEntity
     */
    @PostMapping
    public ResponseEntity<Void> makeBand(@RequestHeader(ConstantUtil.X_USER_NO) Long userNo,
            @Valid @RequestBody CreateBandRequestDto createBandRequestDto) {
        bandManageService.makeBand(userNo, createBandRequestDto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    /**
     * 그룹 정보를 수정 하기 위한 메서드 입니다.
     *
     * @param bandName             수정할 그룹 이름
     * @param modifyBandRequestDto 그룹 수정에 사용될 dto
     * @return 200 ok
     */
    @BandAdminAuth
    @PutMapping("/{bandName}")
    public ResponseEntity<Void> updateBandInfo(@PathVariable("bandName") String bandName,
            @Valid @RequestBody ModifyBandRequestDto modifyBandRequestDto) {
        bandManageService.updateBandInfo(bandName, modifyBandRequestDto);

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    /**
     * 그룹을 삭제 하기 위한 메서드 입니다. <br> soft delete 이며, 그룹을 삭제 하면 복구할 수 없습니다.
     *
     * @param bandName 삭제할 그룹 명
     * @return 204 no content
     */
    @BandCreatorAuth
    @DeleteMapping("/{bandName}")
    public ResponseEntity<Void> removeBand(@PathVariable("bandName") String bandName) {
        bandManageService.removeBand(bandName);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();

    }

    /**
     * 그룹을 탈퇴 하기 위한 메서입니다.
     *
     * @param bandName 탈퇴할 그룹 명
     * @param userNo   탈퇴할 유저 번호
     * @return 204 no content
     */
    @BandUserAuth
    @DeleteMapping("/{bandName}/users/{userNo}")
    public ResponseEntity<Void> leaveBand(@PathVariable("bandName") String bandName,
            @PathVariable("userNo") Long userNo) {
        bandUserManageService.removeBandUser(bandName, userNo);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    /**
     * 그룹 회원 권한을 수정 하기 위한 메서드 입니다. <br> 그룹 회원 권한 수정은 해당 그룹의 admin, creator 권한을 가진 사람만 가능 합니다.
     *
     * @param bandName                       그룹 명
     * @param userNo                         권한을 수정할 회원의 번호
     * @param modifyRoleOfBandUserRequestDto 그룹 회원 권한 수정 시 필요한 정보 dto
     * @return 200 ok
     */
    @BandAdminAuth
    @PutMapping("/{bandName}/users/{userNo}/role")
    public ResponseEntity<Void> updateRoleOfBandUser(@PathVariable("bandName") String bandName,
            @PathVariable("userNo") Long userNo,
            @Valid @RequestBody
            ModifyRoleOfBandUserRequestDto modifyRoleOfBandUserRequestDto) {
        bandUserManageService.modifyRoleOfBandUser(bandName, userNo, modifyRoleOfBandUserRequestDto);

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    /**
     * 그룹 회원 creator 권한을 위임하기 위한 메서드입니다. <br> creator 권한 위임은 creator 권한을 가진 사람만 가능합니다.
     *
     * @param bandName     그룹 명
     * @param targetUserNo 위임할 유저 번호
     * @param userNo       현재 creator 권한인 유저 번호
     * @return ResponseEntity void
     */
    @BandCreatorAuth
    @PutMapping("/{bandName}/users/{userNo}/role-delegation")
    public ResponseEntity<Void> updateCreatorRoleOfBandUser(@PathVariable("bandName") String bandName,
            @PathVariable("userNo") Long targetUserNo,
            @RequestHeader(ConstantUtil.X_USER_NO) Long userNo) {
        bandUserManageService.modifyCreatorRoleOfBandUser(bandName, targetUserNo, userNo);

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    /**
     * 그룹 회원의 추방 여부를 수정 하기 위한 메서드 입니다.<br> 그룹 회원이 추방 된다면, 추방됨과 동시에 그룹에서 탈퇴됩니다. <br> 추방 될 경우 재가입이 불가능 하지만, 추방이 해제되면 재가입이
     * 가능합니다.
     *
     * @param bandName 그룹 명
     * @param userNo   추방할 회원 번호
     * @return 200 ok
     */
    @BandAdminAuth
    @PutMapping("/{bandName}/users/{userNo}/block")
    public ResponseEntity<Void> updateBlockBandUser(@PathVariable("bandName") String bandName,
            @PathVariable("userNo") Long userNo) {
        bandUserManageService.modifyBlockOfBandUser(bandName, userNo);

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    /**
     * 그룹 게시글 분류를 생성하기 위한 메서드입니다.
     *
     * @param bandName   그룹 명
     * @param requestDto 그룹 게시글 분류 생성에 필요한 정보 dto
     * @return 201 created
     */
    @BandAdminAuth
    @PostMapping("/{bandName}/post-types")
    public ResponseEntity<Void> makeBandCategory(@PathVariable("bandName") String bandName,
            @RequestBody CreateBandPostTypeRequestDto requestDto) {
        bandManageService.makeBandCategory(bandName, requestDto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }
}

