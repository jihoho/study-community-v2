package com.project.pagu.global.util;

import java.io.File;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Created by IntelliJ IDEA
 * User: hojun
 * Date: 2021-04-27 Time: 오전 12:00
 */

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileUtil {

    private static final String IMAGE_URL = "/profile/thumbnails";

    public static String createFileName() {
        return UUID.randomUUID().toString() + System.nanoTime();
    }

    public static String createSubPath(String filename, String... paths) {
        return Arrays.stream(paths).map(path -> path + File.separator)
                .collect(Collectors.joining()) + filename;
    }

    public static String createImageUrl(String... paths) {
        return IMAGE_URL + Arrays.stream(paths).map(path -> "/" + path)
                .collect(Collectors.joining());
    }
}
