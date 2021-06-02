package com.project.pagu.domain.model;

import com.project.pagu.domain.board.domain.Board;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Created by IntelliJ IDEA
 * User: yhh1056@naver.com
 * Date: 2021/04/27 Time: 2:11 오전
 */
@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "techStack")
public class BoardTechStack {

    @Id
    @GeneratedValue
    @Column(name = "board_techStack")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_board_id")
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "techStack_id")
    private TechStack techStack;

    public static BoardTechStack of(TechStack techStack) {
        return BoardTechStack
                .builder()
                .techStack(techStack)
                .build();
    }

    public void addBoard(Board board) {
        this.board = board;
        this.techStack.addBoardTechStack(this);
    }
}
