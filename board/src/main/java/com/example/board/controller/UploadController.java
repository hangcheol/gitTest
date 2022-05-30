package com.example.board.controller;

import com.example.board.dto.UploadResultDTO;
import com.example.board.entity.AttachFile;
import com.fasterxml.jackson.databind.util.JSONPObject;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnailator;
import nonapi.io.github.classgraph.utils.FileUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Slf4j
@RestController
public class UploadController {

    @Value("${com.example.upload.path}")
    private String uploadPath;

    @PostMapping("/uploadAjax")
    public ResponseEntity<List<UploadResultDTO>> uploadFile(MultipartFile[] uploadFiles) {

        List<UploadResultDTO> resultDTOList = new ArrayList<>();

        for (MultipartFile uploadFile: uploadFiles) {
            
            // 이미지 파일 아닌 경우 처리
            if (uploadFile.getContentType().startsWith("image") == false) {
                log.warn("이미지 파일이 아닙니다.");
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }

            // 파일명
            String originalName = uploadFile.getOriginalFilename();
            String fileName = originalName.substring(originalName.lastIndexOf("\\") + 1);
            
            log.info("filename: {}", fileName);

            // 폴더 지정(없는경우 생성)
            String folderPath = makeFolder();

            log.info("uploadPath: {}", uploadPath);
            log.info("File.separator: {}", File.separator);
            log.info("folderpath: {}", folderPath);

            String uuid = UUID.randomUUID().toString();

            String saveName = uploadPath + File.separator + folderPath + File.separator + uuid + "_" + fileName;
            Path savePath = Paths.get(saveName);

            log.info("savePath: {}",savePath);

            try {
                uploadFile.transferTo(savePath); // 파일 저장

                // 썸네일 파일명
                String thumbnailSaveName = uploadPath + File.separator + folderPath + File.separator + "s_" + uuid + "_" + fileName;
                File thumbnailFile = new File(thumbnailSaveName);

                // 썸네일 생성 100 * 100
                Thumbnailator.createThumbnail(savePath.toFile(), thumbnailFile, 100, 100);

                resultDTOList.add(new UploadResultDTO(fileName,uuid,folderPath));

            } catch (IOException e) {

                e.printStackTrace();
                log.error("에러 발생");
            }

        } // for 문 종료
        log.info(String.valueOf(resultDTOList));
        return new ResponseEntity<>(resultDTOList, HttpStatus.OK);
    }

    private String makeFolder() {
        String str = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String folderPath = str.replace("//", File.separator);

        log.info("folderPath: {}", folderPath);

        File uploadPathFolder = new File(uploadPath, folderPath);

        if (uploadPathFolder.exists() == false) {
            uploadPathFolder.mkdirs();
        }

        return folderPath;
    }

    @GetMapping("/display")
    public ResponseEntity<byte[]> getFile(String fileName, String size) {

        ResponseEntity<byte[]> result = null;

        try {
            String srcFileName = URLDecoder.decode(fileName, "UTF-8");
            log.info("fileName: {}", srcFileName);

            File file = new File(uploadPath + File.separator + srcFileName);
            
            if (size != null && size.equals("1")) {
                file = new File(file.getParent(), file.getName().substring(2));
            }
            
            log.info("file: {}", file);
            log.info(String.valueOf(file.toPath()));

            HttpHeaders header = new HttpHeaders();

            header.add("Content-Type", Files.probeContentType(file.toPath()));

            result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), header, HttpStatus.OK);
        } catch (Exception e) {
            log.error("에러 확인: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return result;
    }

    @PostMapping("/removeFile")
    public ResponseEntity<Boolean> removeFile(String fileName) {

        String srcFileName = null;
        try {
            srcFileName = URLDecoder.decode(fileName, "UTF-8");
            File file = new File(uploadPath + File.separator + srcFileName);
            boolean result= file.delete();

            File thumbnail = new File(file.getParent(), "s_" + file.getName());
            result = thumbnail.delete();

            return new ResponseEntity<>(result, HttpStatus.OK);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value="resources/summerimages")
    public ResponseEntity<?> summerImage(@RequestParam("file") MultipartFile img, HttpServletRequest request) throws IOException {

//        String path = request.getServletContext().getRealPath("resources/summerimages");
        String path = uploadPath;
        Random random = new Random();

        long currentTime = System.currentTimeMillis();
        int	randomValue = random.nextInt(100);
        String fileName = Long.toString(currentTime) + "_"+randomValue+"_a_"+img.getOriginalFilename();

        File file = new File(path , fileName);
//        Path savePath = Paths.get(saveName);
//        uploadFile.transferTo(savePath);
//
//        Path filePath = Paths.get(String.valueOf(file));
        File pathTest = new File(path);
        pathTest.mkdirs();
//        img.transferTo(file);

        try {
            img.transferTo(file); // 파일 저장

            // 썸네일 파일명
            String thumbnailSaveName = "s_" + fileName;
            File thumbnailFile = new File(path, thumbnailSaveName);

            Path filePath = Paths.get(String.valueOf(file));
            // 썸네일 생성 100 * 100
            Thumbnailator.createThumbnail(filePath.toFile(), thumbnailFile, 100, 100);

        } catch (IOException e) {

            e.printStackTrace();
            log.error("에러 발생");
        }

        return ResponseEntity.ok().body("/display?fileName=" + fileName);
//        return ResponseEntity.ok().body(uploadPath+File.separator+fileName);

    }

}
