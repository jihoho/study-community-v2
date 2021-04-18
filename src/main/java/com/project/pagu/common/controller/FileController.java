package com.project.pagu.common.controller;

import com.project.pagu.common.FileManager;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Created by IntelliJ IDEA
 * User: yhh1056@naver.com
 * Date: 2021/04/18 Time: 8:18 오후
 */

@Controller
@RequiredArgsConstructor
public class FileController {

    private final FileManager fileManager;

    @GetMapping("/profileThumbnails/{filename}")
    public void profileThumbnails(@PathVariable String filename, HttpServletResponse response) throws Exception {
        fileManager.profileThumbnails(filename, response);
    }
}
