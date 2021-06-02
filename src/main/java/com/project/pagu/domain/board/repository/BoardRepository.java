package com.project.pagu.domain.board.repository;

import com.project.pagu.domain.board.domain.Board;
import com.project.pagu.domain.model.StudyStatus;
import com.project.pagu.domain.member.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Created by IntelliJ IDEA
 * User: hojun
 * Date: 2021-04-20 Time: 오후 7:35
 */
public interface BoardRepository extends JpaRepository<Board, Long> {

    Page<Board> findByTitleContaining(String keyword, Pageable pageable);

    @Query("select b from Board b where b.status = :status")
    Page<Board> findByStatusContaining(@Param("status") StudyStatus status, Pageable pageable);

    @Query("select distinct b from Board b left join b.boardSubjects j left join j.subject s "
            + "left join b.boardTechStacks k left join k.techStack t"
            + " where s.name like :keyword"
            + " or t.name like :keyword")
    Page<Board> findBySubject(@Param("keyword") String keyword, Pageable pageable);

    Page<Board> findByMember(Member member, Pageable pageable);
}
