import java.rmi.RemoteException;

public interface DBRequests {
	
	LoginUserResponse loginUser(String username, String password);
	
	LoginUserResponse register(String username, String password, String fn, String ln);
	
	UserContactsResponse getContacts(long userid,String auth);
	
	
	int sendMessage(String from,String to, String message, String auth);


	String addContacts(String id, String fid, String auth);
	
	String uploadRsaKey(String userid, String auth, String rasKey);
	
	
	
	
	
	

}
