package com.project.pagu.member.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by IntelliJ IDEA
 * User: hojun
 * Date: 2021-04-19 Time: 오후 4:45
 */
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
public class ProfileImageDto {
    private String email;
    private String type;
    private String filename;
    private MultipartFile multipartFile;
}
