import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.*;
import org.json.simple.JSONObject;

import java.rmi.Naming;

/**
 * 
 */




public class RMI_Server extends UnicastRemoteObject implements DBRemoteRequests  {

	
	FakeDatabase db;
	
    public RMI_Server() throws RemoteException
    {
        super();
        db = new FakeDatabase();
    }
    
    
    
    

    

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
       
        
        //security manager
//       if (System.getSecurityManager() == null) {
//            System.setSecurityManager(new SecurityManager());
//        }
//        
        /*
        
        try {
            String name = "Service";
            RMI_Server engine = new RMI_Server();
            RMI_Server stub =
                (RMI_Server) UnicastRemoteObject.exportObject(engine, 1099);
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind(name, stub);
            System.out.println("Service bound");
            System.out.println("Listening on port 1099");
        } catch (Exception e) {
            System.err.println("Service exception:");
            e.printStackTrace();
        }*/
       
        try{
        	 LocateRegistry.createRegistry(1099);
        	 RMI_Server srv = new RMI_Server();
        	Naming.rebind("Service", srv);
        } catch (Exception e){
        	e.printStackTrace();
        }
        
        
    }






	@Override
	public String loginUser(String username, String password) throws RemoteException {
		// TODO Auto-generated method stub
		JSONObject fail = new JSONObject();
		fail.put("status", "fail");
		LoginUserResponse response =  db.loginUser(username, password);
		if (response == null) return fail.toJSONString();
		else return response.toJSONString();
	}







	






	@Override
	public String getContacts(String username, String auth) throws RemoteException {
		JSONObject fail = new JSONObject();
		
		fail.put("status", "fail");
		
		long userId  = Long.parseLong(username);
		
		UserContactsResponse response =  db.getContacts(userId, auth);
		if (response == null) return fail.toJSONString();
		else return response.toJSONString();
	}







	@Override
	public String sendMessage(String from, String to, String message, String auth) throws RemoteException {
		// TODO Auto-generated method stub
		long senderId = Long.parseLong(from);
		long recvId = Long.parseLong(to);
		
		MessageBlock msg = new MessageBlock(senderId,recvId,message);
		
		db.depositMessage(msg);
		
		return "OK";
		
		
	}







	@Override      // blocking operation
	public String getNextMessage(String userid, String auth) throws RemoteException {
		JSONObject fail = new JSONObject();
		
		fail.put("status", "fail");
		
		long userId  = Long.parseLong(userid);
		
		if (db.isLoggedIn(userId,auth)){
			
			try{
				 
				MessageBlock store = null;
				
				Monitor holder = new Monitor();
				
				
				// spin lock. guards against spurious wakes
				synchronized (holder){
					
					db.putOnList(userId,holder);
					
					while( (store = db.getMessage(userId, holder)) ==null){
						
						holder.wait();
	
					}
					
					
				}
				
				return store.toJSONString();
				
				
			} catch (Exception e){
				e.printStackTrace();
				return null;
			}
			
			
			
		}
		
		else {
			
			
			return null;
		}
		
		
	}







	@Override
	public boolean isLoggedIn(long userId, String auth) throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}







	@Override
	public String register(String username, String password, String fn, String ln) throws RemoteException {
		// TODO Auto-generated method stub
		return db.register(username, password, fn, ln).toJSONString();
	}







	@Override
	public String addContacts(String id, String fid, String auth) throws RemoteException {
		// TODO Auto-generated method stub
		System.out.println("in the server");
		return db.addContacts(id, fid, auth);
	}







	@Override
	public String uploadRsaKey(String userid, String auth, String rasKey) throws RemoteException {
		// TODO Auto-generated method stub
		return db.uploadRsaKey(userid, auth, rasKey);
	}

}
