package com.project.pagu.modules.board.service;

import com.project.pagu.modules.board.domain.Board;
import com.project.pagu.modules.board.model.BoardViewDto;
import com.project.pagu.modules.board.model.PagedBoardViewDto;
import com.project.pagu.modules.board.model.BoardSaveDto;
import com.project.pagu.modules.board.model.LatestBoardViewDto;
import com.project.pagu.modules.member.domain.Member;
import com.project.pagu.modules.member.domain.MemberId;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

/**
 * Created by IntelliJ IDEA
 * User: hojun
 * Date: 2021-05-04 Time: 오후 12:34
 */
public interface BoardViewService {

    Board findById(Long id);

    BoardViewDto getBoardDetailDto(Long id);

    BoardSaveDto getBoardSaveDto(Long id);

    PageImpl<PagedBoardViewDto> getPagedBoardList(Pageable pageable);

    PageImpl<PagedBoardViewDto> getPagedBoardListByMemberId(Member member, Pageable pageable);

    PageImpl<LatestBoardViewDto> getLatestBoard(int size);

    PageImpl<PagedBoardViewDto> getSearchBoards(String searchType, String keyword, Pageable pageable)
            throws Exception;
}
