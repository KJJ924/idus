package me.jaejoon.idus.config;

import me.jaejoon.idus.order.util.RandomOrderNumber;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author dkansk924@naver.com
 * @since 2021/07/03
 */

@Configuration
public class WebConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public RandomOrderNumber randomOrderNumber() {
        return new RandomOrderNumber();
    }
}
