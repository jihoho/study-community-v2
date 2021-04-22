package com.project.pagu.tag;

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

    @Transactional
    public Subject getOrSave(SubjectForm subjectForm) {
        return subjectRepository.findByName(subjectForm.getName())
                .orElseGet(() -> subjectRepository.save(subjectForm.toEntity()));
    }

}
