import org.json.simple.JSONObject;

public class User {
	
	private long mUserId; // primary key
	private String mFirstName;
	private String mLastName;
	private String mUsername;
	private String mPassword;
	
	
	
	
	public User(long id, String fn, String ln, String un, String pw){
		mUserId = id;
		mFirstName = fn;
		mLastName = ln;
		mUsername = un;
		mPassword = pw;
	}
	public User(long id, String fn, String ln, String un){
		mUserId = id;
		mFirstName = fn;
		mLastName = ln;
		mUsername = un;
	}
	
	public long getId(){
		return mUserId;
	}
	
	public boolean authUser(String password){
		if (mPassword.equals(password)) return true;
		else return false;
	}
	
	
	public JSONObject toJSONString(){
		
		JSONObject resp = new JSONObject();
		resp.put("userid",mUserId);
		resp.put("firstname", mFirstName);
		resp.put("lastname", mLastName);
		resp.put("username", mUsername);
		
		
		
		return resp;
		
	}
	
	
	

}
