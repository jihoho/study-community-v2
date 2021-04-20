package com.project.pagu.board.service;

import com.project.pagu.board.domain.Board;
import com.project.pagu.board.domain.BoardImage;
import com.project.pagu.board.domain.BoardSchedule;
import com.project.pagu.board.model.BoardImageDto;
import com.project.pagu.board.model.BoardSaveRequestDto;
import com.project.pagu.board.repository.BoardImageRepository;
import com.project.pagu.board.repository.BoardRepository;
import com.project.pagu.board.repository.BoardScheduleRepository;
import com.project.pagu.common.file.FileManager;
import com.project.pagu.member.domain.Member;
import com.project.pagu.member.service.MemberService;
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
    private final BoardImageRepository boardImageRepository;
    private final BoardScheduleRepository boardScheduleRepository;
    private final MemberService memberService;
    private final FileManager fileManager;

    @Transactional
    public Long saveBoardDto(Member member, BoardSaveRequestDto dto) {
        // Member엔티티 조회
        Member findMember = memberService.findById(member.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException());

        Board board = dto.toEntity(findMember);
        Board savedBoard = saveBoard(board);

        List<BoardSchedule> boardSchedules = board.getBoardSchedules();
        saveBoardSchedules(boardSchedules);

        List<BoardImage> boardImages = uploadBoardImageDto(board, dto);
        saveBoardImages(boardImages);

        return savedBoard.getId();
    }

    private void saveBoardSchedules(List<BoardSchedule> boardSchedules) {
        for (BoardSchedule boardSchedule : boardSchedules) {
            boardScheduleRepository.save(boardSchedule);
        }
    }

    private void saveBoardImages(List<BoardImage> boardImages) {
        for (BoardImage boardImage : boardImages) {
            boardImageRepository.save(boardImage);
        }
    }

    private Board saveBoard(Board board) {
        return boardRepository.save(board);
    }

    private List<BoardImage> uploadBoardImageDto(Board board,
            BoardSaveRequestDto boardSaveRequestDto) {
        List<BoardImage> boardImages = new ArrayList<>();
        List<BoardImageDto> boardImageDtos = boardSaveRequestDto.toBoardImageDto();
        for (BoardImageDto boardImageDto : boardImageDtos) {
            boardImageDto.setBoardId(board.getId());
            uploadImage(boardImageDto);
            boardImages.add(boardImageDto.toEntity(board));
        }
        return boardImages;
    }

    private void uploadImage(BoardImageDto boardImageDto) {
        if (boardImageDto.getMultipartFile().getSize() != 0) {
            String filename = fileManager.createFileName();
            boardImageDto.setFilename(filename);
            fileManager.uploadBoardImage(boardImageDto);
        }

    }
}
