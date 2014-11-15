package sune.apps.simplechatter;

import java.io.Serializable;

public class FileDataPackage implements Serializable
{
	private static final long serialVersionUID = 8L;
	
	public final String USERNAME;
	public final String TIME;
	public final String IP;
	
	public final String FILE_HASH;
	public final String FILE_NAME;
	public final long FILE_SIZE;
	
	public byte[] BYTES;
	
	public FileDataPackage(String username, String ip, String fileHash, String fileName, long fileSize, int bytesCount)
	{
		this.USERNAME = username;
		this.TIME = Utils.getCurrentDate();
		this.IP = ip;
		
		this.FILE_HASH = fileHash;
		this.FILE_NAME = fileName;
		this.FILE_SIZE = fileSize;
		
		this.BYTES = new byte[bytesCount];
	}
	
	public FileDataPackage SetData(byte[] b)
	{
		this.BYTES = b;
		return this;
	}
}