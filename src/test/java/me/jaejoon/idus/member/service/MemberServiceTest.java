package me.jaejoon.idus.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Optional;
import me.jaejoon.idus.auth.authentication.AuthUser;
import me.jaejoon.idus.auth.service.JwtService;
import me.jaejoon.idus.error.message.ErrorCode;
import me.jaejoon.idus.member.domain.Gender;
import me.jaejoon.idus.member.domain.Member;
import me.jaejoon.idus.member.dto.request.RequestMemberLogin;
import me.jaejoon.idus.member.dto.request.RequestSaveMember;
import me.jaejoon.idus.member.dto.response.ResponseLoginToken;
import me.jaejoon.idus.member.dto.response.ResponseMember;
import me.jaejoon.idus.member.exception.DuplicateEmailException;
import me.jaejoon.idus.member.exception.DuplicateNickname;
import me.jaejoon.idus.member.exception.LoginFailedException;
import me.jaejoon.idus.member.exception.NotFoundMemberException;
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

    @Autowired
    JwtService jwtService;

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


    @Test
    @DisplayName("회원로그인(성공)")
    void memberLogin() {
        //given
        RequestSaveMember requestSaveMember =
            new RequestSaveMember(
                "김재준",
                "nickname",
                "@Abcdef0123",
                "0100000000",
                "test@email.com",
                Gender.NONE);
        memberService.save(requestSaveMember);
        RequestMemberLogin requestMemberLogin = RequestMemberLogin.builder()
            .email("test@email.com")
            .password("@Abcdef0123")
            .build();

        //when
        ResponseLoginToken token = memberService.login(requestMemberLogin);
        AuthUser authUser = jwtService.decode(token.getToken());

        //then
        assertThat(token).isNotNull();
        assertThat(authUser.getEmail()).isEqualTo("test@email.com");
    }

    @Test
    @DisplayName("회원로그인(실패) - pw 다른경우")
    void memberLogin_fail() {
        //given
        RequestSaveMember requestSaveMember =
            new RequestSaveMember(
                "김재준",
                "nickname",
                "@Abcdef0123",
                "0100000000",
                "test@email.com",
                Gender.NONE);
        memberService.save(requestSaveMember);
        RequestMemberLogin requestMemberLogin = RequestMemberLogin.builder()
            .email("test@email.com")
            .password("@bbb1234") //error point
            .build();

        //when
        assertThatThrownBy(() -> memberService.login(requestMemberLogin))

            //then
            .isInstanceOf(LoginFailedException.class)
            .hasMessage(ErrorCode.LOGIN_FAILED.getMessage());
    }

    @Test
    @DisplayName("회원로그인(실패) - ID가 없는경우")
    void memberLogin_fail2() {
        //given
        RequestSaveMember requestSaveMember =
            new RequestSaveMember(
                "김재준",
                "nickname",
                "@Abcdef0123",
                "0100000000",
                "test@email.com",
                Gender.NONE);
        memberService.save(requestSaveMember);
        RequestMemberLogin requestMemberLogin = RequestMemberLogin.builder()
            .email("kjj@email.com") //error point
            .password("@Abcdef0123")
            .build();

        //when
        assertThatThrownBy(() -> memberService.login(requestMemberLogin))

            //then
            .isInstanceOf(LoginFailedException.class)
            .hasMessage(ErrorCode.LOGIN_FAILED.getMessage());
    }

    @Test
    @DisplayName("이메일로 회원 조회(성공)")
    void findByEmailMember() {
        //given
        RequestSaveMember requestSaveMember =
            new RequestSaveMember(
                "김재준",
                "nickname",
                "@Abcdef0123",
                "0100000000",
                "test@email.com",
                Gender.NONE);
        memberService.save(requestSaveMember);

        AuthUser user = new AuthUser("test@email.com");

        //when
        ResponseMember member = memberService.getMemberDetail(user);

        //then
        assertThat(member.getEmail()).isEqualTo(requestSaveMember.getEmail());
        assertThat(member.getName()).isEqualTo(requestSaveMember.getName());
        assertThat(member.getTel()).isEqualTo(requestSaveMember.getTel());
        assertThat(member.getNickname()).isEqualTo(requestSaveMember.getNickname());
    }

    @Test
    @DisplayName("이메일로 회원 조회(실패) - 존재하지 않는 회원")
    void findByEmailMember_fail() {
        //given
        AuthUser user = new AuthUser("test@email.com");
        //when
        assertThatThrownBy(() -> memberService.getMemberDetail(user))
            //then
            .isInstanceOf(NotFoundMemberException.class)
            .hasMessage(ErrorCode.NOT_FOUND_MEMBER.getMessage());
    }
}