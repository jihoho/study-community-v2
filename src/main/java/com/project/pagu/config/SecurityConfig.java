package com.project.pagu.config;

import com.project.pagu.handler.CustomLoginSuccessHandler;
import com.project.pagu.member.domain.Role;
import com.project.pagu.member.service.MemberServiceImpl;
import com.project.pagu.oauth.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Created by IntelliJ IDEA User: yhh1056@naver.com Date: 2021/03/31 Time: 5:45 오후
 */

@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOAuth2UserService;

    private final MemberServiceImpl memberServiceImpl;

    private final PasswordEncoder passwordEncoder;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /** 임시적으로 모든 요청 허용*/

        http.authorizeRequests()
                .antMatchers("/sign-up/**", "/login/**", "/email-check", "/sign-up-success")
                .permitAll()
                .antMatchers("/profile")
                .hasAnyAuthority(Role.GUEST.getKey(), Role.USER.getKey())
                .anyRequest().permitAll()
                .and()

                .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .and()

                .formLogin()
                .loginPage("/login").permitAll()
                .loginProcessingUrl("/login-process")
                .usernameParameter("email")
                .passwordParameter("password")
                .defaultSuccessUrl("/")
                .successHandler(successHandler())
                .and()

                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .and()

                .oauth2Login().loginPage("/oauth-login")
                .userInfoEndpoint()
                .userService(customOAuth2UserService);
    }

    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return new CustomLoginSuccessHandler("/");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(memberServiceImpl)
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


}



