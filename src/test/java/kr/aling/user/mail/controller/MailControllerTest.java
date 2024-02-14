package kr.aling.user.mail.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import kr.aling.user.mail.service.MailService;
import kr.aling.user.user.exception.UserEmailAlreadyUsedException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@AutoConfigureRestDocs(outputDir = "target/snippets")
@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(MailController.class)
class MailControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MailService mailService;

    @Test
    @DisplayName("이메일 인증번호 전송 성공")
    void emailCheck() throws Exception {
        // given
        int authNumber = 999999;
        String email = "test@aling.kr";

        Mockito.when(mailService.sendAuthNumber(any())).thenReturn(authNumber);

        // when
        ResultActions perform = mockMvc.perform(get("/api/v1/email-check")
                .param("email", email));

        // then
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.authNumber", equalTo(authNumber)));
        verify(mailService, times(1)).sendAuthNumber(email);

        // docs
        perform.andDo(document("email-check",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestParameters(
                        parameterWithName("email").description("인증번호를 받을 이메일")
                                .attributes(key("valid").value("Email 형식, 3~100 글자"))
                ),
                responseFields(
                        fieldWithPath("authNumber").type(JsonFieldType.NUMBER).description("생성된 인증번호")
                )));
    }

    @Test
    @DisplayName("이메일 인증번호 전송 실패 - 입력 이메일이 검증 조건에 맞지 않은 경우")
    void signUpNormalUser_invalidInput() throws Exception {
        // given
        String email = "test";

        // when
        ResultActions perform = mockMvc.perform(get("/api/v1/email-check")
                .param("email", email));

        // then
        perform.andDo(print()).andExpect(status().isBadRequest());
        verify(mailService, never()).sendAuthNumber(email);
    }

    @Test
    @DisplayName("이메일 인증번호 전송 실패 - 이미 존재하는 이메일인 경우")
    void signUpNormalUser_alreadyExistsEmail() throws Exception {
        // given
        String email = "test@aling.kr";

        when(mailService.sendAuthNumber(any())).thenThrow(new UserEmailAlreadyUsedException(email));

        // when
        ResultActions perform = mockMvc.perform(get("/api/v1/email-check")
                .param("email", email));

        // then
        perform.andDo(print()).andExpect(status().isConflict());
        verify(mailService, times(1)).sendAuthNumber(email);
    }
}