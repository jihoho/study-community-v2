package com.project.pagu;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA
 * User: hojun
 * Date: 2021-03-29 Time: 오후 3:38
 */
@Controller
public class ViewController {

    @GetMapping("/main-body")
    public ModelAndView mainBody(HttpServletRequest request, HttpServletResponse response){
        ModelAndView mav=new ModelAndView("main-body");
        System.out.println("main-body");
        return mav;
    }

    @GetMapping("/board-list")
    public ModelAndView boardList(){
        ModelAndView mav=new ModelAndView("board-list");
        return mav;
    }

}
