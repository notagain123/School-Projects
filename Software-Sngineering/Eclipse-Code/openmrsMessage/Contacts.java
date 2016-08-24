

import java.io.IOException;
import java.rmi.Naming;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Contacts
 */
@WebServlet(description = "Given a unique user id. This servet responds with a list of other users the account corresponding to the give userid may see and send messages to.", 
urlPatterns = { "/Contacts" })
public class Contacts extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Contacts() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		 try {
	    	 String url = "rmi://localhost/Service";
	    	 DBRemoteRequests contacts = (DBRemoteRequests)Naming.lookup(url);
	    	 

		 		String id = request.getParameter("userid");
		 		String auth = request.getParameter("auth");
	    	 
		 		if ((id == null) | (auth == null) ){
					
					response.getWriter().write("No post parameters");
					return;
				}	
		 	
		 	System.out.println("get contact for : " + id );
		 		
	    	 String availableContactsJSONString = contacts.getContacts(id, auth);
	    	 
	    	 
	    	 response.getWriter().println(availableContactsJSONString);
	       
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
