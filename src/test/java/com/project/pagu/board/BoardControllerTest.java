package com.project.pagu.board;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Created by IntelliJ IDEA
 * User: yhh1056@naver.com
 * Date: 2021/04/01 Time: 1:54 오후
 */

@SpringBootTest
@AutoConfigureMockMvc
class BoardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @DisplayName("메인페이지로 이동한다.")
    @Test
    void move_main_page() throws Exception {
        mockMvc.perform(get("/boards"))
                .andExpect(status().isOk())
                .andExpect(view().name("boards/board-list"))
                .andExpect(content().string(containsString("스터디 등록하기")))
                .andExpect(content().string(containsString("PAGU")))
                .andExpect(content().string(containsString("no")))
                .andExpect(content().string(containsString("title")))
                .andExpect(content().string(containsString("location")))
                .andExpect(content().string(containsString("writer")))
                .andExpect(content().string(containsString("reg date")))
                .andExpect(content().string(containsString("tag")))
                .andExpect(content().string(containsString("status")))
                .andDo(print());
    }
}