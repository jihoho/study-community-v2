package com.project.pagu.member.service;

import com.project.pagu.member.domain.Member;
import com.project.pagu.member.domain.MemberId;
import com.project.pagu.member.domain.MemberType;
import com.project.pagu.member.domain.Role;
import com.project.pagu.member.domain.UserMember;
import com.project.pagu.member.model.MemberSaveRequestDto;
import com.project.pagu.member.repository.MemberRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by IntelliJ IDEA
 * User: hojun
 * Date: 2021-04-02 Time: 오후 10:14
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MemberServiceImpl implements MemberService, UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public Optional<Member> findById(MemberId memberId) {
        return memberRepository.findById(memberId);
    }

    @Override
    public boolean existsByMemberId(MemberId memberId) {
        return memberRepository.existsById(memberId);
    }

    @Override
    public boolean existsByNickname(String nickname) {
        return memberRepository.existsByNickname(nickname);
    }

    @Override
    public boolean existsByEmail(String email) {
        return memberRepository.existsByEmail(email);
    }

    @Override
    @Transactional
    public MemberId saveMember(MemberSaveRequestDto memberSaveRequestDto) {
        Member member = memberRepository.save(memberSaveRequestDto.toEntity());
        autoLogin(member);
        return new MemberId(member.getEmail(), member.getMemberType());
    }

    @Override
    public void autoLogin(Member member) {
        UserDetails userDetails=loadUserByUsername(member.getEmail());
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                userDetails,
                member.getPassword(),
                userDetails.getAuthorities());
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(token);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("## loadUserByUsername ##");
        Optional<Member> memberEntityWrapper =findById(new MemberId(email,
                MemberType.NORMAL));
        if( memberEntityWrapper.isEmpty() ) {
            log.debug("## 계정정보가 존재하지 않습니다. ##");
            throw new UsernameNotFoundException(email);
        }
        Member member=memberEntityWrapper.get();
        List<GrantedAuthority> authorities=new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(Role.GUEST.getKey()));
        return new UserMember(member);
    }
}
