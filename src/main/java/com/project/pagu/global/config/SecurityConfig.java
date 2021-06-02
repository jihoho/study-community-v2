package com.project.pagu.global.config;

import com.project.pagu.global.error.handler.CustomLoginSuccessHandler;
import com.project.pagu.global.error.handler.OAuth2SuccessHandler;
import com.project.pagu.domain.member.service.MemberViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import com.project.pagu.domain.model.Role;

/**
 * Created by IntelliJ IDEA User: yhh1056@naver.com Date: 2021/03/31 Time: 5:45 오후
 */

@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final MemberViewService memberViewService;

    private final PasswordEncoder passwordEncoder;

    private final OAuth2SuccessHandler oAuth2SuccessHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /** 임시적으로 모든 요청 허용*/

        http.authorizeRequests()
                .antMatchers("/sign-up/**", "/login/**", "/email-check", "/sign-up-success").permitAll()
                .antMatchers("/profile/**").hasAnyAuthority(Role.GUEST.getKey(), Role.USER.getKey())
                .antMatchers("/boards/board-form").hasAuthority(Role.USER.getKey())
                .anyRequest().permitAll();

        http.csrf()
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());

        http.formLogin()
                .loginPage("/login").permitAll()
                .loginProcessingUrl("/login-process")
                .usernameParameter("email")
                .passwordParameter("password")
                .defaultSuccessUrl("/")
                .successHandler(successHandler());

        http.logout()
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true);

        http.oauth2Login()
                .successHandler(oAuth2SuccessHandler)
                .loginPage("/oauth-login")
                .failureUrl("/sign-up")
                .userInfoEndpoint()
                .userService(memberViewService);

        http
                .exceptionHandling()
                .accessDeniedPage("/authority");
    }

    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return new CustomLoginSuccessHandler("/");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(memberViewService)
                .passwordEncoder(passwordEncoder)
                .and().inMemoryAuthentication()
                .withUser("guest")
                .password("{noop}123")
                .roles("GUEST")
                .and()
                .withUser("user")
                .password("{noop}123")
                .roles("USER");
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .mvcMatchers("/node_modules/**")
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

}
