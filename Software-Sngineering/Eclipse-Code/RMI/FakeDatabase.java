import java.io.File;
import java.io.FileNotFoundException;
import java.rmi.RemoteException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Scanner;

import org.apache.commons.codec.binary.Base64;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.mysql.jdbc.Connection;


import org.apache.commons.codec.binary.Base64;





public class FakeDatabase implements DBRequests {
	
	Hashtable<Long, User> accountsDB;
	
	Hashtable<Long,UserContacts> accountContacts;
	
	Hashtable <Long, MessageStore> messages;
	
	Hashtable <String, Long> usernames;
	
	Hashtable<Long,String> loginTokens;
	
	Hashtable<Long,Object> NotificationList;
	
	
	FakeDatabase(){
		accountsDB = new Hashtable<Long,User>();
		accountContacts = new Hashtable<Long,UserContacts>();
		usernames = new Hashtable<String,Long>();
		loginTokens = new Hashtable<Long,String>();
		NotificationList = new Hashtable<Long,Object>();
		messages = new Hashtable <Long, MessageStore>();
		//loadUsers();//use this for testing only
		//loadContacts();//user for testing only
		
	}
	
	
	
	
	private void loadUsers(){
		Scanner dummyUserFile = null;
		
		try{
			dummyUserFile = new Scanner(new File("/Users/lijieye/Documents/workspaceEE/android/dummy_users.csv"));
		} catch (FileNotFoundException e){
			System.out.println("File not found");
		}
		
		
		while (dummyUserFile.hasNextLine()){
			
			String userInfo = dummyUserFile.nextLine();
			
			String userInfoFields[] = userInfo.split(",");
			
			try{
				
				Long userId = Long.parseLong(userInfoFields[0]);
				
				User testUser = new User( 
						userId,
						userInfoFields[1],
						userInfoFields[2],
						userInfoFields[3],
						userInfoFields[4]);
				
				
				accountsDB.put(testUser.getId(), testUser);
				usernames.put(userInfoFields[3], userId);
			
				} catch(NumberFormatException e){
					System.out.println("Error: Invalid entry : " + userInfo);
				}
		
		}
		
			System.out.println(accountsDB.size() + " Records loaded into database");
		
	}
	
	
	
	private void loadContacts(){
		Scanner contactFile = null;
		
		try{
			contactFile = new Scanner(new File("/Users/lijieye/Documents/workspaceEE/android/contacts.csv"));
		} catch (FileNotFoundException e){
			
		}
		
		
		while (contactFile.hasNextLine()){
			
			String contactPair = contactFile.nextLine();
			
			String contactIds[] = contactPair.split(",");
			
			try{
				
				long ownerId = Long.parseLong(contactIds[0]);
				long contactId = Long.parseLong(contactIds[1]);
				
				User owner = accountsDB.get(ownerId);
				User myContact = accountsDB.get(contactId);
				
				
				
				if ( (owner != null)  && (myContact != null) ){
					
					
					
					UserContacts theUserContacts = this.accountContacts.get(ownerId);
					
					
					
					if (theUserContacts == null){
						
						theUserContacts = new UserContacts(ownerId);
						
						
					}
					
					theUserContacts.push(contactId);
					this.accountContacts.put(ownerId, theUserContacts);
					
				}
				
				
				
				
			
				} catch(NumberFormatException e){
					System.out.println("Error: Invalid entry : " + contactPair);
				}
		
		}
		
			System.out.println(this.accountContacts.size() + " Records loaded into contact database");
	
		
	}
	
	Long getUserId(String username){
		Connection con = null;
		database db = new database();
		
		con = (Connection) db.connect();
		
		String stmtQuery = "select * from messageusers u where u.username = ?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = con.prepareStatement(stmtQuery);
			pstmt.setString(1, username);
			rs = pstmt.executeQuery();
			rs.next();
	        String id = rs.getString("user_id");
	        long l = Long.parseLong(id);
	        rs.close();
	        pstmt.close();
			con.close();
			System.out.println("get user id successfully");
			return l;
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			System.out.println("failed to get user id");
			return null;
		
		}
	}
	User getUserFromDB(Long id){
		String sid = Long.toString(id);
		Connection con = null;
		database db = new database();
		con = (Connection) db.connect();
		String sql = "select * from messageusers u where u.user_id = ?;";
		PreparedStatement p;
		try {
		p = con.prepareStatement(sql);
		p.setString(1, sid);
		ResultSet result = p.executeQuery();
		result.next();
		String un = result.getString("username");
		String ln = result.getString("lastName");
		String fn = result.getString("firstName");
		
		result.close();
		p.close();
		con.close();
		System.out.println("get user info successfully");
		return new User(id, fn, ln, un);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("failed to get user info");
			return null;
		}
		
	}
	void addUser(String username, String password, String firstName, String lastname){
		// insert into TABLE wi (username, 
		Connection con = null;
		database db = new database();
		con = (Connection) db.connect();
		System.out.println("register for user: " + username);
		String salt = BCrypt.gensalt(12);//use Bcrypt and salt for save password
		String hashed_password = BCrypt.hashpw(password, salt);
		String sql = "INSERT INTO `messageusers`(username,password,salt,firstName,lastName) VALUE (?,?,?,?,?);";
	    PreparedStatement preparedStmt;
		try {
		preparedStmt = con.prepareStatement(sql);
	    preparedStmt.setString (1, username);
	    preparedStmt.setString (2, hashed_password);
	    preparedStmt.setString (3, salt);
	    preparedStmt.setString (4, firstName);
	    preparedStmt.setString (5, lastname);
	    preparedStmt.execute();
	    preparedStmt.close();
	    con.close();
	    System.out.println("add user successfully");
		} catch (SQLException e) {
			System.out.println("Error add user");
			e.printStackTrace();
		}
	}
	boolean addFriend( Long myId, Long friendId){
		// insert into contacts values ....
		Connection con = null;
		database db = new database();
		con = (Connection) db.connect();
		String sql2 = "insert into messagefriends(friend1, friend2) value (?,?);";
		PreparedStatement stmt;
		try {
		stmt = con.prepareStatement(sql2);
		int n1 = myId.intValue();;//if the f1 and f2 is not convertible to int, error will be handle in sql
		int n2 = friendId.intValue();;
		stmt.setInt(1, n1);
		stmt.setInt(2, n2);
		stmt.execute();
		stmt.close();
		String sql3 = "insert into messagefriends(friend1, friend2) value (?,?);";
		PreparedStatement stmt2 = con.prepareStatement(sql3);
		stmt2.setInt(1, n2);
		stmt2.setInt(2, n1);
		stmt2.execute();
		stmt2.close();
		con.close();
		System.out.println("add friend successfully");
		return true;
		} catch (SQLException e) {
			System.out.println("fail add friend");
			e.printStackTrace();
			return false;
		}
	}
	String getFriends(Long myId){
        // do query
		JSONArray jsonArray = new JSONArray();
		Connection con = null;
		database db = new database();
		con = (Connection) db.connect();
		String sql2 = "select * from messagefriends u where u.friend1 = ?;";
		PreparedStatement stmt;
		try {
			stmt = con.prepareStatement(sql2);
		
		int n1 = myId.intValue();//if the sender and reciever is not convertible to int, error will be handle in sql
		stmt.setInt(1, n1);
		ResultSet rs = stmt.executeQuery();
		while (rs.next()){
			JSONObject resp = new JSONObject();
			String f = rs.getString("friend2");
			resp.put("userid", f);
			String sql3 = "select * from messageusers u where u.user_id = ?;";
			PreparedStatement p3 = con.prepareStatement(sql3);
			p3.setString(1, f);
			ResultSet result3 = p3.executeQuery();
			result3.next();
			String un = result3.getString("username");
			String ln = result3.getString("lastName");
			String fn = result3.getString("firstName");
			String id = result3.getString("user_id");
			resp.put("username", un);
			resp.put("lastname", ln);
			resp.put("firstname", fn);
			resp.put("userid", id);
			result3.close();
			p3.close();
			jsonArray.add(resp);
		}
		rs.close();
		stmt.close();
		con.close();
		System.out.println("get friend list successfully");
		return jsonArray.toString();
		} catch (SQLException e) {
			System.out.println("get friend list failed");
			return null;
		}
   

     }
	boolean checkpassword(Long uid, String password){
		String userid = Long.toString(uid);
		Connection con = null;
		database db = new database();
		con = (Connection) db.connect();
		String stmtQuery = "select * from messageusers u where u.user_id = ?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = con.prepareStatement(stmtQuery);
			pstmt.setString(1, userid);
			rs = pstmt.executeQuery();
			rs.next();
	        String p = rs.getString("password");
	        String s = rs.getString("salt");
	        rs.close();
	        pstmt.close();
			con.close();
			String hashed_password = BCrypt.hashpw(password, s);
			if (hashed_password.equals(p)){
				
				System.out.println("password correct");
				return true;
			}else{
				System.out.println("wrong password ");
				return false;
			}
			
			
		
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			System.out.println("wrong password ");
			return false;
		
		}
		
		
		
		
	}

	@Override
	public LoginUserResponse loginUser(String userName, String password) {
		// TODO Auto-generated method stub
		Long userId = getUserId(userName);
		if (userId == null) return null;// user does not exist
		
		//get userid with username
		
		//user exists
		User user = getUserFromDB(userId);
		
		// user somehow not registered on db
		if (user == null) return  null;
		
		// user sucessfully authenticated
		if ( checkpassword(userId, password) == true){
			
			LoginUserResponse loginDetails = new LoginUserResponse(user);
			
			//add token to token store. the authtoken is generated in LoginUserResponse constructor
			// when User is used to construct
			System.out.println(loginDetails.getUserId() + " " + loginDetails.authToken);
			loginTokens.put(userId, loginDetails.authToken);
			return loginDetails;
			
		}
		
		// login fail
		else return null;
	}
	
	@Override
	public LoginUserResponse register(String username, String password, String fn, String ln) {
		// TODO Auto-generated method stub
		addUser(username, password, fn, ln);
		return loginUser(username, password);
	}
	@Override
	public UserContactsResponse getContacts(long userid, String auth) {
		// TODO Auto-generated method stub
		 
		 String storedAuth = loginTokens.get(userid);
		if (  (auth.equals(storedAuth)) == false){
			return null;
		}
		
		// user already logged in
		return new UserContactsResponse(getFriends(userid));
		
		
		
	}

	@Override
	public int sendMessage(String from, String to, String message, String auth) {
		// TODO Auto-generated method stub
		return 1;
	}
	
	
	
	String getGetContactsAsJSONString(long userId){
		
		UserContacts contacts = this.accountContacts.get(userId);
		
		LinkedList<Long> contactList =  contacts.getContacts();
		
		org.json.simple.JSONArray array = new org.json.simple.JSONArray();
		
		
		
		
		for ( Long contactId:contactList){
			
			User contactableUser = this.accountsDB.get(contactId);
			if (contactableUser != null){
				try{
					org.json.simple.JSONObject user = contactableUser.toJSONString();
					array.add(user);
				} catch (Exception e1) {
	                // TODO Auto-generated catch block
	                e1.printStackTrace();
	            }
			}
			
			
		}
		
		
		return array.toJSONString();
		
		
		
	}
	
	
	

	String getGetMessagesAsJSONString(long userId){
		
		MessageStore userMessages = this.messages.get(userId);
		
		//LinkedList<MessageBlock> contactList =  userMessages.getContacts();
		
		//org.json.simple.JSONArray array = new org.json.simple.JSONArray();
		
		//for ( MessageBlock imMessage:contactList){
			
		//}
		//return array.toJSONString();
		
		return "";
		
	}
	
	
	
	
	
	public static void main(String args[]){
		
		FakeDatabase db = new FakeDatabase();
		String json = db.getGetContactsAsJSONString(45670147);
		
		System.out.println(json);
	}
	
	
	synchronized boolean isLoggedIn(long id, String auth){
		 String storedAuth = loginTokens.get(id);
			
		   if (storedAuth == null) return false;
		 	if (  (auth.equals(storedAuth)) == false){
				return false;
			}
			
		 	else return true;
		
		
	}
	
	synchronized void depositMessage(MessageBlock msg){
		long reciever = msg.getRecipient();
		MessageStore store = messages.get(reciever);
		if ( store == null){
			store = new MessageStore(reciever);
		}
		store.push(msg);
		messages.put(reciever, store);
		
		Monitor sleeper = (Monitor)NotificationList.get(reciever);
		if (sleeper != null){
			
			try{
				sleeper.getOwnership();
				//sleeper.notify();
			} catch (IllegalMonitorStateException e){
				e.printStackTrace();
			}
			
		}
	
	}
	
	synchronized void putOnList(long userid, Object holder){
		NotificationList.put(userid,holder);
	}
	
	synchronized MessageBlock getMessage(long userid, Object holder){
		
		MessageStore store = messages.get(userid);
		if ( store == null){
			return null;
		}
		
		MessageBlock msg = store.getAMessage();
		
		return msg;
		
	}




	@Override
	public String addContacts(String id, String sfid, String auth) {
		System.out.println("in fake database");
		// TODO Auto-generated method stub
				long userid = Long.parseLong(id);
				long fid = Long.parseLong(sfid);
				 String storedAuth = loginTokens.get(userid);
				if (  (auth.equals(storedAuth)) == false){
					JSONObject fail = new JSONObject();
					fail.put("status", "fail");
					return fail.toJSONString();
				}
				boolean result = addFriend(userid, fid);
				if(result == true){
					JSONObject successful = new JSONObject();
					successful.put("status", "successful");
					return successful.toJSONString();
				}else{
					JSONObject fail = new JSONObject();
					fail.put("status", "fail");
					return fail.toJSONString();
				}
	}




	@Override
	public String uploadRsaKey(String userid, String auth, String rasKey) {
		// TODO Auto-generated method stub
		long uid = Long.parseLong(userid);
		String storedAuth = loginTokens.get(uid);
		if (  (auth.equals(storedAuth)) == false){
			JSONObject fail = new JSONObject();
			fail.put("status", "fail");
			return fail.toJSONString();
		}
		
		Connection con = null;
		database db = new database();
		con = (Connection) db.connect();
		String sql2 = "insert into messageRsaKey(user_id, key) value (?,?);";
		PreparedStatement stmt;
		try {
		stmt = con.prepareStatement(sql2);
		int n1 = Integer.parseInt(userid);//if the f1 and f2 is not convertible to int, error will be handle in sql
		stmt.setInt(1, n1);
		stmt.setString(2, rasKey);
		stmt.execute();
		stmt.close();
		con.close();
		System.out.println("add friend successfully");
		JSONObject successful = new JSONObject();
		successful.put("status", "successful");
		return successful.toJSONString();
		} catch (SQLException e) {
			System.out.println("fail add friend");
			e.printStackTrace();
			JSONObject fail = new JSONObject();
			fail.put("status", "fail");
			return fail.toJSONString();
		}
	}
	

	

}
