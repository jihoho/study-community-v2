package com.project.pagu.modules.board.model;

import com.project.pagu.modules.board.domain.Board;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by IntelliJ IDEA
 * User: hojun
 * Date: 2021-04-28 Time: 오후 3:34
 */

@Getter
@Setter
@Builder
public class LatestBoardDto {

    private Long id;

    private String title;

    private WriterDto writer;

    private LocalDateTime modifiedDate;


    public static LatestBoardDto createLatestBoardDto(Board board) {

        return LatestBoardDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .writer(WriterDto.createWriterDto(board.getMember()))
                .modifiedDate(board.getModifiedDate()).build();

    }
}
