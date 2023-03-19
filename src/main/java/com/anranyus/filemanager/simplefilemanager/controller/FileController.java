package com.anranyus.filemanager.simplefilemanager.controller;

import com.anranyus.filemanager.simplefilemanager.model.mFile;
import com.anranyus.filemanager.simplefilemanager.service.FileService;
import com.google.gson.Gson;
import graphql.util.Pair;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
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
    public ResponseEntity<String> getFiles(String path,String type,String order){
        if (path!=null) {
            Pair<String, ArrayList<mFile>> pair = fileService.getFiles(type, order, path);
            String data = gson.toJson(pair.second);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Parent-Path",pair.first);
            return ResponseEntity.status(HttpStatus.OK).headers(headers).body(data);
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

    @GetMapping("/api/delete")
    @ResponseBody
    public Boolean delete(String path){
        return  (fileService.deleteFile(path));
    }

    @PostMapping("/api/upload")
    @ResponseBody
    public Boolean upload(String path, MultipartFile file){
        return fileService.saveFile(path,file);
    }

}
