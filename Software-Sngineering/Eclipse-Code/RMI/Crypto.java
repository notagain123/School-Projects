
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.SecureRandom;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.binary.StringUtils;


import java.util.Locale;



public class Crypto {
	
	private static String USERNAMESALT = "THISISAMEANINGFULLYSTRING";
	
	public static String decode(String s) {
	    return StringUtils.newStringUtf16(Base64.decodeBase64(s));
	}
	public static String encode(String s) {
	    return Base64.encodeBase64URLSafeString(StringUtils.getBytesUtf16(s));
	}
	
	public static byte[] decode(byte[] base64Data){
		return Base64.decodeBase64(base64Data);
	}
	
	public static byte[] encode(byte[] base64Data){
		return Base64.encodeBase64URLSafe( base64Data );
	}
	
	public static byte[] encrypt(byte[] salt,String password){
		

		MessageDigest msg=null;
	
		try{
		msg = MessageDigest.getInstance("SHA-256");
		
		} catch (Exception e) {e.printStackTrace();}
	
		ByteBuffer bbuffer = ByteBuffer.allocate(32+password.length());
		bbuffer.put(salt);
		bbuffer.put(password.getBytes());
		
		byte[] bite = bbuffer.array();
		bite = msg.digest(bite);
		
		for(int i =0; i < 202112; i++){
			bite = msg.digest(bite);
		
		}
		
		return bite;
		
	}
	
	
	public static boolean isByteEqual(byte[] a, byte[] b) {
	    if (a.length != b.length) {
	        return false;
	    }

	    int result = 0;
	    for (int i = 0; i < a.length; i++) {
	      result |= a[i] ^ b[i];
	    }
	    return result == 0;
	}
	
	
	public static boolean pwdEqual(byte[] one, byte[] two){
		if (isByteEqual(one, two)) return true;
		else return false;
		
	}

	public static byte[] createUID(String username){
		byte[] UID = encrypt(USERNAMESALT.getBytes(),username.toLowerCase(Locale.ENGLISH));
		byte[] UINDEX = condense(UID,32,4);
		return UINDEX;
	}
	
	
	private  static byte[] condense(byte[] UID, int old_length, int new_length){
		
		
		MessageDigest msg=null;
	    byte[] newBite;
	    byte[] bite;
		
		try{
		msg = MessageDigest.getInstance("SHA-256");
		
		} catch (Exception e) {e.printStackTrace();}
		
		bite = msg.digest(UID);
		newBite = new byte[new_length];
		
		for(int i =0; i < new_length; i++){
			bite = msg.digest(bite);
			
			
			int sum = 0;
			for(int j = 0; j < old_length; j++){
				sum = sum + bite[j]; 
			}
			
			sum = sum%256;
			byte b = 0x00;
			b = (byte) (b | sum);
			newBite[i] = b;
			
		}
		return newBite;
	}
	
	public static char[] createUIDhex(String username){
		return Hex.encodeHex(createUID(username));
		
		
	}
	
	
	public static long createUIDint(String username){
		char[] c = createUIDhex(username);
		
        String UIDHex = "0x";
        long n = 0;
        for(int i =0; i < c.length; i++){
        	UIDHex+=c[i];
        }
        
        n = Long.decode(UIDHex);
        
        return n;
	}
	
	
	
		
	public static byte[] generateSalt(){
		SecureRandom random = new SecureRandom();
	    byte salt[] = new byte[32];
	    random.nextBytes(salt);
	    return salt;
	}
	
	public static char[] getHash(String data, byte[] salt){
		char[] b = Hex.encodeHex(encrypt(salt,data));
		return b;
	}
	
	
	
	public static boolean verifyPwdString(String passwordHash, String passwordtoTest, String salt){
		
		byte[] saltBytes;
		byte[] passwordBytes;
		
		try {
			saltBytes = Hex.decodeHex(salt.toCharArray());
			passwordBytes = Hex.decodeHex(passwordHash.toCharArray());
		} catch (DecoderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		byte[] hashtoTest = encrypt(saltBytes, passwordtoTest);
		
		if(MessageDigest.isEqual(hashtoTest, passwordBytes)){
			return true;
		}
		else return false;
		
	}
	
	
	
	
	
	
	
	
	

}
