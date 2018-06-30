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

/**
 * Servlet implementation class delete
 */
@WebServlet("/delete")
public class delete extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
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
		String date = request.getParameter("date");
		if(date==null) {
			response.setStatus(417);
			return;
		}
		
		String filePath= "e:/lab324data/records/record"+date+".txt";
		String tempFilePath = "e:/lab324data/records/temp.txt";
		File file = new File(filePath);
		if(!file.exists()) {
			response.setStatus(417);
			return;
		}
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
			
			
			BufferedWriter bWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("e:/lab324data/records/remain.txt")),"utf-8"));
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
