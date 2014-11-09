package net.sune.networking;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ClientThread
{
	private final Socket socket0;
	private final Socket socket1;
	
	private final String clientIP;
	private final String username;
	private int client_state;
	
	private ArrayList<DataPackage> messages_received = new ArrayList<DataPackage>();
	private ArrayList<Message> messages_tosend = new ArrayList<Message>();
	
	private static ArrayList<DataPackage> files_received = new ArrayList<DataPackage>();
	private static ArrayList<DataPackage> files_tosend = new ArrayList<DataPackage>();
	
	public ClientThread(Socket socket0, Socket socket1, String clientIP, String username)
	{
		this.socket0 = socket0;
		this.socket1 = socket1;
		
		this.clientIP = clientIP;
		this.username = username;
		this.client_state = 0;
		
		new Thread(receiveMessages).start();
		new Thread(sendMessages).start();
		
		new Thread(receiveFiles).start();
		new Thread(sendFiles).start();
	}
	
	private Runnable receiveMessages = new Runnable()
	{
		@Override
		public void run()
		{
			ObjectInputStream ois;
			
			while(true)
			{
				try
				{
					ois = new ObjectInputStream(new BufferedInputStream(socket0.getInputStream()));
					DataPackage dp = (DataPackage) ois.readObject();
					
					messages_received.add(dp);
				}
				catch(Exception ex)
				{
					client_state = 1;
				}
				
				Utils.sleep(1);
			}
		}
	};
	
	private Runnable sendMessages = new Runnable()
	{
		@Override
		public void run()
		{
			ObjectOutputStream oos;
			int milliseconds = 0;
			
			while(true)
			{
				try
				{
					if(milliseconds == 100)
					{
						oos = new ObjectOutputStream(new BufferedOutputStream(socket0.getOutputStream()));
						oos.writeObject(new DataPackage("client_state", client_state));
						oos.flush();
						
						milliseconds = 0;
					}
					
					if(messages_tosend.size() > 0)
					{
						for(Message msg : messages_tosend)
						{
							try
							{
								oos = new ObjectOutputStream(new BufferedOutputStream(socket0.getOutputStream()));
								oos.writeObject(new DataPackage("message", msg));
								oos.flush();
							}
							catch(Exception ex) {}
						}
	
						messages_tosend.clear();
					}
				}
				catch(Exception ex) {}
				
				milliseconds++;
				Utils.sleep(1);
			}
		}
	};
	
	private Runnable receiveFiles = new Runnable()
	{
		@Override
		public void run()
		{
			ObjectInputStream ois;
			
			while(true)
			{
				try
				{
					ois = new ObjectInputStream(new BufferedInputStream(socket1.getInputStream()));
					DataPackage dp = (DataPackage) ois.readObject();

					files_received.add(dp);
				}
				catch(Exception ex) {}
				
				Utils.sleep(1);
			}
		}
	};
	
	private Runnable sendFiles = new Runnable()
	{
		@Override
		public void run()
		{
			ObjectOutputStream oos;
			
			while(true)
			{
				if(files_tosend.size() > 0)
				{
					for(DataPackage dp : files_tosend)
					{
						try
						{
							oos = new ObjectOutputStream(new BufferedOutputStream(socket1.getOutputStream()));
							oos.writeObject(dp);
							oos.flush();
						}
						catch(Exception ex) {}
					}

					files_tosend.clear();
				}
				
				Utils.sleep(1);
			}
		}
	};
	
	public void addMessage(Message msg)
	{
		messages_tosend.add(msg);
	}
	
	public void addDataPackage(DataPackage dp)
	{
		files_tosend.add(dp);
	}
	
	public void setClientState(int state)
	{
		this.client_state = state;
	}
	
	public void clearMessages()
	{
		messages_received.clear();
	}
	
	public void clearFiles()
	{
		files_received.clear();
	}
	
	public ArrayList<DataPackage> getReceivedMessages()
	{
		return messages_received;
	}
	
	public ArrayList<DataPackage> getReceivedFiles()
	{
		return files_received;
	}
	
	public String getIP()
	{
		return clientIP;
	}
	
	public String getUsername()
	{
		return username;
	}
	
	public int getClientState()
	{
		return client_state;
	}
}