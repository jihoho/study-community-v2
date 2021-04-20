package com.project.pagu.board.repository;

import com.project.pagu.board.domain.BoardImage;
import com.project.pagu.board.domain.BoardSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by IntelliJ IDEA
 * User: hojun
 * Date: 2021-04-21 Time: 오전 1:59
 */
public interface BoardScheduleRepository extends JpaRepository<BoardSchedule,Long> {

}
