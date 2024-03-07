package kr.aling.user.band.controller;

import static kr.aling.user.util.RestDocsUtil.REQUIRED;
import static kr.aling.user.util.RestDocsUtil.REQUIRED_NO;
import static kr.aling.user.util.RestDocsUtil.REQUIRED_YES;
import static kr.aling.user.util.RestDocsUtil.VALID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.aling.user.band.dto.request.CreateBandPostTypeRequestDto;
import kr.aling.user.band.dto.request.CreateBandRequestDto;
import kr.aling.user.band.dto.request.ModifyBandPostTypeRequestDto;
import kr.aling.user.band.dto.request.ModifyBandRequestDto;
import kr.aling.user.band.service.BandManageService;
import kr.aling.user.banduser.dto.request.ModifyRoleOfBandUserRequestDto;
import kr.aling.user.banduser.service.BandUserManageService;
import kr.aling.user.common.utils.ConstantUtil;
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
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

/**
 * 그룹 관리 컨트롤러 테스트.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@AutoConfigureRestDocs(uriPort = 9020)
@WebMvcTest(BandManageController.class)
@MockBean(JpaMetamodelMappingContext.class)
class BandManageControllerTest {
    CreateBandRequestDto createBandRequestDto;
    ModifyBandRequestDto modifyBandRequestDto;
    ModifyRoleOfBandUserRequestDto modifyBandUserRequestDto;
    CreateBandPostTypeRequestDto createBandPostTypeRequestDto;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private BandManageService bandManageService;
    @MockBean
    private BandUserManageService bandUserManageService;
    private String bandUrl = "/api/v1/bands";

    @BeforeEach()
    void setUp() {
        createBandRequestDto = new CreateBandRequestDto();
        modifyBandRequestDto = new ModifyBandRequestDto();
        modifyBandUserRequestDto = new ModifyRoleOfBandUserRequestDto();
        createBandPostTypeRequestDto = new CreateBandPostTypeRequestDto();
    }

    @Test
    @DisplayName("그룹 생성 성공")
    void makeBand_successTest() throws Exception {
        // given
        Long userNo = 1L;

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
                                        .attributes(key(REQUIRED).value(REQUIRED_YES))
                        ),
                        requestFields(
                                fieldWithPath("bandName").type(JsonFieldType.STRING).description("그룹명")
                                        .attributes(key(REQUIRED).value(REQUIRED_YES))
                                        .attributes(key("valid").value("Not Blank, 최대 30글자")),
                                fieldWithPath("isEnter").type(JsonFieldType.BOOLEAN).description("즉시 가입 여부")
                                        .attributes(key(REQUIRED).value(REQUIRED_YES))
                                        .attributes(key("valid").value("Not Null")),
                                fieldWithPath("isViewContent").type(JsonFieldType.BOOLEAN).description("게시글 공개 여부")
                                        .attributes(key(REQUIRED).value(REQUIRED_YES))
                                        .attributes(key("valid").value("Not Null")),
                                fieldWithPath("bandInfo").type(JsonFieldType.STRING).description("그룹 소개글")
                                        .attributes(key(REQUIRED).value(REQUIRED_NO))
                                        .attributes(key("valid").value("최대 1000글자")),
                                fieldWithPath("fileNo").type(JsonFieldType.NUMBER).description("그룹 프로필 파일 번호")
                                        .attributes(key(REQUIRED).value(REQUIRED_NO))
                                        .attributes(key("valid").value(""))
                        )));

        verify(bandManageService, times(1)).makeBand(anyLong(), any());
    }

    @Test
    @DisplayName("그룹 생성 실패_회원 번호 헤더가 없음")
    void makeBand_failTest_noUserNo() throws Exception {
        // given
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

        ReflectionTestUtils.setField(createBandRequestDto, "bandName", "i".repeat(31));
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
    @DisplayName("그룹 생성 실패_그룹 소개글 길이 초과")
    void makeBand_failTest_bandInfo_sizeExceeded() throws Exception {
        // given
        Long userNo = 1L;

        ReflectionTestUtils.setField(createBandRequestDto, "bandName", "testBandName");
        ReflectionTestUtils.setField(createBandRequestDto, "isEnter", false);
        ReflectionTestUtils.setField(createBandRequestDto, "isViewContent", false);
        ReflectionTestUtils.setField(createBandRequestDto, "bandInfo", "testBand10".repeat(101));
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
    @DisplayName("그룹 정보 수정 성공")
    void updateBandInfo_SuccessTest() throws Exception {
        // given
        String bandName = "bandName";

        ReflectionTestUtils.setField(modifyBandRequestDto, "newBandName", "testBandName");
        ReflectionTestUtils.setField(modifyBandRequestDto, "isEnter", true);
        ReflectionTestUtils.setField(modifyBandRequestDto, "isViewContent", true);
        ReflectionTestUtils.setField(modifyBandRequestDto, "bandInfo", "This is band information.");
        ReflectionTestUtils.setField(modifyBandRequestDto, "fileNo", 1L);

        // when
        doNothing().when(bandManageService).updateBandInfo(anyString(), any(ModifyBandRequestDto.class));

        // then
        mockMvc.perform(RestDocumentationRequestBuilders.put(bandUrl + "/{bandName}", bandName)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(modifyBandRequestDto)))
                .andExpect(status().isOk())
                .andDo(document("band-modify",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("bandName").description("그룹 명")
                        ),
                        requestFields(
                                fieldWithPath("newBandName").type(JsonFieldType.STRING).description("그룹 명 변경값")
                                        .attributes(key(REQUIRED).value(REQUIRED_YES))
                                        .attributes(key("valid").value("Not Blank, 최대 30글자")),
                                fieldWithPath("isEnter").type(JsonFieldType.BOOLEAN).description("즉시 가입 여부 변경값")
                                        .attributes(key(REQUIRED).value(REQUIRED_YES))
                                        .attributes(key("valid").value("Not Null")),
                                fieldWithPath("isViewContent").type(JsonFieldType.BOOLEAN).description("게시글 공개 여부 변경값")
                                        .attributes(key(REQUIRED).value(REQUIRED_YES))
                                        .attributes(key("valid").value("Not Null")),
                                fieldWithPath("bandInfo").type(JsonFieldType.STRING).description("그룹 소개글 변경값")
                                        .attributes(key(REQUIRED).value(REQUIRED_NO))
                                        .attributes(key("valid").value("최대 1000글자")),
                                fieldWithPath("fileNo").type(JsonFieldType.NUMBER).description("그룹 프로필 파일 번호 변경값")
                                        .attributes(key(REQUIRED).value(REQUIRED_NO))
                                        .attributes(key("valid").value(""))
                        )));

        verify(bandManageService, times(1)).updateBandInfo(anyString(), any(ModifyBandRequestDto.class));
    }

    @Test
    @DisplayName("그룹 정보 수정 실패_그룹명이 blank")
    void updateBandInfo_failTest_bandName_blank() throws Exception {
        // given
        String bandName = "bandName";

        ReflectionTestUtils.setField(modifyBandRequestDto, "newBandName", null);
        ReflectionTestUtils.setField(modifyBandRequestDto, "isEnter", true);
        ReflectionTestUtils.setField(modifyBandRequestDto, "isViewContent", true);
        ReflectionTestUtils.setField(modifyBandRequestDto, "bandInfo", "This is band information.");
        ReflectionTestUtils.setField(modifyBandRequestDto, "fileNo", 1L);

        // when
        doNothing().when(bandManageService).updateBandInfo(anyString(), any(ModifyBandRequestDto.class));

        // then
        mockMvc.perform(RestDocumentationRequestBuilders.put(bandUrl + "/{bandName}", bandName)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(modifyBandRequestDto)))
                .andExpect(status().isBadRequest());

        verify(bandManageService, times(0)).updateBandInfo(anyString(), any(ModifyBandRequestDto.class));

    }

    @Test
    @DisplayName("그룹 정보 수정 실패_그룹명 길이 초과")
    void updateBandInfo_failTest_bandName_sizeExceeded() throws Exception {
        // given
        String bandName = "bandName";

        ReflectionTestUtils.setField(modifyBandRequestDto, "newBandName", "i".repeat(31));
        ReflectionTestUtils.setField(modifyBandRequestDto, "isEnter", true);
        ReflectionTestUtils.setField(modifyBandRequestDto, "isViewContent", true);
        ReflectionTestUtils.setField(modifyBandRequestDto, "bandInfo", "This is band information.");
        ReflectionTestUtils.setField(modifyBandRequestDto, "fileNo", 1L);

        // when
        doNothing().when(bandManageService).updateBandInfo(anyString(), any(ModifyBandRequestDto.class));

        // then
        mockMvc.perform(RestDocumentationRequestBuilders.put(bandUrl + "/{bandName}", bandName)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(modifyBandRequestDto)))
                .andExpect(status().isBadRequest());

        verify(bandManageService, times(0)).updateBandInfo(anyString(), any(ModifyBandRequestDto.class));
    }

    @Test
    @DisplayName("그룹 정보 수정 실패_즉시 가입 여부가 null")
    void updateBandInfo_failTest_isEnter_null() throws Exception {
        // given
        String bandName = "bandName";

        ReflectionTestUtils.setField(modifyBandRequestDto, "newBandName", "testBandName");
        ReflectionTestUtils.setField(modifyBandRequestDto, "isEnter", null);
        ReflectionTestUtils.setField(modifyBandRequestDto, "isViewContent", true);
        ReflectionTestUtils.setField(modifyBandRequestDto, "bandInfo", "This is band information.");
        ReflectionTestUtils.setField(modifyBandRequestDto, "fileNo", 1L);

        // when
        doNothing().when(bandManageService).updateBandInfo(anyString(), any(ModifyBandRequestDto.class));

        // then
        mockMvc.perform(RestDocumentationRequestBuilders.put(bandUrl + "/{bandName}", bandName)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(modifyBandRequestDto)))
                .andExpect(status().isBadRequest());

        verify(bandManageService, times(0)).updateBandInfo(anyString(), any(ModifyBandRequestDto.class));
    }

    @Test
    @DisplayName("그룹 정보 수정 실패_게시글 공개 여부가 null")
    void updateBandInfo_failTest_isViewContent_null() throws Exception {
        // given
        String bandName = "bandName";

        ReflectionTestUtils.setField(modifyBandRequestDto, "newBandName", "testBandName");
        ReflectionTestUtils.setField(modifyBandRequestDto, "isEnter", true);
        ReflectionTestUtils.setField(modifyBandRequestDto, "isViewContent", null);
        ReflectionTestUtils.setField(modifyBandRequestDto, "bandInfo", "This is band information.");
        ReflectionTestUtils.setField(modifyBandRequestDto, "fileNo", 1L);

        // when
        doNothing().when(bandManageService).updateBandInfo(anyString(), any(ModifyBandRequestDto.class));

        // then
        mockMvc.perform(RestDocumentationRequestBuilders.put(bandUrl + "/{bandName}", bandName)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(modifyBandRequestDto)))
                .andExpect(status().isBadRequest());

        verify(bandManageService, times(0)).updateBandInfo(anyString(), any(ModifyBandRequestDto.class));
    }

    @Test
    @DisplayName("그룹 정보 수정 실패_그룹 소개글 길이 초과")
    void updateBandInfo_failTest_bandInfo_sizeExceeded() throws Exception {
        // given
        String bandName = "bandName";

        ReflectionTestUtils.setField(modifyBandRequestDto, "newBandName", "testBandName");
        ReflectionTestUtils.setField(modifyBandRequestDto, "isEnter", true);
        ReflectionTestUtils.setField(modifyBandRequestDto, "isViewContent", null);
        ReflectionTestUtils.setField(modifyBandRequestDto, "bandInfo", "testBand10".repeat(101));
        ReflectionTestUtils.setField(modifyBandRequestDto, "fileNo", 1L);

        // when
        doNothing().when(bandManageService).updateBandInfo(anyString(), any(ModifyBandRequestDto.class));

        // then
        mockMvc.perform(RestDocumentationRequestBuilders.put(bandUrl + "/{bandName}", bandName)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(modifyBandRequestDto)))
                .andExpect(status().isBadRequest());

        verify(bandManageService, times(0)).updateBandInfo(anyString(), any(ModifyBandRequestDto.class));
    }

    @Test
    @DisplayName("그룹 삭제 성공")
    void removeBand_successTest() throws Exception {
        // given
        String bandName = "bandName";

        // when
        doNothing().when(bandManageService).removeBand(anyString());

        // then
        mockMvc.perform(delete(bandUrl + "/{bandName}", bandName))
                .andExpect(status().isNoContent())
                .andDo(document("band-delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("bandName").description("삭제할 그룹 명")
                        )));

        verify(bandManageService, times(1)).removeBand(anyString());
    }

    @Test
    @DisplayName("그룹 가입 성공")
    void joinBand_successTest() throws Exception {
        // given
        String bandName = "bandName";
        Long userNo = 1L;

        // when
        doNothing().when(bandUserManageService).makeBandUser(anyString(), anyLong());

        // then
        mockMvc.perform(post(bandUrl + "/{bandName}/users", bandName)
                        .header(ConstantUtil.X_TEMP_USER_NO, userNo))
                .andExpect(status().isCreated())
                .andDo(document("band-create-user",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName(ConstantUtil.X_TEMP_USER_NO).description("회원 번호")
                                        .attributes(key(REQUIRED).value(REQUIRED_YES))
                        )));

        verify(bandUserManageService, times(1)).makeBandUser(anyString(), anyLong());
    }

    @Test
    @DisplayName("그룹 가입 실패_회원 번호 헤더가 없는 경우")
    void joinBand_failTest_nonUserNoHeader() throws Exception {
        // given
        String bandName = "bandName";
        String userNo = "";

        // when
        doNothing().when(bandUserManageService).makeBandUser(anyString(), anyLong());

        // then
        mockMvc.perform(post(bandUrl + "/{bandName}/users", bandName)
                        .header(ConstantUtil.X_TEMP_USER_NO, userNo))
                .andExpect(status().isBadRequest());

        verify(bandUserManageService, times(0)).makeBandUser(anyString(), anyLong());
    }

    @Test
    @DisplayName("그룹 탈퇴 성공")
    void leaveBand_successTest() throws Exception {
        // given
        String bandName = "bandName";
        Long userNo = 1L;

        // when
        doNothing().when(bandUserManageService).removeBandUser(anyString(), anyLong());

        // then
        mockMvc.perform(
                        delete(bandUrl + "/{bandName}/users/{userNo}", bandName, userNo))
                .andExpect(status().isNoContent())
                .andDo(document("band-delete-user",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("bandName").description("탈퇴할 그룹 명"),
                                parameterWithName("userNo").description("탈퇴할 회원 번호")
                        )));

        verify(bandUserManageService, times(1)).removeBandUser(anyString(), anyLong());
    }

    @Test
    @DisplayName("그룹 회원 권한 수정 성공")
    void updateRoleOfBandUser_successTest() throws Exception {
        // given
        String bandName = "bandName";
        Long userNo = 1L;

        ReflectionTestUtils.setField(modifyBandUserRequestDto, "bandUserRoleNo", 1);

        // when
        doNothing().when(bandUserManageService)
                .modifyRoleOfBandUser(anyString(), anyLong(), any(ModifyRoleOfBandUserRequestDto.class));

        // then
        mockMvc.perform(
                        RestDocumentationRequestBuilders.put(bandUrl + "/{bandName}/users/{userNo}/role", bandName, userNo)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(modifyBandUserRequestDto)))
                .andExpect(status().isOk())
                .andDo(document("band-modify-user-of-role",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("bandName").description("그룹 명"),
                                parameterWithName("userNo").description("그룹 회원 권한을 수정할 회원 번호")
                        ),
                        requestFields(
                                fieldWithPath("bandUserRoleNo").type(JsonFieldType.NUMBER)
                                        .description("변경할 그룹 회원 권한 번호")
                                        .attributes(key(REQUIRED).value(REQUIRED_YES))
                                        .attributes(key("valid").value("Not Null"))
                        )));

        verify(bandUserManageService, times(1)).modifyRoleOfBandUser(anyString(), anyLong(),
                any(ModifyRoleOfBandUserRequestDto.class));
    }

    @Test
    @DisplayName("그룹 회원 권한 수정 실패_그룹 회원 권한 번호가 null")
    void updateRoleOfBandUser_failTest_bandUserRoleNoIsNull() throws Exception {
        // given
        String bandName = "bandName";
        Long userNo = 1L;

        ReflectionTestUtils.setField(modifyBandUserRequestDto, "bandUserRoleNo", null);

        // when
        doNothing().when(bandUserManageService)
                .modifyRoleOfBandUser(anyString(), anyLong(), any(ModifyRoleOfBandUserRequestDto.class));

        // then
        mockMvc.perform(
                        RestDocumentationRequestBuilders.put(bandUrl + "/{bandName}/users/{userNo}/role", bandName, userNo)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(modifyBandUserRequestDto)))
                .andExpect(status().isBadRequest());

        verify(bandUserManageService, times(0)).modifyRoleOfBandUser(anyString(), anyLong(),
                any(ModifyRoleOfBandUserRequestDto.class));
    }

    @Test
    @DisplayName("그룹 회원 creator 권한 위임 성공")
    void updateCreatorRoleOfBandUser_successTest() throws Exception {
        // given
        String bandName = "bandName";
        Long targetUserNo = 1L;
        Long userNo = 2L;

        // when
        doNothing().when(bandUserManageService).modifyCreatorRoleOfBandUser(anyString(), anyLong(), anyLong());

        // then
        mockMvc.perform(
                        RestDocumentationRequestBuilders.put(bandUrl + "/{bandName}/users/{targetUserNo}/role-delegation",
                                        bandName, targetUserNo)
                                .header(ConstantUtil.X_TEMP_USER_NO, userNo))
                .andExpect(status().isOk())
                .andDo(document("band-modify-creator",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName(ConstantUtil.X_TEMP_USER_NO).description("회원 번호")
                                        .attributes(key(REQUIRED).value(REQUIRED_YES))
                                        .attributes(key("valid").value(""))
                        ),
                        pathParameters(
                                parameterWithName("bandName").description("그룹 명"),
                                parameterWithName("targetUserNo").description("creator 를 위임 받을 회원 번호")
                        )));

        verify(bandUserManageService, times(1)).modifyCreatorRoleOfBandUser(anyString(), anyLong(), anyLong());
    }

    @Test
    @DisplayName("그룹 회원 creator 권한 위임 실패_회원 번호 헤더가 없는 경우")
    void updateCreatorRoleOfBandUser_failTest() throws Exception {
        // given
        String bandName = "bandName";
        Long targetUserNo = 1L;

        // when
        doNothing().when(bandUserManageService).modifyCreatorRoleOfBandUser(anyString(), anyLong(), anyLong());

        // then
        mockMvc.perform(RestDocumentationRequestBuilders.put(
                        bandUrl + "/{bandName}/users/{targetUserNo}/role-delegation", bandName, targetUserNo))
                .andExpect(status().isBadRequest());

        verify(bandUserManageService, times(0)).modifyCreatorRoleOfBandUser(anyString(), anyLong(), anyLong());
    }

    @Test
    @DisplayName("그룹 회원 추방 수정 성공")
    void updateBlockBandUser_successTest() throws Exception {
        // given
        String bandName = "bandName";
        Long userNo = 1L;

        // when
        doNothing().when(bandUserManageService).modifyBlockOfBandUser(anyString(), anyLong());

        // then
        mockMvc.perform(
                        RestDocumentationRequestBuilders.put(bandUrl + "/{bandName}/users/{userNo}/block", bandName, userNo))
                .andExpect(status().isOk())
                .andDo(document("band-modify-user-block",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("bandName").description("그룹 명"),
                                parameterWithName("userNo").description("추방할 회원 번호")
                        )));

        verify(bandUserManageService, times(1)).modifyBlockOfBandUser(anyString(), anyLong());
    }

    @Test
    @DisplayName("그룹 게시글 분류 생성 성공")
    void makeBandCategory_successTest() throws Exception {
        // given
        String bandName = "bandName";

        ReflectionTestUtils.setField(createBandPostTypeRequestDto, "name", "homework");

        // when
        doNothing().when(bandManageService).makeBandPostType(anyString(), any(CreateBandPostTypeRequestDto.class));

        // then
        mockMvc.perform(RestDocumentationRequestBuilders.post(bandUrl + "/{bandName}/post-types", bandName)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createBandPostTypeRequestDto)))
                .andExpect(status().isCreated())
                .andDo(document("band-create-post-type",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("bandName").description("그룹 명")
                        ),
                        requestFields(
                                fieldWithPath("name").type(JsonFieldType.STRING).description("생성할 그룹 게시글 분류 명")
                                        .attributes(key(REQUIRED).value(REQUIRED_YES))
                                        .attributes(key("valid").value("Not Blank, 최대 10글자"))
                        )));

        verify(bandManageService, times(1)).makeBandPostType(anyString(), any(CreateBandPostTypeRequestDto.class));
    }

    @Test
    @DisplayName("그룹 게시글 분류 생성 실패_분류 명이 blank")
    void makeBandCategory_failTest_nameIsBlank() throws Exception {
        // given
        String bandName = "bandName";

        ReflectionTestUtils.setField(createBandPostTypeRequestDto, "name", "  ");

        // when
        doNothing().when(bandManageService).makeBandPostType(anyString(), any(CreateBandPostTypeRequestDto.class));

        // then
        mockMvc.perform(post(bandUrl + "/{bandName}/post-types", bandName)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createBandPostTypeRequestDto)))
                .andExpect(status().isBadRequest());

        verify(bandManageService, times(0)).makeBandPostType(anyString(), any(CreateBandPostTypeRequestDto.class));
    }

    @Test
    @DisplayName("그룹 게시글 분류 생성 실패_분류 명 길이 초과")
    void makeBandCategory_failTest_sizeExceeded() throws Exception {
        // given
        String bandName = "bandName";

        ReflectionTestUtils.setField(createBandPostTypeRequestDto, "name", "a".repeat(11));

        // when
        doNothing().when(bandManageService).makeBandPostType(anyString(), any(CreateBandPostTypeRequestDto.class));

        // then
        mockMvc.perform(post(bandUrl + "/{bandName}/post-types", bandName)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createBandPostTypeRequestDto)))
                .andExpect(status().isBadRequest());

        verify(bandManageService, times(0)).makeBandPostType(anyString(), any(CreateBandPostTypeRequestDto.class));
    }

    @Test
    @DisplayName("그룹 게시글 분류 수정 성공")
    void updateBandCategory_successTest() throws Exception {
        // given
        String bandName = "bandName";
        Long postTypeNo = 1L;
        ModifyBandPostTypeRequestDto requestDto = new ModifyBandPostTypeRequestDto();

        ReflectionTestUtils.setField(requestDto, "bandPostTypeName", "newName");

        // when
        doNothing().when(bandManageService)
                .modifyBandPostType(anyString(), anyLong(), any(ModifyBandPostTypeRequestDto.class));

        // then
        mockMvc.perform(RestDocumentationRequestBuilders.put(bandUrl + "/{bandName}/post-types/{postTypeNo}", bandName,
                                postTypeNo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andDo(document("band-modify-post-type",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                pathParameters(
                                        parameterWithName("bandName").description("그룹 명"),
                                        parameterWithName("postTypeNo").description("그룹 게시글 분류 번호")
                                ),
                                requestFields(
                                        fieldWithPath("bandPostTypeName").type(String.class).description("수정 그룹 게시글 분류 명")
                                                .attributes(key(REQUIRED).value(REQUIRED_YES))
                                                .attributes(key(VALID).value("Not Blank, 최대 10자"))
                                )
                        )
                );

        verify(bandManageService, times(1)).modifyBandPostType(anyString(), anyLong(),
                any(ModifyBandPostTypeRequestDto.class));
    }

    @Test
    @DisplayName("그룹 게시글 분류 수정 실패_그룹 게시글 분류 명이 blank")
    void updateBandCategory_failTest_nameIsBlank() throws Exception {
        // given
        String bandName = "bandName";
        Long postTypeNo = 1L;
        ModifyBandPostTypeRequestDto requestDto = new ModifyBandPostTypeRequestDto();

        ReflectionTestUtils.setField(requestDto, "bandPostTypeName", "a".repeat(11));

        // when
        doNothing().when(bandManageService)
                .modifyBandPostType(anyString(), anyLong(), any(ModifyBandPostTypeRequestDto.class));

        // then
        mockMvc.perform(RestDocumentationRequestBuilders.put(bandUrl + "/{bandName}/post-types/{postTypeNo}", bandName,
                                postTypeNo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());

        verify(bandManageService, times(0)).modifyBandPostType(anyString(), anyLong(),
                any(ModifyBandPostTypeRequestDto.class));
    }

    @Test
    @DisplayName("그룹 게시글 분류 수정 실패_그룹 게시글 분류 명 사이즈 초과")
    void updateBandCategory_failTest_nameSizeExceeded() throws Exception {
        // given
        String bandName = "bandName";
        Long postTypeNo = 1L;
        ModifyBandPostTypeRequestDto requestDto = new ModifyBandPostTypeRequestDto();

        ReflectionTestUtils.setField(requestDto, "bandPostTypeName", "a".repeat(11));

        // when
        doNothing().when(bandManageService)
                .modifyBandPostType(anyString(), anyLong(), any(ModifyBandPostTypeRequestDto.class));

        // then
        mockMvc.perform(RestDocumentationRequestBuilders.put(bandUrl + "/{bandName}/post-types/{postTypeNo}", bandName,
                                postTypeNo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());

        verify(bandManageService, times(0)).modifyBandPostType(anyString(), anyLong(),
                any(ModifyBandPostTypeRequestDto.class));
    }

    @Test
    @DisplayName("그룹 게시글 분류 삭제 성공")
    void deleteBandCategory_successTest() throws Exception {
        // given
        String bandName = "bandName";
        Long postTypeNo = 1L;

        // when
        doNothing().when(bandManageService).deleteBandPostType(anyString(), anyLong());

        // then
        mockMvc.perform(delete(bandUrl + "/{bandName}/post-types/{postTypeNo}", bandName, postTypeNo))
                .andExpect(status().isNoContent())
                .andDo(document("band-delete-post-type",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("bandName").description("그룹 명"),
                                parameterWithName("postTypeNo").description("그룹 게시글 분류 번호")
                        )));

        verify(bandManageService, times(1)).deleteBandPostType(anyString(), anyLong());
    }
}
