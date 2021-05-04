package com.project.pagu.common.manager;

import com.project.pagu.modules.board.domain.BoardImage;
import com.project.pagu.modules.board.model.BoardImageSaveDto;
import com.project.pagu.modules.board.model.BoardSaveDto;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
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

    public List<BoardImage> uploadBoardImageDtos(Long boardId, BoardSaveDto boardSaveDto) {
        List<BoardImage> boardImageList = new ArrayList<>();
        List<BoardImageSaveDto> boardImageSaveDtoList = boardSaveDto.toBoardImageDtoList();
        for (BoardImageSaveDto boardImageSaveDto : boardImageSaveDtoList) {
            boardImageSaveDto.setBoardId(boardId);
            uploadBoardImageDto(boardImageSaveDto);
            boardImageList.add(boardImageSaveDto.toEntity());
        }
        return boardImageList;
    }

    public void uploadProfileImage(MultipartFile multipartFile, String filename, String... paths) {
        String fullPath = profilePath + FileUtil.createSubPath(filename, paths);
        uploadImage(multipartFile, fullPath);
    }

    private void uploadBoardImageDto(BoardImageSaveDto boardImageSaveDto) {

        if (boardImageSaveDto.getMultipartFile().getSize() != 0) {
            String filename = FileUtil.createFileName();
            boardImageSaveDto.setFilename(filename);
            uploadBoardImage(boardImageSaveDto.getMultipartFile(), filename,
                    String.valueOf(boardImageSaveDto.getBoardId()));
        }

    }

    private void uploadBoardImage(MultipartFile multipartFile, String filename, String... paths) {
        String fullPath = boardPath + FileUtil.createSubPath(filename, paths);
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


    public void profileThumbnails(HttpServletResponse response, String filename, String... paths)
            throws Exception {
        File image = newFile(profilePath + FileUtil.createSubPath(filename, paths));
        try (OutputStream out = response.getOutputStream()) {
            isExistImageMakeThumbnail(image, out);
            byte[] buffer = new byte[1024 * 12];
            out.write(buffer);
        }
    }

    public void boardThumbnails(HttpServletResponse response, String filename, String... paths)
            throws Exception {
        File image = newFile(boardPath + FileUtil.createSubPath(filename, paths));
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