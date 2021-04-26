package com.project.pagu.modules.board.model;

import com.project.pagu.modules.board.domain.BoardSchedule;
import com.project.pagu.modules.board.domain.StudyDay;
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

    public BoardSchedule toEntity(){
        return BoardSchedule.builder()
                .studyDay(StudyDay.of(dayKey))
                .startTime(startTime)
                .endTime(endTime)
                .build();
    }
}
