package net.sune.networking;

import java.io.Serializable;

public class Message implements Serializable
{
	private static final long serialVersionUID = 8L;
	
	public final String USERNAME;
	public final String TIME;
	public final String CONTENT;
	public final String IP;
	
	public Message(String username, String time, String content, String ip)
	{
		this.USERNAME = username;
		this.TIME = time;
		this.CONTENT = content;
		this.IP = ip;
	}
}