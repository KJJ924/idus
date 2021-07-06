package me.jaejoon.idus.member.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.jaejoon.idus.authtesthelper.WithAuthUser;
import me.jaejoon.idus.error.message.ErrorCode;
import me.jaejoon.idus.member.domain.Gender;
import me.jaejoon.idus.member.domain.Member;
import me.jaejoon.idus.member.repository.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

/**
 * @author dkansk924@naver.com
 * @since 2021/07/05
 */

@SpringBootTest
@AutoConfigureMockMvc
class MemberAdminApiTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MemberRepository memberRepository;

    @AfterEach
    void clean() {
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("회원정보 상세조회(관리자) - 성공")
    @WithAuthUser(email = "admin@email.com", role = "ROLE_ADMIN")
    void getMemberDetail() throws Exception {
        //given
        Member member = Member.builder()
            .password("password")
            .email("test@test.com")
            .nickname("nickname")
            .name("Name")
            .tel("01012345678")
            .gender(Gender.NONE)
            .build();
        memberRepository.save(member);
        //when
        ResultActions action = mockMvc.perform(get("/admin/members/test@test.com"));

        //then
        action.andExpect(status().isOk());
        action.andExpect(jsonPath("$..[ 'name' ]").value(member.getName()));
        action.andExpect(jsonPath("$..[ 'nickname' ]").value(member.getNickname()));
        action.andExpect(jsonPath("$..[ 'tel' ]").value(member.getTel()));
        action.andExpect(jsonPath("$..[ 'gender' ]").value(member.getGender().getName()));
        action.andExpect(jsonPath("$..[ 'email' ]").value(member.getEmail()));
    }

    @Test
    @DisplayName("회원정보 상세조회(관리자) - 실패(대상 없음)")
    @WithAuthUser(email = "admin@email.com", role = "ROLE_ADMIN")
    void getMemberDetail_fail() throws Exception {

        //when
        ResultActions action = mockMvc.perform(get("/admin/members/test@test.com"));

        //then
        action.andExpect(status().isNotFound());
        action
            .andExpect(jsonPath("$..[ 'message' ]").value(ErrorCode.NOT_FOUND_MEMBER.getMessage()));
        action.andExpect(jsonPath("$..[ 'status' ]").value(404));
    }

    @Test
    @DisplayName("회원정보 상세조회(관리자) - 실패(권한이 없음)")
    @WithAuthUser(email = "admin@email.com")
    void getMemberDetail_fail2() throws Exception {
        //given
        Member member = Member.builder()
            .password("password")
            .email("test@test.com")
            .nickname("nickname")
            .name("Name")
            .tel("01012345678")
            .gender(Gender.NONE)
            .build();
        memberRepository.save(member);

        //when
        ResultActions action = mockMvc.perform(get("/admin/members/test@test.com"));

        //then
        action.andExpect(status().isForbidden());
        action.andExpect(jsonPath("$..[ 'message' ]").value(ErrorCode.FORBIDDEN.getMessage()));
        action.andExpect(jsonPath("$..[ 'status' ]").value(403));
    }

}