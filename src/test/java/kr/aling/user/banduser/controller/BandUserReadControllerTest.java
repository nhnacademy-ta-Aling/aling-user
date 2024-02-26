package kr.aling.user.banduser.controller;

import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import kr.aling.user.banduser.dto.response.GetPostWriterResponseDto;
import kr.aling.user.banduser.service.BandUserReadService;
import org.junit.jupiter.api.BeforeEach;
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

@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(BandUserReadController.class)
@AutoConfigureRestDocs(uriPort = 9020)
class BandUserReadControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    BandUserReadService bandUserReadService;

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("그룹 회원 번호로 회원 정보 조회 API 테스트")
    void getBandUserInfo_by_bandUserNo() throws Exception {
        // given
        GetPostWriterResponseDto getPostWriterResponseDto =
                new GetPostWriterResponseDto(1L, "사용자 이름",
                        "https://kr1-api-object-storage.nhncloudservice.com/v1/AUTH_c20e3b10d61749a2a52346ed0261d79e/aling-dev/HOOKS/x8iCAghRK6gYlovkzsuK-orcOSNNqVgxKtTkpmYYR.jpeg");

        // when
        when(bandUserReadService.getPostWriterInfo(anyLong())).thenReturn(getPostWriterResponseDto);

        // then
        mvc.perform(RestDocumentationRequestBuilders.get("/api/v1/band-users/{bandUserNo}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("userNo").value(getPostWriterResponseDto.getUserNo()))
                .andExpect(jsonPath("username").value(getPostWriterResponseDto.getUsername()))
                .andExpect(jsonPath("profilePath").value(getPostWriterResponseDto.getProfilePath()))
                .andDo(print())
                .andDo(document("band-user-get-by-bandUserNo",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),

                        pathParameters(
                                parameterWithName("bandUserNo").description("그룹 회원 번호")
                        ),

                        responseFields(
                                fieldWithPath("userNo").description("회원 번호"),
                                fieldWithPath("username").description("회원 이름"),
                                fieldWithPath("profilePath").description("프로필 이미지 경로, 없다면 Null 반환")
                        )
                ));
    }
}