package com.project.pagu.main;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by IntelliJ IDEA
 * User: yhh1056@naver.com
 * Date: 2021/03/31 Time: 5:44 오후
 */

@Controller
public class MainController {

    @GetMapping("/")
    public String mainPage() {
        /**
         * todo : 로그인 상태, 최근 게시물 10개
         */
        return "main-body";
    }

    @GetMapping("sign-up")
    public String signUp() {
        /**
         * todo : 회원가입 폼
         */
        return "sign-up";
    }
}
