package com.project.pagu.modules.teckstack;

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


}
