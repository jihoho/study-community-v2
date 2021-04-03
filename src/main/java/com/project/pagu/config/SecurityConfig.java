package com.project.pagu.config;

import com.project.pagu.config.auth.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

/**
 * Created by IntelliJ IDEA User: yhh1056@naver.com Date: 2021/03/31 Time: 5:45 오후
 */

@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /** 임시적으로 모든 요청 허용*/
        http.authorizeRequests().anyRequest().permitAll()
                .and()
                .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .and()
                .oauth2Login().loginPage("/oauth-login")
                .userInfoEndpoint()
                .userService(customOAuth2UserService);
    }

    @Bean
    public PasswordEncoder authKeyEncoder(){
        return new BCryptPasswordEncoder();
    }

}



