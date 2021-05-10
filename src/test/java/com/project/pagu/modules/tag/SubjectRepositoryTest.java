package com.project.pagu.modules.tag;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.project.pagu.modules.board.domain.Board;
import com.project.pagu.modules.board.domain.StudyStatus;
import com.project.pagu.modules.board.repository.BoardRepository;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

/**
 * Created by IntelliJ IDEA
 * User: yhh1056@naver.com
 * Date: 2021/04/25 Time: 4:19 오후
 */

@DataJpaTest
class SubjectRepositoryTest {

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Test
    @DisplayName("주제를 저장하고 게시물 등록시 주제를 등록한다.")
    void save_subject_in_board() {
        Subject subject = Subject.of("Web");
        subjectRepository.save(subject);
        Subject web = subjectRepository.findByName("Web").get();

        BoardSubject boardSubject = BoardSubject.createBoardSubject(web);

        Board board = Board.builder()
                .title("제목")
                .goal("목표")
                .place("장소")
                .status(StudyStatus.READY)
                .build();
        board.addSubject(boardSubject);

        boardRepository.save(board);

        List<Board> boards = boardRepository.findAll();
        Set<BoardSubject> findSubjects = boards.get(0).getBoardSubjects();

        assertTrue(findSubjects.contains(boardSubject));

    }
}