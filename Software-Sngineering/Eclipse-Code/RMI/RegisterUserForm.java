

public class RegisterUserForm {
	private String mUserName;
	private String mFirstName;
	private String mLastName;
	private String mPassword;
	
	RegisterUserForm(String userName, String firstName, String lastName, String password){
		mUserName = userName;
		mFirstName = firstName;
		mLastName = lastName;
		mPassword = password;
	}
	
	public String getUserName(){
		return mUserName;
	}
	
	public String getFirstName(){
		return mFirstName;
	}
	
	public String getLastName(){
		return mLastName;
	}
	
	public String getPassword(){
		return mPassword;
	}
}///AAA-AA-A
