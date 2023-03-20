package com.anranyus.filemanager.simplefilemanager.controller;

import com.anranyus.filemanager.simplefilemanager.Callback;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
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
    public String upload(String path, MultipartFile file){
        try {
            return fileService.saveFile(path,file).toString();
        } catch (IOException e) {
            return e.getMessage();
        }
    }

    /**
     *
     * @param list 其结构为{"list":[mFile1,mFile2]}
     * @return 若存在删除失败的情况 则返回false
     */
    @PostMapping("/api/deleteFiles")
    @ResponseBody
    public String deleteFiles(@RequestBody String list){

        Data data = gson.fromJson(list,Data.class);
        ArrayList<String> result = new ArrayList<>();
        if (data != null){
             result =  fileService.deleteFiles(data.list);
        }else {
            logger.log(Level.SEVERE,"Null request");
        }

        //存在错误则返回错误的条目
        if (result.size()>0){
            return gson.toJson(new Callback(HttpStatus.BAD_REQUEST,gson.toJson(result)));
        }

        return gson.toJson(new Callback(HttpStatus.OK,"Delete completed"));

    }

    //需要临时创建一个类来解析json中的list对象内的数据，这是应该最方便的实现
    private class Data {

        public Data(List<mFile> list) {
            this.list = list;
        }

        private List<mFile> list;

        public List<mFile> getList() {
            return list;
        }

        public void setList(List<mFile> list) {
            this.list = list;
        }
    }


}
