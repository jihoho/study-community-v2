package com.project.pagu.member.service;

import com.project.pagu.common.FileManager;
import com.project.pagu.member.domain.Member;
import com.project.pagu.member.domain.MemberId;
import com.project.pagu.member.domain.MemberType;

import com.project.pagu.member.domain.UserMember;
import com.project.pagu.member.model.OauthMemberSaveDto;
import com.project.pagu.member.model.ProfileImageDto;
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
    private final FileManager fileManager;

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
    @Transactional
    public Member save(Member member) {
        return memberRepository.save(member);
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
        Member findMember = findMember(member);

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

    public Member findMember(Member member) {
        return memberRepository
                .findById(new MemberId(member.getEmail(), member.getMemberType()))
                /** todo: exception handing */
                .orElseThrow(() -> new IllegalArgumentException());
    }

    @Override
    @Transactional
    public Member saveMember(OauthMemberSaveDto OAuthMemberSaveDto) {
        return memberRepository.save(OAuthMemberSaveDto.toEntity());
    }

    @Override
    @Transactional
    public void update(Member member, ProfileRequestDto profileRequestDto) {
        Member findMember = findMember(member);

        updateImageFile(profileRequestDto);
        findMember.updateProfile(profileRequestDto);
    }

    private void updateImageFile(ProfileRequestDto profileRequestDto) {

        if (profileRequestDto.getMultipartFile().getSize() != 0) {
            String fileName = fileManager.createFileName();
            profileRequestDto.setImageFile(fileName);
            ProfileImageDto profileImageDto = profileRequestDto.toProfileImageDto();
            fileManager.uploadProfileImage(profileImageDto);
        }
    }
}
