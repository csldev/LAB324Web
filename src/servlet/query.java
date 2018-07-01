package servlet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import utils.DataHandler;
import utils.SessionUtil;
import utils.Settings;

/**
 * Servlet implementation class query
 */
@WebServlet("/query")
public class query extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private static final String RECORDS_PATH = new Settings().recordsPath;
    
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public query() {
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
	
		
		Writer out = response.getWriter();
		
		String date = request.getParameter("date");
		if(date==null) {
			response.setStatus(417);
			out.append("hahaah");
			return;
		}
		
		StringBuilder recordsSB = new StringBuilder("[");
		try {
			File fileName = new File(RECORDS_PATH+"/record"+date+".txt");
			if(!fileName.exists()) {
				response.setStatus(417);
				return;
			}
			BufferedReader bReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "utf-8"));
			String string = bReader.readLine();
			DataHandler dHandler = new DataHandler();
			while((string = bReader.readLine())!=null) {
				if("".equals(string)) {
					continue;
				}
				recordsSB.append(dHandler.handle(string));
			}
			recordsSB.delete(recordsSB.length()-1,recordsSB.length());
			recordsSB.append("]");
			bReader.close();
			
		} catch (FileNotFoundException e) {
			// TODO: handle exception
			response.setStatus(417);
			return;
		} catch (Exception e) {
			// TODO: handle exception
			response.setStatus(500);
			return;
		}
		
		out.append(recordsSB);
		
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
