package utils;

import java.io.File;

public class Settings {

	public String savePath = "/lab324data";
	//public String savePath = "e:/lab324data";
	
	public String accountPath = savePath+"/account.txt";
	
	public String sessinIdPath = savePath+"/sessionId.txt";
	
	public String sessionTime = savePath+"/sessionTime.txt";
	
	public String recordsPath = savePath+"/records";
	
	public String remainPath = recordsPath+"/remain.txt";
	
	public String[] dirs = new String[]{savePath,recordsPath};
	
	public Settings() {
		for (String string : dirs) {
			File file = new File(string);
			if(!file.exists()) {
				file.mkdirs();
			}
		}
	}
	
}
