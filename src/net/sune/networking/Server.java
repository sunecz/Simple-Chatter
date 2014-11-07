package net.sune.networking;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneLayout;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class Server
{
	private static JFrame frame;
	private static JPanel contentPane;
	private static JPanel panel0;
	private static JPanel panel1;
	private static JPanel subPanel0;
	private static JPanel subPanel1;
	
	private static JList<String> list_clients;
	private static DefaultListModel<String> list_clients_model;
	private static JButton btnDisconnect;
	private static JLabel lblConnectedClients;
	
	private static JTextField txtMessage;
	private static JScrollPane scrollPane;
	private static JTextArea textArea;
	private static JPanel panelList;
	private static JPanel panel2;
	
	private static JPanel subPanel2;
	private static JButton btnSend;
	private static JLabel lblConsole;
	private static JButton btnSendFiles;

	private static void WindowServer(String ip, int port)
	{
		frame = new JFrame();
		frame.setTitle("Server - " + ip + ":" + port);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(100, 100, 730, 430);
		frame.setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		contentPane.setLayout(new BorderLayout(0, 0));
		
		list_clients_model = new DefaultListModel<String>();
		
		panel0 = new JPanel();
		panel0.setBorder(new EmptyBorder(0, 0, 3, 0));
		
		panel1 = new JPanel();
		panel1.setBorder(new EmptyBorder(5, 0, 0, 0));
		panel1.setLayout(new BorderLayout(0, 0));
		btnDisconnect = new JButton("Disconnect client");
		btnDisconnect.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black, 1), BorderFactory.createEmptyBorder(4, 10, 6, 10)));
		
		subPanel0 = new JPanel();
		subPanel0.setBorder(new EmptyBorder(0, 0, 5, 0));

		subPanel1 = new JPanel();
		subPanel1.setLayout(new BorderLayout(0, 0));
		txtMessage = new JTextField();
		txtMessage.setColumns(100);
		txtMessage.setFont(new Font("Consolas", Font.PLAIN, 12));
		txtMessage.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black, 1), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		GridBagLayout gbl_panel0 = new GridBagLayout();
		gbl_panel0.columnWidths = new int[]{342, 253, 0};
		gbl_panel0.rowHeights = new int[]{14, 0};
		gbl_panel0.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gbl_panel0.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panel0.setLayout(gbl_panel0);
		panel1.add(btnDisconnect, BorderLayout.EAST);
		panel1.add(subPanel0, BorderLayout.NORTH);
		subPanel0.setLayout(new BorderLayout(0, 0));
		subPanel0.add(subPanel1);
		subPanel1.add(txtMessage);
		
		subPanel2 = new JPanel();
		subPanel2.setBorder(new EmptyBorder(0, 5, 0, 0));
		subPanel1.add(subPanel2, BorderLayout.EAST);
		subPanel2.setLayout(new GridLayout(1, 0, 0, 0));
		
		btnSend = new JButton("Send");
		btnSend.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black, 1), BorderFactory.createEmptyBorder(4, 10, 6, 10)));
		subPanel2.add(btnSend);
		
		panelList = new JPanel();
		contentPane.add(panelList, BorderLayout.CENTER);
		panelList.setLayout(new GridLayout(0, 1, 0, 0));

		list_clients = new JList<String>();
		panelList.add(list_clients);
		list_clients.setBorder(new LineBorder(new Color(0, 0, 0)));
		list_clients.setModel(list_clients_model);
		contentPane.add(panel0, BorderLayout.NORTH);
		
		lblConnectedClients = new JLabel("Connected clients");
		lblConnectedClients.setHorizontalAlignment(SwingConstants.LEFT);
		lblConnectedClients.setFont(new Font("Tahoma", Font.PLAIN, 11));
		
		GridBagConstraints gbc_lblConnectedClients = new GridBagConstraints();
		gbc_lblConnectedClients.fill = GridBagConstraints.BOTH;
		gbc_lblConnectedClients.insets = new Insets(0, 0, 0, 5);
		gbc_lblConnectedClients.gridx = 0;
		gbc_lblConnectedClients.gridy = 0;
		panel0.add(lblConnectedClients, gbc_lblConnectedClients);
		
		lblConsole = new JLabel("Console");
		lblConsole.setFont(new Font("Tahoma", Font.PLAIN, 11));
		GridBagConstraints gbc_lblConsole = new GridBagConstraints();
		gbc_lblConsole.anchor = GridBagConstraints.WEST;
		gbc_lblConsole.gridx = 1;
		gbc_lblConsole.gridy = 0;
		panel0.add(lblConsole, gbc_lblConsole);
		contentPane.add(panel1, BorderLayout.SOUTH);
		
		btnSendFiles = new JButton("Send files");
		btnSendFiles.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black, 1), BorderFactory.createEmptyBorder(4, 10, 6, 10)));
		btnSendFiles.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if(e.getModifiers() == MouseEvent.BUTTON1_MASK)
				{
					new FileSelectorServer();
				}
			}
		});
		
		panel1.add(btnSendFiles, BorderLayout.WEST);
		
		frame.setContentPane(contentPane);

		panel2 = new JPanel();
		panel2.setBorder(new EmptyBorder(0, 5, 0, 0));
		contentPane.add(panel2, BorderLayout.EAST);
		panel2.setLayout(new GridLayout(0, 1, 0, 0));
		
		scrollPane = new JScrollPane();
		panel2.add(scrollPane);
		scrollPane.setBorder(BorderFactory.createLineBorder(Color.black, 1));
		scrollPane.setLayout(new ScrollPaneLayout());
		scrollPane.setViewportView(textArea);
		
		textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		textArea.setWrapStyleWord(true);
		textArea.setLineWrap(true);
		textArea.setColumns(50);
		textArea.setFont(new Font("Consolas", Font.PLAIN, 12));
		textArea.setEditable(false);
		textArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		frame.setVisible(true);
		
		btnDisconnect.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if(e.getModifiers() == MouseEvent.BUTTON1_MASK)
				{
					int selected = list_clients.getSelectedIndex();
					
					if(selected != -1)
					{
						try
						{
							disconnect(selected);
						}
						catch(Exception ex)
						{
							JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
						}
					}
				}
			}
		});
		
		txtMessage.addKeyListener(new KeyListener()
		{
			@Override
			public void keyPressed(KeyEvent e)
			{
				if(e.getKeyCode() == KeyEvent.VK_ENTER)
				{
					if(!txtMessage.getText().trim().isEmpty())
					{
						sendMessage(txtMessage.getText());
					}
				}
			}

			@Override public void keyReleased(KeyEvent e) {}
			@Override public void keyTyped(KeyEvent e) {}
		});
		
		btnSend.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if(e.getModifiers() == MouseEvent.BUTTON1_MASK)
				{
					if(!txtMessage.getText().trim().isEmpty())
					{
						sendMessage(txtMessage.getText());
					}
				}
			}
		});
		
		frame.addWindowListener(new WindowListener()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				shutdown();
				System.exit(0);
			}

			@Override public void windowActivated(WindowEvent e) {}
			@Override public void windowClosed(WindowEvent e) {}
			@Override public void windowDeactivated(WindowEvent e) {}
			@Override public void windowDeiconified(WindowEvent e) {}
			@Override public void windowIconified(WindowEvent e) {}
			@Override public void windowOpened(WindowEvent e) {}
		});
	}

	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					InetAddress addr = InetAddress.getLocalHost();
					srvIP = addr.getHostAddress();

					WindowServer(srvIP, srvMessagesPort);
					
					try
					{
						srvMessages = new ServerSocket(srvMessagesPort, 0, addr);
						srvFiles = new ServerSocket(srvFilesPort, 0, addr);

						new Thread(acceptMessages).start();
						new Thread(acceptFiles).start();
						
						logText("Server has been started on " + srvIP + ":" + srvMessagesPort + "!");
					}
					catch(Exception ex)
					{
						logText("Server could not be started! Port is already in use!");
					}
				}
				catch(Exception e) {}
			}
		});
	}
	
	private static String srvIP = "";

	private static int srvMessagesPort = 2400;
	private static int srvFilesPort = 2401;
	
	private static ServerSocket srvMessages;
	private static ServerSocket srvFiles;
	
	private static ArrayList<Socket> sockets_messages = new ArrayList<Socket>();
	private static ArrayList<String> clients_usernames = new ArrayList<String>();
	private static ArrayList<Integer> clients_states = new ArrayList<Integer>();
	private static ArrayList<DataPackage> received_messages = new ArrayList<DataPackage>();
	
	private static ArrayList<Message> messagesToSend = new ArrayList<Message>();
	private static ArrayList<Message> messagesUserToSend = new ArrayList<Message>();
	
	private static Runnable acceptMessages = new Runnable()
	{
		@Override
		public void run()
		{
			new Thread(receiveMessages).start();
			new Thread(processMessages).start();
			new Thread(sendMessages).start();
			
			while(true)
			{
				try
				{
					Socket socket = srvMessages.accept();
					
					InetAddress addr = socket.getInetAddress();
					String clientAddr = addr.getHostAddress();
					String clientName = addr.getHostName();
					
					boolean validSocket = true;
					for(Socket s : sockets_messages)
					{
						if(s.getInetAddress().getHostAddress().equals(clientAddr))
						{
							validSocket = false;
						}
					}
					
					if(validSocket)
					{
						ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
						DataPackage dp = (DataPackage) ois.readObject();
						
						String username = "";
						if(dp.getObjectName().equals("username"))
						{
							username = (String) dp.getValue();
						}

						ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
						oos.writeObject(new DataPackage("message", "Welcome to the server, " + username + "!"));
						sendMessage(username + " with IP " + socket.getInetAddress().getHostAddress() + " connected to the server!");

						list_clients_model.addElement(username + " - " + clientAddr + " - " + clientName);

						clients_states.add(0);
						clients_usernames.add(username);
						sockets_messages.add(socket);						
					}
					else
					{
						ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
						oos.writeObject(new DataPackage("message", "Client is not valid or is already connected!"));
					}
				}
				catch(Exception ex) {}
				
				Utils.sleep(1);
			}
		}
	};
	
	private static Runnable receiveMessages = new Runnable()
	{
		@Override
		public void run()
		{
			ObjectInputStream ois;
			
			while(true)
			{
				if(sockets_messages.size() > 0)
				{
					for(Socket socket : sockets_messages)
					{
						try
						{
							ois = new ObjectInputStream(socket.getInputStream());
							Object obj = ois.readObject();
							
							if(obj instanceof DataPackage)
							{
								DataPackage dp = (DataPackage) obj;
								received_messages.add(dp);
							}
						}
						catch(Exception ex) {}
					}
				}
					
				Utils.sleep(1);
			}
		}
	};
	
	private static Runnable processMessages = new Runnable()
	{
		@Override
		public void run()
		{
			while(true)
			{
				if(received_messages.size() > 0)
				{
					for(DataPackage dp : received_messages)
					{
						try
						{
							if(dp.getObjectName().equals("message"))
							{
								Message msg = (Message) dp.getValue();
								
								logText(msg);
								sendUserMessage(dp);
							}
						}
						catch(Exception ex) {}
					}

					received_messages.clear();
				}

				Utils.sleep(1);
			}
		}
	};
	
	private static Runnable sendMessages = new Runnable()
	{
		@Override
		public void run()
		{
			ObjectOutputStream oos;
			
			while(true)
			{
				if(messagesToSend.size() > 0)
				{
					for(Message msg : messagesToSend)
					{
						for(int x = 0; x < sockets_messages.size(); x++)
						{
							try
							{
								Socket socket = sockets_messages.get(x);
								
								oos = new ObjectOutputStream(socket.getOutputStream());
								oos.writeObject(new DataPackage("message", msg));						
							}
							catch(Exception ex) {}
						}
						
						logText(msg.getContent(), "Server");
					}

					txtMessage.setText("");
					messagesToSend.clear();
				}
				
				if(messagesUserToSend.size() > 0)
				{
					for(Message msg : messagesUserToSend)
					{
						for(int x = 0; x < sockets_messages.size(); x++)
						{
							try
							{
								Socket socket = sockets_messages.get(x);
								
								oos = new ObjectOutputStream(socket.getOutputStream());
								oos.writeObject(new DataPackage("message", msg, msg.getUsername(), msg.getIP()));
							}
							catch(Exception ex) {}
						}
					}
					
					messagesUserToSend.clear();
				}
				
				Utils.sleep(1);
			}
		}
	};	
	
	private static void sendMessage(String msg)
	{
		messagesToSend.add(new Message("Server", Utils.getCurrentDateFormatted(), msg, srvIP));
	}
	
	private static void sendUserMessage(DataPackage dp)
	{
		messagesUserToSend.add((Message) dp.getValue());
	}
	
	private static ArrayList<Socket> sockets_files = new ArrayList<Socket>();
	private static ArrayList<FileDataPackage> received_files = new ArrayList<FileDataPackage>();
	
	private static Runnable acceptFiles = new Runnable()
	{
		@Override
		public void run()
		{
			new Thread(receiveFiles).start();
			new Thread(processFiles).start();
			new Thread(sendFiles).start();
			
			while(true)
			{
				try
				{
					Socket socket = srvFiles.accept();
					sockets_files.add(socket);
				}
				catch(Exception ex) {}
				
				Utils.sleep(1);
			}
		}
	};
	
	private static ArrayList<Integer> canSendFile = new ArrayList<Integer>();
	private static int response = 0;
	
	private static Runnable receiveFiles = new Runnable()
	{
		@Override
		public void run()
		{
			ObjectInputStream ois;
			
			while(true)
			{
				if(sockets_files.size() > 0)
				{
					for(int i = 0; i < sockets_files.size(); i++)
					{
						try
						{
							Socket socket = sockets_files.get(i);
							
							ois = new ObjectInputStream(socket.getInputStream());
							Object o = ois.readObject();
							
							if(o instanceof DataPackage)
							{
								DataPackage dp = (DataPackage) o;
								
								if(dp.getObjectName().equals("receive_file_data"))
								{
									int val = (int) dp.getValue();
									
									if(val == 1)
									{
										canContinueSending = true;
									}
									else if(val == 2)
									{
										if(!isSendingFile)
										{
											canSendFile.add(i);
										}
										
										response++;
									}
									else if(val == 3)
									{
										response++;
									}
								}
								else if(dp.getObjectName().equals("user_file_data"))
								{
									FileDataPackage fdp = (FileDataPackage) dp.getValue();
									received_files.add(fdp);
								}
							}
						}
						catch(Exception ex)
						{
							disconnectClient(i);
							i--;
						}
					}
				}
				
				Utils.sleep(1);
			}
		}
	};
	
	private static Runnable processFiles = new Runnable()
	{
		@Override
		public void run()
		{
			while(true)
			{
				if(received_files.size() > 0)
				{
					for(int i = 0; i < received_files.size(); i++)
					{
						try
						{
							FileDataPackage fdp = received_files.get(i);
							filesBytes.add(fdp);
		
							received_files.remove(i);
							i--;
						}
						catch(Exception e) {}
					}
				}
			
				Utils.sleep(1);
			}
		}
	};

	private static ArrayList<File> files = new ArrayList<File>();
	private static ArrayList<FileDataPackage> filesBytes = new ArrayList<FileDataPackage>();
	private static ArrayList<String> fileBytesHashes = new ArrayList<String>();
	private static ArrayList<Object[]> fileTemp = new ArrayList<Object[]>();
	
	private static boolean canContinueSending = false;
	private static boolean isSendingFile = false;
	
	private static Runnable sendFiles = new Runnable()
	{
		@Override
		public void run()
		{
			ObjectOutputStream oos;
			
			while(true)
			{				
				if(sockets_files.size() > 0)
				{
					for(int i = 0; i < filesBytes.size(); i++)
					{
						try
						{
							FileDataPackage fdp = filesBytes.get(i);
							
							String fileHash = fdp.getFileHash();
							String fileName = fdp.getFileName();
							
							long fileSize = fdp.getFileSize();
							int sentBytes = fdp.getBytes().length;
							
							if(!fileBytesHashes.contains(fileHash))
							{
								if(sockets_files.size() > 1)
								{
									for(int k = 0; k < sockets_files.size(); k++)
									{
										try
										{
											Socket socket = sockets_files.get(k);
											
											if(!socket.getInetAddress().getHostAddress().equals(fdp.getIP()))
											{
												oos = new ObjectOutputStream(socket.getOutputStream());
												
												FileDataPackage fdp0 = new FileDataPackage("Server", srvIP, fileHash, fileName, fileSize, 0);
												DataPackage dp = new DataPackage("confirm_receive", fdp0);
												
												oos.writeObject(dp);
											}
										}
										catch(Exception ex) {}
									}
									
									int to = 0;
									while(response != (sockets_files.size() - 1))
									{
										to++;
										if(to == 30000)
										{
											break;
										}
										
										Utils.sleep(1);
									}
									
									isSendingFile = true;									
									
									fileBytesHashes.add(fileHash);
									fileTemp.add(new Object[] {fileHash, sentBytes});
								}
							}
							
							if(sockets_files.size() > 1)
							{
								for(int x = 0; x < sockets_files.size(); x++)
								{
									try
									{
										Socket socket = sockets_files.get(x);
										int client_state = clients_states.get(x);

										if(client_state == 0 && !socket.getInetAddress().getHostAddress().equals(fdp.getIP()) && fileBytesHashes.contains(fileHash))
										{
											oos = new ObjectOutputStream(socket.getOutputStream());
											oos.writeObject(new DataPackage("file_data", fdp));
											canContinueSending = false;
											
											int timeout = 0;
											while(!canContinueSending)
											{
												timeout++;
												if(timeout == 8000)
												{
													logText("Client IP=" + socket.getLocalAddress().getHostAddress() + " has timed out!");
													
													canContinueSending = true;
													canSendFile.remove(new Integer(x));
												}
												
												Utils.sleep(1);
											}
										}
										else
										{
											disconnectClient(i);
										}
									}
									catch(Exception ex) {}
								}
							}
							else
							{
								break;
							}
							
							long allBytes = 0;
							int index = -1;
							
							for(Object[] o : fileTemp)
							{
								String fh = (String) o[0];
								index++;
								
								if(fh.equals(fileHash))
								{
									allBytes = (long) o[1];
									break;
								}
							}
							
							allBytes += sentBytes;
							fileTemp.set(index, new Object[] {fileHash, allBytes});
							
							if(allBytes >= fdp.getFileSize())
							{
								if(index > -1)
								{
									fileBytesHashes.remove(index);
									fileTemp.remove(index);
								}
								
								response = 0;
								isSendingFile = false;
							}
						}
						catch(Exception ex) {}
						
						filesBytes.remove(i);
						i--;
					}
					
					if(files.size() > 0)
					{
						for(int i = 0; i < files.size(); i++)
						{
							try
							{
								File f = files.get(i);
								
								byte[] buffer = new byte[8192];
								long fileSize = f.length();
								
								String fileName = f.getName();
								String fileHash = Utils.hashSHA1(Generator.genRandomString(20) + Utils.getCurrentDate());
								
								for(int k = 0; k < sockets_files.size(); k++)
								{
									try
									{
										Socket socket = sockets_files.get(k);
										oos = new ObjectOutputStream(socket.getOutputStream());
										
										FileDataPackage fdp = new FileDataPackage("Server", srvIP, fileHash, fileName, fileSize, 0);
										DataPackage dp = new DataPackage("confirm_receive", fdp);
										
										oos.writeObject(dp);
									}
									catch(Exception ex) {}
								}
								
								int to = 0;
								while(response != sockets_files.size())
								{
									to++;
									if(to == 30000)
									{
										break;
									}
									
									Utils.sleep(1);
								}
								
								isSendingFile = true;
								
								try
								{
									if(canSendFile.size() > 0)
									{
										FileInputStream fis = new FileInputStream(f);
										BufferedInputStream bis = new BufferedInputStream(fis);
			
										int read = 0;
										boolean close = false;
										
										while((read = bis.read(buffer)) != -1 && !close)
										{
											byte[] bytes = new byte[read];
											System.arraycopy(buffer, 0, bytes, 0, read);
											
											FileDataPackage fdp = new FileDataPackage("Server", srvIP, fileHash, fileName, fileSize, read).SetData(bytes);
											
											if(sockets_files.size() > 0)
											{
												for(int x = 0; x < sockets_files.size(); x++)
												{
													try
													{
														if(canSendFile.size() == 0)
														{
															close = true;
															break;
														}
														
														if(canSendFile.contains(x))
														{
															Socket socket = sockets_files.get(x);
															int client_state = clients_states.get(x);
															
															if(client_state == 0)
															{
																oos = new ObjectOutputStream(socket.getOutputStream());
																oos.writeObject(new DataPackage("file_data", fdp));
																canContinueSending = false;
																
																int timeout = 0;
																while(!canContinueSending)
																{
																	timeout++;
																	if(timeout == 8000)
																	{
																		logText("Client IP=" + socket.getLocalAddress().getHostAddress() + " has timed out!");
																		
																		canContinueSending = true;
																		canSendFile.remove(new Integer(x));
																	}
																	
																	Utils.sleep(1);
																}
															}
															else
															{
																disconnectClient(i);
															}
														}
													}
													catch(Exception ex) {}
												}
											}
											else
											{
												break;
											}
										}
										
										bis.close();
									}
								}
								catch(Exception e) {}
							}
							catch(Exception ex) {}
							
							files.remove(i);
							i--;
							
							response = 0;
							canSendFile.clear();
							isSendingFile = false;
						}
					}
				}
				
				Utils.sleep(1);
			}
		}
	};
	
	public static void sendFile(File f)
	{
		files.add(f);
	}

	private static void logText(Message msg)
	{
		String text = msg.getContent();
		String time = msg.getTime();
		String user = msg.getUsername();
		
		textArea.append("[" + user + " - " + time + "]: " + text + "\n");

		JScrollBar vertical = scrollPane.getVerticalScrollBar();
		vertical.setValue(vertical.getMaximum());
	}
	
	private static void logText(String text)
	{
		logText(text, "Info");
	}
	
	private static void logText(String text, String name)
	{
		String time = Utils.getCurrentDateFormatted();
		textArea.append("[" + name + " - " + time + "]: " + text + "\n");

		JScrollBar vertical = scrollPane.getVerticalScrollBar();
		vertical.setValue(vertical.getMaximum());
	}
	
	private static void disconnectClient(int index)
	{
		try
		{
			Socket socket = sockets_messages.get(index);
			String client_ip = socket.getInetAddress().getHostAddress();
			
			clients_states.remove(index);
			clients_usernames.remove(index);
			sockets_messages.remove(index);
			sockets_files.remove(index);

			list_clients_model.removeElementAt(index);
			logText("Client with IP=" + client_ip + " has been disconnected!");
		}
		catch(Exception ex) {}
	}
	
	private static void disconnect(int index)
	{
		clients_states.set(index, 1);
	}
	
	private static void shutdown()
	{
		for(int i = 0; i < clients_states.size(); i++)
		{
			try
			{
				clients_states.set(i, 2);
			}
			catch(Exception ex) {}
		}
	}
}