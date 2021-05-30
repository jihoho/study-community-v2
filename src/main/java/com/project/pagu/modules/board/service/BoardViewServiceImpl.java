package com.project.pagu.modules.board.service;

import com.project.pagu.modules.board.Exception.BoardNotFoundException;
import com.project.pagu.modules.board.domain.Board;
import com.project.pagu.modules.board.domain.StudyStatus;
import com.project.pagu.modules.board.model.BoardViewDto;
import com.project.pagu.modules.board.model.PagedBoardViewDto;
import com.project.pagu.modules.board.model.BoardSaveDto;
import com.project.pagu.modules.board.model.LatestBoardViewDto;
import com.project.pagu.modules.board.repository.BoardRepository;
import com.project.pagu.modules.comment.service.CommentViewService;
import com.project.pagu.modules.member.domain.Member;
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
        Page<Board> boardPage = boardRepository.findAll(pageable);
        PageImpl<PagedBoardViewDto> boardPageDto = convertBoardPageToBoardPageDto(boardPage,
                pageable);
        return boardPageDto;
    }

    @Override
    public PageImpl<PagedBoardViewDto> getPagedBoardListByMemberId(Member member,
            Pageable pageable) {

        Page<Board> boardPage = boardRepository.findByMember(member, pageable);
        return convertBoardPageToBoardPageDto(boardPage, pageable);
    }

    @Override
    public PageImpl<LatestBoardViewDto> getLatestBoard(int size) {
        Pageable pageable = PageRequest
                .of(0, size, Sort.by("modifiedDate").descending());
        Page<Board> latestBoard = boardRepository.findAll(pageable);
        return convertLatestBoardToLatestBoardDto(latestBoard, pageable);
    }

    @Override
    public PageImpl<PagedBoardViewDto> getSearchBoards(String searchType, String keyword,
            Pageable pageable) throws Exception {

        Page<Board> boardPage;
        if (searchType.equals("TITLE")) {
            boardPage = boardRepository
                    .findByTitleContaining(keyword, pageable);
        }else if (searchType.equals("TAG")){
            boardPage = boardRepository
                    .findBySubject(keyword, pageable);
        }else if(searchType.equals("STATUS")){
            boardPage = boardRepository
                    .findByStatusContaining(StudyStatus.valueOf(keyword), pageable);
        }else{
            throw new Exception();
        }
        return convertBoardPageToBoardPageDto(boardPage, pageable);
    }

    /**
     * Mapper로 변환 예정
     */
    private PageImpl<PagedBoardViewDto> convertBoardPageToBoardPageDto(Page<Board> boardPage, Pageable pageable) {
        List<PagedBoardViewDto> pagedBoardViewDtos = new ArrayList<>();
        for (Board board : boardPage) {
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
