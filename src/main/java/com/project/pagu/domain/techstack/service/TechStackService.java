package com.project.pagu.domain.techstack.service;

import com.project.pagu.domain.model.TechStack;
import com.project.pagu.domain.techstack.repository.TechStackRepository;
import com.project.pagu.domain.techstack.dto.TechStackDto;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Created by IntelliJ IDEA
 * User: yhh1056@naver.com
 * Date: 2021/04/27 Time: 3:32 오후
 */

@Service
@RequiredArgsConstructor
public class TechStackService {

    private final TechStackRepository techStackRepository;

    //    @Transactional
    public TechStack getOrSave(String name) {
        return techStackRepository.findByName(name)
                .orElseGet(() -> techStackRepository.save(TechStack.of(name)));
    }

    public List<TechStackDto> getAllTechStacks() {
        List<TechStack> techStacks = techStackRepository.findAll();
        return techStacks.stream().map(t -> TechStackDto.of(t.getName()))
                .collect(Collectors.toList());
    }


}
