package servlet;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import utils.SessionUtil;
import utils.Settings;

/**
 * Servlet implementation class postRecord
 */
@WebServlet("/postRecord")
public class postRecord extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private static final String RECORDS_PATH = new Settings().recordsPath;
    
    
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
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		SessionUtil sessionUtil = new SessionUtil();
		if(sessionUtil.checkSession(request, response)) {
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
		File remainFile = new File(RECORDS_PATH+"/remain.txt");
		try {
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(remainFile), "utf-8"));
			remain = Double.parseDouble(bufferedReader.readLine());
			bufferedReader.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			response.setStatus(500);
			return;
		}
		
		

		String recordName = "/record"+(date.replaceAll("-", "").substring(0,6))+".txt";
		String recordPath = RECORDS_PATH+recordName;
		File recordFile = new File(recordPath);
		BufferedWriter bWriter;
		boolean first = !recordFile.exists();
		try {
			//bWriter = new BufferedWriter(new FileWriter(recordFile,true));
			bWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(recordFile,true),"utf-8"));
			if (first) {
				bWriter.append("日期..............项目........金额......余额");
			}		
			bWriter.newLine();
			remain -= Double.parseDouble(change);
			DecimalFormat dFormat = new DecimalFormat("#.00");
			String change1 = dFormat.format(Double.parseDouble(change));
			String remain1 = dFormat.format(remain);
			
			
			bWriter.append(date+"........"+item+"........"+change1+"........"+remain1);
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
			//BufferedWriter bWriter2 = new BufferedWriter(new FileWriter(remainFile));
			BufferedWriter bWriter2 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(remainFile),"utf-8"));
			bWriter2.write(remain+"");
			bWriter2.flush();
			bWriter2.close();
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			response.setStatus(500);
		}
		
	}

}
