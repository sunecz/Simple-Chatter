package net.sune.networking;
import java.io.Serializable;

public class DataPackage implements Serializable
{
	private static final long serialVersionUID = 8L;
	
	private String objectName;
	private Object object;
	
	private String time;
	private String username;
	private String ip;
	
	public DataPackage(String objectName, Object value)
	{
		this(objectName, value, "Server", "0.0.0.0");
	}
	
	public DataPackage(String objectName, Object value, String username, String ip)
	{
		this.objectName = objectName;
		this.object = value;
		this.time = Utils.getCurrentDateFormatted();
		this.username = username;
		this.ip = ip;
	}
	
	public String getObjectName()
	{
		return this.objectName;
	}
	
	public Object getValue()
	{
		return this.object;
	}
	
	public String getTime()
	{
		return this.time;
	}
	
	public String getUsername()
	{
		return this.username;
	}
	
	public String getIP()
	{
		return this.ip;
	}
}