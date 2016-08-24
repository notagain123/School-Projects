


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.codec.binary.Hex;



public class RegisterUser {
	private static String sqlurl = "jdbc:mysql://localhost:3306/South";
	private static String sqluser = "root";
	private static String sqlpassword = "admin";
	
	
	public static long BADLOGIN = -100;
	public static long BADREGISTER = -101;
	
	
	public static long register(RegisterUserForm u){
		return registerUser(u);
	}
	
	
	
	
	
	
	
	
	
	private static long registerUser(RegisterUserForm userForm){
		Connection con = null;
        Statement st = null;
        ResultSet rs = null;

        
        /**
         * fields
         * user[0] = Lastname
         * user[1] = Firstname
         * user[2] = Username
         * 
         */
        
        
        String lastName = userForm.getLastName();
        String firstName = userForm.getFirstName();
        String userName = userForm.getUserName();
        
        
        
        long UIDHex = Crypto.createUIDint(userName);
        
        String sqlquery0 = "select * from users where UID=?";
        String sqlquery1 = "INSERT INTO users VALUES (?,?,?,?,?)"; 
        String sqlQueryInsertPassword = "INSERT INTO passwords VALUES(?,?)";
        try {
            con = DriverManager.getConnection(sqlurl, sqluser, sqlpassword);
            PreparedStatement selectUser = (PreparedStatement) con.prepareStatement(sqlquery0);
            PreparedStatement insertUser = (PreparedStatement) con.prepareStatement(sqlquery1);
            PreparedStatement insertPassword = (PreparedStatement) con.prepareStatement(sqlQueryInsertPassword);
            
            selectUser.setLong(1, UIDHex);
    		
           
            rs = selectUser.executeQuery();
            
            if (rs.next()) {
            	System.out.println("That username is already registered");
                return BADREGISTER;
            }
            
            
            else{ // no people registered with that username... we may proceed

            	byte[] salt = Crypto.generateSalt();
                char[] passwordhash = Crypto.getHash(userForm.getPassword(), salt);
                char[] saltHex = Hex.encodeHex(salt);
                String saltedPasswordHashString = new String(passwordhash);
                String saltString = new String(saltHex);
            	
                insertUser.setLong(1, UIDHex);  
                insertUser.setString(2, userName);
                insertUser.setString(3, lastName);
                insertUser.setString(4, firstName);
                insertUser.setString(5, saltString);
                
                insertPassword.setLong(1, UIDHex);
                insertPassword.setString(2, saltedPasswordHashString);
                
                long insertUserResult = insertUser.executeUpdate();
                long insertPasswordResult = insertPassword.executeUpdate();
            }

        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(RegisterUser.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);

        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (st != null) {
                    st.close();
                }
                if (con != null) {
                    con.close();
                }

            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(RegisterUser.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
		
		return UIDHex;
	}
	
	public static long login(String username, String password){
		return loginUser(username,password);
	}
	
	
	
	
	
	
	
	private static long loginUser(String username, String passwordEntered){
		Connection con = null;
        Statement st = null;
        ResultSet rs = null;

        long UIDHex = Crypto.createUIDint(username);
        
        String sqlquery2 = "select * from passwords where UID=?";
        
         
        
        try {
        	
        	
        	Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(sqlurl, sqluser, sqlpassword);
            String sqlQueryGetUser = "select * from users where UID=?";
            PreparedStatement selectUser = (PreparedStatement) con.prepareStatement(sqlQueryGetUser);
           
            selectUser.setLong(1, UIDHex);
      
            rs = selectUser.executeQuery();      
            // UID username fn ln salt
            
            if (rs.next()) { // points to first result. if return false, means no user with un/pw combo
            	String usernameres = rs.getString(2);
            	String firstName = rs.getString(3);
            	String lastName = rs.getString(4);
            	String salt = rs.getString(5);
            	
            	if(!usernameres.equalsIgnoreCase(username)) return BADLOGIN;
        		
        		// get password;
                
            	PreparedStatement selectPwd = (PreparedStatement) con.prepareStatement(sqlquery2);
                selectPwd.setLong(1, UIDHex);
                rs = null;
                rs = selectPwd.executeQuery();
            		
                if(rs.next()){
                	
                	String passwordHash = rs.getString(2);
                	   
                	boolean isVerified = Crypto.verifyPwdString(passwordHash,passwordEntered,salt);
                	if(isVerified){
                		System.out.println("logged in as " + username);
                			return UIDHex;
                	} else return BADLOGIN;
                		
                		
                	   
                		
                	} else return BADLOGIN;
            		
            		
            		
            	}else return BADLOGIN;
            	
            
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(RegisterUser.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);

        } catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (st != null) {
                    st.close();
                }
                if (con != null) {
                    con.close();
                }

            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(RegisterUser.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
		
		return BADLOGIN;
	}
	
	
}