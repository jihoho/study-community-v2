package com.project.pagu.domain.board.service;

import com.project.pagu.domain.board.domain.Board;
import com.project.pagu.domain.board.dto.BoardViewDto;
import com.project.pagu.domain.board.dto.PagedBoardViewDto;
import com.project.pagu.domain.board.dto.BoardSaveDto;
import com.project.pagu.domain.board.dto.LatestBoardViewDto;
import com.project.pagu.domain.member.domain.Member;
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

    PageImpl<PagedBoardViewDto> getSearchBoards(String searchType, String keyword, Pageable pageable);
}
