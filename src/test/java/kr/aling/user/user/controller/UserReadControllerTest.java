package kr.aling.user.user.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
class UserReadControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserReadService userReadService;

    @Test
    @DisplayName("회원 존재여부 확인 성공")
    void isExistsUser() throws Exception {
        // given
        int userNo = 1;

        Mockito.when(userReadService.isExistsUserNo(any())).thenReturn(true);

        // when
        ResultActions perform = mockMvc.perform(
                RestDocumentationRequestBuilders.get("/api/v1/users/check/{userNo}", userNo)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data", equalTo(Boolean.TRUE)));

        // docs
        perform.andDo(document("user-is-exists-user",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(parameterWithName("userNo").description("확인할 유저번호")),
                responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("응답 성공여부"),
                        fieldWithPath("status").type(JsonFieldType.STRING).description("HTTP Status"),
                        fieldWithPath("data").type(JsonFieldType.BOOLEAN).description("이메일 존재여부"),
                        fieldWithPath("errorMessage").type(JsonFieldType.STRING).description("에러 메세지")
                )));
    }
}