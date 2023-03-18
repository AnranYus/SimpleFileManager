package com.anranyus.filemanager.simplefilemanager.utils;

import com.anranyus.filemanager.simplefilemanager.model.mFile;

import java.util.ArrayList;
import java.util.Comparator;

public class FileOrder {

    public static ArrayList<mFile> AscendOrderBySize(ArrayList<mFile> list){
        list.sort(new Comparator<mFile>() {
            @Override
            public int compare(mFile o1, mFile o2) {
                return (int) (o1.getSize()-o2.getSize());
            }
        });
        return list;
    }

    public static ArrayList<mFile> DescendOrderBySize(ArrayList<mFile> list){
        list.sort(new Comparator<mFile>() {
            @Override
            public int compare(mFile o1, mFile o2) {
                return (int)(o2.getSize()-o1.getSize());
            }
        });
        return list;
    }
}
