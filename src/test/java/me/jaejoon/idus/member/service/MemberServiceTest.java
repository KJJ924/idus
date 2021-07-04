package me.jaejoon.idus.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Optional;
import me.jaejoon.idus.error.message.ErrorCode;
import me.jaejoon.idus.member.domain.Gender;
import me.jaejoon.idus.member.domain.Member;
import me.jaejoon.idus.member.dto.request.RequestSaveMember;
import me.jaejoon.idus.member.exception.DuplicateEmailException;
import me.jaejoon.idus.member.exception.DuplicateNickname;
import me.jaejoon.idus.member.repository.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author dkansk924@naver.com
 * @since 2021/07/04
 */

@SpringBootTest
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;


    @AfterEach
    void clean() {
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("회원저장(성공)")
    void memberSave() {
        //given
        RequestSaveMember requestSaveMember =
            new RequestSaveMember(
                "김재준",
                "nickname",
                "@Abcdef0123",
                "0100000000",
                "test@email.com",
                Gender.NONE);
        //when
        memberService.save(requestSaveMember);

        //then
        Optional<Member> member = memberRepository.findByEmail(requestSaveMember.getEmail());
        assertThat(member).isNotEmpty();
        assertThat(member.get().getName()).isEqualTo(requestSaveMember.getName());

    }

    @Test
    @DisplayName("회원저장되었을때 password 는 암호화(bcrypt) 되어있어야한다.")
    void memberSave1() {
        //given
        RequestSaveMember requestSaveMember =
            new RequestSaveMember(
                "김재준",
                "nickname",
                "@Abcdef0123",
                "0100000000",
                "test@email.com",
                Gender.NONE);
        //when
        memberService.save(requestSaveMember);

        //then
        Optional<Member> member = memberRepository.findByEmail(requestSaveMember.getEmail());
        assertThat(member).isNotEmpty();
        assertThat(member.get().getPassword()).contains("bcrypt");
    }

    @Test
    @DisplayName("회원저장(실패) - 중복된 이메일")
    void memberSave_fail() {
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
                "test@test.com",
                Gender.NONE);

        //when
        assertThatThrownBy(() -> memberService.save(requestSaveMember))

            //then
            .isInstanceOf(DuplicateEmailException.class)
            .hasMessage(ErrorCode.DUPLICATION_EMAIL.getMessage());
    }

    @Test
    @DisplayName("회원저장(실패) - 중복된 별명")
    void memberSave_fail2() {
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
                "nickname",
                "@Abcdef0123",
                "0100000000",
                "test@email.com",
                Gender.NONE);

        //when
        assertThatThrownBy(() -> memberService.save(requestSaveMember))

            //then
            .isInstanceOf(DuplicateNickname.class)
            .hasMessage(ErrorCode.DUPLICATION_NICKNAME.getMessage());
    }

}