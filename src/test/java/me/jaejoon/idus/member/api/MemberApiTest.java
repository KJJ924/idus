package me.jaejoon.idus.member.api;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.jaejoon.idus.member.domain.Gender;
import me.jaejoon.idus.member.dto.request.RequestSaveMember;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

/**
 * @author dkansk924@naver.com
 * @since 2021/07/04
 */

@SpringBootTest
@AutoConfigureMockMvc
class MemberApiTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("회원 저장 성공")
    void memberSaveTest() throws Exception {
        //given
        RequestSaveMember requestSaveMember =
            new RequestSaveMember(
                "김재준",
                "nickname",
                "@Abcdef0123",
                "0100000000",
                "test@email.com",
                Gender.NONE);
        String content = objectMapper.writeValueAsString(requestSaveMember);

        //when
        ResultActions actions = mockMvc.perform(post("/members")
            .contentType(MediaType.APPLICATION_JSON)
            .content(content));

        //then
        actions.andExpect(status().isCreated());
        actions.andExpect(jsonPath("$..[ 'name' ]").value("김재준"));
        actions.andExpect(jsonPath("$..[ 'nickname' ]").value("nickname"));
        actions.andExpect(jsonPath("$..[ 'tel' ]").value("0100000000"));
        actions.andExpect(jsonPath("$..[ 'gender' ]").value("비공개"));
        actions.andExpect(jsonPath("$..[ 'email' ]").value("test@email.com"));
    }
}