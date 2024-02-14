package kr.aling.user.user.service;

import java.util.List;
import kr.aling.user.user.dto.response.GetBandInfoResponseDto;

/**
 * 회원 읽기 전용 서비스.
 *
 * @author : 여운석
 * @author : 이수정
 * @since : 1.0
 **/
public interface UserReadService {

    /**
     * 존재하는 이메일인지 확인합니다.
     *
     * @param email 확인할 이메일
     * @return 이메일 존재 여부
     * @author : 이수정
     * @since : 1.0
     */
    boolean isExistsEmail(String email);

    /**
     * 존재하는 회원인지 확인합니다.
     *
     * @param userNo 확인할 회원의 번호
     * @return 회원 존재 여부
     * @author : 이수정
     * @since : 1.0
     */
    boolean isExistsUserNo(Long userNo);

    /**
     * 해당 회원이 가입한 그룹 정보 목록 조회 메서드.
     *
     * @param userNo 회원 번호
     * @return 해당 회원이 가입한 그룹 정보 목록
     */
    List<GetBandInfoResponseDto> getJoinedBandInfoList(Long userNo);
}
