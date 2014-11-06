package net.sune.networking;

import java.awt.BorderLayout;
import java.awt.Color;
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.JProgressBar;

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
	private static JButton btnDisconnect;
	private static JButton btnConnect;
	private static JLabel lblDownloadInfo;
	private static JButton btnSendFiles;
	private static JPanel panel;
	private static JPanel subPanel3;
	private static JProgressBar prgbarDownload;
	
	private static void WindowClient(String ip)
	{
		frame = new JFrame();
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Client - " + ip);
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
		gbl_subPanel2.rowHeights = new int[]{27, 0, 0};
		gbl_subPanel2.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gbl_subPanel2.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		subPanel2.setLayout(gbl_subPanel2);
		
		btnConnect = new JButton("Connect");
		btnConnect.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if(e.getModifiers() == MouseEvent.BUTTON1_MASK)
				{
					connectToServerDialog();
				}
			}
		});
		
		btnConnect.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black, 1), BorderFactory.createEmptyBorder(4, 10, 6, 10)));
		GridBagConstraints gbc_btnConnect = new GridBagConstraints();
		gbc_btnConnect.fill = GridBagConstraints.BOTH;
		gbc_btnConnect.insets = new Insets(0, 0, 5, 5);
		gbc_btnConnect.gridx = 0;
		gbc_btnConnect.gridy = 0;
		subPanel2.add(btnConnect, gbc_btnConnect);
		
		btnDisconnect = new JButton("Disconnect");
		btnDisconnect.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if(e.getModifiers() == MouseEvent.BUTTON1_MASK)
				{
					try
					{
						disconnect();
					}
					catch(Exception ex) {}
				}
			}
		});
		
		btnDisconnect.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black, 1), BorderFactory.createEmptyBorder(4, 10, 6, 10)));
		GridBagConstraints gbc_btnDisconnect = new GridBagConstraints();
		gbc_btnDisconnect.fill = GridBagConstraints.BOTH;
		gbc_btnDisconnect.insets = new Insets(0, 0, 5, 0);
		gbc_btnDisconnect.gridx = 1;
		gbc_btnDisconnect.gridy = 0;
		subPanel2.add(btnDisconnect, gbc_btnDisconnect);
		
		subPanel3 = new JPanel();
		GridBagConstraints gbc_subPanel3 = new GridBagConstraints();
		gbc_subPanel3.gridwidth = 2;
		gbc_subPanel3.fill = GridBagConstraints.BOTH;
		gbc_subPanel3.gridx = 0;
		gbc_subPanel3.gridy = 1;
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
					}
				}
			}
		});
		
		subPanel1.add(btnSend);
		btnSend.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black, 1), BorderFactory.createEmptyBorder(4, 10, 6, 10)));
		
		panel = new JPanel();
		panel.setBorder(new EmptyBorder(5, 0, 0, 0));
		panel2.add(panel, BorderLayout.SOUTH);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] {480, 0};
		gbl_panel.rowHeights = new int[]{23, 0};
		gbl_panel.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		btnSendFiles = new JButton("Send files");
		btnSendFiles.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black, 1), BorderFactory.createEmptyBorder(4, 10, 6, 10)));
		btnSendFiles.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if(e.getModifiers() == MouseEvent.BUTTON1_MASK)
				{
					new FileSelectorClient();
				}
			}
		});
		
		GridBagConstraints gbc_btnSendFiles = new GridBagConstraints();
		gbc_btnSendFiles.anchor = GridBagConstraints.NORTHWEST;
		gbc_btnSendFiles.gridx = 0;
		gbc_btnSendFiles.gridy = 0;
		panel.add(btnSendFiles, gbc_btnSendFiles);
		
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
			
			WindowClient(localIP);
			connectToServerDialog();
			
			new Thread(receiveMessages).start();
			new Thread(sendMessages).start();
			new Thread(receiveFiles).start();
			new Thread(sendFiles).start();
		}
		catch(Exception e) {}
	}
	
	private static boolean enableThreads = true;
	private static void connectToServer(String serverIP, int serverPort, int serverPort2)
	{
		try
		{
			if(socket_messages != null)
			{
				socket_messages.close();
				socket_files.close();
				
				logText("Client has been disconnected from server " + srvIP + ":" + srvMessagesPort + "!");
			}
			
			enableThreads = false;
			logText("Connecting to " + serverIP + ":" + serverPort + "...");

			srvIP = serverIP;
			srvMessagesPort = serverPort;
			srvFilesPort = serverPort2;
			
			InetSocketAddress sa_messages = new InetSocketAddress(srvIP, srvMessagesPort);
			socket_messages = new Socket();
			socket_messages.connect(sa_messages, 8000);
			
			InetSocketAddress sa_files = new InetSocketAddress(srvIP, srvFilesPort);
			socket_files = new Socket();
			socket_files.connect(sa_files, 8000);
			
			ObjectOutputStream oos = new ObjectOutputStream(socket_messages.getOutputStream());
			oos.writeObject(new DataPackage("username", username));

			ObjectInputStream ois = new ObjectInputStream(socket_messages.getInputStream());
			DataPackage data = (DataPackage) ois.readObject();
			
			if(data.getObjectName().equals("message"))
			{
				logText(data);
			}
			
			enableThreads = true;
		}
		catch(Exception ex)
		{
			logText("Cannot connect to the server!");
		}
	}
	
	private static void connectToServerDialog()
	{
		String strIP = JOptionPane.showInputDialog(frame, "Server IP", "Connect to a server", JOptionPane.PLAIN_MESSAGE);
		
		if(strIP != null && !strIP.isEmpty())
		{
			String strPort = JOptionPane.showInputDialog(frame, "Server PORT", "Connect to a server", JOptionPane.PLAIN_MESSAGE);

			if(strPort != null && !strPort.isEmpty())
			{
				int intPort = Integer.parseInt(strPort);
				String strPort2 = JOptionPane.showInputDialog(frame, "Server PORT2", "Connect to a server", JOptionPane.PLAIN_MESSAGE);

				if(strPort2 != null && !strPort2.isEmpty())
				{
					int intPort2 = Integer.parseInt(strPort2);

					new Thread(new Runnable()
					{
						public void run()
						{
							connectToServer(strIP, intPort, intPort2);
						}
					}).start();						
				}					
			}
		}
	}
	
	private static String srvIP = "";
	private static String localIP = "";
	
	private static int srvMessagesPort = 2400;
	private static int srvFilesPort = 2401;
	
	private static Socket socket_messages;
	private static Socket socket_files;

	private static int client_state = 0;
	private static String username = "Unknown";
	
	private static ArrayList<DataPackage> received_messages = new ArrayList<DataPackage>();
	private static ArrayList<Message> messagesToSend = new ArrayList<Message>();
	
	private static Runnable receiveMessages = new Runnable()
	{
		@Override
		public void run()
		{
			new Thread(processMessages).start();
			ObjectInputStream ois;
			
			while(true)
			{
				try
				{
					if(enableThreads)
					{
						ois = new ObjectInputStream(socket_messages.getInputStream());
						DataPackage dp = (DataPackage) ois.readObject();
						
						received_messages.add(dp);
					}	
				}
				catch(Exception e) {}

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
				try
				{
					if(enableThreads)
					{
						for(int i = 0; i < received_messages.size(); i++)
						{
							DataPackage data = received_messages.get(i);
							
							if(data.getObjectName().equals("client_state"))
							{
								int receive_state = (int) data.getValue();
								
								switch(receive_state)
								{
									case 1:	JOptionPane.showMessageDialog(null, "You have been disconnected by the server!", "Disconnected", JOptionPane.INFORMATION_MESSAGE);	break;
									case 2: JOptionPane.showMessageDialog(null, "Server has been shut down!", "Disconnected", JOptionPane.INFORMATION_MESSAGE);	break;
								}

								if(receive_state != 0)
								{
									received_messages.clear();
									disconnect();
								}
								
								client_state = receive_state;
							}
							else if(data.getObjectName().equals("message"))
							{
								logText(data);
							}
							
							received_messages.remove(i);
							i--;
						}
					}
				}
				catch(Exception e) {}

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
				try
				{
					if(enableThreads)
					{
						if(messagesToSend.size() > 0)
						{
							for(int i = 0; i < messagesToSend.size(); i++)
							{
								Message msg = messagesToSend.get(i);
								
								oos = new ObjectOutputStream(socket_messages.getOutputStream());
								oos.writeObject(new DataPackage("message", msg.getContent(), msg.getUsername(), msg.getIP()));
								
								messagesToSend.remove(i);
								i--;
							}
	
							txtMessage.setText("");
						}
					}
				}
				catch(Exception e) {}
				
				Utils.sleep(1);
			}
		}
	};
	
	private static void sendMessage(String msg)
	{
		messagesToSend.add(new Message(username, Utils.getCurrentDateFormatted(), msg, localIP));
	}

	private static ArrayList<FileDataPackage> received_files = new ArrayList<FileDataPackage>();
	private static ArrayList<DataPackage> fileStatusesToSend = new ArrayList<DataPackage>();

	private static Runnable receiveFiles = new Runnable()
	{
		@Override
		public void run()
		{
			new Thread(processFiles).start();
			ObjectInputStream ois;
			
			while(true)
			{
				try
				{
					if(enableThreads)
					{
						ois = new ObjectInputStream(socket_files.getInputStream());
						DataPackage dp = (DataPackage) ois.readObject();
						
						int s = 0;
						if(dp.getObjectName().equals("file_data"))
						{
							FileDataPackage fdp = (FileDataPackage) dp.getValue();
							received_files.add(fdp);
							s = 1;
						}
						else if(dp.getObjectName().equals("confirm_receive"))
						{
							confirmReceiveFileData = (FileDataPackage) dp.getValue();
							confirmReceiveFile = true;
						}

						sendFileStatus(s);
					}
				}
				catch(Exception e) {}

				Utils.sleep(1);
			}
		}
	};
	
	private static ArrayList<String> fileHashes = new ArrayList<String>();
	private static ArrayList<String> fileBanned = new ArrayList<String>();
	private static BufferedOutputStream fileBOS = null;
	private static long downloaded = 0;
	
	private static FileDataPackage confirmReceiveFileData = null;
	private static boolean confirmReceiveFile = false;
	
	private static Runnable processFiles = new Runnable()
	{
		@Override
		public void run()
		{
			while(true)
			{
				try
				{
					if(enableThreads)
					{
						if(confirmReceiveFile)
						{
							String fHash = confirmReceiveFileData.getFileHash();
							String fName = confirmReceiveFileData.getFileName();
							String fUser = confirmReceiveFileData.getUsername();
							
							int confirm = JOptionPane.showConfirmDialog(frame, fUser + " is sending you a file (" + fName + ").\nWould you like to accept receiving?", "Receive file", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
							if(confirm == JOptionPane.YES_OPTION)
							{
								JFileChooser jfc = new JFileChooser();
								jfc.setName(fName);
								
								if(jfc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION)
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
						
						for(int i = 0; i < received_files.size(); i++)
						{
							FileDataPackage data = received_files.get(i);
							String hash = data.getFileHash();
							
							if(!fileBanned.contains(hash))
							{
								String fileName = data.getFileName();
								long fileSize = data.getFileSize();
								int index = fileHashes.indexOf(hash);
								byte[] bytes = data.getBytes();
								
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
					}
				}
				catch(Exception e) {}

				Utils.sleep(1);
			}
		}
	};
	
	private static ArrayList<File> files = new ArrayList<File>();

	private static Runnable sendFiles = new Runnable()
	{
		@Override
		public void run()
		{			
			ObjectOutputStream oos;
			
			while(true)
			{
				try
				{
					if(enableThreads)
					{
						if(fileStatusesToSend.size() > 0)
						{
							for(DataPackage dp : fileStatusesToSend)
							{
								oos = new ObjectOutputStream(socket_files.getOutputStream());
								oos.writeObject(dp);
							}
						}
						
						fileStatusesToSend.clear();
						
						if(files.size() > 0)
						{
							for(int i = 0; i < files.size(); i++)
							{
								File f = files.get(i);
								
								byte[] buffer = new byte[8192];
								long fileSize = f.length();
								
								String fileName = f.getName();
								String fileHash = Utils.hashSHA1(Generator.genRandomString(20) + Utils.getCurrentDate());
								
								try
								{
									FileInputStream fis = new FileInputStream(f);
									BufferedInputStream bis = new BufferedInputStream(fis);
		
									int read = 0;
									boolean close = false;
									
									while((read = bis.read(buffer)) != -1 && !close)
									{
										byte[] bytes = new byte[read];
										System.arraycopy(buffer, 0, bytes, 0, read);
										
										FileDataPackage fdp = new FileDataPackage(username, srvIP, fileHash, fileName, fileSize, read).SetData(bytes);
										
										try
										{
											if(client_state == 0)
											{
												oos = new ObjectOutputStream(socket_files.getOutputStream());
												oos.writeObject(new DataPackage("user_file_data", fdp));
											}
											else
											{
												disconnect();
											}
										}
										catch(Exception ex) {}
									}
									
									bis.close();
								}
								catch(Exception e) {}

								files.remove(i);
								i--;
							}
						}
					}
				}
				catch(Exception e) {}

				Utils.sleep(1);
			}
		}
	};

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
		String text = (String) dp.getValue();
		String time = dp.getTime();
		String user = dp.getUsername();
		
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
			socket_messages.close();
			socket_files.close();
		
			logText("Client has been disconnected from server " + srvIP + ":" + srvMessagesPort + "!");
		}
		catch(Exception ex) {}
	}
}