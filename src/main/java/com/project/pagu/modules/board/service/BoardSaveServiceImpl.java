package com.project.pagu.modules.board.service;

import com.project.pagu.common.exception.AccessDeniedException;
import com.project.pagu.common.manager.FileManager;
import com.project.pagu.modules.board.domain.Board;
import com.project.pagu.modules.board.domain.BoardImage;
import com.project.pagu.modules.board.model.BoardSaveDto;
import com.project.pagu.modules.board.repository.BoardRepository;
import com.project.pagu.modules.member.domain.Member;
import com.project.pagu.modules.member.domain.MemberId;
import com.project.pagu.modules.member.service.MemberViewService;
import com.project.pagu.modules.tag.BoardSubject;
import com.project.pagu.modules.tag.Subject;
import com.project.pagu.modules.tag.SubjectService;
import com.project.pagu.modules.teckstack.BoardTechStack;
import com.project.pagu.modules.teckstack.TechStack;
import com.project.pagu.modules.teckstack.TechStackService;
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
