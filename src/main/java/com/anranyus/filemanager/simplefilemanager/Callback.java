package com.anranyus.filemanager.simplefilemanager;

import org.springframework.http.HttpStatus;

public class Callback {
    HttpStatus status;
    String info;

    public Callback(HttpStatus status, String info) {
        this.status = status;
        this.info = info;
    }
}
