package com.project.pagu.modules.board.model;

import com.project.pagu.modules.board.domain.BoardSchedule;
import com.project.pagu.modules.board.domain.StudyDay;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardScheduleDto {

    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime startTime;

    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime endTime;

    private int dayKey;

    private StudyDay studyDay;

    public BoardSchedule toEntity() {
        return BoardSchedule.builder()
                .studyDay(StudyDay.of(dayKey))
                .startTime(startTime)
                .endTime(endTime)
                .build();
    }

    public static BoardScheduleDto createBoardScheduleDto(BoardSchedule boardSchedule) {
        return BoardScheduleDto.builder()
                .startTime(boardSchedule.getStartTime())
                .endTime(boardSchedule.getEndTime())
                .studyDay(boardSchedule.getStudyDay())
                .build();
    }
}
