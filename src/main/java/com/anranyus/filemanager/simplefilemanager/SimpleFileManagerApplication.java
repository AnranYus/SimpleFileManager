package com.anranyus.filemanager.simplefilemanager;

import com.anranyus.filemanager.simplefilemanager.exception.NullPathException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.net.URISyntaxException;
import java.util.logging.Logger;

@SpringBootApplication
public class SimpleFileManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SimpleFileManagerApplication.class, args);
	}

}
