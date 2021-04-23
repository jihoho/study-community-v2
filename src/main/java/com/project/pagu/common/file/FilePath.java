package com.project.pagu.common.file;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by IntelliJ IDEA
 * User: hojun
 * Date: 2021-04-19 Time: 오후 5:32
 */

public class FilePath {

    @Value("${profile.image.filepath}")
    private static String PROFILE_IMAGE_PATH;

    @Value("${board.image.filepath}")
    private static String BOARD_IMAGE_PATH;

    public static final String PROFILE_IMAGE = PROFILE_IMAGE_PATH;
    public static final String BOARD_IMAGE = BOARD_IMAGE_PATH;
}
