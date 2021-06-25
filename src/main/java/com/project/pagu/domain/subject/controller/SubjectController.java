package com.project.pagu.domain.subject.controller;

import com.project.pagu.domain.subject.service.SubjectService;
import com.project.pagu.domain.subject.dto.SubjectDto;
import com.project.pagu.domain.techstack.dto.TechStackDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by IntelliJ IDEA
 * User: hojun
 * Date: 2021-04-29 Time: 오전 12:13
 */

@RestController
@RequiredArgsConstructor
public class SubjectController {

    private final SubjectService subjectService;

    @GetMapping("/subjects")
    public List<SubjectDto> getAllSubjects() {
        return subjectService.getAllSubjects();
    }

}
