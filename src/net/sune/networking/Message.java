package net.sune.networking;

public class Message
{
	private String username;
	private String time;
	private String content;
	private String ip;
	
	public Message(String username, String time, String content, String ip)
	{
		this.username = username;
		this.time = time;
		this.content = content;
		this.ip = ip;
	}
	
	public String getUsername()
	{
		return this.username;
	}
	
	public String getTime()
	{
		return this.time;
	}
	
	public String getContent()
	{
		return this.content;
	}
	
	public String getIP()
	{
		return this.ip;
	}
}