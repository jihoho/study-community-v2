package com.project.pagu.member.service;

import com.project.pagu.member.domain.Member;
import com.project.pagu.member.domain.MemberId;
import com.project.pagu.member.model.MemberSaveRequestDto;
import com.project.pagu.member.repository.MembersRespository;
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
public class MembersService implements UserDetailsService {
    private final MembersRespository membersRespository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public boolean existsById(MemberId memberId) {
        return membersRespository.existsById(memberId);
    }

    @Transactional
    public boolean existsByNickname(String nickname) {
        return membersRespository.existsByNickname(nickname);
    }

    @Transactional
    public MemberId save(MemberSaveRequestDto memberSaveRequestDto) {
        Member member = (Member) membersRespository.save(memberSaveRequestDto.toEntity());
        return new MemberId(member.getEmail(), member.getMemberType());
    }

    @Transactional
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
