package me.jaejoon.idus.member.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author dkansk924@naver.com
 * @since 2021/07/03
 */

@RestController
@RequestMapping("/members")
public class MemberApi {

    @PostMapping
    public ResponseEntity<Object> save() {
        //todo
        return null;
    }
}
