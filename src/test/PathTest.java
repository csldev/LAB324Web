package test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class PathTest {

	private String absolutePath = "/pathTest/";
	private String relativePath = "pathTest/";
	private String relativePath2 = "./pathTest/";
	
	private String[] paths = new String[] {absolutePath,relativePath,relativePath2};
	public PathTest() {
		// TODO Auto-generated constructor stub
		for (String string : paths) {
			File file = new File(string);
			if(!file.exists()) {
				file.mkdirs();
			}
		}
	}
	
	public String writeAbsoluteFile(String fileName) {
		File absFile = new File(absolutePath+fileName);
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(absFile));
			writer.write(fileName);
			writer.flush();
			writer.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return absFile.getAbsolutePath();
	}
	
	public String writeRelativeFile(String fileName) {
		File relaFile = new File(relativePath+fileName);
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(relaFile));
			writer.write(fileName);
			writer.flush();
			writer.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return relaFile.getAbsolutePath();
	}
	
	public String writeRelativeFile2(String fileName) {
		File relaFile = new File(relativePath2+fileName);
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(relaFile));
			writer.write(fileName);
			writer.flush();
			writer.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return relaFile.getAbsolutePath();
	}
	
}
