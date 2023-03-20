package com.anranyus.filemanager.simplefilemanager;

import com.anranyus.filemanager.simplefilemanager.service.FileService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.system.ApplicationHome;

import java.io.File;

@SpringBootApplication
public class SimpleFileManagerApplication {

	public static String runPath;

	public static void main(String[] args) {
		SpringApplication.run(SimpleFileManagerApplication.class, args);
		ApplicationHome home = new ApplicationHome(SimpleFileManagerApplication.class);
		File jarFile = home.getSource();
		runPath = jarFile.getParent();
		if(FileService.rootPath==null){
			FileService.rootPath = runPath;
		}
	}

}
