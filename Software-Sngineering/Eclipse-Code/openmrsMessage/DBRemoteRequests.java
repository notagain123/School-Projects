import java.rmi.RemoteException;

public interface DBRemoteRequests extends java.rmi.Remote  {
	
	// returns JSON encoded LoginUserResponse
	String loginUser(String username, String password) throws RemoteException;
	
	//returns JSON encoded RegisterUserResponse
	String register(String username, String password, String fn, String ln)throws RemoteException;
	
	//returns JSON encoded UserContactsResponse
	String getContacts(String username,String auth)throws RemoteException;
	
	boolean isLoggedIn(long userId,String auth)throws RemoteException;
	
	String sendMessage(String from,String to, String message, String auth)throws RemoteException;
	
	// blocking function returns JSON encoded MessageBlock
	String getNextMessage(String userid, String auth) throws RemoteException;

	String addContacts(String id, String fid, String auth) throws RemoteException;

	String uploadRsaKey(String userid, String auth, String rasKey) throws RemoteException;


	
	
	

}
