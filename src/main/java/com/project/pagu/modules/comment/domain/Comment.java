package com.project.pagu.modules.comment.domain;

import static javax.persistence.FetchType.LAZY;

import com.project.pagu.common.domain.BaseTimeEntity;
import com.project.pagu.modules.board.domain.Board;
import com.project.pagu.modules.member.domain.Member;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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
 * Date: 2021-04-30 Time: 오후 2:39
 */

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "study_board_id")
    private Board board;

    @Lob
    private String content;

    @Lob
    private String backupContent;

    @ManyToOne(fetch = LAZY)
    @JoinColumns({
            @JoinColumn(name = "email"),
            @JoinColumn(name = "member_type")
    })
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "super_comment_id")
    private Comment superComment;


    @OneToMany(mappedBy = "superComment", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Comment> subComment = new ArrayList<>();

    private int depth = 0;

    private boolean isRemove = false;

    public void remove() {
        if (isRemove == false) {
            this.backupContent = content;
            this.content = "삭제된 댓글 입니다.";
            this.isRemove = true;
        }
    }

    public void updateContent(String content) {
        this.content = content;
    }

    //==연관관계 편의 메서드==//
    public void registerMember(Member findMember) {
        this.member = findMember;
        findMember.addComment(this);
    }

    public void registerBoard(Board board) {
        this.board = board;
        board.addComment(this);
    }

    public void registerSuperComment(Comment superComment) {
        this.superComment = superComment;
        this.depth = superComment.getDepth() + 1;
        superComment.addSubComment(this);
    }

    private void addSubComment(Comment comment) {
        this.subComment.add(comment);
    }
}
