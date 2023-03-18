package com.anranyus.filemanager.simplefilemanager;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class SimpleFileManagerApplicationTests {

	@Test
	void contextLoads() {

	}

	@Test
	public void traverseFile(){
		ArrayList<File> filePool = new ArrayList<>();
		ArrayList<File> dirPool = new ArrayList<>();
		HashMap<String,ArrayList<File>> map = new HashMap<>();

		File file = new File("C:/");
		File[] fileList = file.listFiles();
		if (fileList != null) {
			for (File item : fileList){
				if (item.isFile()){
					filePool.add(item);
					System.out.println(item.getName());
				}else {
					dirPool.add(item);
				}
			}
		}
		map.put("file",filePool);
		map.put("dir",dirPool);

	}

}
