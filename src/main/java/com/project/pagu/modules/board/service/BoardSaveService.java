package com.project.pagu.modules.board.service;

import com.project.pagu.modules.board.model.BoardSaveDto;
import com.project.pagu.modules.member.domain.MemberId;

/**
 * Created by IntelliJ IDEA
 * User: hojun
 * Date: 2021-05-04 Time: 오후 12:31
 */
public interface BoardSaveService {

    Long saveBoardDto(MemberId memberId, BoardSaveDto dto);

    void update(MemberId memberId, Long boardId, BoardSaveDto dto);
}
