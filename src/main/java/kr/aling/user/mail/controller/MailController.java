package kr.aling.user.mail.controller;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import kr.aling.user.common.exception.CustomException;
import kr.aling.user.mail.dto.request.CheckAuthNumberMailRequestDto;
import kr.aling.user.mail.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
     * @return 200 OK
     * @author : 이수정
     * @since : 1.0
     */
    @GetMapping("/api/v1/email-check")
    public ResponseEntity<Void> emailCheck(@RequestParam @NotBlank @Size(min = 3, max = 100) @Email String email) {
        mailService.sendAuthNumber(email);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * 전달받은 인증번호가 유효한지 확인합니다.
     *
     * @param requestDto 이메일, 검증할 인증번호를 담은 Dto
     * @return 200 OK
     * @author : 이수정
     * @since : 1.0
     */
    @PostMapping("/api/v1/email-check")
    public ResponseEntity<Void> authNumberCheck(
            @Valid @RequestBody CheckAuthNumberMailRequestDto requestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new CustomException(HttpStatus.BAD_REQUEST,
                    "check auth number request valid error - " + bindingResult.getAllErrors());
        }
        mailService.checkAuthNumber(requestDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
