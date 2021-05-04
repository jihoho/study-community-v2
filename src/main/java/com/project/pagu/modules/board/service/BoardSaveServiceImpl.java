package com.project.pagu.modules.board.service;

import com.project.pagu.common.exception.AccessDeniedException;
import com.project.pagu.common.manager.FileManager;
import com.project.pagu.common.manager.FileUtil;
import com.project.pagu.modules.board.domain.Board;
import com.project.pagu.modules.board.domain.BoardImage;
import com.project.pagu.modules.board.domain.BoardSchedule;
import com.project.pagu.modules.board.model.BoardImageSaveDto;
import com.project.pagu.modules.board.model.BoardSaveDto;
import com.project.pagu.modules.board.model.BoardScheduleDto;
import com.project.pagu.modules.board.repository.BoardRepository;
import com.project.pagu.modules.member.domain.Member;
import com.project.pagu.modules.member.service.MemberViewService;
import com.project.pagu.modules.tag.BoardSubject;
import com.project.pagu.modules.tag.Subject;
import com.project.pagu.modules.tag.SubjectService;
import com.project.pagu.modules.teckstack.BoardTechStack;
import com.project.pagu.modules.teckstack.TechStack;
import com.project.pagu.modules.teckstack.TechStackService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by IntelliJ IDEA
 * User: hojun
 * Date: 2021-05-04 Time: 오후 12:38
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardSaveServiceImpl implements BoardSaveService {

    private final MemberViewService memberViewService;
    private final BoardViewService boardViewService;
    private final BoardRepository boardRepository;
    private final SubjectService subjectService;
    private final TechStackService techStackService;
    private final FileManager fileManager;

    @Override
    @Transactional
    public Board saveBoard(Board board) {
        return boardRepository.save(board);
    }

    @Override
    @Transactional
    public Long saveBoardDto(Member member, BoardSaveDto dto) { // Member엔티티 조회

        if (member == null) {
            throw new AccessDeniedException();
        }

        Member findMember = memberViewService.findById(member.getMemberId());
        Board board = registerTagToBoard(dto.toEntity(), dto.getSubjects(), dto.getTechStacks());
        board.setMember(findMember);
        Board savedBoard = saveBoard(board);

        List<BoardImage> boardImageList = uploadBoardImageDto(savedBoard.getId(), dto);
        savedBoard.addBoardImageList(boardImageList);

        return savedBoard.getId();
    }

    @Override
    @Transactional
    public void update(Member member, Long id, BoardSaveDto dto) {

        Board board = boardViewService.findById(id);
        if (member == null || !member.getMemberId().equals(board.getMember().getMemberId())) {
            throw new AccessDeniedException();
        }
        registerTagToBoard(board, dto.getSubjects(), dto.getTechStacks());

        //todo : 게시물 내용 수정 코드 필요
        //todo : dto에 있는 데이터 보드로 전송, view에서 status 입력 넘기는 부분

        //            List<BoardImage> boardImageList = uploadBoardImageDto(savedBoard.getId(), dto);
        //            savedBoard.addBoardImageList(boardImageList);

    }


    private Board registerTagToBoard(Board board, String subjects, String techStacks) {
        Set<Subject> subjectSet = createSubject(subjects);
        Set<TechStack> techStackSet = createTechStack(techStacks);

        for (Subject subject : subjectSet) {
            BoardSubject boardSubject = BoardSubject.createBoardSubject(subject);
            board.addSubject(boardSubject);
        }
        for (TechStack techStack : techStackSet) {
            BoardTechStack boardTechStack = BoardTechStack.of(techStack);
            board.addTechStack(boardTechStack);
        }

        return board;
    }

    private Set<Subject> createSubject(String subject) {
        return Arrays.stream(subject.split(","))
                .map(subjectService::getOrSave)
                .collect(Collectors.toSet());
    }

    private Set<TechStack> createTechStack(String techStacks) {
        return Arrays.stream(techStacks.split(","))
                .map(techStackService::getOrSave)
                .collect(Collectors.toSet());
    }


    private List<BoardImage> uploadBoardImageDto(Long boardId,
            BoardSaveDto boardSaveDto) {

        List<BoardImage> boardImageList = new ArrayList<>();
        List<BoardImageSaveDto> boardImageSaveDtoList = boardSaveDto.toBoardImageDtoList();
        for (BoardImageSaveDto boardImageSaveDto : boardImageSaveDtoList) {
            boardImageSaveDto.setBoardId(boardId);
            uploadImage(boardImageSaveDto);
            boardImageList.add(boardImageSaveDto.toEntity());
        }
        return boardImageList;
    }

    private void uploadImage(BoardImageSaveDto boardImageSaveDto) {

        if (boardImageSaveDto.getMultipartFile().getSize() != 0) {
            String filename = FileUtil.createFileName();
            boardImageSaveDto.setFilename(filename);
            fileManager.uploadBoardImage(boardImageSaveDto.getMultipartFile(), filename,
                    String.valueOf(boardImageSaveDto.getBoardId()));
        }

    }
}
