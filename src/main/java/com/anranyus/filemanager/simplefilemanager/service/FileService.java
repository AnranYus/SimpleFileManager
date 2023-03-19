package com.anranyus.filemanager.simplefilemanager.service;

import com.anranyus.filemanager.simplefilemanager.exception.NullPathException;
import com.anranyus.filemanager.simplefilemanager.model.mFile;
import com.anranyus.filemanager.simplefilemanager.utils.FileOrder;
import graphql.util.Pair;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Logger;


@Service
public class FileService {

    Logger logger = Logger.getLogger(this.getClass().getSimpleName());

    public static String rootPath;

    public FileService(Environment env) throws NullPathException {
        //定义根目录
        rootPath = env.getProperty("rootpath");
        if (rootPath==null){
            throw new NullPathException();
        }

    }

    //遍历目录,以map形式返回文件和文件夹列表

    public ArrayList<mFile> traverseFile(String path){
        if (path.equals("/")||path.equals("")){
            //若路径为空则当访问根目录
            if (rootPath!=null){
                path = rootPath;
            }
        }else {
            path = rootPath+path;
        }
        ArrayList<mFile> files = new ArrayList<>();
        ArrayList<mFile> dirs = new ArrayList<>();

        File file = new File(path);
        File[] fileList = file.listFiles();
        if (fileList != null) {
            for (File item : fileList){
                mFile mFile;

                //格式化最后修改时间
                Date date = new Date(item.lastModified());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String formattedDate = sdf.format(date);

                String parent = file.getParent();
                if (parent==null){
                    parent = rootPath;
                }
                //求出相对根路径的地址
                String itemPath = item.getAbsolutePath();
                String relativePath = itemPath.substring(itemPath.indexOf(rootPath)+rootPath.length()-1);

                mFile = new mFile(item.getName(),relativePath,item.length(),formattedDate,null,parent);

                if (item.isFile()){
                    mFile.setIsFile(true);
                    files.add(mFile);
                }else {
                    mFile.setIsFile(false);
                    dirs.add(mFile);
                }
            }

            dirs.addAll(files);

        }
        return dirs;
    }

    /**
     *
     * @param type 排序类型
     * @param order 排序方式
     * @param path 请求路径
     * @return Pair(父路径,当前路径的文件列表)
     */
    public Pair<String,ArrayList<mFile>> getFiles(String type,String order,String path){
        ArrayList<mFile> list =  traverseFile(path);
        Pair<String,ArrayList<mFile>> pair;
        String parentPath = getParentPath(path);
        if (type!=null&&order!=null) {
            switch (type) {
                case "S": {
                    if (order.equals("A")) {
                        list = FileOrder.AscendOrderBySize(list);
                    } else {
                        list = FileOrder.DescendOrderBySize(list);
                    }
                }
                case "D": {
                    //TODO 按时间排序
                }
                default:
                    pair = new Pair<>(parentPath,list);
                    return pair;
            }
        }else {
            return new Pair<>(parentPath,list);
        }

    }

    public String getParentPath(String nowPath){
        String parent =  new File(nowPath).getParent();
        if (parent==null|| nowPath.equals(FileService.rootPath)){
            //没有父路径则要求请求根目录
            parent = "";
        }
        return parent;
    }

    public Boolean deleteFile(String path){
        path = rootPath+path;
        File file = new File(path);
        if (file.exists()){
            return file.delete();
        }else {
            return false;
        }
    }

    /**
     *
     * @param path 要储存的文件路径
     * @param file 储存的文件
     * @return 储存成功与否
     */

    public Boolean saveFile(String path, MultipartFile file){
        if (path.equals("/")){
            path = rootPath;
        }
        path =rootPath + path +File.separator;
        logger.warning(path);
        if (!file.isEmpty()){
            try {
                byte[] bytes = file.getBytes();
                String savePath = path+file.getOriginalFilename();
                Files.write(Path.of(savePath),bytes);
                logger.warning("File save in "+savePath);
                return true;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }else {
            return false;
        }
    }


}
