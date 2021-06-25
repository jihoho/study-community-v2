package com.project.pagu.domain.board.dto;

import com.project.pagu.domain.board.domain.BoardImage;
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
public class BoardImageSaveDto {

    private Long boardId;
    private String filename;
    private MultipartFile multipartFile;

    @Builder
    public BoardImageSaveDto(MultipartFile multipartFile) {
        this.multipartFile = multipartFile;
    }

    public BoardImage toEntity() {
        return BoardImage.builder()
                .filename(this.filename)
                .build();
    }
}
