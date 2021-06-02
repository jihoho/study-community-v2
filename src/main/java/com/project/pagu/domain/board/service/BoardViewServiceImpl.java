package com.project.pagu.domain.board.service;

import com.project.pagu.global.error.exception.BoardNotFoundException;
import com.project.pagu.domain.board.domain.Board;
import com.project.pagu.domain.model.StudyStatus;
import com.project.pagu.domain.board.dto.BoardViewDto;
import com.project.pagu.domain.board.dto.PagedBoardViewDto;
import com.project.pagu.domain.board.dto.BoardSaveDto;
import com.project.pagu.domain.board.dto.LatestBoardViewDto;
import com.project.pagu.domain.board.repository.BoardRepository;
import com.project.pagu.domain.comment.service.CommentViewService;
import com.project.pagu.domain.member.domain.Member;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by IntelliJ IDEA
 * User: hojun
 * Date: 2021-05-04 Time: 오후 12:37
 */

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class BoardViewServiceImpl implements BoardViewService {

    private final BoardRepository boardRepository;
    private final CommentViewService commentViewService;

    @Override
    public Board findById(Long id) {
        return boardRepository.findById(id).orElseThrow(BoardNotFoundException::new);
    }

    @Override
    public BoardViewDto getBoardDetailDto(Long id) {
        Board board = findById(id);
        BoardViewDto boardViewDto = BoardViewDto.createBoardViewDto(board);
        boardViewDto.setCommentList(commentViewService.findCommentsByBoardId(board.getId()));
        return boardViewDto;
    }

    @Override
    public BoardSaveDto getBoardSaveDto(Long id) {
        return BoardSaveDto.createBoardSaveDto(findById(id));
    }

    @Override
    public PageImpl<PagedBoardViewDto> getPagedBoardList(Pageable pageable) {
        return convertBoardPageToBoardPageDto(boardRepository.findAll(pageable), pageable);
    }

    private PageImpl<PagedBoardViewDto> convertBoardPageToBoardPageDto(Page<Board> boardPage, Pageable pageable) {
        List<PagedBoardViewDto> pagedBoardViewDtos = new ArrayList<>();
        for (Board board : boardPage) {
            pagedBoardViewDtos.add(PagedBoardViewDto.creatBoardPageDto(board));
        }
        return new PageImpl<>(pagedBoardViewDtos, pageable, boardPage.getTotalElements());
    }

    @Override
    public PageImpl<PagedBoardViewDto> getPagedBoardListByMemberId(Member member, Pageable pageable) {
        return convertBoardPageToBoardPageDto(boardRepository.findByMember(member, pageable), pageable);
    }

    @Override
    public PageImpl<LatestBoardViewDto> getLatestBoard(int size) {
        Pageable pageable = PageRequest.of(0, size, Sort.by("modifiedDate").descending());
        Page<Board> latestBoard = boardRepository.findAll(pageable);
        return convertLatestBoardToLatestBoardDto(latestBoard, pageable);
    }

    private PageImpl<LatestBoardViewDto> convertLatestBoardToLatestBoardDto(Page<Board> latestBoard, Pageable pageable) {
        List<LatestBoardViewDto> latestBoardViewDtos = new ArrayList<>();
        for (Board board : latestBoard) {
            latestBoardViewDtos.add(LatestBoardViewDto.createLatestBoardDto(board));
        }
        return new PageImpl<>(latestBoardViewDtos, pageable, latestBoard.getTotalElements());

    }

    @Override
    public PageImpl<PagedBoardViewDto> getSearchBoards(String searchType, String keyword, Pageable pageable) {
        return convertBoardPageToBoardPageDto(getBoardPage(searchType, keyword, pageable), pageable);
    }

    private Page<Board> getBoardPage(String searchType, String keyword, Pageable pageable) {
        if (searchType.equals("TITLE")) {
            return boardRepository.findByTitleContaining(keyword, pageable);
        }

        if (searchType.equals("TAG")) {
            return boardRepository.findBySubject(keyword, pageable);
        }

        if (searchType.equals("STATUS")) {
            return boardRepository.findByStatusContaining(StudyStatus.valueOf(keyword), pageable);
        }

        throw new IllegalArgumentException();
    }
}
