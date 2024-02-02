package kr.aling.user.normaluser.controller;

import kr.aling.user.normaluser.service.NormalUserManageService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(NormalUserManageController.class)
class NormalUserManageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NormalUserManageService normalUserManageService;

    @Test
    @DisplayName("일반회원가입 성공")
    void signUpNormalUser() {

    }

    @Test
    @DisplayName("일반회원가입 실패 - 이미 존재하는 ID(Email)인 경우")
    void signUpNormalUser_alreadyExistsEmail_throwAlreadyExistsEmailException() {

    }

    @Test
    @DisplayName("일반회원가입 실패 - 입력 데이터가 null 혹은 @Valid 검증 조건에 맞지 않은 경우")
    void signUpNormalUser_invalidInput() {

    }

    @Test
    @DisplayName("일반회원가입 실패 - ID(Email), 비밀번호, 이름이 지정된 정규 표현식에 부합하지 않는 경우")
    void signUpNormalUser_notConformingToRegex() {

    }
}