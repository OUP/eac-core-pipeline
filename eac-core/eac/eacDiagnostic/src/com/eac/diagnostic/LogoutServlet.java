package com.eac.diagnostic;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class LoginServlet
 */

public class LogoutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	final static Properties propE= new Properties(); 
    /**
     * @throws IOException 
     * @see HttpServlet#HttpServlet()
     */
    public LogoutServlet() throws IOException {
        super();
        
       
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		HttpSession session=request.getSession();
		if(session!=null)
		{
			session.invalidate();
			session.setMaxInactiveInterval(0);
			session=null;
		}
		response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "-1");
		response.sendRedirect("/eacDiagnostic/");
		
	}

	
}
