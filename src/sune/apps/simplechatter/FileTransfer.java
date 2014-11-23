package sune.apps.simplechatter;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

public class FileTransfer implements Runnable
{
	private final String UID;
	
	private final File file;
	private final FileTransferInterface fti;
	private final int bufferSize = 8192;
	
	private final String fileName;
	private final long totalBytes;
	private long readBytes;
	
	private boolean paused = false;
	private boolean canceled = false;

	public FileTransfer(String path, FileTransferInterface fti)
	{
		this(new File(path), fti);
	}
	
	public FileTransfer(File file, FileTransferInterface fti)
	{
		this.UID = Utils.hashSHA1(Utils.hashSHA1(Generator.genRandomString(10)) + Utils.getCurrentDate());
		
		this.file = file;
		this.fti = fti;
		this.totalBytes = file.length();
		this.fileName = file.getName();
	}

	@Override
	public void run()
	{
		try
		{
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
			byte[] buffer = new byte[bufferSize];
			
			int read = 0;
			while((read = bis.read(buffer)) != -1)
			{
				if(canceled)	break;
				if(paused)
				{
					while(paused)
					{
						Utils.sleep(1);
					}
				}
				
				byte[] bs = new byte[read];
				System.arraycopy(buffer, 0, bs, 0, read);
			
				readBytes += read;
				fti.readBytes(this, new BytesInfo(bs, readBytes, totalBytes));
			}
			
			bis.close();
			fti.completed(this);
		}
		catch(Exception ex) {}
	}
	
	public void begin()
	{
		fti.beginTransfer(this);
		new Thread(this).start();
	}
	
	public void cancel()
	{
		canceled = true;
		readBytes = 0;
		
		fti.canceled(this);
	}
	
	public void pause()
	{
		paused = true;
		fti.paused(this);
	}
	
	public void proceed()
	{
		paused = false;
		fti.proceed(this);
	}
	
	public String getUID()
	{
		return UID;
	}
	
	public long getReadBytes()
	{
		return readBytes;
	}
	
	public long getTotalBytes()
	{
		return totalBytes;
	}
	
	public String getFileName()
	{
		return fileName;
	}
	
	public final class BytesInfo
	{
		private final byte[] bytes;
		private final long readBytes;
		private final long totalBytes;
		
		public BytesInfo(byte[] bytes, long readBytes, long totalBytes)
		{
			this.bytes = bytes;
			this.readBytes = readBytes;
			this.totalBytes = totalBytes;
		}
		
		public byte[] getBytes()
		{
			return bytes;
		}
		
		public long getReadBytes()
		{
			return readBytes;
		}
		
		public long getTotalBytes()
		{
			return totalBytes;
		}
	}
}