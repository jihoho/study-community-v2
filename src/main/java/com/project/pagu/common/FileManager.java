package com.project.pagu.common;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;
import javax.servlet.http.HttpServletResponse;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by IntelliJ IDEA
 * User: yhh1056@naver.com
 * Date: 2021/04/18 Time: 2:22 오전
 */

@Component
public class FileManager {

    public String createFileName() {
        return UUID.randomUUID().toString() + System.nanoTime();
    }

    public void uploadProfileImage(MultipartFile uploadFile, String fileName) {
        File file = newFile(fileName);
        findDirectory(file);
        upload(uploadFile, file);
    }

    private File newFile(String fileName) {
        String absolutePath = System.getProperty("user.home");
        String filePath = FilePath.PROFILE_IMAGE.getPath() + fileName;
        return new File(absolutePath + filePath);
    }

    private void findDirectory(File file) {
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

    public void profileThumbnails(String filename, HttpServletResponse response) throws Exception {
        File image = newFile(filename);
        try (OutputStream out = response.getOutputStream()) {
            isExistImageMakeThumbnail(image, out);
            byte[] buffer = new byte[1024 * 8];
            out.write(buffer);
        }
    }

    private void isExistImageMakeThumbnail(File image, OutputStream out) throws IOException {
        if (image.exists()) {
            Thumbnails.of(image).size(200, 200).outputFormat("png").toOutputStream(out);
        }
    }
}