package kr.aling.user.band.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.aling.user.band.dto.request.CreateBandRequestDto;
import kr.aling.user.band.service.BandManageService;
import kr.aling.user.common.utils.ConstantUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Some description here.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@AutoConfigureRestDocs(outputDir = "target/snippets")
@WebMvcTest(BandManageController.class)
@MockBean(JpaMetamodelMappingContext.class)
class BandManageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BandManageService bandManageService;

    private String bandUrl = "/api/v1/bands";

    @Test
    @DisplayName("그룹 생성 성공")
    void makeBand_successTest() throws Exception {
        // given
        Long userNo = 1L;

        CreateBandRequestDto createBandRequestDto = new CreateBandRequestDto();
        ReflectionTestUtils.setField(createBandRequestDto, "bandName", "testBandName");
        ReflectionTestUtils.setField(createBandRequestDto, "isEnter", false);
        ReflectionTestUtils.setField(createBandRequestDto, "isViewContent", false);
        ReflectionTestUtils.setField(createBandRequestDto, "bandInfo", "I'm band!");
        ReflectionTestUtils.setField(createBandRequestDto, "fileNo", 1L);

        // when
        doNothing().when(bandManageService).makeBand(anyLong(), any());

        // then
        mockMvc.perform(post(bandUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(ConstantUtil.X_TEMP_USER_NO, userNo)
                        .content(objectMapper.writeValueAsString(createBandRequestDto)))
                .andExpect(status().isCreated())
                .andDo(document("band-create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName(ConstantUtil.X_TEMP_USER_NO).description(
                                        "회원 번호")
                        ),
                        requestFields(
                                fieldWithPath("bandName").type(JsonFieldType.STRING).description("그룹명")
                                        .attributes(key("valid").value("Not Blank, 최대 30글자")),
                                fieldWithPath("isEnter").type(JsonFieldType.BOOLEAN).description("즉시 가입 여부")
                                        .attributes(key("valid").value("Not Null")),
                                fieldWithPath("isViewContent").type(JsonFieldType.BOOLEAN).description("게시글 공개 여부")
                                        .attributes(key("valid").value("Not Null")),
                                fieldWithPath("bandInfo").type(JsonFieldType.STRING).description("그룹 소개글")
                                        .attributes(key("valid").value("Not Blank, 최대 1000글자")),
                                fieldWithPath("fileNo").type(JsonFieldType.NUMBER).description("그룹 프로필 파일 번호")
                                        .attributes(key("valid").value(""))
                        )));

        verify(bandManageService, times(1)).makeBand(anyLong(), any());
    }

    @Test
    @DisplayName("그룹 생성 실패_회원 번호 헤더가 없음")
    void makeBand_failTest_noUserNo() throws Exception {
        // given
        CreateBandRequestDto createBandRequestDto = new CreateBandRequestDto();
        ReflectionTestUtils.setField(createBandRequestDto, "bandName", "testBandName");
        ReflectionTestUtils.setField(createBandRequestDto, "isEnter", false);
        ReflectionTestUtils.setField(createBandRequestDto, "isViewContent", false);
        ReflectionTestUtils.setField(createBandRequestDto, "bandInfo", "I'm band!");
        ReflectionTestUtils.setField(createBandRequestDto, "fileNo", null);

        // when
        doNothing().when(bandManageService).makeBand(anyLong(), any());

        // then
        mockMvc.perform(post(bandUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createBandRequestDto)))
                .andExpect(status().isBadRequest());

        verify(bandManageService, times(0)).makeBand(anyLong(), any());
    }

    @Test
    @DisplayName("그룹 생성 실패_그룹명이 blank")
    void makeBand_failTest_bandName_blank() throws Exception {
        // given
        Long userNo = 1L;

        CreateBandRequestDto createBandRequestDto = new CreateBandRequestDto();
        ReflectionTestUtils.setField(createBandRequestDto, "bandName", "    ");
        ReflectionTestUtils.setField(createBandRequestDto, "isEnter", false);
        ReflectionTestUtils.setField(createBandRequestDto, "isViewContent", false);
        ReflectionTestUtils.setField(createBandRequestDto, "bandInfo", "I'm band!");
        ReflectionTestUtils.setField(createBandRequestDto, "fileNo", null);

        // when
        doNothing().when(bandManageService).makeBand(anyLong(), any());

        // then
        mockMvc.perform(post(bandUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(ConstantUtil.X_TEMP_USER_NO, userNo)
                        .content(objectMapper.writeValueAsString(createBandRequestDto)))
                .andExpect(status().isBadRequest());

        verify(bandManageService, times(0)).makeBand(anyLong(), any());
    }

    @Test
    @DisplayName("그룹 생성 실패_그룹명 길이 초과")
    void makeBand_failTest_bandName_sizeExceeded() throws Exception {
        // given
        Long userNo = 1L;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            sb.append("testBand10");
        }

        CreateBandRequestDto createBandRequestDto = new CreateBandRequestDto();
        ReflectionTestUtils.setField(createBandRequestDto, "bandName", sb.toString());
        ReflectionTestUtils.setField(createBandRequestDto, "isEnter", false);
        ReflectionTestUtils.setField(createBandRequestDto, "isViewContent", false);
        ReflectionTestUtils.setField(createBandRequestDto, "bandInfo", "I'm band!");
        ReflectionTestUtils.setField(createBandRequestDto, "fileNo", null);

        // when
        doNothing().when(bandManageService).makeBand(anyLong(), any());

        // then
        mockMvc.perform(post(bandUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(ConstantUtil.X_TEMP_USER_NO, userNo)
                        .content(objectMapper.writeValueAsString(createBandRequestDto)))
                .andExpect(status().isBadRequest());

        verify(bandManageService, times(0)).makeBand(anyLong(), any());
    }

    @Test
    @DisplayName("그룹 생성 실패_즉시 가입 여부가 null")
    void makeBand_failTest_isEnter_null() throws Exception {
        // given
        Long userNo = 1L;

        CreateBandRequestDto createBandRequestDto = new CreateBandRequestDto();
        ReflectionTestUtils.setField(createBandRequestDto, "bandName", "testBandName");
        ReflectionTestUtils.setField(createBandRequestDto, "isEnter", null);
        ReflectionTestUtils.setField(createBandRequestDto, "isViewContent", false);
        ReflectionTestUtils.setField(createBandRequestDto, "bandInfo", "I'm band!");
        ReflectionTestUtils.setField(createBandRequestDto, "fileNo", null);

        // when
        doNothing().when(bandManageService).makeBand(anyLong(), any());

        // then
        mockMvc.perform(post(bandUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(ConstantUtil.X_TEMP_USER_NO, userNo)
                        .content(objectMapper.writeValueAsString(createBandRequestDto)))
                .andExpect(status().isBadRequest());

        verify(bandManageService, times(0)).makeBand(anyLong(), any());
    }

    @Test
    @DisplayName("그룹 생성 실패_게시물 공개 여부가 null")
    void makeBand_failTest_isViewContent_null() throws Exception {
        // given
        Long userNo = 1L;

        CreateBandRequestDto createBandRequestDto = new CreateBandRequestDto();
        ReflectionTestUtils.setField(createBandRequestDto, "bandName", "testBandName");
        ReflectionTestUtils.setField(createBandRequestDto, "isEnter", false);
        ReflectionTestUtils.setField(createBandRequestDto, "isViewContent", null);
        ReflectionTestUtils.setField(createBandRequestDto, "bandInfo", "I'm band!");
        ReflectionTestUtils.setField(createBandRequestDto, "fileNo", null);

        // when
        doNothing().when(bandManageService).makeBand(anyLong(), any());

        // then
        mockMvc.perform(post(bandUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(ConstantUtil.X_TEMP_USER_NO, userNo)
                        .content(objectMapper.writeValueAsString(createBandRequestDto)))
                .andExpect(status().isBadRequest());

        verify(bandManageService, times(0)).makeBand(anyLong(), any());
    }

    @Test
    @DisplayName("그룹 생성 실패_그룹 소개글이 blank")
    void makeBand_failTest_bandInfo_blank() throws Exception {
        // given
        Long userNo = 1L;

        CreateBandRequestDto createBandRequestDto = new CreateBandRequestDto();
        ReflectionTestUtils.setField(createBandRequestDto, "bandName", "testBandName");
        ReflectionTestUtils.setField(createBandRequestDto, "isEnter", false);
        ReflectionTestUtils.setField(createBandRequestDto, "isViewContent", false);
        ReflectionTestUtils.setField(createBandRequestDto, "bandInfo", "   ");
        ReflectionTestUtils.setField(createBandRequestDto, "fileNo", null);

        // when
        doNothing().when(bandManageService).makeBand(anyLong(), any());

        // then
        mockMvc.perform(post(bandUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(ConstantUtil.X_TEMP_USER_NO, userNo)
                        .content(objectMapper.writeValueAsString(createBandRequestDto)))
                .andExpect(status().isBadRequest());

        verify(bandManageService, times(0)).makeBand(anyLong(), any());
    }

    @Test
    @DisplayName("그룹 생성 실패_그룹 소개글 길이 초과")
    void makeBand_failTest_bandInfo_sizeExceeded() throws Exception {
        // given
        Long userNo = 1L;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 101; i++) {
            sb.append("testBand10");
        }

        CreateBandRequestDto createBandRequestDto = new CreateBandRequestDto();
        ReflectionTestUtils.setField(createBandRequestDto, "bandName", "testBandName");
        ReflectionTestUtils.setField(createBandRequestDto, "isEnter", false);
        ReflectionTestUtils.setField(createBandRequestDto, "isViewContent", false);
        ReflectionTestUtils.setField(createBandRequestDto, "bandInfo", sb.toString());
        ReflectionTestUtils.setField(createBandRequestDto, "fileNo", null);

        // when
        doNothing().when(bandManageService).makeBand(anyLong(), any());

        // then
        mockMvc.perform(post(bandUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(ConstantUtil.X_TEMP_USER_NO, userNo)
                        .content(objectMapper.writeValueAsString(createBandRequestDto)))
                .andExpect(status().isBadRequest());

        verify(bandManageService, times(0)).makeBand(anyLong(), any());
    }
}
