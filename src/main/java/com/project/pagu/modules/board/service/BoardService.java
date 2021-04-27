package com.project.pagu.modules.board.service;

import com.project.pagu.common.manager.FileUtil;
import com.project.pagu.modules.board.domain.Board;
import com.project.pagu.modules.board.domain.BoardImage;
import com.project.pagu.modules.board.domain.BoardSchedule;
import com.project.pagu.modules.board.model.BoardDetailDto;
import com.project.pagu.modules.board.model.BoardImageDto;
import com.project.pagu.modules.board.model.BoardSaveRequestDto;
import com.project.pagu.modules.board.model.BoardScheduleDto;
import com.project.pagu.modules.board.repository.BoardRepository;
import com.project.pagu.common.manager.FileManager;
import com.project.pagu.modules.member.domain.Member;
import com.project.pagu.modules.member.service.MemberService;
import com.project.pagu.modules.tag.BoardSubject;
import com.project.pagu.modules.tag.Subject;
import com.project.pagu.modules.tag.SubjectService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final SubjectService subjectService;

    @Transactional
    public Long saveBoardDto(Member member, BoardSaveRequestDto dto) {
        // Member엔티티 조회
        Member findMember = memberService.findById(member.getMemberId());
        List<BoardSchedule> boardSchedule = createBoardSchedule(dto.getBoardSchedules());
        Set<Subject> subject = createSubject(dto.getSubjects());

        Board board = dto.toEntity();
        board.setMember(findMember);
        board.addBoardScheduleList(boardSchedule);

        for (Subject subject1 : subject) {
            BoardSubject boardSubject = BoardSubject.createBoardSubject(subject1);
            board.addSubject(boardSubject);
        }

        Board savedBoard = saveBoard(board);

        List<BoardImage> boardImageList = uploadBoardImageDto(savedBoard.getId(), dto);
        savedBoard.addBoardImageList(boardImageList);

        return savedBoard.getId();
    }

    private Set<Subject> createSubject(String subject) {
        return Arrays.stream(subject.split(","))
                .map(subjectService::getOrSave)
                .collect(Collectors.toSet());
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
            String filename = FileUtil.createFileName();
            boardImageDto.setFilename(filename);
            fileManager.uploadBoardImage(boardImageDto.getMultipartFile(), filename,
                    String.valueOf(boardImageDto.getBoardId()));
        }

    }

    public Page<Board> getPagedBoardList(Pageable pageable) {

        Page<Board> boardPage = boardRepository.findAll(pageable);
        return boardPage;

    }

    public BoardDetailDto getBoardDetailDto(Long id) {

        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException());
        return BoardDetailDto.CreateBoardDetailDto(board);

    }

}
