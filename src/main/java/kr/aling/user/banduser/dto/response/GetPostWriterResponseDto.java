package kr.aling.user.banduser.dto.response;

import lombok.Getter;

/**
 * 게시글 작성자 정보를 조회 하기 위한 응답 Dto.
 *
 * @author 박경서
 * @since 1.0
 **/
@Getter
public class GetPostWriterResponseDto {

    private Long userNo;
    private String username;
    private String profilePath;

    /**
     * 프로필 사진이 없는 회원 응답 Dto 생성자.
     *
     * @param userNo   회원 번호
     * @param username 회원 이름
     */
    public GetPostWriterResponseDto(Long userNo, String username) {
        this.userNo = userNo;
        this.username = username;
    }

    /**
     * 프로필 사진이 있는 회원 응답 Dto 생성자.
     *
     * @param userNo      회원 번호
     * @param username    회원 이름
     * @param profilePath 프로필 사진 경로
     */
    public GetPostWriterResponseDto(Long userNo, String username, String profilePath) {
        this.userNo = userNo;
        this.username = username;
        this.profilePath = profilePath;
    }
}
