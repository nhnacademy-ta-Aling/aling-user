package kr.aling.user.user.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import kr.aling.user.user.dto.response.LoginResponseDto;
import kr.aling.user.user.dto.resquest.LoginRequestDto;
import kr.aling.user.user.service.LoginService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@AutoConfigureRestDocs(uriPort = 9020)
@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(LoginController.class)
class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LoginService loginService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("회원 로그인 성공")
    void login() throws Exception {
        //given
        String email = "test@test.com";
        String password = "password";

        LoginRequestDto requestDto = new LoginRequestDto(email, password);
        LoginResponseDto responseDto = new LoginResponseDto(1L, List.of("ROLE_TEST"));

        when(loginService.login(any())).thenReturn(responseDto);

        //when
        ResultActions perform = mockMvc.perform(
                RestDocumentationRequestBuilders.post("/api/v1/login")
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON));

        //then
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().exists("X-User-No"))
                .andExpect(header().string("X-User-No", String.valueOf(responseDto.getUserNo())))
                .andExpect(header().exists("X-User-Role"))
                .andExpect(header().string("X-User-Role", objectMapper.writeValueAsString(responseDto.getRoles())));

        //docs
        perform.andDo(document("user-login",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                        fieldWithPath(".email").description("로그인 할 이메일").type(String.class)
                                .attributes(key("valid").value("최소 3자, 최대 50자")),
                        fieldWithPath(".password").description("비밀번호").type(String.class)
                                .attributes(key("valid").value("최소 8자, 최대 20자"))
                ),
                responseHeaders(
                        headerWithName("X-User-No").description("회원 번호"),
                        headerWithName("X-User-Role").description("회원 권한 리스트")
                )));
    }
}