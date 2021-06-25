package com.project.pagu.domain.techstack.controller;

import com.project.pagu.domain.techstack.dto.TechStackDto;
import com.project.pagu.domain.techstack.service.TechStackService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by yhh1056
 * Date: 2021/06/02 Time: 12:55 오후
 */

@RestController
@RequiredArgsConstructor
public class TechStackController {

    private final TechStackService techStackService;

    @GetMapping("/tech-stacks")
    public List<TechStackDto> getAllTechStacks() {
        return techStackService.getAllTechStacks();
    }

}
