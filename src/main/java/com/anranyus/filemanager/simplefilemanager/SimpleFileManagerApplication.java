package com.anranyus.filemanager.simplefilemanager;

import com.anranyus.filemanager.simplefilemanager.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.core.env.Environment;

import java.io.File;

@SpringBootApplication
public class SimpleFileManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SimpleFileManagerApplication.class, args);
	}

}
