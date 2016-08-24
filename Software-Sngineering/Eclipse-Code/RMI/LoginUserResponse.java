
import org.apache.commons.codec.binary.Base64;
import org.json.simple.JSONObject;

public class LoginUserResponse{
	
	String name;
	int id;
	
	String authToken;
	
	private User mUser;
	

	LoginUserResponse(String n, int i){
		name = n;
		id= i;
	}
	
	LoginUserResponse(User u){
		mUser = u;
		generateAuthToken();
	}
	
	public String toJSONString(){
		JSONObject obj = mUser.toJSONString();
		
		
		obj.put("authtoken", authToken);
		obj.put("status", "sucessful");
		return obj.toJSONString();
		
	}
	
	void generateAuthToken(){
		
		java.security.SecureRandom random=  new java.security.SecureRandom();
		
		byte[] buffer = new byte[16];
		
		random.nextBytes(buffer);
		
		
		String b64token = Base64.encodeBase64URLSafeString(buffer);
		
		authToken = b64token;
		
	}
	
	Long getUserId(){
		return mUser.getId();
	}
	

}
