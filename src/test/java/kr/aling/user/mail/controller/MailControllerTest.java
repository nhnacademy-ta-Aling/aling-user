package kr.aling.user.mail.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.aling.user.mail.dto.request.CheckAuthNumberMailRequestDto;
import kr.aling.user.mail.exception.MailAuthNumberInvalidException;
import kr.aling.user.mail.service.MailService;
import kr.aling.user.user.exception.UserEmailAlreadyUsedException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("이메일 인증번호 전송 성공")
    void emailCheck() throws Exception {
        // given
        String email = "test@aling.kr";

        doNothing().when(mailService).sendAuthNumber(email);

        // when
        ResultActions perform = mockMvc.perform(get("/api/v1/email-check")
                .param("email", email));

        // then
        perform.andDo(print())
                .andExpect(status().isOk());

        verify(mailService, times(1)).sendAuthNumber(email);

        // docs
        perform.andDo(document("email-check",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestParameters(
                        parameterWithName("email").description("인증번호를 받을 이메일")
                                .attributes(key("valid").value("Email 형식, 3~100 글자"))
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

        doThrow(UserEmailAlreadyUsedException.class).when(mailService).sendAuthNumber(any());

        // when
        ResultActions perform = mockMvc.perform(get("/api/v1/email-check")
                .param("email", email));

        // then
        perform.andDo(print()).andExpect(status().isConflict());
        verify(mailService, times(1)).sendAuthNumber(email);
    }

    @Test
    @DisplayName("인증번호 유효 확인 성공")
    void authNumberCheck() throws Exception {
        // given
        CheckAuthNumberMailRequestDto requestDto = new CheckAuthNumberMailRequestDto("test@aling.kr", "999999");

        doNothing().when(mailService).checkAuthNumber(any());

        // when
        ResultActions perform = mockMvc.perform(post("/api/v1/email-check")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)));

        // then
        perform.andDo(print())
                .andExpect(status().isOk());

        verify(mailService, times(1)).checkAuthNumber(any());

        // docs
        perform.andDo(document("email-auth-number-check",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                        fieldWithPath("email").type(JsonFieldType.STRING).description("이메일")
                                .attributes(key("valid").value("Not Blank, 최소 3자, 최대 100자, 이메일형식")),
                        fieldWithPath("authNumber").type(JsonFieldType.STRING).description("인증번호")
                                .attributes(key("valid").value("Not Blank, 최소 6자, 최대 6자"))
                )));
    }

    @Test
    @DisplayName("인증번호 유효 확인 실패 - 입력 데이터가 @Valid 검증 조건에 맞지 않은 경우")
    void authNumberCheck_invalidInput() throws Exception {
        // given
        CheckAuthNumberMailRequestDto requestDto = new CheckAuthNumberMailRequestDto("id", "9");

        // when
        ResultActions perform = mockMvc.perform(post("/api/v1/email-check")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)));

        // then
        perform.andDo(print()).andExpect(status().isBadRequest());
        verify(mailService, never()).checkAuthNumber(any());
    }

    @Test
    @DisplayName("인증번호 유효 확인 실패 - 유효하지 않은 인증번호인 경우")
    void authNumberCheck_invalidAuthNumber() throws Exception {
        // given
        CheckAuthNumberMailRequestDto requestDto = new CheckAuthNumberMailRequestDto("test@aling.kr", "999999");

        doThrow(MailAuthNumberInvalidException.class).when(mailService).checkAuthNumber(any());

        // when
        ResultActions perform = mockMvc.perform(post("/api/v1/email-check")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)));

        // then
        perform.andDo(print()).andExpect(status().isBadRequest());
        verify(mailService, times(1)).checkAuthNumber(any());
    }
}