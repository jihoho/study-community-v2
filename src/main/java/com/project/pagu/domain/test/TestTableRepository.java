package com.project.pagu.domain.test;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by IntelliJ IDEA User: hojun Date: 2021-04-01 Time: 오후 7:59
 */
@Repository
public class TestTableRepository {

    @PersistenceContext
    private EntityManager em;

    public Long save(TestTable testTable) {
        em.persist(testTable);
        return testTable.getId();
    }

    public TestTable find(Long id) {
        return em.find(TestTable.class, id);
    }

}
