package kr.aling.user.companyuser.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import kr.aling.user.companyuser.dto.request.CreateCompanyUserRequestDto;
import kr.aling.user.companyuser.dto.response.CreateCompanyUserResponseDto;
import kr.aling.user.companyuser.dummy.CompanyUserDummy;
import kr.aling.user.companyuser.entity.CompanyUser;
import kr.aling.user.companyuser.service.CompanyUserManageService;
import kr.aling.user.user.dummy.UserDummy;
import kr.aling.user.user.entity.AlingUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

/**
 * 법인회원 컨트롤러 테스트.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@WebMvcTest(CompanyUserManageController.class)
@AutoConfigureRestDocs(outputDir = "target/snippets")
@MockBean(JpaMetamodelMappingContext.class)
class CompanyAlingUserManageControllerTest {

    @Autowired
    MockMvc mockMvc;
    @MockBean
    CompanyUserManageService companyUserManageService;
    PasswordEncoder passwordEncoder;
    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    @Test
    @DisplayName("법인 회원가입 성공")
    void registerCompanyUser() throws Exception {
        passwordEncoder = new BCryptPasswordEncoder();
        AlingUser alingUser = UserDummy.dummyEncoder(passwordEncoder);
        ReflectionTestUtils.setField(alingUser, "userNo", 10_000_000L);
        CompanyUser companyUser = CompanyUserDummy.dummy(alingUser);

        CreateCompanyUserRequestDto requestDto = new CreateCompanyUserRequestDto();
        ReflectionTestUtils.setField(requestDto, "email", alingUser.getEmail());
        ReflectionTestUtils.setField(requestDto, "password", "nhn123456");
        ReflectionTestUtils.setField(requestDto, "name", alingUser.getName());
        ReflectionTestUtils.setField(requestDto, "address", alingUser.getAddress());
        ReflectionTestUtils.setField(requestDto, "companyRegistrationNo", companyUser.getRegistrationNo());
        ReflectionTestUtils.setField(requestDto, "companySize", companyUser.getCompanySize());
        ReflectionTestUtils.setField(requestDto, "companySector", companyUser.getSector());

        when(companyUserManageService.registerCompanyUser(any()))
                .thenReturn(new CreateCompanyUserResponseDto(alingUser.getName()));

        String url = "/api/v1/companies";

        mockMvc.perform(post(url)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(alingUser.getName()))
                .andDo(print())
                .andDo(document("company-user-registration",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                PayloadDocumentation.subsectionWithPath("email").description("가입 이메일")
                                        .attributes(key("valid").value("Email 형식")),
                                PayloadDocumentation.subsectionWithPath("password").description("비밀번호")
                                        .attributes(key("valid").value("Not Blank, 최대 20글자")),
                                PayloadDocumentation.subsectionWithPath("name").description("법인 이름")
                                        .attributes(key("valid").value("Not Blank, 최대 50글자")),
                                PayloadDocumentation.subsectionWithPath("address").description("소재지")
                                        .attributes(key("valid").value("Not Blank, 최대 200글자")),
                                PayloadDocumentation.subsectionWithPath("companyRegistrationNo")
                                        .description("사업자등록번호").attributes(key("valid").value("Not Blank, 10글자")),
                                PayloadDocumentation.subsectionWithPath("companySize").description("법인 규모")
                                        .attributes(key("valid").value("기업 규모 값")),
                                PayloadDocumentation.subsectionWithPath("companySector").description("법인 업종")
                                        .attributes(key("valid").value("Not Blank, 최대 50글자"))),
                        responseFields(fieldWithPath("name").description("법인 이름"))
                ));

        verify(companyUserManageService, times(1)).registerCompanyUser(any());
    }
}