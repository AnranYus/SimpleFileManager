package com.anranyus.filemanager.simplefilemanager.model;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Objects;
public class mFile {
    String name;
    Long size;
    String path;
    String changeDate;
    String type;
    Boolean isFile;

    public mFile(String name,String path, Long size, String changeDate, String type, Boolean isFile) {
        this.name = name;
        this.path = path;
        this.size = size;
        this.changeDate = changeDate;
        this.type = type;
        this.isFile = isFile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        mFile file = (mFile) o;
        return Objects.equals(name, file.name) && Objects.equals(size, file.size) && Objects.equals(changeDate, file.changeDate) && Objects.equals(type, file.type) && Objects.equals(isFile, file.isFile);
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, size, changeDate, type, isFile);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(String changeDate) {
        this.changeDate = changeDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getFile() {
        return isFile;
    }

    public void setFile(Boolean file) {
        isFile = file;
    }
}
