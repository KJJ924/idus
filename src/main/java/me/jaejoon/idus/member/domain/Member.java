package me.jaejoon.idus.member.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.jaejoon.idus.member.domain.code.Gender;
import me.jaejoon.idus.member.domain.code.Role;

/**
 * @author dkansk924@naver.com
 * @since 2021/07/03
 */

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "seq")
@Getter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_ID")
    private Long seq;

    @Column(name = "NAME", length = 20, nullable = false)
    private String name;

    @Column(name = "NICKNAME", unique = true, length = 30, nullable = false)
    private String nickname;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Column(name = "TEL", length = 20, nullable = false)
    private String tel;

    @Column(name = "EMAIL", length = 100, nullable = false, unique = true)
    private String email;

    @Column(name = "GENDER", nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "ROLE")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    private Member(String name, String nickname, String password, String tel, String email,
        Gender gender, Role role) {
        this.name = name;
        this.nickname = nickname;
        this.password = password;
        this.tel = tel;
        this.email = email;
        this.gender = gender;
        this.role = role;
    }
}
