package com.project.pagu.modules.tag;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by IntelliJ IDEA
 * User: yhh1056@naver.com
 * Date: 2021/04/22 Time: 7:05 오후
 */
public interface SubjectRepository extends JpaRepository<Subject, Long> {

    Optional<Subject> findByName(String name);
}
