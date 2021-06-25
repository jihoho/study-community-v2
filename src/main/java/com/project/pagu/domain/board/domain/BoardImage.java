package com.project.pagu.domain.board.domain;

import static javax.persistence.FetchType.LAZY;

import com.project.pagu.domain.board.domain.Board;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

/**
 * Created by IntelliJ IDEA
 * User: hojun
 * Date: 2021-04-19 Time: 오후 11:48
 */
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class BoardImage {

    @Id
    @GeneratedValue
    @Column(name = "board_image_id")
    private Long id;

    private String filename;

    @CreatedDate
    private LocalDateTime createdDate;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "study_board_id")
    private Board board;

    public void setBoard(Board board) {
        this.board = board;
    }

}
