package com.project.pagu.member.service;

import com.project.pagu.common.file.FileManager;
import com.project.pagu.member.domain.Member;
import com.project.pagu.member.domain.MemberId;
import com.project.pagu.member.domain.MemberType;

import com.project.pagu.member.domain.UserMember;
import com.project.pagu.member.model.OauthMemberSaveDto;
import com.project.pagu.member.model.ProfileRequestDto;
import com.project.pagu.member.model.MemberSaveRequestDto;
import com.project.pagu.member.repository.MemberRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
    private final FileManager fileManager;

    @Override
    public Member findById(MemberId memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new UsernameNotFoundException(memberId.toString()));
    }

    @Override
    public boolean existsById(MemberId memberId) {
        return memberRepository.existsById(memberId);
    }

    @Override
    public boolean existsByNickname(String nickname) {
        return memberRepository.existsByNickname(nickname);
    }

    @Override
    @Transactional
    public MemberId saveMember(MemberSaveRequestDto memberSaveRequestDto) {
        Member saveMember = memberRepository.save(memberSaveRequestDto.toEntity());
        return MemberId.of(saveMember.getEmail(), saveMember.getMemberType());
    }

    @Override
    @Transactional
    public Member save(Member member) {
        return memberRepository.save(member);
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
        return new UserMember(findById(MemberId.of(email, MemberType.NORMAL)));
    }

    @Override
    public ProfileRequestDto convertMemberToProfileRequestDto(Member member) {
        Member findMember = findById(member.getMemberId());

        return ProfileRequestDto.builder()
                .email(findMember.getEmail())
                .memberType(findMember.getMemberType().getKey())
                .nickname(findMember.getNickname())
                .changeNickname(findMember.getNickname())
                .imageFile(findMember.getImageFile())
                .imageUrl(findMember.getImageUrl())
                .link(findMember.getLink())
                .info(findMember.getInfo())
                .career(findMember.getCareer())
                .position(findMember.getPostion())
                .build();
    }

    @Override
    @Transactional
    public Member saveMember(OauthMemberSaveDto OAuthMemberSaveDto) {
        return memberRepository.save(OAuthMemberSaveDto.toEntity());
    }

    @Override
    @Transactional
    public void update(Member member, ProfileRequestDto profileRequestDto) {
        updateImageFile(profileRequestDto);
        member.updateProfile(profileRequestDto);
        memberRepository.save(member);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<GrantedAuthority> updatedAuthorities = new ArrayList<>(auth.getAuthorities());
        updatedAuthorities.add(new SimpleGrantedAuthority(member.getRoleKey()));
        Authentication newAuth = new UsernamePasswordAuthenticationToken(
                auth.getPrincipal(), auth.getCredentials(), updatedAuthorities);
        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }

    private void updateImageFile(ProfileRequestDto profileRequestDto) {
        if (profileRequestDto.getMultipartFile() == null) {
            return;
        }

        if (profileRequestDto.getMultipartFile().getSize() != 0) {
            String fileName = fileManager.createFileName();
            profileRequestDto.setImageFile(fileName);
            fileManager.uploadProfileImage(profileRequestDto.getMultipartFile(), fileName, profileRequestDto.getMemberType(), profileRequestDto.getEmail());
        }
    }
}
