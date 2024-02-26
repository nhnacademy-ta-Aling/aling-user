package kr.aling.user.normaluser.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.format.DateTimeFormatter;
import kr.aling.user.normaluser.dto.request.CreateNormalUserRequestDto;
import kr.aling.user.normaluser.dummy.NormalUserDummy;
import kr.aling.user.normaluser.entity.NormalUser;
import kr.aling.user.normaluser.service.NormalUserManageService;
import kr.aling.user.user.dummy.UserDummy;
import kr.aling.user.user.entity.AlingUser;
import kr.aling.user.user.exception.UserEmailAlreadyUsedException;
import kr.aling.user.wantjobtype.dummy.WantJobTypeDummy;
import kr.aling.user.wantjobtype.entity.WantJobType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@AutoConfigureRestDocs(outputDir = "target/snippets")
@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(NormalUserManageController.class)
class NormalUserManageControllerTest {

    private final String TMP_PASSWORD = "########";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NormalUserManageService normalUserManageService;

    @Autowired
    private ObjectMapper objectMapper;

    private AlingUser alingUser;
    private WantJobType wantJobType;
    private NormalUser normalUser;

    @BeforeEach
    void setUp() {
        alingUser = UserDummy.dummyEncoder(new BCryptPasswordEncoder());
        wantJobType = WantJobTypeDummy.dummy();
        normalUser = NormalUserDummy.dummy(alingUser, wantJobType);
    }

    @Test
    @DisplayName("일반회원 가입 성공")
    void signUpNormalUser() throws Exception {
        // given
        CreateNormalUserRequestDto requestDto = new CreateNormalUserRequestDto(
                alingUser.getEmail(), TMP_PASSWORD, alingUser.getName(), normalUser.getWantJobType().getWantJobTypeNo(),
                normalUser.getPhoneNo(), normalUser.getBirth().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
        );

        // when
        ResultActions perform = mockMvc.perform(post("/api/v1/normals")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)));

        // then
        perform.andDo(print())
                .andExpect(status().isCreated());

        // docs
        perform.andDo(document("normal-user-signup",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                        fieldWithPath("id").type(JsonFieldType.STRING).description("아이디")
                                .attributes(key("valid").value("Not Blank, 최소 3자, 최대 100자, 이메일형식")),
                        fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호")
                                .attributes(key("valid").value("Not Blank, 최소 8자, 최대 20자")),
                        fieldWithPath("name").type(JsonFieldType.STRING).description("이름")
                                .attributes(key("valid").value("Not Blank, 최소 1자, 최대 50자")),
                        fieldWithPath("wantJobTypeNo").type(JsonFieldType.NUMBER).description("구직희망타입")
                                .attributes(key("valid").value("Not Null, 양수")),
                        fieldWithPath("phoneNo").type(JsonFieldType.STRING).description("연락처")
                                .attributes(key("valid").value("Not Blank, 최소 9자, 최대 11자, 전화번호 \\'-\\' 부호 없이")),
                        fieldWithPath("birth").type(JsonFieldType.STRING).description("생년월일")
                                .attributes(key("valid").value("Not Blank, 최소 8자, 최대 8자, 생년월일"))
                )));
    }

    @Test
    @DisplayName("일반회원 가입 실패 - 입력 데이터가 @Valid 검증 조건에 맞지 않은 경우")
    void signUpNormalUser_invalidInput() throws Exception {
        // given
        CreateNormalUserRequestDto requestDto = new CreateNormalUserRequestDto("", "", "", null, "", "");

        // when
        ResultActions perform = mockMvc.perform(post("/api/v1/normals")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)));

        // then
        perform.andDo(print()).andExpect(status().isBadRequest());
        verify(normalUserManageService, never()).registerNormalUser(requestDto);
    }

    @Test
    @DisplayName("일반회원 가입 실패 - 이미 존재하는 ID(Email)인 경우")
    void signUpNormalUser_alreadyExistsEmail() throws Exception {
        // given
        CreateNormalUserRequestDto requestDto = new CreateNormalUserRequestDto(
                alingUser.getEmail(), TMP_PASSWORD, alingUser.getName(), normalUser.getWantJobType().getWantJobTypeNo(),
                normalUser.getPhoneNo(), normalUser.getBirth().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
        );
        doThrow(UserEmailAlreadyUsedException.class).when(normalUserManageService).registerNormalUser(any());

        // when
        ResultActions perform = mockMvc.perform(post("/api/v1/normals")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)));

        // then
        perform.andDo(print()).andExpect(status().isConflict());
        verify(normalUserManageService, never()).registerNormalUser(requestDto);
    }
}