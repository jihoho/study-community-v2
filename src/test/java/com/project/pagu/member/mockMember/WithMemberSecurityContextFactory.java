package com.project.pagu.member.mockMember;

import com.project.pagu.member.domain.MemberId;
import com.project.pagu.member.model.MemberSaveRequestDto;
import com.project.pagu.member.service.MemberServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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
    private MemberServiceImpl memberService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public SecurityContext createSecurityContext(WithMember withMember) {
        String nickname = withMember.value();

        MemberSaveRequestDto dto = new MemberSaveRequestDto();
        dto.setNickname(nickname);
        dto.setEmail(nickname + "@email.com");
        dto.setPassword(passwordEncoder.encode("123123a!"));
        MemberId memberId = memberService.saveMember(dto);

        UserDetails principal = memberService.loadUserByUsername(dto.getEmail());
        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, principal.getPassword(), principal.getAuthorities());
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        return context;
    }
}
