package servlet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import utils.SessionUtil;
import utils.Settings;

/**
 * Servlet implementation class login
 */
@WebServlet("/login")
public class login extends HttpServlet {
	private static final long serialVersionUID = 1L;
    /**
     * Default constructor. 
     */
    public login() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		
		Writer out = response.getWriter();
		String mode = request.getParameter("mode");
		mode = mode==null?"-1":mode;
		
		String account = request.getParameter("account");
		account = account==null?"-1":account;
		String password = request.getParameter("password");
		password = password==null?"-1":password;
		String sId = request.getParameter("sessionId");
		sId = sId==null?"-1":sId;
		SessionUtil sUtil = new SessionUtil();
		String sessionId = "";
		
		
		switch (mode) {
		case "login":
			sessionId = sUtil.login(account, password);
			break;
		case "register":
			sessionId = sUtil.register(account, password);
			break;
		default:
			break;
		}
		if(!"".equals(sessionId)) {
			out.append(sessionId);
		}else {
			response.setStatus(402);
			out.append("-1");
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
