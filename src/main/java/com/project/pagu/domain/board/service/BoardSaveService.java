package com.project.pagu.domain.board.service;

import com.project.pagu.domain.board.dto.BoardSaveDto;
import com.project.pagu.domain.model.MemberId;

/**
 * Created by IntelliJ IDEA
 * User: hojun
 * Date: 2021-05-04 Time: 오후 12:31
 */
public interface BoardSaveService {

    Long saveBoardDto(MemberId memberId, BoardSaveDto dto);

    void update(MemberId memberId, Long boardId, BoardSaveDto dto);

    void delete(MemberId memberId,Long boardId);
}
