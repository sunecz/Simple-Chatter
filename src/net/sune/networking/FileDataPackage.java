package net.sune.networking;

import java.io.Serializable;

@SuppressWarnings("serial")
public class FileDataPackage implements Serializable
{
	private String username;
	private String time;
	private String ip;
	
	private String fileName;
	private long totalBytesCount;
	private byte[] bytes;
	
	private final int buffer = 8192;
	
	public FileDataPackage(String username, String ip, String fileName, long totalBytesCount)
	{
		this.username = username;
		this.ip = ip;
		
		this.fileName = fileName;
		this.bytes = new byte[buffer];
		this.totalBytesCount = totalBytesCount;
	}
	
	public FileDataPackage SetData(byte[] b)
	{
		if(b.length <= buffer)
		{
			this.bytes = b;
		}
		
		return this;
	}
	
	public byte[] getBytes()
	{
		return this.bytes;
	}
	
	public long getTotalBytesCount()
	{
		return this.totalBytesCount;
	}
	
	public String getFileName()
	{
		return this.fileName;
	}
	
	public String getUsername()
	{
		return this.username;
	}
	
	public String getIP()
	{
		return this.ip;
	}
	
	public String getTime()
	{
		return this.time;
	}
}