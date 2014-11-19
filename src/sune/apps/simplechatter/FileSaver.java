package sune.apps.simplechatter;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;

public class FileSaver
{
	private BufferedOutputStream bos;
	
	private final String filePath;
	private final String fileName;
	private final String fileHash;
	private final long fileSize;
	
	private long savedBytes;
	
	public FileSaver(String path, String name, String hash, long size)
	{
		this.filePath = path;
		this.fileName = name;
		this.fileHash = hash;
		this.fileSize = size;
		
		try
		{
			this.bos = new BufferedOutputStream(new FileOutputStream(path));
		}
		catch(Exception ex) {}
	}
	
	public void save(byte[] bytes)
	{
		try
		{
			bos.write(bytes);
			savedBytes += bytes.length;
			
			if(savedBytes >= fileSize)
			{
				close();
			}
		}
		catch(Exception ex) {}
	}
	
	public void close()
	{
		try
		{
			bos.close();
		}
		catch(Exception ex) {}
	}
	
	public long getSavedBytes()
	{
		return savedBytes;
	}
	
	public String getFilePath()
	{
		return filePath;
	}
	
	public String getFileName()
	{
		return fileName;
	}
	
	public String getFileHash()
	{
		return fileHash;
	}
	
	public long getFileSize()
	{
		return fileSize;
	}
}