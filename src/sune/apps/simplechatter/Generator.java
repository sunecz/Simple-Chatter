package sune.apps.simplechatter;

import java.security.SecureRandom;

public class Generator
{
	public static String genRandomString(int len) 
	{
		String AN = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		
		StringBuilder sb = new StringBuilder(len);
		SecureRandom rnd = new SecureRandom();
		
		for(int i = 0; i < len; i++)
		{
			sb.append(AN.charAt(rnd.nextInt(AN.length())));
		}
		
		return sb.toString();
	}
}