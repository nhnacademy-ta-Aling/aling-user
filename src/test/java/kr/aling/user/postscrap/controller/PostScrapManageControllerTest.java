package kr.aling.user.postscrap.controller;

import static kr.aling.user.util.RestDocsUtil.REQUIRED;
import static kr.aling.user.util.RestDocsUtil.REQUIRED_YES;
import static kr.aling.user.util.RestDocsUtil.VALID;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import kr.aling.user.post.exception.PostNotFoundException;
import kr.aling.user.postscrap.service.PostScrapManageService;
import kr.aling.user.user.exception.UserNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;


@AutoConfigureRestDocs
@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(PostScrapManageController.class)
class PostScrapManageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostScrapManageService postScrapManageService;

    @Test
    @DisplayName("게시물 스크랩 성공")
    void postScrap() throws Exception {
        // given
        Long postNo = 1L;
        Long userNo = 1L;

        // when
        ResultActions perform = mockMvc.perform(post("/api/v1/post-scraps/{postNo}", postNo)
                .param("userNo", String.valueOf(userNo)));

        // then
        perform.andDo(print())
                .andExpect(status().isOk());

        // docs
        perform.andDo(document("post-scrap-post-scrap",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                        parameterWithName("postNo").description("스크랩할 게시물의 번호")
                                .attributes(key(REQUIRED).value(REQUIRED_YES))
                                .attributes(key(VALID).value(""))
                ),
                requestParameters(
                        parameterWithName("userNo").description("스크랩하는 회원의 번호")
                                .attributes(key(REQUIRED).value(REQUIRED_YES))
                                .attributes(key(VALID).value(""))
                )));
    }

    @Test
    @DisplayName("게시물 스크랩 실패 - 존재하지 않는 회원인 경우")
    void postScrap_userNotFound() throws Exception {
        // given
        Long postNo = 1L;
        Long userNo = 1L;

        doThrow(UserNotFoundException.class).when(postScrapManageService).postScrap(anyLong(), anyLong());

        // when
        ResultActions perform = mockMvc.perform(post("/api/v1/post-scraps/{postNo}", postNo)
                .param("userNo", String.valueOf(userNo)));

        // then
        perform.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("게시물 스크랩 실패 - 존재하지 않는 게시물인 경우")
    void postScrap_postNotFound() throws Exception {
        // given
        Long postNo = 1L;
        Long userNo = 1L;

        doThrow(PostNotFoundException.class).when(postScrapManageService).postScrap(anyLong(), anyLong());

        // when
        ResultActions perform = mockMvc.perform(post("/api/v1/post-scraps/{postNo}", postNo)
                .param("userNo", String.valueOf(userNo)));

        // then
        perform.andDo(print())
                .andExpect(status().isNotFound());
    }
}