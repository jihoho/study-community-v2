package com.project.pagu.board.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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

    private String title;
    private String subjects;
    private String teckStacks;
    private String goal;
    private String place;

    private List<BoardScheduleDto> boardSchedules = new ArrayList<>();

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate recruitmentStartAt;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate recruitmentEndAt;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate termsStartAt;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate termsEndAt;

    private String etc;

    private List<MultipartFile> fileList = new ArrayList<>();


}
