package com.project.pagu.member.service;

import com.project.pagu.member.domain.Member;
import com.project.pagu.member.domain.MemberId;
import com.project.pagu.member.model.MemberSaveRequestDto;
import com.project.pagu.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public boolean existsById(MemberId memberId) {
        return memberRepository.existsById(memberId);
    }

    public boolean existsByNickname(String nickname) {
        return memberRepository.existsByNickname(nickname);
    }

    public boolean existsByEmail(String email) {
        return memberRepository.existsByEmail(email);
    }

    @Transactional
    public MemberId save(MemberSaveRequestDto memberSaveRequestDto) {
        Member member = memberRepository.save(memberSaveRequestDto.toEntity());
        return new MemberId(member.getEmail(), member.getMemberType());
    }

    public void encryptPassword(MemberSaveRequestDto memberSaveRequestDto) {
        String password = memberSaveRequestDto.getPassword();
        memberSaveRequestDto.setPassword(passwordEncoder.encode(password));
        memberSaveRequestDto.setPasswordCheck(passwordEncoder.encode(password));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
