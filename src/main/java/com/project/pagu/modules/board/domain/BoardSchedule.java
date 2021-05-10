package com.project.pagu.modules.board.domain;

import static javax.persistence.FetchType.LAZY;
import java.time.LocalTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Created by IntelliJ IDEA
 * User: hojun
 * Date: 2021-04-19 Time: 오후 11:16
 */
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
public class BoardSchedule {

    @Id
    @GeneratedValue
    @Column(name = "schedule_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StudyDay studyDay;

    private LocalTime startTime;

    private LocalTime endTime;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "study_board_id")
    private Board board;

    public void setBoard(Board board) {
        this.board = board;
    }
}
