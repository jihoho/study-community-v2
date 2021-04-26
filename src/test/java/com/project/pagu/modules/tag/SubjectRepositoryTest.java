package com.project.pagu.modules.tag;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.project.pagu.modules.board.domain.Board;
import com.project.pagu.modules.board.domain.StudyStatus;
import com.project.pagu.modules.board.repository.BoardRepository;
import java.util.List;
import java.util.Set;
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
    void test() {
        Subject subject1 = Subject.builder().name("Web").build();
        Subject subject2 = Subject.builder().name("Backend").build();
        Subject subject3 = Subject.builder().name("Frontend").build();
        subjectRepository.save(subject1);
        subjectRepository.save(subject2);
        subjectRepository.save(subject3);


        Subject web = subjectRepository.findByName("Web").get();
        Subject backend = subjectRepository.findByName("Backend").get();
        Subject frontend = subjectRepository.findByName("Frontend").get();

        BoardSubject boardSubject1 = BoardSubject.createBoardSubject(web);
        BoardSubject boardSubject2 = BoardSubject.createBoardSubject(backend);
        BoardSubject boardSubject3 = BoardSubject.createBoardSubject(frontend);

        Board board = Board.builder()
                .title("제목")
                .goal("목표")
                .place("장소")
                .status(StudyStatus.READY)
                .build();
        board.addSubject(boardSubject1, boardSubject3, boardSubject2);

        boardRepository.save(board);

        List<Board> boards = boardRepository.findAll();
        Set<BoardSubject> findSubjects = boards.get(0).getBoardSubjects();

        assertTrue(findSubjects.contains(boardSubject1));
        assertTrue(findSubjects.contains(boardSubject2));
        assertTrue(findSubjects.contains(boardSubject3));

    }
}