package net.sune.networking;

import java.util.ArrayList;

public class FileData
{
	private String fileName;
	private long fileSize;
	
	private ArrayList<Byte> bytes = new ArrayList<Byte>();
	
	public FileData(String fileName, long fileSize)
	{
		this.fileName = fileName;
		this.fileSize = fileSize;
	}
	
	public void addBytes(byte[] bs)
	{
		for(byte b : bs)
		{
			bytes.add(b);
		}
	}
	
	public byte[] getBytes()
	{
		byte[] bs = new byte[bytes.size()];
		
		for(int i = 0; i < bs.length; i++)
		{
			Byte b = bytes.get(i);
			bs[i] = (byte) b;
		}
		
		return bs;
	}
	
	public int getCurrentSize()
	{
		return this.bytes.size();
	}
	
	public String getFileName()
	{
		return this.fileName;
	}
	
	public long getFileSize()
	{
		return this.fileSize;
	}
}