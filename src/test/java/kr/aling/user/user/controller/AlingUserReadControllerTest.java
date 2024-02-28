package kr.aling.user.user.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import kr.aling.user.band.service.BandReadService;
import kr.aling.user.user.dto.response.LoginResponseDto;
import kr.aling.user.user.dto.resquest.LoginRequestDto;
import kr.aling.user.user.service.UserReadService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@AutoConfigureRestDocs(outputDir = "target/snippets")
@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(UserReadController.class)
class AlingUserReadControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserReadService userReadService;

    @MockBean
    private BandReadService bandReadService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("회원 존재여부 확인 성공")
    void isExistsUser() throws Exception {
        // given
        int userNo = 1;

        Mockito.when(userReadService.isExistsUserNo(any())).thenReturn(true);

        // when
        ResultActions perform = mockMvc.perform(
                RestDocumentationRequestBuilders.get("/api/v1/users/check/{userNo}", userNo));

        // then
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.isExists", equalTo(Boolean.TRUE)));

        // docs
        perform.andDo(document("user-is-exists-user",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(parameterWithName("userNo").description("확인할 유저번호")),
                responseFields(
                        fieldWithPath("isExists").type(JsonFieldType.BOOLEAN).description("이메일 존재여부")
                )));
    }

    @Test
    @DisplayName("회원 로그인 성공")
    void login() throws Exception {
        //given
        String email = "test@test.com";
        String password = "password";

        LoginRequestDto requestDto = new LoginRequestDto(email, password);
        LoginResponseDto responseDto = new LoginResponseDto(1L, List.of("ROLE_TEST"));

        Mockito.when(userReadService.login(any())).thenReturn(responseDto);

        //when
        ResultActions perform = mockMvc.perform(
                RestDocumentationRequestBuilders.get("/api/v1/users/login")
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON));

        //then
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userNo", equalTo(responseDto.getUserNo().intValue())))
                .andExpect(jsonPath("$.roles.[0]", equalTo(responseDto.getRoles().get(0))));

        //docs
        perform.andDo(document("user-login",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                        fieldWithPath(".email").description("로그인 할 이메일").type(String.class)
                                .attributes(key("valid").value("최소 3자, 최대 50자")),
                        fieldWithPath(".password").description("비밀번호").type(String.class)
                                .attributes(key("valid").value("최소 8자, 최대 20자"))),
                responseFields(
                        fieldWithPath(".userNo").description("회원 번호").type(Long.TYPE),
                        fieldWithPath(".roles[]").description("권한 리스트").type(List.class))));
    }
}