package com.project.pagu.modules.board.model;

import com.project.pagu.modules.board.domain.Board;
import com.project.pagu.modules.board.domain.StudyStatus;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by IntelliJ IDEA
 * User: hojun
 * Date: 2021-04-20 Time: 오후 3:38
 */

@Getter
@Setter
@ToString
public class BoardSaveRequestDto {

    @NotBlank
    @Size(max = 50, message = "50글자 이내만 가능 합니다.")
    private String title;

    @NotBlank
    private String subjects;

    @NotBlank
    private String techStacks;

    @NotBlank
    @Size(max = 50, message = "50글자 이내만 가능 합니다.")
    private String goal;

    @NotBlank
    @Size(max = 50, message = "50글자 이내만 가능 합니다.")
    private String place;

    @NotEmpty(message = "스터디 일정을 등록해주세요.")
    private List<BoardScheduleDto> boardSchedules = new ArrayList<>();

    @NotNull(message = "모집 시작 기간을 등록해주세요.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate recruitmentStartAt;

    @NotNull(message = "모집 종료 기간을 등록해주세요.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate recruitmentEndAt;

    @NotNull(message = "스터디 시작 기간을 등록해주세요.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate termsStartAt;

    @NotNull(message = "스터디 종료 기간을 등록해주세요.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate termsEndAt;

    private String etc;

    private List<MultipartFile> fileList = new ArrayList<>();

    public Board toEntity() {
        return Board.builder()
                .title(this.title)
                .goal(this.goal)
                .place(this.place)
                .recruitmentStartAt(this.recruitmentStartAt)
                .recruitmentEndAt(this.recruitmentEndAt)
                .termsStartAt(this.termsStartAt)
                .termsEndAt(this.termsEndAt)
                .etc(this.etc)
                .status(StudyStatus.READY)
                .subjects(this.subjects)
                .techStacks(this.techStacks)
                .build();
    }

    public List<BoardImageDto> toBoardImageDtoList() {
        List<BoardImageDto> boardImageDtos = new ArrayList<>();
        for (MultipartFile multipartFile : fileList) {
            boardImageDtos.add(new BoardImageDto(multipartFile));
        }
        return boardImageDtos;
    }

}
