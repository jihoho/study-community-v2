package com.project.pagu.common.file;

import com.project.pagu.board.model.BoardImageDto;
import com.project.pagu.member.model.ProfileImageDto;
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

    private static final String ABSOLUTE_PATH = System.getProperty("user.home");

    public String createFileName() {
        return UUID.randomUUID().toString() + System.nanoTime();
    }

    public String creatSubPathProfile(ProfileImageDto profileImageDto) {
        return profileImageDto.getType() + File.separator + profileImageDto.getEmail()
                + File.separator + profileImageDto.getFilename();
    }

    public String creatSubPathBoardImage(BoardImageDto boardImageDto) {
        return boardImageDto.getBoardId() + File.separator + boardImageDto.getFilename();
    }

    public void uploadProfileImage(ProfileImageDto profileImageDto) {
        File file = newFileProfileImage(profileImageDto);
        findDirectory(file);
        upload(profileImageDto.getMultipartFile(), file);
    }

    public void uploadBoardImage(BoardImageDto boardImageDto) {
        File file = newFileBoardImage(boardImageDto);
        findDirectory(file);
        upload(boardImageDto.getMultipartFile(), file);
    }

    private File newFileProfileImage(ProfileImageDto profileImageDto) {
        String subPath = creatSubPathProfile(profileImageDto);
        String filePath = FilePath.PROFILE_IMAGE + subPath;
        return new File(ABSOLUTE_PATH + filePath);
    }

    private File newFileBoardImage(BoardImageDto boardImageDto) {
        String subPath = creatSubPathBoardImage(boardImageDto);
        String filePath = FilePath.BOARD_IMAGE + subPath;
        return new File(ABSOLUTE_PATH + filePath);
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

    public void profileThumbnails(ProfileImageDto profileImageDto, HttpServletResponse response)
            throws Exception {
        File image = newFileProfileImage(profileImageDto);
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