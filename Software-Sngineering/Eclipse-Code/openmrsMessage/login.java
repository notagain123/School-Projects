

import java.io.IOException;
import java.rmi.Naming;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



/**
 * Servlet implementation class login
 */
@WebServlet("/login")
public class login extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public login() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    
    
    
    
    void loginuser(){
    	/*
		//Connection con = db.connect();
		JSONObject resp = new JSONObject();
		
		
		
		System.out.println(userName + " " + password);
		
		if ((userName == null) | (password == null) ){
			
			response.getWriter().write("No post parameters");
			return;
		}
		
		//LoginUserResponse loginResponse = db.loginUser(userName,password);
		LoginUserResponse loginResponse = db.loginUser(userName,password);
		
		
		// sucess
		if (loginResponse != null){
			
			response.getWriter().write( loginResponse.toJSONString());
			
		}
		
		else{
			resp.put("status", "failed");
			resp.put("user_id", "");
			response.getWriter().write( resp.toJSONString()  );
		}
		
		*/
    }
    

    

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		

    	
	    try {
	    	 String url = "rmi://localhost/Service";
	    	 DBRemoteRequests loginRequest = (DBRemoteRequests)Naming.lookup(url);
	    	 

		 		String userName = request.getParameter("userName");
		 		String password = request.getParameter("password");
	    	 
		 		if ((userName == null) | (password == null) ){
					
					response.getWriter().write("No post parameters");
					return;
				}	
		 	
		 	System.out.println("lofin for user" + userName);
		 		
	    	 String authResponse = loginRequest.loginUser(userName, password);
	    	 
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
