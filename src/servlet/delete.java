package servlet;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import utils.SessionUtil;
import utils.Settings;

/**
 * Servlet implementation class delete
 */
@WebServlet("/delete")
public class delete extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private static final String RECORDS_PATH = new Settings().recordsPath;
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public delete() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		SessionUtil sessionUtil = new SessionUtil();
		if(sessionUtil.checkSession(request, response)) {
			return;
		}
	
	
		int dateInt = 0 ;
		
		File direct = new File(RECORDS_PATH);
		File[] files = direct.listFiles();
		for(int i=0;i<files.length;i++) {
			if(files[i].isFile()) {
				String name = files[i].getName();
				
				if(name.startsWith("record")) {
					String dateString = name.replaceAll("[^0-9]", "");				 
					dateInt = Integer.parseInt(dateString) > dateInt ? Integer.parseInt(dateString) : dateInt; 
					
				}							
			}
		}
		
		String date = dateInt+"";
		
		if(date.length()<4) {
			response.setStatus(417);
			return;
		}
		
		String filePath= RECORDS_PATH+"/record"+date+".txt";
		String tempFilePath = RECORDS_PATH+"/temp.txt";
		File file = new File(filePath);

		File tempFile = new File(tempFilePath);
		double remain = -1;
		
		try {
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
			BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tempFile),"utf-8"));
			
			String lastLine = bufferedReader.readLine();
			String tempLine;
		
			int count = 0;
			while( (tempLine=bufferedReader.readLine()) !=null) {
				if("".equals(tempLine)) {
					continue;
				}
				bufferedWriter.append(lastLine);
				bufferedWriter.newLine();
				if(count>0) {
					remain = Double.parseDouble(lastLine.substring(lastLine.lastIndexOf("....")+4,lastLine.length()));	
				}
				lastLine = tempLine;
				count++;
			}
			
			bufferedReader.close();
			bufferedWriter.flush();
			bufferedWriter.close();
			
			
			BufferedWriter bWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(RECORDS_PATH+"/remain.txt")),"utf-8"));
			bWriter.write(remain+"");
			bWriter.flush();
			bWriter.close();
			
			if(count<=1) {
				file.delete();
				tempFile.delete();
			}else {
				file.delete();
				tempFile.renameTo(file);	
			}
			
		}catch(FileNotFoundException e) {
			response.setStatus(417);
		}catch (Exception e) {
			// TODO: handle exception
			response.setStatus(500);
		}
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
