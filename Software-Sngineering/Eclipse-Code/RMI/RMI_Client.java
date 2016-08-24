import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMI_Client {
	
	
	public static void main(String[] args){
		/*if (System.getSecurityManager() == null) {
	        System.setSecurityManager(new SecurityManager());
	    }*/
		
		PrintWriter out = null;
		
		try{
			out = new PrintWriter("LOL.txt");
			
		} catch ( FileNotFoundException e){
			e.printStackTrace();
		}
		
	    try {
	    	 String url = "rmi://localhost/Service";
	    	 RMI__NotifyListeners_Interface comp = (RMI__NotifyListeners_Interface)Naming.lookup(url);
	    	 comp.notifyNewMessage(new Long(123456), " fninishing project");
	    	 comp.waitForMessage(new Long(1234), out);
	       
	    } catch (Exception e) {
	        System.err.println("ComputePi exception:");
	        e.printStackTrace();
	    }
	}    
	
	

}
