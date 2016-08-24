

import java.io.IOException;
import java.rmi.Naming;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class GetMessage
 */
@WebServlet("/GetMessage")
public class GetMessage extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetMessage() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
	    	 String url = "rmi://localhost/Service";
	    	 DBRemoteRequests message = (DBRemoteRequests)Naming.lookup(url);
	    	 

		 		String id = request.getParameter("userid");
		 		String auth = request.getParameter("auth");
	    	 
		 		if ((id == null) | (auth == null) ){
					
					response.getWriter().write("No post parameters");
					return;
				}	
		 	
		 	 System.out.println("Fetch message for : " + id);
		 		
		 	 //block
		 	String availableContactsJSONString = message.getNextMessage(id,auth);
		 	 if(availableContactsJSONString!=null){
		 		 response.getWriter().println(availableContactsJSONString);
		 	 }
	    	 
	       
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
	
	
	private class NewsListener extends Thread{
		
		HttpServletRequest mRequest;
		HttpServletResponse mResponse;
		
		NewsListener(HttpServletRequest request, HttpServletResponse response){
			mRequest =request;
			mResponse = response;
		}
		
		@Override
		public void run(){
			
			
			
			
			
		}
		
		
	}

}
