package servlet;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import utils.SessionUtil;
import utils.Settings;

/**
 * Servlet implementation class download
 */
@WebServlet("/download")
public class download extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String RECORDS_PATH = new Settings().recordsPath;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public download() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		String date = request.getParameter("date");
		if(date == null) {
			response.setStatus(417);
			return;
		}
		
		String fileName = "record"+date+".txt";
		
		response.reset();
		response.setHeader("Content-Disposition", "attachment;filename="+URLEncoder.encode(fileName, "UTF-8"));
		response.setHeader("Connection", "close");
		response.setHeader("Content-Type", "application/octet-stream");
				
		OutputStream ops = null;
		FileInputStream fis =null;
		byte[] buffer = new byte[8192];
		int bytesRead = 0;
				
		
		try {
		    ops = response.getOutputStream();
		    fis = new FileInputStream(RECORDS_PATH+"/"+fileName);
		    while((bytesRead = fis.read(buffer, 0, 8192)) != -1){
		         ops.write(buffer, 0, bytesRead);
		    }
		    ops.flush();
		    } catch (IOException e) {
			e.printStackTrace();
		    } finally {
			try {
			    if(fis != null){
				fis.close();
			    }
			    if(ops != null){
				ops.close();
			    }
			} catch (IOException e) {
			    e.printStackTrace();
			}
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
