package utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Console;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class SessionUtil{
	private long time = 30*24*60*60*1000;
	private String sessionTimePath = "e:/lab324data/sessionTime.txt";
	private String accountPath = "e:/lab324data/account.txt";
	private int[] keyTab = new int[] {67,98,93,78,77,79,103,110,74,120,115,65,67};
	
	public String getSessionId(String account){
		long sessionTime = System.currentTimeMillis()+time;
		return caculateSessionId(account, sessionTime);
	}
	
	private String caculateSessionId(String account,long sessionTime){
		StringBuilder sessionId = new StringBuilder();
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter(new File(sessionTimePath)));
			writer.write(sessionTime+"");
			writer.flush();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
	
	
	public boolean checkSessionID(String account,String sessionId){
		
		BufferedReader bReader;
		long sessionTime = 0;
		try {
			bReader = new BufferedReader(new FileReader(new File(sessionTimePath)));
			sessionTime = Long.parseLong(bReader.readLine());
			bReader.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return caculateSessionId(account, sessionTime).equals(sessionId);
	}
	
	public String register(String account, String password) {
		String sessionId = "";
		try {
			BufferedWriter bWriter = new BufferedWriter(new FileWriter(new File(accountPath)));
			bWriter.write("account:"+account+";password:"+password);
			bWriter.flush();
			bWriter.close();
			sessionId = getSessionId(account);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sessionId;
	}
	
	public String checkLogin(String account, String password) {
		String sessionId = "";
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(accountPath)));
			String string = bufferedReader.readLine();
			bufferedReader.close();
			String sAccount = string.substring(string.indexOf(":")+1, string.indexOf(";"));
			String sPassword = string.substring(string.indexOf("password:")+9);
			if(sAccount.equals(account) && sPassword.equals(password)) {
				sessionId = getSessionId(sAccount);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return sessionId;
	}
	
	public static void main(String args[]) throws Exception{
		SessionUtil sUtil = new SessionUtil();
		//String sessionId = sUtil.getSessionId("asdfghj");
		//System.out.println(sessionId);
		Thread.sleep(3000);
		System.out.println(sUtil.checkSessionID("csl","CjaTTQ43631"));
	}

}
