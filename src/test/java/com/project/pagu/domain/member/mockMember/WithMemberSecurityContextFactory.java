package com.project.pagu.domain.member.mockMember;

import com.project.pagu.domain.member.domain.Member;
import com.project.pagu.domain.model.MemberType;
import com.project.pagu.domain.model.Role;
import com.project.pagu.domain.member.repository.MemberRepository;
import com.project.pagu.domain.member.service.MemberViewService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

/**
 * Created by IntelliJ IDEA
 * User: yhh1056@naver.com
 * Date: 2021/04/21 Time: 12:22 오후
 */
public class WithMemberSecurityContextFactory implements WithSecurityContextFactory<WithMember> {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberViewService memberViewService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public SecurityContext createSecurityContext(WithMember withMember) {
        String nickname = withMember.value();

        Member member=givenMember(nickname);
        memberRepository.save(member);
        UserDetails principal = memberViewService.loadUserByUsername(member.getEmail());
        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, principal.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_USER")));
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        return context;
    }

    public Member givenMember(String nickname) {
        return Member.builder()
                .email(nickname+"@email.com")
                .memberType(MemberType.NORMAL)
                .nickname(nickname)
                .role(Role.USER)
                .imageFilename(null)
                .career("취준생")
                .position("백엔드")
                .link("test@gi.com")
                .info("안녕하세요")
                .password(passwordEncoder.encode("123123a!"))
                .build();
    }
}