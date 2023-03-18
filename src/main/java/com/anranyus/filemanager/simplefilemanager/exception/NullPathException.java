package com.anranyus.filemanager.simplefilemanager.exception;

public class NullPathException extends Exception{
    public NullPathException() {
        super("You must define root path,it like: --rootpath=[You root path]");
    }
}
