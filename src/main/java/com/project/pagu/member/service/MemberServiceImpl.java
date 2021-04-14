package com.project.pagu.member.service;

import com.project.pagu.member.domain.Member;
import com.project.pagu.member.domain.MemberId;
import com.project.pagu.member.domain.MemberType;

import com.project.pagu.member.domain.UserMember;
import com.project.pagu.member.model.OAuthMemberSaveDto;
import com.project.pagu.member.model.ProfileRequestDto;
import com.project.pagu.member.model.MemberSaveRequestDto;
import com.project.pagu.member.repository.MemberRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
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
        Member saveMember = memberRepository.save(memberSaveRequestDto.toEntity());
        return new MemberId(saveMember.getEmail(), saveMember.getMemberType());
    }

    @Override
    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email)
                //todo : 예외 처리 수정
                .orElseThrow(() -> new IllegalArgumentException());
    }

    @Override
    public void login(Member member) {
        UserDetails userDetails = loadUserByUsername(member.getEmail());
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
        Member member = findById(new MemberId(email,
                MemberType.NORMAL)).orElseThrow(() -> new UsernameNotFoundException(email));
        return new UserMember(member);
    }

    @Override
    public ProfileRequestDto convertMemberToProfileRequestDto(Member member) {
        return ProfileRequestDto.builder()
                .email(member.getEmail())
                .memberType(member.getMemberType().getKey())
                .nickname(member.getNickname())
                .filename(member.getFilename())
                .link(member.getLink())
                .info(member.getInfo())
                .career(member.getCareer())
                .position(member.getPostion())
                .build();
    }

    @Override
    @Transactional
    public Member saveMember(OAuthMemberSaveDto OAuthMemberSaveDto) {
        return memberRepository.save(OAuthMemberSaveDto.toEntity());
    }
}
