package com.project.pagu.domain.board.service;

import com.project.pagu.global.error.exception.AccessDeniedException;
import com.project.pagu.global.util.FileManager;
import com.project.pagu.domain.board.domain.Board;
import com.project.pagu.domain.board.domain.BoardImage;
import com.project.pagu.domain.board.dto.BoardSaveDto;
import com.project.pagu.domain.board.repository.BoardRepository;
import com.project.pagu.domain.member.domain.Member;
import com.project.pagu.domain.model.MemberId;
import com.project.pagu.domain.member.service.MemberViewService;
import com.project.pagu.domain.model.BoardSubject;
import com.project.pagu.domain.subject.domain.Subject;
import com.project.pagu.domain.subject.service.SubjectService;
import com.project.pagu.domain.model.BoardTechStack;
import com.project.pagu.domain.model.TechStack;
import com.project.pagu.domain.techstack.service.TechStackService;
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
    public Long saveBoardDto(MemberId memberId, BoardSaveDto dto) { // Member엔티티 조회

        Member findMember = memberViewService.findById(memberId);
        Board board = registerTagToBoard(dto.toEntity(), dto.getSubjects(), dto.getTechStacks());
        board.addBoardScheduleList(dto.createBoardSchedules());
        board.setMember(findMember);
        Board savedBoard = boardRepository.save(board);
        registerTagToBoard(board, dto.getSubjects(), dto.getTechStacks());

        List<BoardImage> boardImageList = fileManager.uploadBoardImageDtos(savedBoard.getId(), dto);
        savedBoard.addBoardImageList(boardImageList);

        return savedBoard.getId();
    }

    @Override
    @Transactional
    public void update(MemberId memberId, Long id, BoardSaveDto dto) {

        Board board = boardViewService.findById(id);
        if (!memberId.equals(board.getMember().getMemberId())) {
            throw new AccessDeniedException("해당 댓글의 주인이 아닙니다.");
        }
        registerTagToBoard(board, dto.getSubjects(), dto.getTechStacks());
        board.update(dto);

        // todo : 게시물 이미지 수정 코드 필요
        //            List<BoardImage> boardImageList = uploadBoardImageDto(savedBoard.getId(), dto);
        //            savedBoard.addBoardImageList(boardImageList);

    }


    private Board registerTagToBoard(Board board, String subjects, String techStacks) {
        Set<Subject> subjectSet = createSubject(subjects);
        Set<TechStack> techStackSet = createTechStack(techStacks);
        board.registerSubjects(subjectSet.stream().map(BoardSubject::of).collect(Collectors.toList()));
        board.registerTechStacks(techStackSet.stream().map(BoardTechStack::of).collect(Collectors.toList()));

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


}
