package kr.aling.user.band.controller;

import static kr.aling.user.common.utils.ConstantUtil.X_TEMP_USER_NO;
import static kr.aling.user.util.RestDocsUtil.REQUIRED;
import static kr.aling.user.util.RestDocsUtil.REQUIRED_NO;
import static kr.aling.user.util.RestDocsUtil.REQUIRED_YES;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
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
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import kr.aling.user.band.dto.response.ExistsBandNameResponseDto;
import kr.aling.user.band.dto.response.GetBandInfoResponseDto;
import kr.aling.user.band.dto.response.GetBandInfoWithBandUserResponseDto;
import kr.aling.user.band.dto.response.external.GetBandPostTypeResponseDto;
import kr.aling.user.band.service.BandReadService;
import kr.aling.user.banduser.dto.response.GetBandUserAndUserInfoResponseDto;
import kr.aling.user.banduser.dto.response.GetBandUserAuthResponseDto;
import kr.aling.user.banduser.dto.response.GetBandUserInfoResponseDto;
import kr.aling.user.banduser.service.BandUserReadService;
import kr.aling.user.common.dto.PageResponseDto;
import kr.aling.user.user.dto.response.GetUserSimpleInfoResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

/**
 * 그룹 조회 컨트롤러 테스트.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@AutoConfigureRestDocs(uriPort = 9020)
@WebMvcTest(BandReadController.class)
@MockBean(JpaMetamodelMappingContext.class)
class BandReadControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BandReadService bandReadService;

    @MockBean
    private BandUserReadService bandUserReadService;

    private final String bandUrl = "/api/v1/bands";

    GetBandInfoResponseDto getBandInfoResponseDto;
    GetBandUserInfoResponseDto getBandUserInfoResponseDto;
    GetUserSimpleInfoResponseDto getUserSimpleInfoResponseDto;

    @BeforeEach
    void setUp() {
        getBandInfoResponseDto = new GetBandInfoResponseDto(1L, "bandName",1L, "band information", true, true, false);
        getBandUserInfoResponseDto = new GetBandUserInfoResponseDto(1L, 1);
        getUserSimpleInfoResponseDto = new GetUserSimpleInfoResponseDto(1L, 1L, "user name");
    }

    @Test
    @DisplayName("그룹명 중복 검사 성공")
    void groupNameExists_successTest() throws Exception {
        // given
        String bandName = "testBandName";

        ExistsBandNameResponseDto existsBandNameResponseDto = new ExistsBandNameResponseDto(false);

        // when
        when(bandReadService.existBandName(anyString())).thenReturn(existsBandNameResponseDto);

        mockMvc.perform(get(bandUrl + "/check-duplicate" + "?bandName={bandName}", bandName)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isExist").value(existsBandNameResponseDto.getIsExist()))
                .andDo(document("band-get-checkName",
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

    @Test
    @DisplayName("그룹명으로 그룹 상세 정보 조회 성공")
    void bandDetailInfo_successTest() throws Exception {
        // given
        GetBandInfoWithBandUserResponseDto getResponseDto = new GetBandInfoWithBandUserResponseDto(getBandInfoResponseDto, getBandUserInfoResponseDto);
        String bandName = "testBandName";

        // when
        when(bandReadService.getBandDetailInfo(anyString(), anyLong())).thenReturn(getResponseDto);

        mockMvc.perform(RestDocumentationRequestBuilders.get(bandUrl + "/{bandName}", bandName)
                        .header(X_TEMP_USER_NO, 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("bandInfo.bandNo").value(getResponseDto.getBandInfo().getBandNo()))
                .andExpect(jsonPath("bandInfo.name").value(getResponseDto.getBandInfo().getName()))
                .andExpect(jsonPath("bandInfo.fileNo").value(getResponseDto.getBandInfo().getFileNo()))
                .andExpect(jsonPath("bandInfo.info").value(getResponseDto.getBandInfo().getInfo()))
                .andExpect(jsonPath("bandInfo.isEnter").value(getResponseDto.getBandInfo().getIsEnter()))
                .andExpect(jsonPath("bandInfo.isViewContent").value(getResponseDto.getBandInfo().getIsViewContent()))
                .andExpect(jsonPath("bandInfo.isUpload").value(getResponseDto.getBandInfo().getIsUpload()))
                .andExpect(jsonPath("bandUserInfo.bandUserNo").value(getResponseDto.getBandUserInfo().getBandUserNo()))
                .andExpect(jsonPath("bandUserInfo.bandUserRoleNo").value(getResponseDto.getBandUserInfo().getBandUserRoleNo()))
                .andDo(print())
                .andDo(document("band-get-detail-by-bandName",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),

                        requestHeaders(
                                headerWithName(X_TEMP_USER_NO).description("유저 번호")
                                        .attributes(key(REQUIRED).value(REQUIRED_YES))
                        ),

                        pathParameters(
                                parameterWithName("bandName").description("그룹 이름")
                        ),

                        responseFields(
                                fieldWithPath("bandInfo.bandNo").description("그룹 번호"),
                                fieldWithPath("bandInfo.name").description("그룹 이름"),
                                fieldWithPath("bandInfo.fileNo").description("그룹 프로필 사진 파일 번호"),
                                fieldWithPath("bandInfo.info").description("그룹 설명"),
                                fieldWithPath("bandInfo.isEnter").description("그룹 가입 여부"),
                                fieldWithPath("bandInfo.isViewContent").description("그룹 게시글 공개 여부"),
                                fieldWithPath("bandInfo.isUpload").description("그룹 파일 업로드 여부"),
                                fieldWithPath("bandUserInfo.bandUserNo").description("그룹 회원 번호"),
                                fieldWithPath("bandUserInfo.bandUserRoleNo").description("그룹 회원 역할 번호")
                        )
                ));

        verify(bandReadService, times(1)).getBandDetailInfo(anyString(), anyLong());
    }

    @Test
    @DisplayName("그룹 명을 통해 그룹 리스트 조회 API 테스트")
    void searchBandBasicInfoList_successTest() throws Exception {
        // given
        String bandName = "bandName";
        PageResponseDto<GetBandInfoResponseDto> pageResponseDto
                = new PageResponseDto<>(0, 1, 1L , List.of(getBandInfoResponseDto));

        // when
        when(bandReadService.getSearchBandInfoList(anyString(), any(Pageable.class))).thenReturn(pageResponseDto);

        // then
        mockMvc.perform(RestDocumentationRequestBuilders.get(bandUrl + "/search")
                .param("bandName", bandName)
                        .param("page", "0")
                        .param("size", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("pageNumber").value(pageResponseDto.getPageNumber()))
                .andExpect(jsonPath("totalPages").value(pageResponseDto.getTotalPages()))
                .andExpect(jsonPath("totalElements").value(pageResponseDto.getTotalElements()))
                .andExpect(jsonPath("$.content[0].bandNo").value(pageResponseDto.getContent().get(0).getBandNo()))
                .andExpect(jsonPath("$.content[0].name").value(pageResponseDto.getContent().get(0).getName()))
                .andExpect(jsonPath("$.content[0].fileNo").value(pageResponseDto.getContent().get(0).getFileNo()))
                .andExpect(jsonPath("$.content[0].info").value(pageResponseDto.getContent().get(0).getInfo()))
                .andExpect(jsonPath("$.content[0].isEnter").value(pageResponseDto.getContent().get(0).getIsEnter()))
                .andExpect(jsonPath("$.content[0].isViewContent").value(pageResponseDto.getContent().get(0).getIsViewContent()))
                .andExpect(jsonPath("$.content[0].isUpload").value(pageResponseDto.getContent().get(0).getIsUpload()))
                .andDo(print())
                .andDo(document("band-get-page-by-bandName",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("bandName").description("그룹 명")
                                        .attributes(key(REQUIRED).value(REQUIRED_YES)),
                                parameterWithName("page").description("페이지 번호")
                                        .attributes(key(REQUIRED).value(REQUIRED_NO)),
                                parameterWithName("size").description("페이지 사이즈")
                                        .attributes(key(REQUIRED).value(REQUIRED_NO))
                        ),
                        responseFields(
                                fieldWithPath("pageNumber").description("페이지 번호"),
                                fieldWithPath("totalPages").description("총 페이지"),
                                fieldWithPath("totalElements").description("전체 개수"),
                                fieldWithPath("content[].bandNo").description("그룹 번호"),
                                fieldWithPath("content[].name").description("그룹 이름"),
                                fieldWithPath("content[].fileNo").description("그룹 프로필 사진 파일 번호"),
                                fieldWithPath("content[].info").description("그룹 설명"),
                                fieldWithPath("content[].isEnter").description("그룹 가입 여부"),
                                fieldWithPath("content[].isViewContent").description("그룹 게시글 공개 여부"),
                                fieldWithPath("content[].isUpload").description("그룹 파일 업로드 가능 여부")
                        )
                ));

        verify(bandReadService, times(1)).getSearchBandInfoList(anyString(), any(Pageable.class));
    }

    @Test
    @DisplayName("그룹에 가입된 유저 조회 API 테스트")
    void bandUserList_api_test() throws Exception {
        // given
        GetBandUserAndUserInfoResponseDto getBandUserAndUserInfoResponseDto =
                new GetBandUserAndUserInfoResponseDto(getBandUserInfoResponseDto, getUserSimpleInfoResponseDto);

        PageResponseDto<GetBandUserAndUserInfoResponseDto> pageResponseDto
                = new PageResponseDto<>(0, 1, 1L , List.of(getBandUserAndUserInfoResponseDto));

        // when
        when(bandUserReadService.getBandUserList(anyString(), any())).thenReturn(pageResponseDto);

        // then
        mockMvc.perform(RestDocumentationRequestBuilders.get(bandUrl + "/{bandName}/users", "test-band")
                        .param("page", "0")
                        .param("size", "1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("pageNumber").value(0))
                .andExpect(jsonPath("totalPages").value(1))
                .andExpect(jsonPath("$.content[0].bandUserInfo.bandUserNo").value(pageResponseDto.getContent().get(0).getBandUserInfo().getBandUserNo()))
                .andExpect(jsonPath("$.content[0].bandUserInfo.bandUserRoleNo").value(pageResponseDto.getContent().get(0).getBandUserInfo().getBandUserRoleNo()))
                .andExpect(jsonPath("$.content[0].userInfo.userNo").value(pageResponseDto.getContent().get(0).getUserInfo().getUserNo()))
                .andExpect(jsonPath("$.content[0].userInfo.fileNo").value(pageResponseDto.getContent().get(0).getUserInfo().getFileNo()))
                .andExpect(jsonPath("$.content[0].userInfo.name").value(pageResponseDto.getContent().get(0).getUserInfo().getName()))
                .andDo(print())
                .andDo(document("band-get-user-list",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),

                        pathParameters(
                                parameterWithName("bandName").description("그룹 이름")
                        ),

                        requestParameters(
                                parameterWithName("page").description("페이지 번호")
                                        .attributes(key(REQUIRED).value(REQUIRED_NO)),
                                parameterWithName("size").description("페이지 사이즈")
                                        .attributes(key(REQUIRED).value(REQUIRED_NO))
                        ),

                        responseFields(
                                fieldWithPath("pageNumber").description("페이지 번호"),
                                fieldWithPath("totalPages").description("총 페이지"),
                                fieldWithPath("totalElements").description("전체 개수"),
                                fieldWithPath("content[].bandUserInfo.bandUserNo").description("그룹 회원 번호"),
                                fieldWithPath("content[].bandUserInfo.bandUserRoleNo").description("그룹 회원 권한 번호"),
                                fieldWithPath("content[].userInfo.userNo").description("회원 번호"),
                                fieldWithPath("content[].userInfo.fileNo").description("회원 프로필 파일 번호"),
                                fieldWithPath("content[].userInfo.name").description("회원 이름")
                        )

                ));

        verify(bandUserReadService, times(1)).getBandUserList(anyString(), any());
    }

    @Test
    @DisplayName("특정 그룹의 그룹 게시글 분류 리스트 조회 API 테스트")
    void bandPostTypeList_api_test() throws Exception {
        GetBandPostTypeResponseDto getBandPostTypeResponseDto = new GetBandPostTypeResponseDto();
        ReflectionTestUtils.setField(getBandPostTypeResponseDto, "name", "post type name");

        List<GetBandPostTypeResponseDto> list = List.of(getBandPostTypeResponseDto);

        when(bandReadService.getBandPostTypeList(anyString())).thenReturn(list);

        mockMvc.perform(RestDocumentationRequestBuilders.get(bandUrl + "/{bandName}/band-post-types", "test-band")
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(list.get(0).getName()))
                .andDo(print())
                .andDo(document("band-get-post-type-list",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),

                        pathParameters(
                                parameterWithName("bandName").description("그룹 이름")
                        ),

                        responseFields(
                                fieldWithPath("[].name").description("그룹 게시글 분류 이름")
                        )
                ));

        verify(bandReadService, times(1)).getBandPostTypeList(anyString());
    }

    @Test
    @DisplayName("그룹 회원 권한 이름 조회 API 테스트")
    void bandUserRoleInfo_api_test() throws Exception {
        GetBandUserAuthResponseDto getBandUserAuthResponseDto = new GetBandUserAuthResponseDto("ROLE_ADMIN");

        when(bandUserReadService.getBandUserInfo(anyLong(), anyLong())).thenReturn(getBandUserAuthResponseDto);

        mockMvc.perform(RestDocumentationRequestBuilders.get(bandUrl + "/{bandNo}/users/{userNo}/role", 1L, 1L)
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("bandUserRoleName").value(getBandUserAuthResponseDto.getBandUserRoleName()))
                .andDo(print())
                .andDo(document("band-get-role-name-of-user",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),

                        pathParameters(
                                parameterWithName("bandNo").description("그룹 번호"),
                                parameterWithName("userNo").description("회원 번호")
                        ),

                        responseFields(
                                fieldWithPath("bandUserRoleName").description("그룹 회원 권한")
                        )
                ));

        verify(bandUserReadService, times(1)).getBandUserInfo(anyLong(), anyLong());
    }

}
