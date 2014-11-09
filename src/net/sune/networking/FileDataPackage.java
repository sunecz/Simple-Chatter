package net.sune.networking;

import java.io.Serializable;

public class FileDataPackage implements Serializable
{
	private static final long serialVersionUID = 8L;
	
	private String username;
	private String time;
	private String ip;
	
	private String fileHash;
	private String fileName;
	private long fileSize;
	
	private byte[] bytes;
	
	public FileDataPackage(String username, String ip, String fileHash, String fileName, long fileSize, int bytesCount)
	{
		this.username = username;
		this.ip = ip;
		
		this.fileHash = fileHash;
		this.fileName = fileName;
		this.fileSize = fileSize;
		
		this.bytes = new byte[bytesCount];
	}
	
	public FileDataPackage SetData(byte[] b)
	{
		this.bytes = b;
		return this;
	}
	
	public byte[] getBytes()
	{
		return this.bytes;
	}
	
	public String getFileHash()
	{
		return this.fileHash;
	}

	public String getFileName()
	{
		return this.fileName;
	}
	
	public long getFileSize()
	{
		return this.fileSize;
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