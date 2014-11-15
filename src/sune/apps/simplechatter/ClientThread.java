package sune.apps.simplechatter;

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
	
	private final String serverIP;
	private final String clientIP;
	private final String username;
	private int client_state;
	
	private final ArrayList<DataPackage> messages_received;
	private final ArrayList<Message> messages_tosend;
	
	private final ArrayList<DataPackage> files_tosend;
	private final ArrayList<String> files_allowed;
	private final ArrayList<String> files_canceled;
	private final ArrayList<FileDataPackage> files_sent;
	
	private int file_status;
	private boolean isWaiting;
	private boolean isWaitingMSG;
	
	public ClientThread(Socket socket0, Socket socket1, String serverIP, String clientIP, String username)
	{
		this.socket0 = socket0;
		this.socket1 = socket1;
		
		this.serverIP = serverIP;
		this.clientIP = clientIP;
		this.username = username;
		this.client_state = 0;
		
		this.messages_received = new ArrayList<>();
		this.messages_tosend = new ArrayList<>();
		
		this.files_tosend = new ArrayList<>();
		this.files_allowed = new ArrayList<>();
		this.files_canceled = new ArrayList<>();
		this.files_sent = new ArrayList<>();
		
		this.file_status = 0;
		this.isWaiting = false;
		this.isWaitingMSG = false;
		
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
					
					if(dp.OBJECT_NAME.equals("send_message"))
					{
						isWaitingMSG = false;
					}
					else
					{
						messages_received.add(dp);
					}
				}
				catch(Exception ex) {}
				
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
					if(milliseconds == 50)
					{
						oos = new ObjectOutputStream(new BufferedOutputStream(socket0.getOutputStream()));
						oos.writeObject(new DataPackage("client_state", client_state));
						oos.flush();
						
						milliseconds = 0;
						Utils.sleep(1);
					}
					
					if(messages_tosend.size() > 0)
					{
						while(messages_tosend.size() > 0)
						{
							try
							{
								Message msg = messages_tosend.get(0);
								
								oos = new ObjectOutputStream(new BufferedOutputStream(socket0.getOutputStream()));
								oos.writeObject(new DataPackage("message", msg));
								oos.flush();
								
								isWaitingMSG = true;
								while(isWaitingMSG)
								{
									Utils.sleep(1);
								}
							}
							catch(Exception ex) {}
							
							messages_tosend.remove(0);
							Utils.sleep(1);
						}
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

					if(dp.OBJECT_NAME.equals("receive_file_data"))
					{
						file_status = (int) dp.OBJECT;
					}
					else if(dp.OBJECT_NAME.equals("cancel_sending"))
					{
						Object[] val = (Object[]) dp.OBJECT;
						files_canceled.add((String) val[0]);
					}
					else if(dp.OBJECT_NAME.equals("user_file_data"))
					{
						FileDataPackage fdp = (FileDataPackage) dp.OBJECT;
						files_sent.add(fdp);
					}
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
					while(files_tosend.size() > 0)
					{
						try
						{
							DataPackage dp = files_tosend.get(0);
							
							if(dp.OBJECT_NAME.equals("send_file"))
							{
								oos = new ObjectOutputStream(new BufferedOutputStream(socket1.getOutputStream()));
								oos.writeObject(dp);
								oos.flush();
								
								continue;
							}
							
							FileDataPackage fdp = (FileDataPackage) dp.OBJECT;
							if(!files_allowed.contains(fdp.FILE_HASH) && !files_canceled.contains(fdp.FILE_HASH))
							{
								DataPackage cr = new DataPackage("confirm_receive", new FileDataPackage("Server", serverIP, fdp.FILE_HASH, fdp.FILE_NAME, fdp.FILE_SIZE, 0));
								
								oos = new ObjectOutputStream(new BufferedOutputStream(socket1.getOutputStream()));
								oos.writeObject(cr);
								oos.flush();
								
								int to = 0;
								while(to < 30000)
								{
									if(file_status > 0)
									{
										switch(file_status)
										{
											case 2: files_allowed.add(fdp.FILE_HASH);	break;
											case 3: files_canceled.add(fdp.FILE_HASH);	break;
										}
										
										file_status = 0;
										break;
									}
									
									to++;
									if(to == 30000)
									{
										files_canceled.add(fdp.FILE_HASH);
									}
									
									Utils.sleep(1);
								}
							}
							
							if(files_allowed.contains(fdp.FILE_HASH) && !files_canceled.contains(fdp.FILE_HASH))
							{
								oos = new ObjectOutputStream(new BufferedOutputStream(socket1.getOutputStream()));
								oos.writeObject(dp);
								oos.flush();

								isWaiting = true;
								while(file_status == 0)
								{
									Utils.sleep(1);
								}
								
								isWaiting = false;
								file_status = 0;
							}
						}
						catch(Exception ex) {}
						
						files_tosend.remove(0);
						Utils.sleep(1);
					}
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
		client_state = state;
	}
	
	public void clearMessages()
	{
		messages_received.clear();
	}

	public ArrayList<DataPackage> getReceivedMessages()
	{
		return messages_received;
	}
	
	public void removeSentFile(int index)
	{
		files_sent.remove(index);
	}
	
	public ArrayList<FileDataPackage> getSentFiles()
	{
		return files_sent;
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
	
	public boolean isWaiting()
	{
		return isWaiting;
	}
	
	public boolean isWaitingMSG()
	{
		return isWaitingMSG;
	}
}