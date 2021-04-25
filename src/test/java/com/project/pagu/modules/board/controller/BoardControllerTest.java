package com.project.pagu.modules.board.controller;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.project.pagu.modules.member.mockMember.WithMember;
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

    @DisplayName("게시물 전체 페이지로 이동한다.")
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

    @DisplayName("게시물 등록 페이지로 이동한다.")
    @Test
    @WithMember
    void boards_form() throws Exception {
        mockMvc.perform(get("/boards/board-form"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("boardSaveRequestDto"))
                .andExpect(view().name("boards/board-form"))
                .andExpect(content().string(containsString("스터디 모집 공고 등록")))
                .andExpect(content().string(containsString("제목")))
                .andExpect(content().string(containsString("주제")))
                .andExpect(content().string(containsString("목표")))
                .andExpect(content().string(containsString("장소")))
                .andExpect(content().string(containsString("시간")))
                .andExpect(content().string(containsString("모집 기간")))
                .andExpect(content().string(containsString("스터디 기간")))
                .andDo(print());
    }

    @DisplayName("게시물 단건 조회 페이지로 이동한다.")
    @Test
    void get_board() throws Exception {
        mockMvc.perform(get("/boards/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(view().name("boards/board-detail"))
                .andExpect(content().string(containsString("주제")))
                .andExpect(content().string(containsString("기술")))
                .andExpect(content().string(containsString("목표")))
                .andExpect(content().string(containsString("장소")))
                .andExpect(content().string(containsString("시간")))
                .andExpect(content().string(containsString("모집 기간")))
                .andExpect(content().string(containsString("스터디 기간")))
                .andDo(print());
    }

    @DisplayName("게시물 수정페이지로 이동한다.")
    @Test
    void get_board_update_form() throws Exception {
        mockMvc.perform(get("/boards/{id}/form", 1))
                .andExpect(status().isOk())
                .andExpect(view().name("boards/board-update"))
                .andExpect(content().string(containsString("스터디 모집 공고 수정")))
                .andExpect(content().string(containsString("제목")))
                .andExpect(content().string(containsString("주제")))
                .andExpect(content().string(containsString("목표")))
                .andExpect(content().string(containsString("장소")))
                .andExpect(content().string(containsString("시간")))
                .andExpect(content().string(containsString("모집 상태")))
                .andExpect(content().string(containsString("스터디 기간")))
                .andDo(print());
    }
}