package com.anranyus.filemanager.simplefilemanager.service;

import com.anranyus.filemanager.simplefilemanager.exception.NullPathException;
import com.anranyus.filemanager.simplefilemanager.model.mFile;
import com.anranyus.filemanager.simplefilemanager.utils.FileOrder;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Logger;


@Service
public class FileService {

    Logger logger = Logger.getLogger(this.getClass().getSimpleName());

    String rootPath;

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
            if (rootPath!=null){
                path = rootPath;
            }
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

                if (item.isFile()){
                    mFile = new mFile(item.getName(),item.getAbsolutePath(),item.length(),formattedDate,null,true);
                    files.add(mFile);
                }else {
                    mFile = new mFile(item.getName() + "/", item.getAbsolutePath(), item.length(), formattedDate, null, false);
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
     * @return 文件列表
     */
    public ArrayList<mFile> getFiles(String type,String order,String path){
        ArrayList<mFile> list =  traverseFile(path);
        if (type!=null&&order!=null) {
            switch (type) {
                case "S": {
                    if (order.equals("A")) {
                        return FileOrder.AscendOrderBySize(list);
                    } else {
                        return FileOrder.DescendOrderBySize(list);
                    }
                }
                case "D": {
                    //TODO 按时间排序
                }
                default:
                    return list;

            }
        }else {
            return list;
        }

    }


}
