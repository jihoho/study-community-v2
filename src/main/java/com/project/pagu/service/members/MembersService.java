package com.project.pagu.service.members;

import com.project.pagu.domain.member.Member;
import com.project.pagu.domain.member.MemberId;
import com.project.pagu.web.dto.MemberSaveRequestDto;
import com.project.pagu.domain.member.MembersRespository;
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
    public MemberSaveRequestDto encryptPassword(MemberSaveRequestDto memberSaveRequestDto){
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
