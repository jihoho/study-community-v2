package com.project.pagu.domain.model;

import com.project.pagu.domain.board.domain.Board;
import com.project.pagu.domain.subject.domain.Subject;
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
 * Date: 2021/04/25 Time: 4:13 오후
 */

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "subject")
public class BoardSubject {

    @Id
    @GeneratedValue
    @Column(name = "board_subject")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_board_id")
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    private Subject subject;

    public static BoardSubject of(Subject subject) {
        return BoardSubject
                .builder()
                .subject(subject)
                .build();
    }

    public void addBoard(Board board) {
        this.board = board;
        this.subject.addBoardSubject(this);
    }
}
