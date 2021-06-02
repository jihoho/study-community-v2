package com.project.pagu.domain.member.repository;

import com.project.pagu.domain.member.domain.Member;
import com.project.pagu.domain.model.MemberId;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by IntelliJ IDEA
 * User: hojun
 * Date: 2021-04-02 Time: 오후 9:18
 */
public interface MemberRepository extends JpaRepository<Member, MemberId> {

    boolean existsByNickname(String nickname);

    Optional<Member> findByNickname(String nickname);
}
