

import java.io.IOException;
import java.rmi.Naming;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class rsaKey
 */
@WebServlet("/rsaKey")
public class rsaKey extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public rsaKey() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
	    	 String url = "rmi://localhost/Service";
	    	 DBRemoteRequests uploadKey = (DBRemoteRequests)Naming.lookup(url);
	    	 

		 		String userid = request.getParameter("userid");
		 		String auth = request.getParameter("auth");
		 		String rasKey = request.getParameter("rasKey");
	    	 
		 		if ((rasKey == null) | (userid == null) | (auth == null)){
					
					response.getWriter().write("No post parameters");
					return;
				}	
		 	
		 	System.out.println("upload the public key for user " + userid);
		 		
	    	String authResponse = uploadKey.uploadRsaKey(userid, auth, rasKey);
	    	 
	    	 response.getWriter().println(authResponse);
	       
	    } catch (Exception e) {
	        System.err.println("Error connecting with rmi service interface on localhost");
	        e.printStackTrace();
	    }
	}

}
