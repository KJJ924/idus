package me.jaejoon.idus.member.api;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.jaejoon.idus.authtesthelper.WithAuthUser;
import me.jaejoon.idus.error.message.ErrorCode;
import me.jaejoon.idus.member.domain.Gender;
import me.jaejoon.idus.member.domain.Member;
import me.jaejoon.idus.member.domain.Role;
import me.jaejoon.idus.member.dto.request.RequestMemberLogin;
import me.jaejoon.idus.member.dto.request.RequestSaveMember;
import me.jaejoon.idus.member.repository.MemberRepository;
import me.jaejoon.idus.member.service.MemberService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

/**
 * @author dkansk924@naver.com
 * @since 2021/07/04
 */

@SpringBootTest
@AutoConfigureMockMvc
class MemberAccountApiTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MemberService memberService;

    @AfterEach
    void clean() {
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("회원 저장(성공)")
    void memberSaveTest() throws Exception {
        //given
        RequestSaveMember requestSaveMember =
            new RequestSaveMember(
                "김재준",
                "nickname",
                "@Abcdef0123",
                "0100000000",
                "test@email.com",
                Gender.NONE,
                Role.USER);
        String content = objectMapper.writeValueAsString(requestSaveMember);

        //when
        ResultActions actions = mockMvc.perform(post("/members/signup")
            .contentType(MediaType.APPLICATION_JSON)
            .content(content));

        //then
        actions.andExpect(status().isCreated());
        actions.andExpect(jsonPath("$..[ 'name' ]").value(requestSaveMember.getName()));
        actions.andExpect(jsonPath("$..[ 'nickname' ]").value(requestSaveMember.getNickname()));
        actions.andExpect(jsonPath("$..[ 'tel' ]").value(requestSaveMember.getTel()));
        actions
            .andExpect(jsonPath("$..[ 'gender' ]").value(requestSaveMember.getGender().getName()));
        actions.andExpect(jsonPath("$..[ 'email' ]").value(requestSaveMember.getEmail()));
    }


    @Test
    @DisplayName("회원 저장(실패) - 중복된 이메일")
    void memberSaveTest_fail() throws Exception {
        //given
        Member alreadyMember = Member.builder()
            .password("password")
            .email("test@test.com")
            .nickname("nickname")
            .name("Name")
            .tel("01012345678")
            .gender(Gender.NONE)
            .build();
        memberRepository.save(alreadyMember);

        RequestSaveMember requestSaveMember =
            new RequestSaveMember(
                "김재준",
                "kjj",
                "@Abcdef0123",
                "0100000000",
                "test@test.com",  //error point (중복된 이메일)
                Gender.NONE,
                Role.USER);

        String content = objectMapper.writeValueAsString(requestSaveMember);

        //when
        ResultActions actions = mockMvc.perform(post("/members/signup")
            .contentType(MediaType.APPLICATION_JSON)
            .content(content));

        //then
        actions.andExpect(status().isConflict());
        actions.andExpect(
            jsonPath("$..[ 'message' ]").value(ErrorCode.DUPLICATION_EMAIL.getMessage()));
        actions.andExpect(jsonPath("$..[ 'status' ]").value(409));
    }


    @Test
    @DisplayName("회원 저장(실패) - 중복된 별명")
    void memberSaveTest_fail2() throws Exception {
        //given
        Member alreadyMember = Member.builder()
            .password("password")
            .email("test@test.com")
            .nickname("nickname")
            .name("Name")
            .tel("01012345678")
            .gender(Gender.NONE)
            .build();
        memberRepository.save(alreadyMember);

        RequestSaveMember requestSaveMember =
            new RequestSaveMember(
                "김재준",
                "nickname", //error point (중복된 닉네임)
                "@Abcdef0123",
                "0100000000",
                "test@email.com",
                Gender.NONE,
                Role.USER);

        String content = objectMapper.writeValueAsString(requestSaveMember);

        //when
        ResultActions actions = mockMvc.perform(post("/members/signup")
            .contentType(MediaType.APPLICATION_JSON)
            .content(content));

        //then
        actions.andExpect(status().isConflict());
        actions.andExpect(
            jsonPath("$..[ 'message' ]").value(ErrorCode.DUPLICATION_NICKNAME.getMessage()));
        actions.andExpect(jsonPath("$..[ 'status' ]").value(409));
    }


    @Test
    @DisplayName("회원 로그인 성공")
    void member_login() throws Exception {

        //given
        RequestSaveMember requestSaveMember =
            new RequestSaveMember(
                "김재준",
                "nickname",
                "@Abcdef0123",
                "0100000000",
                "test@email.com",
                Gender.NONE,
                Role.USER);
        memberService.save(requestSaveMember);

        RequestMemberLogin requestMemberLogin = RequestMemberLogin.builder()
            .email("test@email.com")
            .password("@Abcdef0123")
            .build();
        String content = objectMapper.writeValueAsString(requestMemberLogin);

        //when
        ResultActions actions = mockMvc.perform(post("/members/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(content));

        //then
        actions.andExpect(status().isOk());
        actions.andExpect(jsonPath("$..[ 'token' ]").exists());
    }


    @Test
    @DisplayName("회원 로그인(실패) - pw 틀린경우")
    void member_login_fail() throws Exception {

        //given
        RequestSaveMember requestSaveMember =
            new RequestSaveMember(
                "김재준",
                "nickname",
                "@Abcdef0123",
                "0100000000",
                "test@email.com",
                Gender.NONE,
                Role.USER);
        memberService.save(requestSaveMember);

        RequestMemberLogin requestMemberLogin = RequestMemberLogin.builder()
            .email("test@email.com")
            .password("test") // errorPoint
            .build();
        String content = objectMapper.writeValueAsString(requestMemberLogin);

        //when
        ResultActions actions = mockMvc.perform(post("/members/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(content));

        //then
        actions.andExpect(status().isBadRequest());
        actions.andExpect(jsonPath("$..[ 'message' ]").value(ErrorCode.LOGIN_FAILED.getMessage()));
        actions.andExpect(jsonPath("$..[ 'status' ]").value(400));
    }

    @Test
    @DisplayName("회원 로그인(실패) - 해당 ID(email) 없는 경우")
    void member_login_fail2() throws Exception {

        //given
        RequestSaveMember requestSaveMember =
            new RequestSaveMember(
                "김재준",
                "nickname",
                "@Abcdef0123",
                "0100000000",
                "test@email.com",
                Gender.NONE,
                Role.USER);
        memberService.save(requestSaveMember);

        RequestMemberLogin requestMemberLogin = RequestMemberLogin.builder()
            .email("kjj@email.com") // errorPoint
            .password("@Abcdef0123")
            .build();
        String content = objectMapper.writeValueAsString(requestMemberLogin);

        //when
        ResultActions actions = mockMvc.perform(post("/members/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(content));

        //then
        actions.andExpect(status().isBadRequest());
        actions.andExpect(jsonPath("$..[ 'message' ]").value(ErrorCode.LOGIN_FAILED.getMessage()));
        actions.andExpect(jsonPath("$..[ 'status' ]").value(400));
    }


    @Test
    @DisplayName("본인 상세 정보보기(실패) - 인증 token 값이 없는경우")
    void memberDetail_fail() throws Exception {
        //given
        RequestSaveMember requestSaveMember =
            new RequestSaveMember(
                "김재준",
                "nickname",
                "@Abcdef0123",
                "0100000000",
                "test@email.com",
                Gender.NONE,
                Role.USER);
        memberService.save(requestSaveMember);

        //when
        ResultActions actions = mockMvc.perform(get("/members/personal-info")
            .header(HttpHeaders.AUTHORIZATION, ""));

        //then
        actions.andExpect(status().isUnauthorized());
        actions.andExpect(jsonPath("$..[ 'message' ]")
            .value(ErrorCode.NOT_EXISTENCE_OR_INVALID_TOKEN.getMessage()));
        actions.andExpect(jsonPath("$..[ 'status' ]").value(401));
    }

    @Test
    @DisplayName("본인 상세 정보보기(성공)")
    @WithAuthUser(email = "test@email.com")
    void memberDetail() throws Exception {
        //given
        RequestSaveMember requestSaveMember =
            new RequestSaveMember(
                "김재준",
                "nickname",
                "@Abcdef0123",
                "0100000000",
                "test@email.com",
                Gender.NONE,
                Role.USER);
        memberService.save(requestSaveMember);

        //when
        ResultActions actions = mockMvc.perform(get("/members/personal-info"));

        //then
        actions.andExpect(status().isOk());
        actions.andExpect(jsonPath("$..[ 'name' ]").value(requestSaveMember.getName()));
        actions.andExpect(jsonPath("$..[ 'nickname' ]").value(requestSaveMember.getNickname()));
        actions.andExpect(jsonPath("$..[ 'tel' ]").value(requestSaveMember.getTel()));
        actions
            .andExpect(jsonPath("$..[ 'gender' ]").value(requestSaveMember.getGender().getName()));
        actions.andExpect(jsonPath("$..[ 'email' ]").value(requestSaveMember.getEmail()));

    }

}