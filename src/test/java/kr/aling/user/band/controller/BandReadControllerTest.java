package kr.aling.user.band.controller;

import static kr.aling.user.util.RestDocsUtil.REQUIRED;
import static kr.aling.user.util.RestDocsUtil.REQUIRED_YES;
import static org.mockito.ArgumentMatchers.anyString;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import kr.aling.user.band.dto.response.ExistsBandNameResponseDto;
import kr.aling.user.band.service.BandReadService;
import kr.aling.user.banduser.service.BandUserReadService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * 그룹 조회 컨트롤러 테스트.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@AutoConfigureRestDocs(outputDir = "target/snippets")
@WebMvcTest(BandReadController.class)
@MockBean(JpaMetamodelMappingContext.class)
class BandReadControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BandReadService bandReadService;

    @MockBean
    private BandUserReadService bandUserReadService;

    private String bandUrl = "/api/v1/bands";

    @Test
    @DisplayName("그룹명 중복 검사 성공")
    void groupNameExists_successTest() throws Exception {
        // given
        String bandName = "testBandName";

        ExistsBandNameResponseDto existsBandNameResponseDto = new ExistsBandNameResponseDto(false);

        // when
        when(bandReadService.existBandName(anyString())).thenReturn(existsBandNameResponseDto);

        mockMvc.perform(get(bandUrl + "/check-duplicate" + "?bandName={bandName}", bandName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isExist").value(existsBandNameResponseDto.getIsExist()))
                .andDo(document("band-read-checkName",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("bandName")
                                        .attributes(key(REQUIRED).value(REQUIRED_YES))
                                        .description("중복 확인할 그룹명")),
                        responseFields(
                                fieldWithPath("isExist").type(JsonFieldType.BOOLEAN)
                                        .description("그룹명 존재 여부"))

                ));

        verify(bandReadService, times(1)).existBandName(anyString());

    }
}