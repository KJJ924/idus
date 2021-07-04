package me.jaejoon.idus.member.repository;

import me.jaejoon.idus.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author dkansk924@naver.com
 * @since 2021/07/03
 */

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByEmail(String email);

    boolean existsByNickName(String nickName);
}
