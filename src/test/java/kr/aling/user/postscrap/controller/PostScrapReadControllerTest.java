package kr.aling.user.postscrap.controller;

import static kr.aling.user.util.RestDocsUtil.REQUIRED;
import static kr.aling.user.util.RestDocsUtil.REQUIRED_NO;
import static kr.aling.user.util.RestDocsUtil.REQUIRED_YES;
import static kr.aling.user.util.RestDocsUtil.VALID;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import kr.aling.user.common.dto.PageResponseDto;
import kr.aling.user.postscrap.dto.response.IsExistsPostScrapResponseDto;
import kr.aling.user.postscrap.dto.response.NumberOfPostScrapResponseDto;
import kr.aling.user.postscrap.dto.response.ReadPostScrapsResponseDto;
import kr.aling.user.postscrap.service.PostScrapReadService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@AutoConfigureRestDocs
@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(PostScrapReadController.class)
class PostScrapReadControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostScrapReadService postScrapReadService;

    @Test
    @DisplayName("게시물 스크랩 여부 확인 성공")
    void isExistsPostScrap() throws Exception {
        // given
        Long postNo = 1L;
        Long userNo = 1L;

        when(postScrapReadService.isExistsPostScrap(anyLong(), anyLong())).thenReturn(
                new IsExistsPostScrapResponseDto(Boolean.FALSE));

        // when
        ResultActions perform = mockMvc.perform(get("/api/v1/post-scraps/{postNo}", postNo)
                .param("userNo", String.valueOf(userNo)));

        // then
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isScrap", equalTo(Boolean.FALSE)));

        // docs
        perform.andDo(document("post-scrap-is-exists-post-scrap",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                        parameterWithName("postNo").description("스크랩 여부 조회할 게시물의 번호")
                                .attributes(key(REQUIRED).value(REQUIRED_YES))
                                .attributes(key(VALID).value(""))
                ),
                requestParameters(
                        parameterWithName("userNo").description("스크랩 확인할 회원의 번호")
                                .attributes(key(REQUIRED).value(REQUIRED_YES))
                                .attributes(key(VALID).value(""))
                ),
                responseFields(
                        fieldWithPath("isScrap").type(JsonFieldType.BOOLEAN).description("스크랩 여부")
                )));
    }

    @Test
    @DisplayName("게시물 스크랩 횟수 조회 성공")
    void getNumberOfPostScrap() throws Exception {
        // given
        Long postNo = 1L;

        when(postScrapReadService.getNumberOfPostScrap(anyLong())).thenReturn(new NumberOfPostScrapResponseDto(100L));

        // when
        ResultActions perform = mockMvc.perform(get("/api/v1/post-scraps/{postNo}/number", postNo));

        // then
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.number", equalTo(100)));

        // docs
        perform.andDo(document("post-scrap-get-number-of-post-scrap",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                        parameterWithName("postNo").description("스크랩 횟수를 조회할 게시물의 번호")
                                .attributes(key(REQUIRED).value(REQUIRED_YES))
                                .attributes(key(VALID).value(""))
                ),
                responseFields(
                        fieldWithPath("number").type(JsonFieldType.NUMBER).description("스크랩 횟수")
                )));
    }

    @Test
    @DisplayName("게시물 스크랩용 게시물 페이징 조회 성공")
    void getPostScraps() throws Exception {
        // given
        int page = 0;
        int size = 3;

        Long postNo = 1L;
        String content = "1";
        Boolean isDelete = false;
        Boolean isOpen = true;

        ReadPostScrapsResponseDto responseDto = new ReadPostScrapsResponseDto();
        ReflectionTestUtils.setField(responseDto, "postNo", postNo);
        ReflectionTestUtils.setField(responseDto, "content", content);
        ReflectionTestUtils.setField(responseDto, "isDelete", isDelete);
        ReflectionTestUtils.setField(responseDto, "isOpen", isOpen);

        PageResponseDto<ReadPostScrapsResponseDto> pageResponseDto = new PageResponseDto<>(
                0,
                1,
                1L,
                List.of(responseDto)
        );
        when(postScrapReadService.getPostScraps(anyLong(), any())).thenReturn(pageResponseDto);

        // when
        ResultActions perform = mockMvc.perform(get("/api/v1/post-scraps")
                .param("userNo", String.valueOf(1L))
                .param("page", String.valueOf(page))
                .param("size", String.valueOf(size)));

        // then
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pageNumber", equalTo(0)))
                .andExpect(jsonPath("$.totalPages", equalTo(1)))
                .andExpect(jsonPath("$.totalElements", equalTo(1)))
                .andExpect(jsonPath("$.content[0].postNo", equalTo(postNo.intValue())))
                .andExpect(jsonPath("$.content[0].content", equalTo(content)))
                .andExpect(jsonPath("$.content[0].isDelete", equalTo(isDelete)))
                .andExpect(jsonPath("$.content[0].isOpen", equalTo(isOpen)));

        verify(postScrapReadService, times(1)).getPostScraps(anyLong(), any());

        // docs
        perform.andDo(document("post-scrap-get-post-scraps",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestParameters(
                        parameterWithName("userNo").description("스크랩 확인할 회원의 번호")
                                .attributes(key(REQUIRED).value(REQUIRED_YES))
                                .attributes(key(VALID).value("")),
                        parameterWithName("size").description("페이지네이션 사이즈")
                                .attributes(key(REQUIRED).value(REQUIRED_NO))
                                .attributes(key(VALID).value("")),
                        parameterWithName("page").description("페이지네이션 페이지 번호")
                                .attributes(key(REQUIRED).value(REQUIRED_NO))
                                .attributes(key(VALID).value(""))
                ),
                responseFields(
                        fieldWithPath("pageNumber").type(JsonFieldType.NUMBER).description("현재 페이지"),
                        fieldWithPath("totalPages").type(JsonFieldType.NUMBER).description("총 페이지"),
                        fieldWithPath("totalElements").type(JsonFieldType.NUMBER).description("총 게시물 수"),
                        fieldWithPath("content[].postNo").type(JsonFieldType.NUMBER).description("게시물 번호"),
                        fieldWithPath("content[].content").type(JsonFieldType.STRING).description("게시물 내용"),
                        fieldWithPath("content[].isDelete").type(JsonFieldType.BOOLEAN).description("게시물 삭제여부"),
                        fieldWithPath("content[].isOpen").type(JsonFieldType.BOOLEAN).description("게시물 공개여부")
                )));
    }
}