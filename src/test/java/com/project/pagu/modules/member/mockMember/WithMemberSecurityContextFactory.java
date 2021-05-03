package com.project.pagu.modules.member.mockMember;

import com.project.pagu.modules.member.model.MemberSaveRequestDto;
import com.project.pagu.modules.member.service.MemberSaveService;
import com.project.pagu.modules.member.service.MemberSaveServiceImpl;
import com.project.pagu.modules.member.service.MemberViewService;
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
    private MemberViewService MemberViewService;

    @Autowired
    private MemberSaveService memberSaveService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public SecurityContext createSecurityContext(WithMember withMember) {
        String nickname = withMember.value();

        MemberSaveRequestDto dto = new MemberSaveRequestDto();
        dto.setNickname(nickname);
        dto.setEmail(nickname + "@email.com");
        dto.setPassword(passwordEncoder.encode("123123a!"));
        memberSaveService.saveMember(dto);

        UserDetails principal = MemberViewService.loadUserByUsername(dto.getEmail());
        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, principal.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_USER")));
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        return context;
    }
}
