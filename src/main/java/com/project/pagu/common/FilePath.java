package com.project.pagu.common;

import lombok.RequiredArgsConstructor;

/**
 * Created by IntelliJ IDEA
 * User: yhh1056@naver.com
 * Date: 2021/04/18 Time: 8:34 오후
 */

@RequiredArgsConstructor
public enum FilePath {
    PROFILE_IMAGE("/Desktop/pagu/profile_image/");

    private final String path;

    public String getPath() {
        return this.path;
    }
}
