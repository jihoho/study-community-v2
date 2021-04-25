package com.project.pagu.modules.tag;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by IntelliJ IDEA
 * User: yhh1056@naver.com
 * Date: 2021/04/22 Time: 7:15 오후
 */

@Service
@RequiredArgsConstructor
public class SubjectService {

    private final SubjectRepository subjectRepository;

//    @Transactional
    public Subject getOrSave(String name) {
        return subjectRepository.findByName(name)
                .orElseGet(() -> subjectRepository.save(Subject.builder().name(name).build()));
    }

}
