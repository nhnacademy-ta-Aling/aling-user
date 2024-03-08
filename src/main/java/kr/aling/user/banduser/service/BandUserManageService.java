package kr.aling.user.banduser.service;

import kr.aling.user.banduser.dto.request.ModifyRoleOfBandUserRequestDto;

/**
 * 그룹 회원 관리 service.
 *
 * @author : 정유진
 * @since : 1.0
 **/
public interface BandUserManageService {

    /**
     * 그룹 회원을 생성 하기 위한 메서드 입니다.<br>
     * 추방된 회원은 다시 가입할 수 없습니다.
     *
     * @param bandName 그룹 명
     * @param userNo 회원 번호
     */
    void makeBandUser(String bandName, Long userNo);

    /**
     * 그룹 회원을 삭제 하기 위한 메서드 입니다. soft delete 로 동작 합니다.
     *
     * @param bandName 그룹 명
     * @param userNo   회원 번호
     */
    void removeBandUser(String bandName, Long userNo);

    /**
     * 그룹 회원의 그룹 회원 권한을 수정 하기 위한 메서드 입니다.
     *
     * @param bandName                       그룹 명
     * @param userNo                         회원 번호
     * @param modifyRoleOfBandUserRequestDto 그룹 회원 권한 수정 정보를 담은 dto
     */
    void modifyRoleOfBandUser(String bandName, Long userNo,
            ModifyRoleOfBandUserRequestDto modifyRoleOfBandUserRequestDto);

    /**
     * creator 권한을 위임 하기 위한 메서드 입니다.
     *
     * @param bandName     그룹 명
     * @param targetUserNo creator 권한을 위임 받을 회원 번호
     * @param userNo       creator 권한 회원 번호
     */
    void modifyCreatorRoleOfBandUser(String bandName, Long targetUserNo, Long userNo);

    /**
     * 그룹 회원 추방 상태를 변경 하기 위한 메서드 입니다.
     *
     * @param bandName 그룹 명
     * @param userNo   회원 번호
     */
    void modifyBlockOfBandUser(String bandName, Long userNo);
}
