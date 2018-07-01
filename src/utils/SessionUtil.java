package utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Console;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SessionUtil{
    private static final String SAVE_PATH = new Settings().savePath;
	private long time = 30*24*60*60*1000L;
	private String sessionTimePath = SAVE_PATH+"/sessionTime.txt";
	private String accountPath = SAVE_PATH+"/account.txt";
	private String sessionIdPath = SAVE_PATH+"/sessionId.txt";
	private int[] keyTab = new int[] {67,98,93,78,77,79,103,110,74,120,115,65,67};
	
	public String getSessionId(String account){
		long currentTime = Calendar.getInstance().getTimeInMillis();
		long sessionTime = currentTime+time;
		System.out.println("current time "+currentTime);
		System.out.println("duration "+time);
		System.out.println("session time "+sessionTime);
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
	
	public boolean checkSessionID(String sessionId) {
		BufferedReader bufferedReader;
		try {
			bufferedReader = new BufferedReader(new FileReader(new File(sessionIdPath)));
			String sId = bufferedReader.readLine();
			bufferedReader.close();
			return sId.equals(sessionId);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return false;
	}
	
	public int checkSession(String sessionId) {
		if(!checkSessionID(sessionId) ||!checkSessionTime()) {
			return  401;
		}
		return 200;
	}
	
	public boolean checkSessionTime() {
		BufferedReader bufferedReader;
		try {
			bufferedReader = new BufferedReader(new FileReader(new File(sessionTimePath)));
			long sTime = Long.parseLong(bufferedReader.readLine().replace("[^0-9]", ""));
			long currentTime = Calendar.getInstance().getTimeInMillis();
			bufferedReader.close();
			System.out.println(currentTime);
			System.out.println(sTime);
			return currentTime < sTime;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return false;
	}
	
	public String register(String account, String password) {
		String sessionId = "";
		try {
			BufferedWriter bWriter = new BufferedWriter(new FileWriter(new File(accountPath)));
			bWriter.write("account:"+account+";password:"+password);
			bWriter.flush();
			bWriter.close();
			sessionId = getSessionId(account);
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(sessionIdPath)));
			bw.write(sessionId);
			bw.flush();
			bw.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sessionId;
	}
	
	public String login(String account, String password) {
		
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
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(sessionIdPath)));
			bw.write(sessionId);
			bw.flush();
			bw.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return sessionId;
	}
	
	public boolean checkSession(HttpServletRequest request, HttpServletResponse response) {
		
		Cookie[] cookies = request.getCookies();
		String sidcheck = "";
		for (Cookie cookie : cookies) {
			if(cookie.getName().equals("sessionId")) {
				sidcheck = cookie.getValue();
				break;
			}
		}
		if("".equals(sidcheck)) {
			response.setStatus(401);
			return true;
		}
	 
		int checkCode = checkSession(sidcheck);
		if(checkCode!=200) {
			response.setStatus(checkCode);
			return true;
		}
		return false;
	}
	
}
