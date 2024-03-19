package kr.aling.user.user.service;

import java.util.List;
import java.util.Set;
import kr.aling.user.user.dto.response.ReadPostAuthorInfoResponseDto;
import kr.aling.user.user.dto.response.ReadUserInfoResponseDto;
import kr.aling.user.user.dto.resquest.ReadPostAuthorInfoRequestDto;

/**
 * 유저 정보 조회 서비스 인터페이스입니다.
 *
 * @author : 이성준
 * @since : 1.0
 */
public interface UserInfoReadService {

    /**
     * 여러개의 게시물 작성자 정보에 대한 조회 메서드입니다.
     *
     * @param requests 조회할 게시물 작성자 목록
     * @return 게시물과 대응하는 작성자 정보
     * @author : 이성준
     * @since : 1.0
     */
    List<ReadPostAuthorInfoResponseDto> requestPostAuthorInfo(Set<ReadPostAuthorInfoRequestDto> requests);

    /**
     * 일반 유저 정보를 조회하는 메서드입니다
     *
     * @param userNo 조회할 일반 유저 식별 번호
     * @return 조회된 유저 정보 응답 객체
     * @author : 이성준
     * @since : 1.0
     */
    ReadUserInfoResponseDto readNormalUser(Long userNo);

    /**
     * 여러명의 유저에 대한 정보 조회 메서드입니다.
     *
     * @param userNoList 조회할 유저 식별번호 목록
     * @return 조회된 유저 정보 응답 객체 목록
     * @author : 이성준
     * @since : 1.0
     */
    List<ReadUserInfoResponseDto> readUserInfos(List<Long> userNoList);
}
