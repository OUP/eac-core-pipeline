package com.eac.diagnostic;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class LoginServlet
 */

public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	final static Properties propE= new Properties(); 
    /**
     * @throws IOException 
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() throws IOException {
        super();
        
       
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		response.sendRedirect("/eacDiagnostic/");
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		InputStream is = getClass().getResourceAsStream("/resources/eacDiagnostic.properties");
    	propE.load(is);
		String username=request.getParameter("j_username");
		String password=request.getParameter("j_password");
		
		if(username.equals(propE.getProperty("diagnostic.username")) && password.equals(propE.getProperty("diagnostic.password")))
		{   HttpSession session=request.getSession();
		    session.setMaxInactiveInterval(20*60);
		    session.setAttribute("diagnosticSessionId", session.getId());
		    session.setAttribute("username", username);
		    response.sendRedirect("/eacDiagnostic/TestEACResponse");
		    /*RequestDispatcher rd = request.getRequestDispatcher("TestEACResponse");
			rd.forward(request,response);*/
			
			}
		else
		{   
			request.setAttribute("errormessage","Invalid username and password");
		    request.getRequestDispatcher("/Login.jsp").forward(request, response);
		}
		
	}

}
