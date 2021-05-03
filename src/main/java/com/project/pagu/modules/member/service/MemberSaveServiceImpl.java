package com.project.pagu.modules.member.service;

import com.project.pagu.common.manager.FileManager;
import com.project.pagu.common.manager.FileUtil;
import com.project.pagu.modules.member.domain.Member;
import com.project.pagu.modules.member.domain.MemberId;
import com.project.pagu.modules.member.domain.MemberType;

import com.project.pagu.modules.member.domain.UserMember;
import com.project.pagu.modules.member.exception.MemberNotFoundException;
import com.project.pagu.modules.member.model.OauthMemberSaveDto;
import com.project.pagu.modules.member.model.ProfileRequestDto;
import com.project.pagu.modules.member.model.MemberSaveRequestDto;
import com.project.pagu.modules.member.repository.MemberRepository;
import com.project.pagu.common.manager.OauthFactory;
import com.project.pagu.modules.member.domain.OauthMember;
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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
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
public class MemberSaveServiceImpl implements MemberSaveService {

    private final MemberRepository memberRepository;
    private final FileManager fileManager;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public MemberId saveMember(MemberSaveRequestDto memberSaveRequestDto) {
        Member saveMember = memberRepository.save(memberSaveRequestDto.toEntity());
        return MemberId.of(saveMember.getEmail(), saveMember.getMemberType());
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

    @Override
    @Transactional
    public void changePassword(MemberId memberId, String newPassword) {
        memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new)
                .changePassword(passwordEncoder.encode(newPassword));
    }

    @Override
    @Transactional
    public void deleteMember(MemberId memberId) {
        memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new)
                .delete();
    }

    private void updateImageFile(ProfileRequestDto profileRequestDto) {
        if (profileRequestDto.getMultipartFile() == null) {
            return;
        }

        if (profileRequestDto.getMultipartFile().getSize() != 0) {
            String fileName = FileUtil.createFileName();
            profileRequestDto.setImageFilename(fileName);
            fileManager.uploadProfileImage(profileRequestDto.getMultipartFile(), fileName, profileRequestDto.getMemberType(), profileRequestDto.getEmail());
        }
    }

}
