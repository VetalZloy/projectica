package com.vetalzloy.projectica.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * This class transforms email to gravatar URL. Size can be set, default picture - no.
 * @author VetalZloy
 *
 */
public class GravatarUtil {
	
	private static final int DEFAULT_SIZE = 200;
	
	public static String getGravatarUrl(String email){		
		return getGravatarUrl(email, DEFAULT_SIZE);
	}
	
	public static String getGravatarUrl(String email, int size){
		String url = "https://www.gravatar.com/avatar/" + 
					  md5Hex(email) + 
					  "/?s=" + size + 
					  "&d=https%3A%2F%2Fprojectica.me%2Fimg%2Fdefault-gravatar.png";	
		return url;
	}
	
	private static String hex(byte[] array) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < array.length; ++i) {
			sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));        
	    }
		return sb.toString();
	}
	  
	private static String md5Hex (String message) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			return hex (md.digest(message.getBytes("CP1251")));
	    } catch (NoSuchAlgorithmException e) {
	    	e.printStackTrace();
	    } catch (UnsupportedEncodingException e) {
	    	e.printStackTrace();
	    }	      
	    return null;
	  }
	
}
