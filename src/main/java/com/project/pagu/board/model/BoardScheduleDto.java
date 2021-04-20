package com.project.pagu.board.model;

import java.time.LocalTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * Created by IntelliJ IDEA
 * User: hojun
 * Date: 2021-04-20 Time: 오후 3:52
 */

@Getter
@Setter
@ToString
public class BoardScheduleDto {

    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime startTime;

    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime endTime;

    private int dayKey;
}
