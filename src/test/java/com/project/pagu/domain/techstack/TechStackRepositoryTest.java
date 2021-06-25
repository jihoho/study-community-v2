package com.project.pagu.domain.techstack;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.project.pagu.domain.board.domain.Board;
import com.project.pagu.domain.model.BoardTechStack;
import com.project.pagu.domain.model.StudyStatus;
import com.project.pagu.domain.board.repository.BoardRepository;
import com.project.pagu.domain.model.TechStack;
import com.project.pagu.domain.techstack.repository.TechStackRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

/**
 * Created by IntelliJ IDEA
 * User: yhh1056@naver.com
 * Date: 2021/04/27 Time: 3:03 오후
 */
@DataJpaTest
class TechStackRepositoryTest {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private TechStackRepository techStackRepository;

    @Test
    void save_tech_stack() {
        TechStack java = TechStack.of("JAVA");
        techStackRepository.save(java);

        TechStack findTechStack = techStackRepository.findByName("JAVA").get();

        BoardTechStack boardTechStack = BoardTechStack.of(findTechStack);

        Board board = Board.builder()
                .title("제목")
                .goal("목표")
                .place("장소")
                .status(StudyStatus.READY)
                .build();
        board.addTechStack(boardTechStack);

        boardRepository.save(board);

        Board findBoard = boardRepository.findAll().get(0);

        assertTrue(findBoard.getBoardTechStacks().contains(boardTechStack));

    }
}