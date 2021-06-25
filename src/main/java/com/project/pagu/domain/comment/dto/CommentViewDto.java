package com.project.pagu.domain.comment.dto;

import com.project.pagu.domain.comment.domain.Comment;
import com.project.pagu.domain.member.domain.Member;
import com.project.pagu.domain.model.MemberType;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by IntelliJ IDEA
 * User: hojun
 * Date: 2021-04-30 Time: 오후 4:35
 */

@Getter
@Setter
@AllArgsConstructor
@Builder
public class CommentViewDto {

    private Long boardId;

    private Long commentId;

    private String email;

    private MemberType memberType;

    private String nickname;

    private String imageUrl;

    private String content;

    private boolean isRemoved;

    private LocalDateTime modifiedDate;

    @Builder.Default
    private List<CommentViewDto> subComment = new ArrayList<>();

    public static CommentViewDto createCommentResponseDto(Comment comment) {
        Member member = comment.getMember();
        return CommentViewDto.builder()
                .boardId(comment.getBoard().getId())
                .commentId(comment.getId())
                .email(member.getEmail())
                .memberType(member.getMemberType())
                .nickname(member.getNickname())
                .imageUrl(member.getProfileImageUrl())
                .content(comment.getContent())
                .isRemoved(comment.isRemove())
                .modifiedDate(comment.getModifiedDate()).build();
    }
}
