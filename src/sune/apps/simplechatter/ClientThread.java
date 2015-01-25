package sune.apps.simplechatter;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ClientThread
{
	private final Server server;
	
	private final Socket socket0;
	private final Socket socket1;
	
	private final String clientIP;
	private final String username;
	private int client_state;
	
	private final ArrayList<DataPackage> messages_received;
	private final ArrayList<DataPackage> messages_tosend;
	
	private final ArrayList<DataPackage> files_tosend;
	private final ArrayList<FileDataPackage> files_sent;
	private final ArrayList<String> files_allowed;
	private final ArrayList<String> files_canceled;
	
	private int file_status;
	private boolean isWaiting;
	private boolean isWaitingMSG;
	
	public ClientThread(Server server, Socket socket0, Socket socket1, String clientIP, String username)
	{
		this.server = server;
		this.socket0 = socket0;
		this.socket1 = socket1;

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
					else if(dp.OBJECT_NAME.equals("receive_file_data"))
					{
						file_status = (int) dp.OBJECT;
					}
					else if(dp.OBJECT_NAME.equals("cancel_sending"))
					{
						Object[] val = (Object[]) dp.OBJECT;
						files_canceled.add((String) val[0]);
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
			int timeout = 0;
			
			while(true)
			{
				try
				{
					if(timeout >= 1000)
					{
						messages_tosend.add(new DataPackage("client_state", client_state));
						timeout = 0;
					}
					
					if(messages_tosend.size() > 0)
					{
						while(messages_tosend.size() > 0)
						{
							try
							{
								DataPackage dp = messages_tosend.get(0);
								
								oos = new ObjectOutputStream(new BufferedOutputStream(socket0.getOutputStream()));
								oos.writeObject(dp);
								oos.flush();
								
								if(!dp.OBJECT_NAME.equals("client_state") && !dp.OBJECT_NAME.equals("send_file"))
								{
									isWaitingMSG = true;
									int to = 0;
									
									while(isWaitingMSG && to < 15000)
									{
										to++;
										Utils.sleep(1);
									}
								}
							}
							catch(Exception ex) {}
							
							messages_tosend.remove(0);
							Utils.sleep(1);
						}
					}
				}
				catch(Exception ex) {}

				timeout++;
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

					if(dp.OBJECT_NAME.equals("user_file_data"))
					{
						FileDataPackage fdp = (FileDataPackage) dp.OBJECT;
						server.addUserFile(fdp);
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
						DataPackage dp = files_tosend.get(0);
						
						if(dp != null)
						{
							FileDataPackage fdp = (FileDataPackage) dp.OBJECT;
							
							try
							{
								if(!files_allowed.contains(fdp.FILE_HASH) && !files_canceled.contains(fdp.FILE_HASH))
								{
									DataPackage cr = new DataPackage("confirm_receive", new FileDataPackage(fdp.USERNAME, fdp.IP, fdp.FILE_HASH, fdp.FILE_NAME, fdp.FILE_SIZE, 0));
									
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
							}
							catch(Exception ex) {}
							
							try
							{
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
						}
						
						files_tosend.remove(0);
						Utils.sleep(1);
					}
				}
				
				Utils.sleep(1);
			}
		}
	};
	
	public void addMessage(DataPackage dp)
	{
		messages_tosend.add(dp);
	}
	
	public void addDataPackage(DataPackage dp)
	{
		files_tosend.add(dp);
	}
	
	public void setClientState(int state)
	{
		client_state = state;
	}
	
	public void removeSentFile(int index)
	{
		files_sent.remove(index);
	}
	
	public void clearMessages()
	{
		messages_received.clear();
	}

	public ArrayList<DataPackage> getReceivedMessages()
	{
		return messages_received;
	}

	public ArrayList<String> getAllowedFiles()
	{
		return files_allowed;
	}
	
	public ArrayList<String> getCanceledFiles()
	{
		return files_canceled;
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
	
	public int getFileStatus()
	{
		return file_status;
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