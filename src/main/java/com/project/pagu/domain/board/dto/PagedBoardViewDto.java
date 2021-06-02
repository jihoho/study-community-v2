package com.project.pagu.domain.board.dto;

import com.project.pagu.domain.board.domain.Board;
import com.project.pagu.domain.model.BoardSubject;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by IntelliJ IDEA
 * User: hojun
 * Date: 2021-04-27 Time: 오후 7:21
 */

@Getter
@Setter
@Builder
public class PagedBoardViewDto {

    private Long id;

    private String title;

    private String place;

    private WriterDto writerDto;

    private LocalDateTime modifiedDate;

    @Builder.Default
    private List<String> tags = new ArrayList<>();

    private String status;

    public static PagedBoardViewDto creatBoardPageDto(Board board) {

        PagedBoardViewDto pagedBoardViewDto = PagedBoardViewDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .place(board.getPlace())
                .writerDto(WriterDto.createWriterDto(board.getMember()))
                .modifiedDate(board.getModifiedDate())
                .status(board.getStatus().getStatusName()).build();
        pagedBoardViewDto.addTags(board.getBoardSubjects());
        return pagedBoardViewDto;

    }

    private void addTags(Set<BoardSubject> boardSubjects) {

        for (BoardSubject boardSubject : boardSubjects) {
            this.tags.add(boardSubject.getSubject().getName());
        }

    }


}
