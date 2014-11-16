package sune.apps.simplechatter;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class Client
{
	private static JFrame frame;
	private static JPanel panel0;
	private static JLabel lblMessages;
	private static JPanel panel1;
	private static JTextArea textArea;
	private static JScrollPane scrollPane;
	private static JPanel panel2;
	private static JTextField txtMessage;
	private static JPanel subPanel0;
	private static JButton btnSend;
	private static JPanel subPanel1;
	private static JPanel subPanel2;
	private static JLabel lblDownloadInfo;
	private static JPanel subPanel3;
	private static JProgressBar prgbarDownload;
	private static JMenuBar menuBar;
	private static JMenu mnClient;
	private static JMenuItem mntmConnect;
	private static JMenuItem mntmDisconnect;
	private static JMenu mnFile;
	private static JMenuItem mntmCancelSending;
	private static JMenuItem mntmSendFiles;
	
	private static void WindowClient(String ip, int port)
	{
		frame = new JFrame();
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Client - " + ip + ":" + port);
		frame.setSize(506, 442);
		frame.setLocationRelativeTo(null);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		
		panel0 = new JPanel();
		panel0.setBorder(new EmptyBorder(10, 10, 0, 10));
		frame.getContentPane().add(panel0, BorderLayout.NORTH);
		panel0.setLayout(new BorderLayout(0, 0));
		
		lblMessages = new JLabel("Messages");
		panel0.add(lblMessages, BorderLayout.SOUTH);
		lblMessages.setFont(new Font("Tahoma", Font.PLAIN, 11));
		
		subPanel2 = new JPanel();
		subPanel2.setBorder(new EmptyBorder(0, 0, 10, 0));
		panel0.add(subPanel2, BorderLayout.NORTH);
		GridBagLayout gbl_subPanel2 = new GridBagLayout();
		gbl_subPanel2.columnWidths = new int[]{160, 160, 0};
		gbl_subPanel2.rowHeights = new int[]{0, 0};
		gbl_subPanel2.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gbl_subPanel2.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		subPanel2.setLayout(gbl_subPanel2);
		
		subPanel3 = new JPanel();
		GridBagConstraints gbc_subPanel3 = new GridBagConstraints();
		gbc_subPanel3.gridwidth = 2;
		gbc_subPanel3.fill = GridBagConstraints.BOTH;
		gbc_subPanel3.gridx = 0;
		gbc_subPanel3.gridy = 0;
		subPanel2.add(subPanel3, gbc_subPanel3);
		subPanel3.setLayout(new BorderLayout(0, 0));
		
		lblDownloadInfo = new JLabel("No downloads are running");
		subPanel3.add(lblDownloadInfo, BorderLayout.WEST);
		
		prgbarDownload = new JProgressBar();
		subPanel3.add(prgbarDownload, BorderLayout.SOUTH);
		
		panel1 = new JPanel();
		panel1.setBorder(new EmptyBorder(3, 10, 10, 10));
		frame.getContentPane().add(panel1, BorderLayout.CENTER);
		panel1.setLayout(new BorderLayout(0, 0));

		scrollPane = new JScrollPane();
		scrollPane.setBorder(BorderFactory.createLineBorder(Color.black, 1));
		panel1.add(scrollPane);
		
		textArea = new JTextArea();
		textArea.setWrapStyleWord(true);
		textArea.setLineWrap(true);
		scrollPane.setViewportView(textArea);
		textArea.setFont(new Font("Consolas", Font.PLAIN, 12));
		textArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		textArea.setEditable(false);
		
		panel2 = new JPanel();
		panel2.setBorder(new EmptyBorder(5, 0, 0, 0));
		panel1.add(panel2, BorderLayout.SOUTH);
		panel2.setLayout(new BorderLayout(0, 0));
		
		subPanel0 = new JPanel();
		subPanel0.setBorder(new EmptyBorder(0, 0, 0, 0));
		panel2.add(subPanel0, BorderLayout.CENTER);
		subPanel0.setLayout(new GridLayout(0, 1, 0, 0));
		
		txtMessage = new JTextField();
		subPanel0.add(txtMessage);
		txtMessage.setFont(new Font("Consolas", Font.PLAIN, 12));
		txtMessage.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black, 1), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		txtMessage.setColumns(10);
		
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
						txtMessage.setText("");
					}
				}
			}

			@Override public void keyReleased(KeyEvent e) {}
			@Override public void keyTyped(KeyEvent e) {}
		});

		subPanel1 = new JPanel();
		subPanel1.setBorder(new EmptyBorder(0, 5, 0, 0));
		panel2.add(subPanel1, BorderLayout.EAST);
		subPanel1.setLayout(new GridLayout(0, 1, 0, 0));
		
		btnSend = new JButton("Send");
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
						txtMessage.setText("");
					}
				}
			}
		});
		
		subPanel1.add(btnSend);
		btnSend.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black, 1), BorderFactory.createEmptyBorder(4, 10, 6, 10)));
		
		menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		mnClient = new JMenu("Client");
		menuBar.add(mnClient);
		
		mntmConnect = new JMenuItem("Connect");
		mntmConnect.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				connectToServerDialog();
			}
		});
		
		mnClient.add(mntmConnect);
		
		mntmDisconnect = new JMenuItem("Disconnect");
		mntmDisconnect.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				disconnect();
			}
		});
		
		mnClient.add(mntmDisconnect);
		
		mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		mntmSendFiles = new JMenuItem("Send files");
		mntmSendFiles.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				new FileSelector(new FileSelectorActions()
				{
					@Override
					public void Send(File f)
					{
						Client.sendFile(f);
					}
				});
			}
		});
		
		mnFile.add(mntmSendFiles);
		
		mntmCancelSending = new JMenuItem("Cancel receiving");
		mntmCancelSending.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				cancelReceiving(true);
			}
		});
		
		mnFile.add(mntmCancelSending);
		
		frame.setVisible(true);
		frame.addWindowListener(new WindowListener()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				try
				{
					socket_messages.close();
					socket_files.close();
				}
				catch(Exception ex) {}
				
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
		try
		{
			localIP = InetAddress.getLocalHost().getHostAddress();
			username = System.getProperty("user.name");
			
			WindowClient(localIP, srvMessagesPort);
			connectToServerDialog();
			
			new Thread(receiveMessages).start();
			new Thread(processMessages).start();
			new Thread(sendMessages).start();
			
			new Thread(receiveFiles).start();
			new Thread(processFiles).start();
			new Thread(sendFiles).start();
		}
		catch(Exception ex) {}
	}

	private static String srvIP = "";
	private static String localIP = "";
	
	private final static int srvMessagesPort = 2400;
	private final static int srvFilesPort = 2401;
	
	private static Socket socket_messages;
	private static Socket socket_files;

	private static String username = "Unknown";
	private static boolean enableThreads = true;
	private static boolean disconnected = true;
	
	private static ArrayList<DataPackage> received_messages = new ArrayList<DataPackage>();
	private static ArrayList<DataPackage> messagesToSend = new ArrayList<DataPackage>();
	
	private static void connectToServer(String serverIP, int serverPort, int serverPort2)
	{
		if(socket_messages != null && socket_files != null)
		{
			try
			{
				socket_messages.close();
				socket_files.close();
				
				logText("Client has been disconnected from server " + srvIP + ":" + srvMessagesPort + "!");
			}
			catch(Exception ex) {}			
		}
		
		srvIP = serverIP;
		enableThreads = false;
		
		frame.setTitle("Client " + serverIP + ":" + serverPort);
		logText("Connecting to " + serverIP + ":" + serverPort + "...");
		
		try
		{
			socket_messages = new Socket();
			socket_messages.connect(new InetSocketAddress(srvIP, srvMessagesPort), 8000);
			socket_messages.setSoTimeout(8000);

			socket_files = new Socket();
			socket_files.connect(new InetSocketAddress(srvIP, srvFilesPort), 8000);
			socket_files.setSoTimeout(8000);
		}
		catch(Exception ex)
		{
			socket_messages = null;
			socket_files = null;
		}
		finally
		{
			if(socket_messages != null && socket_files != null)
			{
				try
				{
					ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(socket_messages.getOutputStream()));
					oos.writeObject(new DataPackage("username", username));
					oos.flush();

					ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(socket_messages.getInputStream()));
					DataPackage data = (DataPackage) ois.readObject();
					
					if(data.OBJECT_NAME.equals("message"))
					{
						logText(data);
					}
					
					enableThreads = true;
					disconnected = false;
				}
				catch(Exception ex) {}
			}
			else
			{
				logText("Cannot connect to the server!");
			}
		}
	}
	
	private static void connectToServerDialog()
	{
		String strIP = JOptionPane.showInputDialog(frame, "Server IP", "Connect to a server", JOptionPane.QUESTION_MESSAGE);
		
		if(strIP != null && !strIP.isEmpty())
		{
			if(Utils.isValidIP(strIP))
			{
				new Thread(new Runnable()
				{
					public void run()
					{
						connectToServer(strIP, srvMessagesPort, srvFilesPort);
					}
				}).start();	
			}
			else
			{
				JOptionPane.showMessageDialog(frame, "Invalid IP address", "Connect to a server", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	private static Runnable receiveMessages = new Runnable()
	{
		@Override
		public void run()
		{
			ObjectInputStream ois;
			
			while(true)
			{
				if(enableThreads)
				{
					try
					{
						ois = new ObjectInputStream(new BufferedInputStream(socket_messages.getInputStream()));
						DataPackage dp = (DataPackage) ois.readObject();

						received_messages.add(dp);
						
						if(dp.OBJECT_NAME.equals("message"))
						{
							sendData(new DataPackage("send_message", 1));
						}
					}
					catch(Exception ex)
					{
						if(!disconnected)
						{
							disconnect();
						}
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
				if(enableThreads)
				{
					if(received_messages.size() > 0)
					{
						for(DataPackage data : received_messages)
						{
							if(data.OBJECT_NAME.equals("message"))
							{
								logText((Message) data.OBJECT);
							}
						}
					
						received_messages.clear();
					}
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
				if(enableThreads)
				{
					if(messagesToSend.size() > 0)
					{
						for(DataPackage dp : messagesToSend)
						{
							try
							{
								oos = new ObjectOutputStream(new BufferedOutputStream(socket_messages.getOutputStream()));
								oos.writeObject(dp);
								oos.flush();
							}
							catch(Exception ex) {}
						}

						messagesToSend.clear();
					}
				}

				Utils.sleep(1);
			}
		}
	};
	
	private static void sendMessage(String msg)
	{
		messagesToSend.add(new DataPackage("message", new Message(username, Utils.getCurrentDateFormatted(), msg, localIP), username, localIP));
	}
	
	private static void sendData(DataPackage dp)
	{
		messagesToSend.add(dp);
	}
	
	private static ArrayList<FileDataPackage> received_files = new ArrayList<FileDataPackage>();
	private static ArrayList<DataPackage> fileStatusesToSend = new ArrayList<DataPackage>();
	private static ArrayList<String> fileHashes = new ArrayList<String>();
	private static ArrayList<String> fileBanned = new ArrayList<String>();
	private static ArrayList<File> files = new ArrayList<File>();

	private static FileDataPackage confirmReceiveFileData = null;
	private static boolean confirmReceiveFile = false;
	private static BufferedOutputStream fileBOS = null;
	private static long downloaded = 0;
	
	private static boolean isWaiting = false;
	
	private static Runnable receiveFiles = new Runnable()
	{
		@Override
		public void run()
		{
			ObjectInputStream ois;
			
			while(true)
			{
				if(enableThreads)
				{
					try
					{
						ois = new ObjectInputStream(new BufferedInputStream(socket_files.getInputStream()));
						DataPackage dp = (DataPackage) ois.readObject();

						int s = 0;
						if(dp.OBJECT_NAME.equals("file_data"))
						{
							FileDataPackage fdp = (FileDataPackage) dp.OBJECT;
							received_files.add(fdp);
							s = 1;
						}
						else if(dp.OBJECT_NAME.equals("confirm_receive"))
						{
							confirmReceiveFileData = (FileDataPackage) dp.OBJECT;
							confirmReceiveFile = true;
						}
						else if(dp.OBJECT_NAME.equals("send_file"))
						{
							isWaiting = false;
						}

						sendFileStatus(s);
					}
					catch(Exception ex) {}
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
				if(enableThreads)
				{
					try
					{
						if(confirmReceiveFile)
						{
							String fHash = confirmReceiveFileData.FILE_HASH;
							String fName = confirmReceiveFileData.FILE_NAME;
							String fUser = confirmReceiveFileData.USERNAME;
							
							int confirm = JOptionPane.showConfirmDialog(frame, fUser + " is sending you a file (" + fName + ").\nWould you like to accept receiving?", "Receive file", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
							if(confirm == JOptionPane.YES_OPTION)
							{
								JFileChooser jfc = new JFileChooser();
								jfc.setName(fName);
								
								if(jfc.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION)
								{
									String path = jfc.getSelectedFile().getAbsolutePath();
									FileOutputStream fos = new FileOutputStream(path);
									
									fileBOS = new BufferedOutputStream(fos);
									fileHashes.add(fHash);
									
									sendFileStatus(2);
								}
								else
								{
									fileBanned.add(fHash);
									sendFileStatus(3);
								}
							}
							else
							{
								fileBanned.add(fHash);
								sendFileStatus(3);
							}
							
							confirmReceiveFileData = null;
							confirmReceiveFile = false;
						}
						
						if(received_files.size() > 0)
						{
							for(int i = 0; i < received_files.size(); i++)
							{
								try
								{
									FileDataPackage data = received_files.get(i);
									String hash = data.FILE_HASH;
									
									if(!fileBanned.contains(hash))
									{
										String fileName = data.FILE_NAME;
										long fileSize = data.FILE_SIZE;
										int index = fileHashes.indexOf(hash);
										byte[] bytes = data.BYTES;
										
										fileBOS.write(bytes);
										downloaded += bytes.length;
										
										double percent = Math.round(((double) (downloaded * 100)) / ((double) fileSize) * 10.0) / 10.0;
										lblDownloadInfo.setText("Downloading " + fileName + "... " + percent + "%");
										prgbarDownload.setValue((int) Math.round(percent));
		
										if(downloaded >= fileSize)
										{
											fileHashes.remove(index);
											lblDownloadInfo.setText("No downloads are running");
											prgbarDownload.setValue(0);
											
											downloaded = 0;
											fileBOS.close();
										}
									}
									
									received_files.remove(i);
									i--;
								}
								catch(Exception ex) {}
							}
						}
					}
					catch(Exception ex) {}
				}
					
				Utils.sleep(1);
			}
		}
	};

	private static Runnable sendFiles = new Runnable()
	{
		@Override
		public void run()
		{			
			ObjectOutputStream oos;
			
			while(true)
			{
				if(enableThreads)
				{
					if(fileStatusesToSend.size() > 0)
					{
						for(DataPackage dp : fileStatusesToSend)
						{
							try
							{
								oos = new ObjectOutputStream(new BufferedOutputStream(socket_files.getOutputStream()));
								oos.writeObject(dp);
								oos.flush();
							}
							catch(Exception ex) {}
						}
						
						fileStatusesToSend.clear();
					}

					if(files.size() > 0)
					{
						for(int i = 0; i < files.size(); i++)
						{
							try
							{
								File file = files.get(i);
								
								byte[] buffer = new byte[8192];
								long fileSize = file.length();
								
								String fileName = file.getName();
								String fileHash = Utils.hashSHA1(Generator.genRandomString(20) + Utils.getCurrentDate());
								BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
	
								int read = 0;
								while((read = bis.read(buffer)) != -1)
								{
									byte[] bytes = new byte[read];
									System.arraycopy(buffer, 0, bytes, 0, read);
									
									FileDataPackage fdp = new FileDataPackage(username, srvIP, fileHash, fileName, fileSize, read).SetData(bytes);
									
									try
									{
										oos = new ObjectOutputStream(new BufferedOutputStream(socket_files.getOutputStream()));
										oos.writeObject(new DataPackage("user_file_data", fdp));
										oos.flush();
									}
									catch(Exception ex) {}
									
									while(isWaiting)
									{
										Utils.sleep(1);
									}
									
									Utils.sleep(1);
								}
								
								bis.close();
								
								files.remove(i);
								i--;
							}
							catch(Exception ex) {}
						}
					}
				}

				Utils.sleep(1);
			}
		}
	};

	private static void cancelReceiving(boolean smsg)
	{
		try
		{
			fileStatusesToSend.add(new DataPackage("cancel_sending", 1, username, localIP));
			
			fileHashes.remove(0);
			lblDownloadInfo.setText("No downloads are running");
			prgbarDownload.setValue(0);
			downloaded = 0;

			fileBOS.close();
		}
		catch(Exception ex) {}
		finally
		{
			if(smsg)
			{
				JOptionPane.showMessageDialog(frame, "File receiving has been canceled!", "Canceled", JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}
	
	public static void sendFile(File f)
	{
		files.add(f);
	}
	
	private static void sendFileStatus(int s)
	{
		fileStatusesToSend.add(new DataPackage("receive_file_data", s, username, localIP));
	}

	private static void logText(DataPackage dp)
	{
		String text = (String) dp.OBJECT;
		String time = dp.TIME;
		String user = dp.USERNAME;
		
		textArea.append("[" + user + " - " + time + "]: " + text + "\n");

		JScrollBar vertical = scrollPane.getVerticalScrollBar();
		vertical.setValue(vertical.getMaximum());
	}
	
	private static void logText(Message msg)
	{
		String text = msg.CONTENT;
		String time = msg.TIME;
		String user = msg.USERNAME;
		
		textArea.append("[" + user + " - " + time + "]: " + text + "\n");

		JScrollBar vertical = scrollPane.getVerticalScrollBar();
		vertical.setValue(vertical.getMaximum());
	}
	
	private static void logText(String text)
	{
		String time = Utils.getCurrentDateFormatted();
		textArea.append("[Info - " + time + "]: " + text + "\n");

		JScrollBar vertical = scrollPane.getVerticalScrollBar();
		vertical.setValue(vertical.getMaximum());
	}
	
	private static void disconnect()
	{
		try
		{
			if(!socket_messages.isClosed() && !socket_files.isClosed())
			{
				disconnected = true;
				socket_messages.close();
				socket_files.close();

				JOptionPane.showMessageDialog(frame, "You have been disconnected from the server!", "Disconnected", JOptionPane.INFORMATION_MESSAGE);
				
				logText("Client has been disconnected from server " + srvIP + ":" + srvMessagesPort + "!");
				cancelReceiving(false);
			}
			else
			{
				logText("Client is alredy disconnected!");
			}
		}
		catch(Exception ex) {}
	}
}