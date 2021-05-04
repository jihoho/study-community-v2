package com.project.pagu.modules.board.service;

import com.project.pagu.modules.board.Exception.BoardNotFoundException;
import com.project.pagu.modules.board.domain.Board;
import com.project.pagu.modules.board.model.BoardViewDto;
import com.project.pagu.modules.board.model.PagedBoardViewDto;
import com.project.pagu.modules.board.model.BoardSaveDto;
import com.project.pagu.modules.board.model.LatestBoardViewDto;
import com.project.pagu.modules.board.repository.BoardRepository;
import com.project.pagu.modules.comment.service.CommentService;
import com.project.pagu.modules.tag.BoardSubject;
import com.project.pagu.modules.teckstack.BoardTechStack;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
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
    private final CommentService commentService;

    @Override
    public Board findById(Long id) {
        return boardRepository.findById(id).orElseThrow(BoardNotFoundException::new);
    }


    @Override
    public BoardViewDto getBoardDetailDto(Long id) {
        Board board = findById(id);
        BoardViewDto boardViewDto = BoardViewDto.createBoardDetailDto(board);
        boardViewDto.setCommentList(commentService.findCommentsByBoardId(board.getId()));
        return boardViewDto;
    }

    @Override
    public BoardSaveDto getBoardSaveDto(Long id) {
        Board board = findById(id);

        BoardSaveDto dto = new BoardSaveDto();
        dto.setId(board.getId());
        dto.setTitle(board.getTitle());
        dto.setSubjects(subjectToString(board.getBoardSubjects()));
        dto.setTechStacks(techStackToString(board.getBoardTechStacks()));
        dto.setGoal(board.getGoal());
        dto.setPlace(board.getPlace());
        dto.setBoardSchedules(dto.getBoardSchedules());
        dto.setStatus(board.getStatus());
        dto.setRecruitmentStartAt(board.getRecruitmentStartAt());
        dto.setRecruitmentEndAt(board.getRecruitmentEndAt());
        dto.setTermsStartAt(board.getTermsStartAt());
        dto.setTermsEndAt(board.getTermsEndAt());
        dto.setEtc(board.getEtc());

        return dto;
    }

    @Override
    public PageImpl<PagedBoardViewDto> getPagedBoardList(Pageable pageable) {
        Page<Board> boardPage = boardRepository.findAll(pageable);
        PageImpl<PagedBoardViewDto> boardPageDto = convertBoardPageToBoardPageDto(boardPage,
                pageable);
        return boardPageDto;
    }

    @Override
    public PageImpl<LatestBoardViewDto> getLatestBoard(int size) {
        Pageable pageable = PageRequest
                .of(0, size, Sort.by("modifiedDate").descending());
        Page<Board> latestBoard = boardRepository.findAll(pageable);
        return convertLatestBoardToLatestBoardDto(latestBoard, pageable);
    }

    @Override
    public PageImpl<PagedBoardViewDto> getSearchBoards(String keyword, Pageable pageable) {
        Page<com.project.pagu.modules.board.domain.Board> boardPage = boardRepository
                .findByTitleContaining(keyword, pageable);
        return convertBoardPageToBoardPageDto(boardPage, pageable);
    }

    private String subjectToString(Set<BoardSubject> boardSubjects) {
        // todo : 로직 깔끔하게 수정할것
        String subject = "";
        for (BoardSubject boardSubject : boardSubjects) {
            subject += boardSubject.getSubject().getName() + ",";
        }
        return subject;
    }

    private String techStackToString(Set<BoardTechStack> boardTechStacks) {
        String techStack = "";
        for (BoardTechStack boardTechStack : boardTechStacks) {
            techStack += boardTechStack.getTechStack().getName() + ",";
        }
        return techStack;
    }

    /**
     * Mapper로 변환 예정
     */
    private PageImpl<PagedBoardViewDto> convertBoardPageToBoardPageDto(
            Page<com.project.pagu.modules.board.domain.Board> boardPage, Pageable pageable) {
        List<PagedBoardViewDto> pagedBoardViewDtos = new ArrayList<>();
        for (com.project.pagu.modules.board.domain.Board board : boardPage) {
            pagedBoardViewDtos.add(PagedBoardViewDto.creatBoardPageDto(board));
        }
        return new PageImpl<>(pagedBoardViewDtos, pageable,
                boardPage.getTotalElements());
    }

    private PageImpl<LatestBoardViewDto> convertLatestBoardToLatestBoardDto(Page<Board> latestBoard,
            Pageable pageable) {

        List<LatestBoardViewDto> latestBoardViewDtos = new ArrayList<>();
        for (Board board : latestBoard) {
            latestBoardViewDtos.add(LatestBoardViewDto.createLatestBoardDto(board));
        }
        return new PageImpl<>(latestBoardViewDtos, pageable, latestBoard.getTotalElements());

    }
}
