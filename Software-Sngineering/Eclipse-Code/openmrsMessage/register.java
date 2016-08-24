

import java.io.IOException;
import java.rmi.Naming;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class register
 */
@WebServlet("/register")
public class register extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public register() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 try {
	    	 String url = "rmi://localhost/Service";
	    	 DBRemoteRequests registerRequest = (DBRemoteRequests)Naming.lookup(url);
	    	 

		 		String userName = request.getParameter("userName");
		 		String password = request.getParameter("password");
		 		String fn = request.getParameter("firstname");
		 		String ln = request.getParameter("lastname");
	    	 
		 		if ((userName == null) | (password == null)|(fn == null) |(ln == null) ){
					
					response.getWriter().write("No post parameters");
					return;
				}	
		 	
		 	System.out.println("register for user" + userName);
		 		
	    	 String authResponse = registerRequest.register(userName, password, fn, ln);
	    	 
	    	 response.getWriter().println(authResponse);
	       
	    } catch (Exception e) {
	        System.err.println("Error connecting with rmi service interface on localhost");
	        e.printStackTrace();
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
