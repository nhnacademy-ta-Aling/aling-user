package kr.aling.user.mail.controller;

import javax.validation.Valid;
import kr.aling.user.common.exception.CustomException;
import kr.aling.user.mail.dto.request.CheckMailRequestDto;
import kr.aling.user.mail.dto.response.CheckMailResponseDto;
import kr.aling.user.mail.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 회원가입 이메일 인증을 위한 Controller.
 *
 * @author : 이수정
 * @since : 1.0
 */
@RequiredArgsConstructor
@RestController
public class MailController {

    private final MailService mailService;

    /**
     * 전달받은 이메일에 인증번호를 보냅니다.
     *
     * @param requestDto 인증번호를 보낼 이메일을 담은 Dto
     * @return 서버에서 생성한 인증번호
     * @author : 이수정
     * @since : 1.0
     */
    @GetMapping("/api/v1/emailcheck")
    public ResponseEntity<CheckMailResponseDto> emailCheck(@Valid @RequestBody CheckMailRequestDto requestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "email valid error - " + bindingResult.getAllErrors());
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CheckMailResponseDto(mailService.sendAuthNumber(requestDto.getEmail())));
    }
}
