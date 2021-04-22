package com.project.pagu.board.model;

import com.project.pagu.board.domain.Board;
import com.project.pagu.board.domain.BoardImage;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by IntelliJ IDEA
 * User: hojun
 * Date: 2021-04-21 Time: 오전 12:05
 */

@Getter
@Setter
public class BoardImageDto {

    private Long boardId;
    private String filename;
    private MultipartFile multipartFile;

    @Builder
    public BoardImageDto(MultipartFile multipartFile) {
        this.multipartFile = multipartFile;
    }

    public BoardImage toEntity(Board board) {
        return BoardImage.builder()
                .filename(this.filename)
                .board(board)
                .build();
    }
}
