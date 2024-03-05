package kr.aling.user.user.controller;

import static kr.aling.user.util.RestDocsUtil.REQUIRED;
import static kr.aling.user.util.RestDocsUtil.REQUIRED_YES;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
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
import kr.aling.user.band.dto.response.GetBandDetailInfoResponseDto;
import kr.aling.user.band.service.BandReadService;
import kr.aling.user.common.utils.ConstantUtil;
import kr.aling.user.user.dto.response.LoginResponseDto;
import kr.aling.user.user.dto.resquest.LoginRequestDto;
import kr.aling.user.user.service.UserReadService;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@AutoConfigureRestDocs(uriPort = 9020)
@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(UserReadController.class)
class AlingUserReadControllerTest {

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserReadService userReadService;
    @MockBean
    private BandReadService bandReadService;

    private final String url = "/api/v1/users";

    @Test
    @DisplayName("회원 존재여부 확인 성공")
    void isExistsUser() throws Exception {
        // given
        int userNo = 1;

        when(userReadService.isExistsUserNo(any())).thenReturn(true);

        // when
        ResultActions perform = mockMvc.perform(
                RestDocumentationRequestBuilders.get("/api/v1/users/check/{userNo}", userNo)
                        .contentType(MediaType.APPLICATION_JSON));

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
    @DisplayName("회원의 가입 그룹 목록 조회 성공 테스트")
    void getJoinedBandInfoList_successTest() throws Exception {
        // given
        String userNo = "1";

        GetBandDetailInfoResponseDto getBandDetailInfoResponseDto =
                new GetBandDetailInfoResponseDto(1L, "bandName", 1L, "band information", true, true, false, 1L, 1);

        // when
        when(bandReadService.getJoinedBandInfoList(anyLong()))
                .thenReturn(List.of(getBandDetailInfoResponseDto));

        // then
        mockMvc.perform(RestDocumentationRequestBuilders.get(url + "/my-bands")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(ConstantUtil.X_TEMP_USER_NO, userNo))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].bandNo").value(getBandDetailInfoResponseDto.getBandNo()))
                .andExpect(jsonPath("$[0].name").value(getBandDetailInfoResponseDto.getName()))
                .andExpect(jsonPath("$[0].fileNo").value(getBandDetailInfoResponseDto.getFileNo()))
                .andExpect(jsonPath("$[0].info").value(getBandDetailInfoResponseDto.getInfo()))
                .andExpect(jsonPath("$[0].isEnter").value(getBandDetailInfoResponseDto.getIsEnter()))
                .andExpect(jsonPath("$[0].isViewContent").value(getBandDetailInfoResponseDto.getIsViewContent()))
                .andExpect(jsonPath("$[0].isUpload").value(getBandDetailInfoResponseDto.getIsUpload()))
                .andExpect(jsonPath("$[0].bandUserNo").value(getBandDetailInfoResponseDto.getBandUserNo()))
                .andExpect(jsonPath("$[0].bandUserRoleNo").value(getBandDetailInfoResponseDto.getBandUserRoleNo()))
                .andDo(document("user-get-my-bands",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName(ConstantUtil.X_TEMP_USER_NO).description("회원 번호")
                                        .attributes(key(REQUIRED).value(REQUIRED_YES))
                        ),
                        responseFields(
                                fieldWithPath("[].bandNo").description("그룹 번호").type(Long.class),
                                fieldWithPath("[].name").description("그룹 명").type(String.class),
                                fieldWithPath("[].fileNo").description("파일 번호").type(Long.class),
                                fieldWithPath("[].info").description("그룹 소개글").type(String.class),
                                fieldWithPath("[].isEnter").description("그룹 즉시 가입 여부").type(Boolean.class),
                                fieldWithPath("[].isViewContent").description("그룹 게시글 공개 여부").type(Boolean.class),
                                fieldWithPath("[].isUpload").description("파일 업로드 가능 여부").type(Boolean.class),
                                fieldWithPath("[].bandUserNo").description("그룹 회원 번호").type(Long.class),
                                fieldWithPath("[].bandUserRoleNo").description("그룹 회원 권한 번호").type(Integer.class)
                        )
                ));

    }

    @Test
    @DisplayName("회원 로그인 성공")
    void login() throws Exception {
        //given
        String email = "test@test.com";
        String password = "password";

        LoginRequestDto requestDto = new LoginRequestDto(email, password);
        LoginResponseDto responseDto = new LoginResponseDto(1L, List.of("ROLE_TEST"));

        when(userReadService.login(any())).thenReturn(responseDto);

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