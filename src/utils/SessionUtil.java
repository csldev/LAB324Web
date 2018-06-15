package utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Console;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class SessionUtil{
	private long time = 30*24*60*60*1000;
	private String saveFilePath = "e:/lab324data/sessionTime.txt";
	private int[] keyTab = new int[] {67,98,93,78,77,79,103,110,74,120,115,65,67};
	
	public String getSessionId(String account) throws Exception {
		long sessionTime = System.currentTimeMillis()+time;
		return caculateSessionId(account, sessionTime);
	}
	
	private String caculateSessionId(String account,long sessionTime) throws Exception{
		StringBuilder sessionId = new StringBuilder();
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(saveFilePath)));
		writer.write(sessionTime+"");
		writer.flush();
		writer.close();
		int multi = 1;
		while(sessionTime>0) {
			int factor = (int) (sessionTime%10);
			multi *= factor==0?1:factor;
			sessionTime /= 10;
		}
		
		int index = 0;
		while(multi>0) {
			int charr = multi%10;
			sessionId.append((char)(charr+keyTab[index++]));
			multi /= 10;
		}
		
		while(account.length()>0) {
			int factor = ((int)account.charAt(0)-keyTab[index--]);
			sessionId.append(Math.abs(factor));
			account = account.substring(1, account.length());
		}
		return sessionId.toString();
	}
	
	
	public boolean checkSessionID(String account,String sessionId) throws Exception {
		
		BufferedReader bReader = new BufferedReader(new FileReader(new File(saveFilePath)));
		
		long sessionTime = Long.parseLong(bReader.readLine());
		
		return caculateSessionId(account, sessionTime).equals(sessionId);
	}
	
	
	
	public static void main(String args[]) throws Exception{
		SessionUtil sUtil = new SessionUtil();
		String sessionId = sUtil.getSessionId("asdfghj");
		System.out.println(sessionId);
		Thread.sleep(3000);
		System.out.println(sUtil.checkSessionID("asdfhj",sessionId));
	}

}
