package servlet;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import utils.SessionUtil;

/**
 * Servlet implementation class postRecord
 */
@WebServlet("/postRecord")
public class postRecord extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public postRecord() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//String sessionId = request.getHeader("Cookie");
		Cookie[] cookies = request.getCookies();
		String sessionId = "";
		SessionUtil sessionUtil = new SessionUtil();
		for (Cookie cookie : cookies) {
			if(cookie.getName().equals("sessionId")) {
				sessionId = cookie.getValue();
			}
		}
		
		
		
		if("".equals(sessionId) || !sessionUtil.checkSessionID(sessionId)) {
			response.setStatus(401);
			return;
		}
		
		String date = request.getParameter("date");
		String item = request.getParameter("item");
		String change = request.getParameter("change");
		if(date==null || item==null || change==null) {
			response.setStatus(417);
			return;
		}
		
		
		//获取余额
		
		double remain;
		File remainFile = new File("e:/lab324data/records/remain.txt");
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(remainFile));
			remain = Double.parseDouble(bufferedReader.readLine());
			bufferedReader.close();
		} catch (Exception e) {
			// TODO: handle exception
			response.setStatus(500);
			return;
		}
		
		
		Calendar calendar = Calendar.getInstance();
		int month = calendar.get(Calendar.MONTH)+1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		String recordName = "record"+calendar.get(Calendar.YEAR)+""+(month>9?month+"":"0"+month)+(day>0?day+"":"0"+day)+".txt";
		String recordPath = "e:/lab324data/records/"+recordName;
		File recordFile = new File(recordPath);
		BufferedWriter bWriter;
		boolean first = !recordFile.exists();
		try {
			bWriter = new BufferedWriter(new FileWriter(recordFile,true));
			if (first) {
				bWriter.append("日期..............项目........金额......余额");
			}		
			bWriter.newLine();
			remain -= Double.parseDouble(change);
			bWriter.append(date+"........"+item+"........"+change+"........"+remain);
			bWriter.flush();
			bWriter.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			response.setStatus(500);
			return;
		}
		
		//设置余额
		try {
			BufferedWriter bWriter2 = new BufferedWriter(new FileWriter(remainFile));
			bWriter2.write(remain+"");
			bWriter2.flush();
			bWriter.close();
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			response.setStatus(500);
		}
		
	}

}
