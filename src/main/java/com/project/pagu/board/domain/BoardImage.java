package com.project.pagu.board.domain;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

/**
 * Created by IntelliJ IDEA
 * User: hojun
 * Date: 2021-04-19 Time: 오후 11:48
 */
@Entity
@NoArgsConstructor
public class BoardImage {

    @Id
    @GeneratedValue
    @Column(name = "board_image_id")
    private Long id;

    private String filename;

    @CreatedDate
    private LocalDateTime createdDate;

    @ManyToOne
    @JoinColumn(name = "study_board_id")
    private Board board;

    @Builder
    public BoardImage(String filename, Board board) {
        this.filename = filename;
        setBoard(board);
    }

    //==연관관계 편의 메서드==//
    private void setBoard(Board board) {
        this.board = board;
        board.addBoardImage(this);
    }
}
