package com.project.pagu.modules.board.repository;

import com.project.pagu.modules.board.domain.Board;
import com.project.pagu.modules.member.domain.Member;
import com.project.pagu.modules.member.domain.MemberId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by IntelliJ IDEA
 * User: hojun
 * Date: 2021-04-20 Time: 오후 7:35
 */
public interface BoardRepository extends JpaRepository<Board,Long> {

    Page<Board> findByTitleContaining(String keyword, Pageable pageable);

    Page<Board> findByMember(Member member, Pageable pageable);
}
