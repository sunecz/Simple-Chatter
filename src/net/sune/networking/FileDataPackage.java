package net.sune.networking;

public class FileDataPackage
{
	private String username;
	private String time;
	private String ip;
	
	private String fileName;
	private int bytesCount;
	private byte[] bytes;
	
	private final int buffer = 8192;
	
	public FileDataPackage(String username, String ip, String fileName)
	{
		this.username = username;
		this.ip = ip;
		
		this.fileName = fileName;
		this.bytes = new byte[buffer];
	}
	
	public FileDataPackage SetData(byte[] b)
	{
		if(b.length <= buffer)
		{
			this.bytes = b;
			this.bytesCount = b.length;
		}
		
		return this;
	}
	
	public byte[] getBytes()
	{
		return this.bytes;
	}
	
	public int getBytesCount()
	{
		return this.bytesCount;
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