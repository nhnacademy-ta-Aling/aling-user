package kr.aling.user.mail.controller;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import kr.aling.user.mail.dto.response.CheckMailResponseDto;
import kr.aling.user.mail.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 회원가입 이메일 인증을 위한 Controller.
 *
 * @author : 이수정
 * @since : 1.0
 */
@Validated
@RequiredArgsConstructor
@RestController
public class MailController {

    private final MailService mailService;

    /**
     * 전달받은 이메일에 인증번호를 보냅니다.
     *
     * @param email 인증번호를 보낼 이메일
     * @return 서버에서 생성한 인증번호
     * @author : 이수정
     * @since : 1.0
     */
    @GetMapping("/api/v1/email-check")
    public ResponseEntity<CheckMailResponseDto> emailCheck(@RequestParam @NotBlank @Size(min = 3, max = 100) @Email String email) {
        return ResponseEntity.status(HttpStatus.OK).body(new CheckMailResponseDto(mailService.sendAuthNumber(email)));
    }
}
