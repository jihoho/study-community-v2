package com.project.pagu.domain.test;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by IntelliJ IDEA User: hojun Date: 2021-04-01 Time: 오후 7:58
 */
@Entity
@Getter @Setter
public class TestTable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;

}
