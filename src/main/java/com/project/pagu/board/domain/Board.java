package com.project.pagu.board.domain;

import com.project.pagu.common.BaseTimeEntity;
import com.project.pagu.member.domain.Member;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Created by IntelliJ IDEA
 * User: hojun
 * Date: 2021-04-19 Time: 오후 10:57
 */
@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Board extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "study_board_id")
    private Long id;

    private String title;

    private String goal;

    private String place;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "email"),
            @JoinColumn(name = "member_type")
    })
    private Member member;

    private LocalDate recruitmentStartAt;

    private LocalDate recruitmentEndAt;

    private LocalDate termsStartAt;

    private LocalDate termsEndAt;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<BoardSchedule> boardSchedules = new ArrayList<>();

    @OneToMany(mappedBy = "board")
    private List<BoardImage> boardImages = new ArrayList<>();

    @Lob
    private String etc;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StudyStatus status;

    /**
     * 추후 해시태그 기반으로 수정
     */
    private String subjects;

    private String techStacks;

    //==연관관계 편의 메서드==//
    public void addBoardSchedule(BoardSchedule boardSchedule) {
        boardSchedules.add(boardSchedule);
        boardSchedule.setBoard(this);
    }

    private void setMember(Member member) {
        this.member = member;
        member.addBoard(this);
    }

    public void addBoardImage(BoardImage boardImage) {
        boardImages.add(boardImage);
    }
}
