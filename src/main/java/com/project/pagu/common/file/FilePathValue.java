package com.project.pagu.common.file;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by IntelliJ IDEA
 * User: hojun
 * Date: 2021-04-19 Time: 오후 5:32
 */
@Component
public class FilePathValue {
    public static String PROFILE_IMAGE_PATH;

    @Value("${profile.image.filepath}")
    public void setProfileImagePath(String profileImagePath) {
        PROFILE_IMAGE_PATH = profileImagePath;
    }
}
