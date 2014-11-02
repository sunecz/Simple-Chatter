package net.sune.networking;

import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;

public class Utils
{
	public static void sleep(long milliseconds)
	{
		try
		{
			Thread.sleep(milliseconds);
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
		}
	}
	
	public static String getCurrentDateFormatted()
	{
		DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
		return dateFormat.format(new Date());
	}
	
	public static String getCurrentDate()
	{
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		return dateFormat.format(new Date());
	}
	
	public static String hashSHA1(String str)
	{
	    String sha1 = "";
	    
	    try
	    {
	        MessageDigest crypt = MessageDigest.getInstance("SHA-1");
	        crypt.reset();
	        crypt.update(str.getBytes("UTF-8"));
	        
	        byte[] hash = crypt.digest();
	        Formatter formatter = new Formatter();
	        
		    for(byte b : hash)
		    {
		        formatter.format("%02x", b);
		    }
		    
		    sha1 = formatter.toString();
		    formatter.close();
	    }
	    catch(Exception e) {}
	    
	    return sha1;
	}
}