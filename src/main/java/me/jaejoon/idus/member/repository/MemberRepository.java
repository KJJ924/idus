package me.jaejoon.idus.member.repository;

import java.util.Optional;
import me.jaejoon.idus.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author dkansk924@naver.com
 * @since 2021/07/03
 */

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    Optional<Member> findByEmail(String email);
}
