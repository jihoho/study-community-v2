package com.project.pagu.common.manager;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.http.HttpServletResponse;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by IntelliJ IDEA
 * User: yhh1056@naver.com
 * Date: 2021/04/18 Time: 2:22 오전
 */

@Component
public class FileManager {

    @Value("${profile.image.filepath}")
    private String profilePath;

    @Value("${board.image.filepath}")
    private String boardPath;

    private static final String ABSOLUTE_PATH = System.getProperty("user.home");

    public void uploadProfileImage(MultipartFile multipartFile, String filename, String... paths) {
        String fullPath = profilePath + FileUtil.createSubPath(filename, paths);
        uploadImage(multipartFile, fullPath);
    }

    private void uploadImage(MultipartFile multipartFile, String fullPath) {
        File file = newFile(fullPath);
        isExistedDirectory(file);
        upload(multipartFile, file);
    }

    private File newFile(String path) {
        return new File(ABSOLUTE_PATH + path);
    }

    private void isExistedDirectory(File file) {
        if (!file.exists()) {
            file.getParentFile().mkdirs();
        }
    }

    private void upload(MultipartFile uploadFile, File file) {
        try {
            uploadFile.transferTo(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void uploadBoardImage(MultipartFile multipartFile, String filename, String... paths) {
        String fullPath = boardPath + FileUtil.createSubPath(filename, paths);
        uploadImage(multipartFile, fullPath);
    }

    public void profileThumbnails(HttpServletResponse response, String filename, String... paths) throws Exception {
        File image = newFile(profilePath + FileUtil.createSubPath(filename, paths));
        try (OutputStream out = response.getOutputStream()) {
            isExistImageMakeThumbnail(image, out);
            byte[] buffer = new byte[1024 * 12];
            out.write(buffer);
        }
    }

    private void isExistImageMakeThumbnail(File image, OutputStream out) throws IOException {
        if (image.exists()) {
            Thumbnails.of(image).size(200, 200).outputFormat("png").toOutputStream(out);
        }
    }
}