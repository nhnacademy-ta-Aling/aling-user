package kr.aling.user.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.validation.Valid;
import javax.ws.rs.core.MediaType;
import kr.aling.user.user.dto.response.LoginResponseDto;
import kr.aling.user.user.dto.resquest.LoginRequestDto;
import kr.aling.user.user.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 로그인 RestController.
 *
 * @author 이수정
 * @since 1.0
 */
@RequiredArgsConstructor
@RequestMapping("/api/v1/login")
@RestController
public class LoginController {

    private static final String USER_NO = "X-User-No";
    private static final String USER_ROLE = "X-User-Role";

    private final LoginService loginService;
    private final ObjectMapper objectMapper;

    /**
     * 아이디(E-mail)와 비밀번호를 검증하여 로그인합니다.
     *
     * @param loginRequestDto 아이디, 비밀번호가 담긴 요청 Dto
     * @return 유저 아이디와 역할을 헤더로 반환
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    public ResponseEntity<Void> login(@RequestBody @Valid LoginRequestDto loginRequestDto)
            throws JsonProcessingException {
        LoginResponseDto loginResponseDto = loginService.login(loginRequestDto);

        HttpHeaders headers = new HttpHeaders();
        headers.add(USER_NO, String.valueOf(loginResponseDto.getUserNo()));
        headers.add(USER_ROLE, objectMapper.writeValueAsString(loginResponseDto.getRoles()));

        return ResponseEntity.ok().headers(headers).build();
    }
}
