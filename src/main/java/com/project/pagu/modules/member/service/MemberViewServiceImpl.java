package com.project.pagu.modules.member.service;

import com.project.pagu.common.manager.OauthFactory;
import com.project.pagu.modules.member.domain.Member;
import com.project.pagu.modules.member.domain.MemberId;
import com.project.pagu.modules.member.domain.MemberType;
import com.project.pagu.modules.member.domain.OauthMember;
import com.project.pagu.modules.member.domain.UserMember;
import com.project.pagu.modules.member.exception.MemberNotFoundException;
import com.project.pagu.modules.member.model.ProfileDto;
import com.project.pagu.modules.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by IntelliJ IDEA
 * User: yhh1056@naver.com
 * Date: 2021/05/03 Time: 2:32 오후
 */

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MemberViewServiceImpl implements MemberViewService {

    private final MemberRepository memberRepository;

    @Override
    public Member findById(MemberId memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);
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
    public ProfileDto convertToProfileViewDtoBy(String nickname) {
        Member findMember = memberRepository.findByNickname(nickname)
                .orElseThrow(MemberNotFoundException::new);

        return ProfileDto.builder()
                .email(findMember.getEmail())
                .memberType(findMember.getMemberType().getKey())
                .nickname(findMember.getNickname())
                .changeNickname(findMember.getNickname())
                .imageFilename(findMember.getImageFilename())
                .oauthImageUrl(findMember.getOauthImageUrl())
                .profileImageUrl(findMember.getProfileImageUrl())
                .link(findMember.getLink())
                .info(findMember.getInfo())
                .career(findMember.getCareer())
                .position(findMember.getPostion())
                .build();
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
        return new UserMember(memberRepository.findById(MemberId.of(email, MemberType.NORMAL))
                .orElseThrow(MemberNotFoundException::new));
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(userRequest);
        // 현재 로그인 진행 중인 서비스
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        Member loginMember = OauthFactory.of(registrationId, oAuth2User.getAttributes());
        return new OauthMember(getOrSyncImage(loginMember));
    }

    private Member getOrSyncImage(Member member) {
        return memberRepository.findById(member.getMemberId())
                .map(entity -> entity.updateImage(member.getOauthImageUrl()))
                .orElse(member);
    }
}
