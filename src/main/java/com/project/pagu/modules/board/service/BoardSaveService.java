package com.project.pagu.modules.board.service;

import com.project.pagu.modules.board.domain.Board;
import com.project.pagu.modules.board.model.BoardSaveDto;
import com.project.pagu.modules.member.domain.Member;

/**
 * Created by IntelliJ IDEA
 * User: hojun
 * Date: 2021-05-04 Time: 오후 12:31
 */
public interface BoardSaveService {

    Board saveBoard(Board board);

    Long saveBoardDto(Member member, BoardSaveDto dto);

    void update(Member member, Long id, BoardSaveDto dto);
}
