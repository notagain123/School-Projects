

import java.io.IOException;
import java.rmi.Naming;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class sendMessage
 */
@WebServlet("/sendMessage")
public class sendMessage extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public sendMessage() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
	    	 String url = "rmi://localhost/Service";
	    	 DBRemoteRequests sendMessage = (DBRemoteRequests)Naming.lookup(url);
	    	 

		 		String sid = request.getParameter("sid");
		 		String rid = request.getParameter("rid");
		 		String msg = request.getParameter("msg");
		 		
		 		String auth = request.getParameter("auth");
	    	 
		 		if ((rid == null) | (sid == null) | (auth == null) | (msg == null) ){
					
					response.getWriter().write("No post parameters");
					return;
				}	
		 	
		 	 System.out.println("send message: from" + sid + "to " + rid + "  :"+ msg);
		 		
	    	 String availableContactsJSONString = sendMessage.sendMessage(sid, rid, msg, auth);
	    	 
	    	 
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
