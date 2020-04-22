package com.crud.controller;

import com.crud.config.FileUploadResponse;
import com.crud.service.FileStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class FileController {
    private static final Logger logger = LoggerFactory.getLogger(FileController.class);
    public ArrayList<String> arrayList = new ArrayList<String>();
    @Autowired
    private FileStorageService fileStorageService;

    @CrossOrigin
    @PostMapping("/uploadFile")
    public FileUploadResponse uploadFile(@RequestPart("file") MultipartFile file) {
        String fileName = fileStorageService.storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(fileName)
                .toUriString();
        arrayList.add(fileName);
//        return new UploadFileResponse(fileName, fileDownloadUri,
//                file.getContentType(), file.getSize());

        return new FileUploadResponse(fileDownloadUri);
    }

    @CrossOrigin
    @PostMapping("/uploadMultipleFiles")
    public List<FileUploadResponse> uploadMultipleFiles(@RequestPart("files") MultipartFile[] files) {
        List<FileUploadResponse> list = Arrays.asList(files)
                .stream()
                .map(file -> uploadFile(file))
                .collect(Collectors.toList());
        return list;
    }

    @CrossOrigin
    @GetMapping("/downloadFile/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        // Load file as Resource
        Resource resource = fileStorageService.loadFileAsResource(fileName);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

}