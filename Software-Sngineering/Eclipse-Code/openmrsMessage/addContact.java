

import java.io.IOException;
import java.rmi.Naming;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class addContract
 */
@WebServlet("/addContact")
public class addContact extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public addContact() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
	    	 String url = "rmi://localhost/Service";
	    	 DBRemoteRequests contacts = (DBRemoteRequests)Naming.lookup(url);
	    	 

		 		String id = request.getParameter("userid");
		 		String fid = request.getParameter("fid");
		 		String auth = request.getParameter("auth");
		 		String key = request.getParameter("initiator_pub_key");
	    	 
		 		if ((id == null) | (auth == null)|(auth==null) ){
					
					response.getWriter().write("No post parameters");
					return;
				}	
		 	
		 	System.out.println("add contact for user " + id);
		 		
	    	 String availableContactsJSONString = contacts.addContacts(id, fid, auth);
	    	 String req = contacts.sendMessage(id, fid, key, auth);
	    	 
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
