package com.project.pagu.domain.member;

import com.project.pagu.domain.member.Member;
import com.project.pagu.domain.member.MemberId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Created by IntelliJ IDEA
 * User: hojun
 * Date: 2021-04-02 Time: 오후 9:18
 */
public interface MembersRespository extends JpaRepository<Member, MemberId> {
    boolean existsByNickname(String nickname);
}
