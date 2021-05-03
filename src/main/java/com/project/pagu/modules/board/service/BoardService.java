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
import com.project.pagu.modules.comment.service.CommentService;
import com.project.pagu.modules.member.domain.Member;
import com.project.pagu.modules.member.service.MemberSaveService;
import com.project.pagu.modules.member.service.MemberViewService;
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
    private final MemberSaveService memberSaveService;
    private final MemberViewService memberViewService;
    private final SubjectService subjectService;
    private final TechStackService techStackService;
    private final CommentService commentService;

    @Transactional
    public Long saveBoardDto(Member member, BoardSaveRequestDto dto) {
        // Member엔티티 조회
        Member findMember = memberViewService.findById(member.getMemberId());
        List<BoardSchedule> boardSchedule = createBoardSchedule(dto.getBoardSchedules());
        Set<Subject> subject = createSubject(dto.getSubjects());
        Set<TechStack> techStacks = createTechStack(dto.getTechStacks());

        com.project.pagu.modules.board.domain.Board board = dto.toEntity();
        board.setMember(findMember);
//        board.setMember(member);
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
        com.project.pagu.modules.board.domain.Board savedBoard = saveBoard(board);

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

    private com.project.pagu.modules.board.domain.Board saveBoard(
            com.project.pagu.modules.board.domain.Board board) {
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

        Page<com.project.pagu.modules.board.domain.Board> boardPage = boardRepository.findAll(pageable);
        PageImpl<BoardPageDto> boardPageDto = convertBoardPageToBoardPageDto(boardPage,pageable);
        return boardPageDto;

    }

    /**
     * Mapper로 변환 예정
     */
    private PageImpl<BoardPageDto> convertBoardPageToBoardPageDto(Page<com.project.pagu.modules.board.domain.Board> boardPage,Pageable pageable) {
        List<BoardPageDto> boardPageDtos = new ArrayList<>();
        for (com.project.pagu.modules.board.domain.Board board : boardPage) {
            boardPageDtos.add(BoardPageDto.creatBoardPageDto(board));
        }
        return new PageImpl<BoardPageDto>(boardPageDtos,pageable,boardPage.getTotalElements());
    }


    public BoardDetailDto getBoardDetailDto(Long id) {

        com.project.pagu.modules.board.domain.Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException());
        BoardDetailDto boardDetailDto=BoardDetailDto.createBoardDetailDto(board);
        boardDetailDto.setCommentList(commentService.findCommentsByBoardId(board.getId()));
        return boardDetailDto;

    }

    public PageImpl<LatestBoardDto> getLatestBoard(int size) {

        Pageable pageable = PageRequest.of(0, size, Sort.by("modifiedDate").descending());
        Page<com.project.pagu.modules.board.domain.Board> latestBoard = boardRepository.findAll(pageable);
        return convertLatestBoardToLatestBoardDto(latestBoard, pageable);

    }

    private PageImpl<LatestBoardDto> convertLatestBoardToLatestBoardDto(Page<com.project.pagu.modules.board.domain.Board> latestBoard,
            Pageable pageable) {

        List<LatestBoardDto> latestBoardDtos = new ArrayList<>();
        for (com.project.pagu.modules.board.domain.Board board : latestBoard) {
            latestBoardDtos.add(LatestBoardDto.createLatestBoardDto(board));
        }
        return new PageImpl<>(latestBoardDtos, pageable, latestBoard.getTotalElements());

    }

    public PageImpl<BoardPageDto> getSearchBoards(String keyword, Pageable pageable) {
        Page<com.project.pagu.modules.board.domain.Board> boardPage = boardRepository.findByTitleContaining(keyword, pageable);
        return convertBoardPageToBoardPageDto(boardPage, pageable);
    }

    @Transactional
    public void update(Member member, Long id, BoardSaveRequestDto dto) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException());
        if (member.getBoards().contains(board)) {
            List<BoardSchedule> boardSchedule = createBoardSchedule(dto.getBoardSchedules());
            Set<Subject> subject = createSubject(dto.getSubjects());
            Set<TechStack> techStacks = createTechStack(dto.getTechStacks());

            board.addBoardScheduleList(boardSchedule);

            for (Subject subject1 : subject) {
                BoardSubject boardSubject = BoardSubject.createBoardSubject(subject1);
                board.addSubject(boardSubject);
            }

            for (TechStack techStack : techStacks) {
                BoardTechStack boardTechStack = BoardTechStack.of(techStack);
                board.addTechStack(boardTechStack);
            }

            //todo : dto에 있는 데이터 보드로 전송, view에서 status 입력 넘기는 부분

//            List<BoardImage> boardImageList = uploadBoardImageDto(savedBoard.getId(), dto);
//            savedBoard.addBoardImageList(boardImageList);

        }

    }

    //todo: 뷰 dto로 변경
    public BoardSaveRequestDto getBoardSaveDto(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException());

        BoardSaveRequestDto dto = new BoardSaveRequestDto();
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


}
