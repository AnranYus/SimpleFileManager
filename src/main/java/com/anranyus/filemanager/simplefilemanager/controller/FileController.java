package com.anranyus.filemanager.simplefilemanager.controller;

import com.anranyus.filemanager.simplefilemanager.service.FileService;
import com.google.gson.Gson;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.logging.Logger;

@Controller
public class FileController {
    final
    FileService fileService;
    Logger logger = Logger.getLogger(this.getClass().getSimpleName());
    Gson gson = new Gson();
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("/api/getFiles")
    @ResponseBody
    public String getFiles(String path,String type,String order){
        if (path!=null) {
            logger.warning(path);
            return gson.toJson(fileService.getFiles(type, order, path));
        }else {
            return null;
        }
    }

    @GetMapping("/api/download")
    public ResponseEntity<Resource> download(String path) throws IOException {
        Resource resource = new FileSystemResource(path);
        return ResponseEntity.ok()
                .contentLength(resource.contentLength())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);

    }

}
