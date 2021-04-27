package com.project.pagu.modules.board.domain;

import static javax.persistence.FetchType.LAZY;

import com.project.pagu.common.domain.BaseTimeEntity;
import com.project.pagu.modules.member.domain.Member;
import com.project.pagu.modules.tag.BoardSubject;
import com.project.pagu.modules.teckstack.BoardTechStack;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_board_id")
    private Long id;

    private String title;

    private String goal;

    private String place;

    @ManyToOne(fetch = LAZY)
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
    @Builder.Default
    private List<BoardSchedule> boardSchedules = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    @Builder.Default
    private List<BoardImage> boardImages = new ArrayList<>();

    @Lob
    private String etc;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StudyStatus status;

    /**
     * 추후 해시태그 기반으로 수정
     */
    @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL)
    @Builder.Default
    private Set<BoardSubject> boardSubjects = new HashSet<>();

    @OneToMany(mappedBy = "techStack", cascade = CascadeType.ALL)
    @Builder.Default
    private Set<BoardTechStack> boardTechStacks = new HashSet<>();

    //==연관관계 편의 메서드==//
    public void addBoardScheduleList(List<BoardSchedule> boardSchedules) {
        for (BoardSchedule boardSchedule : boardSchedules) {
            addBoardSchedule(boardSchedule);
        }
    }

    public void addBoardSchedule(BoardSchedule boardSchedule) {
        this.boardSchedules.add(boardSchedule);
        boardSchedule.setBoard(this);
    }

    public void setMember(Member member) {
        this.member = member;
        member.addBoard(this);
    }

    public void addBoardImageList(List<BoardImage> boardImageList) {
        for (BoardImage boardImage : boardImageList) {
            addBoardImage(boardImage);
        }
    }

    private void addBoardImage(BoardImage boardImage) {
        this.boardImages.add(boardImage);
        boardImage.setBoard(this);
    }

    public void addSubject(BoardSubject... boardSubjects) {
        for (BoardSubject boardSubject : boardSubjects) {
            this.boardSubjects.add(boardSubject);
            boardSubject.addBoard(this);
        }
    }

    public void addTechStack(BoardTechStack... boardTechStacks) {
        for (BoardTechStack boardTechStack : boardTechStacks) {
            this.boardTechStacks.add(boardTechStack);
            boardTechStack.addBoard(this);
        }
    }

}
