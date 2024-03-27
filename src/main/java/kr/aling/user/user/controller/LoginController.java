package kr.aling.user.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.validation.Valid;
import kr.aling.user.user.dto.request.LoginRequestDto;
import kr.aling.user.user.dto.response.LoginResponseDto;
import kr.aling.user.user.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    @PostMapping
    public ResponseEntity<Void> login(@RequestBody @Valid LoginRequestDto loginRequestDto)
            throws JsonProcessingException {
        return loginCommonResponse(loginService.login(loginRequestDto));
    }

    /**
     * Oauth2 GitHub 로그인 후 받은 code로 로그인을 수행합니다.
     *
     * @param code GitHub AccessToken을 받기 위한 code
     * @return 유저 아이디와 역할을 헤더로 반환
     */
    @GetMapping("/oauth/github")
    public ResponseEntity<Void> github(@RequestParam String code) throws JsonProcessingException {
        return loginCommonResponse(loginService.github(code));
    }

    /**
     * Oauth2 Google 로그인 후 받은 code로 로그인을 수행합니다.
     *
     * @param code Google AccessToken을 받기 위한 code
     * @return 유저 아이디와 역할을 헤더로 반환
     */
    @GetMapping("/oauth/google")
    public ResponseEntity<Void> google(@RequestParam String code) throws JsonProcessingException {
        return loginCommonResponse(loginService.google(code));
    }

    /**
     * 로그인 후 JWT AccessToken, RefreshToken에 담을 유저 아이디와 역할을 반환하기 위한 로그인 공통의 응답을 헤더로 반환합니다.
     *
     * @param loginResponseDto 회원의 유저 아이디와 역할을 담은 Dto
     * @return 유저 아이디와 역할을 헤더로 담은 200 OK
     */
    private ResponseEntity<Void> loginCommonResponse(LoginResponseDto loginResponseDto)
            throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.add(USER_NO, String.valueOf(loginResponseDto.getUserNo()));
        headers.add(USER_ROLE, objectMapper.writeValueAsString(loginResponseDto.getRoles()));

        return ResponseEntity.ok().headers(headers).build();
    }
}
