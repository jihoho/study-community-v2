package com.project.pagu.modules.board.service;

import com.project.pagu.modules.board.domain.Board;
import com.project.pagu.modules.board.domain.BoardImage;
import com.project.pagu.modules.board.domain.BoardSchedule;
import com.project.pagu.modules.board.model.BoardImageDto;
import com.project.pagu.modules.board.model.BoardSaveRequestDto;
import com.project.pagu.modules.board.model.BoardScheduleDto;
import com.project.pagu.modules.board.repository.BoardRepository;
import com.project.pagu.common.manager.FileManager;
import com.project.pagu.modules.member.domain.Member;
import com.project.pagu.modules.member.service.MemberService;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by IntelliJ IDEA
 * User: hojun
 * Date: 2021-04-20 Time: 오후 7:34
 */

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class BoardService {

    private final BoardRepository boardRepository;
    private final FileManager fileManager;
    private final MemberService memberService;

    @Transactional
    public Long saveBoardDto(Member member, BoardSaveRequestDto dto) {
        // Member엔티티 조회
        Member findMember = memberService.findById(member.getMemberId());
        Board board = dto.toEntity();
        board.setMember(findMember);
        board.addBoardScheduleList(createBoardSchedule(dto.getBoardSchedules()));
        Board savedBoard = saveBoard(board);
        savedBoard.addBoardImageList(uploadBoardImageDto(savedBoard.getId(), dto));

        return savedBoard.getId();
    }

    public List<BoardSchedule> createBoardSchedule(List<BoardScheduleDto> boardSchedules) {
        List<BoardSchedule> boardScheduleList = new ArrayList<>();
        for (BoardScheduleDto boardScheduleDto : boardSchedules) {
            BoardSchedule boardSchedule = boardScheduleDto.toEntity();
            boardScheduleList.add(boardSchedule);
        }
        return boardScheduleList;
    }

    private Board saveBoard(Board board) {
        return boardRepository.save(board);
    }

    private List<BoardImage> uploadBoardImageDto(Long boardId,
            BoardSaveRequestDto boardSaveRequestDto) {

        List<BoardImage> boardImageList = new ArrayList<>();
        List<BoardImageDto> boardImageDtoList = boardSaveRequestDto.toBoardImageDtoList();
        for (BoardImageDto boardImageDto : boardImageDtoList) {
            boardImageDto.setBoardId(boardId);
            uploadImage(boardImageDto);
            boardImageList.add(boardImageDto.toEntity());
        }
        return boardImageList;
    }

    private void uploadImage(BoardImageDto boardImageDto) {

        if (boardImageDto.getMultipartFile().getSize() != 0) {
            String filename = fileManager.createFileName();
            boardImageDto.setFilename(filename);
            fileManager.uploadBoardImage(boardImageDto.getMultipartFile(), filename,
                    String.valueOf(boardImageDto.getBoardId()));
        }

    }
}
