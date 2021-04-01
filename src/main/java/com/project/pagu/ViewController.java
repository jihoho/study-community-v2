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

    @GetMapping("/board-form")
    public ModelAndView boardForm(){
        ModelAndView mav= new ModelAndView("board-form");
        return mav;
    }

    @GetMapping("/board-detail")
    public ModelAndView boardDetail(){
        ModelAndView mav=new ModelAndView("board-detail");
        return mav;
    }
    @GetMapping("/board-update")
    public ModelAndView boardUpdate(){
        ModelAndView mav=new ModelAndView("board-update");
        return mav;
    }
}
