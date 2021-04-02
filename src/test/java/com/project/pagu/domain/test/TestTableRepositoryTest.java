package com.project.pagu.domain.test;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by IntelliJ IDEA
 * User: hojun
 * Date: 2021-04-02 Time: 오후 12:10
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
class TestTableRepositoryTest {
    @Autowired
    TestTableRepository testTableRepository;

    @Test
    @Transactional
    @DisplayName("JPA 연동 테스트")
    public void testJpa() throws Exception{
        // given
        TestTable testTable=new TestTable();
        testTable.setUsername("testA");
        // when
        Long savedId=testTableRepository.save(testTable);
        TestTable findTestTable = testTableRepository.find(savedId);


        // then
        Assertions.assertThat(findTestTable.getId()).isEqualTo(testTable.getId());
        Assertions.assertThat(findTestTable.getUsername()).isEqualTo(findTestTable.getUsername());
        Assertions.assertThat(findTestTable).isEqualTo(testTable);

    }

}