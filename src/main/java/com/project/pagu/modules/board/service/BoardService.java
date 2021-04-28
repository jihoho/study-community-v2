package com.project.pagu.modules.board.service;

import com.project.pagu.common.manager.FileUtil;
import com.project.pagu.modules.board.domain.Board;
import com.project.pagu.modules.board.domain.BoardImage;
import com.project.pagu.modules.board.domain.BoardSchedule;
import com.project.pagu.modules.board.model.BoardDetailDto;
import com.project.pagu.modules.board.model.BoardImageDto;
import com.project.pagu.modules.board.model.BoardPageDto;
import com.project.pagu.modules.board.model.BoardSaveRequestDto;
import com.project.pagu.modules.board.model.BoardScheduleDto;
import com.project.pagu.modules.board.model.LatestBoardDto;
import com.project.pagu.modules.board.repository.BoardRepository;
import com.project.pagu.common.manager.FileManager;
import com.project.pagu.modules.member.domain.Member;
import com.project.pagu.modules.member.service.MemberService;
import com.project.pagu.modules.tag.BoardSubject;
import com.project.pagu.modules.tag.Subject;
import com.project.pagu.modules.tag.SubjectService;
import com.project.pagu.modules.teckstack.BoardTechStack;
import com.project.pagu.modules.teckstack.TechStackService;
import com.project.pagu.modules.teckstack.TechStack;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
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
    private final TechStackService techStackService;

    @Transactional
    public Long saveBoardDto(Member member, BoardSaveRequestDto dto) {
        // Member엔티티 조회
        Member findMember = memberService.findById(member.getMemberId());
        List<BoardSchedule> boardSchedule = createBoardSchedule(dto.getBoardSchedules());
        Set<Subject> subject = createSubject(dto.getSubjects());
        Set<TechStack> techStacks = createTechStack(dto.getTechStacks());

        Board board = dto.toEntity();
        board.setMember(findMember);
        board.addBoardScheduleList(boardSchedule);

        for (Subject subject1 : subject) {
            BoardSubject boardSubject = BoardSubject.createBoardSubject(subject1);
            board.addSubject(boardSubject);
        }

        for (TechStack techStack : techStacks) {
            BoardTechStack boardTechStack = BoardTechStack.of(techStack);
            board.addTechStack(boardTechStack);
        }

        //Board savedBoard = boardRepository.save(board);
        Board savedBoard = saveBoard(board);

        List<BoardImage> boardImageList = uploadBoardImageDto(savedBoard.getId(), dto);
        savedBoard.addBoardImageList(boardImageList);

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

    public PageImpl<BoardPageDto> getPagedBoardList(Pageable pageable) {

        Page<Board> boardPage = boardRepository.findAll(pageable);
        PageImpl<BoardPageDto> boardPageDto = convertBoardPageToBoardPageDto(boardPage,pageable);
        return boardPageDto;

    }

    /**
     * Mapper로 변환 예정
     */
    private PageImpl<BoardPageDto> convertBoardPageToBoardPageDto(Page<Board> boardPage,Pageable pageable) {
        List<BoardPageDto> boardPageDtos = new ArrayList<>();
        for (Board board : boardPage) {
            boardPageDtos.add(BoardPageDto.creatBoardPageDto(board));
        }
        return new PageImpl<BoardPageDto>(boardPageDtos,pageable,boardPage.getTotalElements());
    }


    public BoardDetailDto getBoardDetailDto(Long id) {

        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException());
        return BoardDetailDto.CreateBoardDetailDto(board);

    }

    public PageImpl<LatestBoardDto> getLatestBoard(int size) {

        Pageable pageable = PageRequest.of(0, size, Sort.by("modifiedDate").descending());
        Page<Board> latestBoard = boardRepository.findAll(pageable);
        return convertLatestBoardToLatestBoardDto(latestBoard, pageable);

    }

    private PageImpl<LatestBoardDto> convertLatestBoardToLatestBoardDto(Page<Board> latestBoard,
            Pageable pageable) {

        List<LatestBoardDto> latestBoardDtos = new ArrayList<>();
        for (Board board : latestBoard) {
            latestBoardDtos.add(LatestBoardDto.createLatestBoardDto(board));
        }
        return new PageImpl<>(latestBoardDtos, pageable, latestBoard.getTotalElements());

    }
}
