package me.jaejoon.idus.auth.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.time.Duration;
import java.util.Date;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import me.jaejoon.idus.auth.JwtProperties;
import me.jaejoon.idus.auth.authentication.AuthUser;
import me.jaejoon.idus.member.domain.Member;
import org.springframework.stereotype.Service;

/**
 * @author dkansk924@naver.com
 * @since 2021/07/05
 */

@Service
@RequiredArgsConstructor
public class JwtService {
    //fixme 유저 권한 추가해야함

    private final JwtProperties jwtProperties;
    private static final String EMAIL = "email";
    private static final String ROLE = "role";
    private static final String EXPIRY_TIME = "exp";
    private static final String ISSUED_AT = "iat";

    public AuthUser decode(String token) throws JWTVerificationException {
        JWTVerifier verifier = getVerifier();

        DecodedJWT decode = JWT.decode(token);
        verifier.verify(decode);

        String email = decode.getClaim(EMAIL).asString();
        return new AuthUser(email);
    }

    public String encode(Member member) {
        Date now = new Date();
        return JWT.create().withJWTId(UUID.randomUUID().toString())
            .withClaim(EMAIL, member.getEmail())
            .withClaim(ISSUED_AT, now)
            .withClaim(EXPIRY_TIME, new Date(now.getTime() + Duration.ofMinutes(30).toMillis()))
            .sign(getAlgorithm());
    }

    private JWTVerifier getVerifier() {
        return JWT.require(getAlgorithm()).build();
    }

    private Algorithm getAlgorithm() {
        return Algorithm.HMAC256(jwtProperties.getSecretKey());
    }
}
