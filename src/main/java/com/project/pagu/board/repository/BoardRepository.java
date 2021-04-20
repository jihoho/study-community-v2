package com.project.pagu.board.repository;

import com.project.pagu.board.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by IntelliJ IDEA
 * User: hojun
 * Date: 2021-04-20 Time: 오후 7:35
 */
public interface BoardRepository extends JpaRepository<Board,Long> {

}
