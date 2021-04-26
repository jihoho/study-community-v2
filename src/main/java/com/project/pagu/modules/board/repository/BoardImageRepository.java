package com.project.pagu.modules.board.repository;

import com.project.pagu.modules.board.domain.BoardImage;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by IntelliJ IDEA
 * User: hojun
 * Date: 2021-04-21 Time: 오전 1:59
 */
public interface BoardImageRepository extends JpaRepository<BoardImage,Long> {

}
