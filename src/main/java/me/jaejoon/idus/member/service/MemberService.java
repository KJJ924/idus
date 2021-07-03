package me.jaejoon.idus.member.service;

import lombok.RequiredArgsConstructor;
import me.jaejoon.idus.member.repository.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author dkansk924@naver.com
 * @since 2021/07/03
 */

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

}
