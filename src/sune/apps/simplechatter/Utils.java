package sune.apps.simplechatter;

import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	
	public static boolean isValidIP(String ip)
	{
		Pattern p = Pattern.compile("(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})");
		Matcher m = p.matcher(ip);
		
		if(m.matches())
		{
			for(int i = 1; i < m.groupCount(); i++)
			{
				int val = Integer.parseInt(m.group(i));
				
				if(val >= 0 && val <= 255)
				{
					if(i == (m.groupCount() - 1) && val == 0)
					{
						return false;
					}
				}
				else
				{
					return false;
				}
			}
			
			return true;
		}
		
		return false;
	}
}