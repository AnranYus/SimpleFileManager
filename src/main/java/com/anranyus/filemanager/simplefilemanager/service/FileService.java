package com.anranyus.filemanager.simplefilemanager.service;

import com.anranyus.filemanager.simplefilemanager.model.mFile;
import com.anranyus.filemanager.simplefilemanager.utils.FileOrder;
import graphql.util.Pair;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;


@Service
public class FileService {

    Logger logger = Logger.getLogger(this.getClass().getSimpleName());
    public static String rootPath;
    public FileService(Environment env) {
        rootPath = env.getProperty("rootpath");
        if (rootPath!=null) {
            if (!rootPath.endsWith(File.separator)) {
                rootPath = rootPath + File.separator;
            }
        }
    }

    //遍历目录,以map形式返回文件和文件夹列表

    public ArrayList<mFile> traverseFile(String path){
        logger.warning(rootPath);
        if (path.equals("/")||path.equals("")){
            //若路径为空则当访问根目录
            if (rootPath !=null){
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
                String relativePath = itemPath.substring(itemPath.indexOf(rootPath)+rootPath.length());

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
        if (parent==null|| nowPath.equals(rootPath)){
            //没有父路径则要求请求根目录
            parent = "";
        }
        return parent;
    }

    public Boolean deleteFile(String path){
        path = rootPath+path;
        File file = new File(path);
        if (file.exists()){
            return delete(file);
        }else {
            return false;
        }
    }

    public Boolean delete(File file) {
        if (file.isDirectory()) {
            boolean result = true;
            // 遍历文件夹中的所有文件和子文件夹
            for (File subFile : Objects.requireNonNull(file.listFiles())) {
                // 如果是文件，则直接删除
                if (subFile.isFile()) {
                    if (!subFile.delete()){
                        //有文件删除失败则返回false
                        String info = "Delete file "+subFile.getPath()+" fail!";
                        logger.log(Level.SEVERE,info);
                        result = false;
                    }
                }
                // 如果是文件夹，则递归调用删除函数
                else {
                    delete(subFile);
                }
            }

            // 删除空文件夹
            file.delete();
            return result;
        }else {
            return file.delete();
        }
    }

    /**
     *
     * @param path 要储存的文件路径
     * @param file 储存的文件
     * @return 储存成功与否
     */

    public Boolean saveFile(String path, MultipartFile file) throws IOException {
        if (path.equals("/")){
            path = rootPath;
        }
        path =rootPath + path +File.separator;
        logger.warning(path);
        if (!file.isEmpty()){
            byte[] bytes = file.getBytes();
            String savePath = path+file.getOriginalFilename();
            Files.write(Path.of(savePath),bytes);
            logger.warning("File save in "+savePath);
            return true;

        }else {
            return false;
        }
    }

    /**
     *
     * @param files 删除的文件列表
     * @return 存在错误的条目
     */
    public ArrayList<String> deleteFiles(List<mFile> files){
        ArrayList<String> infoList = new ArrayList<>();
        for (mFile file : files) {
            String info;
            if (!deleteFile(file.getPath())) {
                info = "Delete file: "+file.getPath()+" fail!";
                logger.log(Level.SEVERE,info);
                infoList.add(info);
            }else {
                info = "Deleted file: "+file.getPath();
                logger.log(Level.WARNING,info);
            }

        }

        return infoList;
    }

    public Resource getResource(String path){
        path = rootPath + path;
        return new FileSystemResource(path);

    }

}
