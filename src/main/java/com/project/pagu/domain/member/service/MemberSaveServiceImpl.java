package com.project.pagu.domain.member.service;

import com.project.pagu.global.util.FileManager;
import com.project.pagu.global.util.FileUtil;
import com.project.pagu.domain.member.domain.Member;
import com.project.pagu.domain.model.MemberId;

import com.project.pagu.global.error.exception.MemberNotFoundException;
import com.project.pagu.domain.member.dto.OauthSaveDto;
import com.project.pagu.domain.member.dto.ProfileDto;
import com.project.pagu.domain.member.dto.SignUpDto;
import com.project.pagu.domain.member.repository.MemberRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final MemberViewService memberViewService;
    private final FileManager fileManager;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void saveMember(SignUpDto signUpDto) {
        memberRepository.save(signUpDto.toEntity());
    }

    @Override
    @Transactional
    public void saveMember(OauthSaveDto OAuthSaveDto) {
        memberRepository.save(OAuthSaveDto.toEntity());
    }

    @Override
    @Transactional
    public void update(MemberId memberId, ProfileDto profileDto) {
        updateImageFile(profileDto);
        Member findMember = memberViewService.findById(memberId);
        findMember.updateProfile(profileDto);

        updateAuthentication(findMember);
    }

    private void updateAuthentication(Member member) {
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

    private void updateImageFile(ProfileDto profileDto) {
        if (profileDto.getMultipartFile() == null) {
            return;
        }

        if (profileDto.getMultipartFile().getSize() != 0) {
            String fileName = FileUtil.createFileName();
            profileDto.setImageFilename(fileName);
            fileManager.uploadProfileImage(profileDto.getMultipartFile(), fileName, profileDto
                    .getMemberType(), profileDto.getEmail());
        }
    }

}
