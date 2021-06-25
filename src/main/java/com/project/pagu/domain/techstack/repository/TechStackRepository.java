package com.project.pagu.domain.techstack.repository;

import com.project.pagu.domain.model.TechStack;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by IntelliJ IDEA
 * User: yhh1056@naver.com
 * Date: 2021/04/27 Time: 3:04 오후
 */
public interface TechStackRepository extends JpaRepository<TechStack, Long> {

    Optional<TechStack> findByName(String java);
}
