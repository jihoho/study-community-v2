package com.project.pagu.domain.subject.service;

import com.project.pagu.domain.subject.domain.Subject;
import com.project.pagu.domain.subject.repository.SubjectRepository;
import com.project.pagu.domain.subject.dto.SubjectDto;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    //    @Transactional
    //    public Subject getOrSave(String name) {
    //        return subjectRepository.findByName(name)
    //                .orElseGet(() -> subjectRepository.save(Subject.of(name)));
    //    }

    public List<SubjectDto> getAllSubjects() {
        List<Subject> subjects = subjectRepository.findAll();
        return subjects.stream().map(s -> SubjectDto.of(s.getName())).collect(Collectors.toList());
    }
}
