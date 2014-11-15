package sune.apps.simplechatter;
import java.io.Serializable;

public class DataPackage implements Serializable
{
	private static final long serialVersionUID = 8L;
	
	public final String OBJECT_NAME;
	public final Object OBJECT;
	
	public final String TIME;
	public final String USERNAME;
	public final String IP;
	
	public DataPackage(String objectName, Object value)
	{
		this(objectName, value, "Server", "0.0.0.0");
	}
	
	public DataPackage(String objectName, Object value, String username, String ip)
	{
		this.OBJECT_NAME = objectName;
		this.OBJECT = value;
		this.TIME = Utils.getCurrentDateFormatted();
		this.USERNAME = username;
		this.IP = ip;
	}
}