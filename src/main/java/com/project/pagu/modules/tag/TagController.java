package com.project.pagu.modules.tag;

import com.project.pagu.modules.teckstack.TechStackDto;
import com.project.pagu.modules.teckstack.TechStackService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by IntelliJ IDEA
 * User: hojun
 * Date: 2021-04-29 Time: 오전 12:13
 */

@RestController
@RequiredArgsConstructor
public class TagController {

    private final SubjectService subjectService;
    private final TechStackService techStackService;

    @GetMapping("/subjects")
    public List<SubjectDto> getAllSubjects() {
        return subjectService.getAllSubjects();
    }

    @GetMapping("/tech-stacks")
    public List<TechStackDto> getAllTechStacks() {
        return techStackService.getAllTechStacks();
    }

}
