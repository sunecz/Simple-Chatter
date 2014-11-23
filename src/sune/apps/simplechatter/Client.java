package sune.apps.simplechatter;

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
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
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
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class Client
{
	private static JFrame frame;
	private static JPanel mainPanel;
	private static JTextArea textArea;
	private static JScrollPane scrollPane1;
	private static JPanel panel2;
	private static JTextField txtMessage;
	private static JPanel subPanel0;
	private static JButton btnSend;
	private static JPanel subPanel1;
	private static JMenuBar menuBar;
	private static JMenu mnClient;
	private static JMenuItem mntmConnect;
	private static JMenuItem mntmDisconnect;
	private static JMenu mnFile;
	private static JMenuItem mntmSendFiles;
	private static JPanel panel1;
	private static JPanel panel0;
	private static JScrollPane scrollPane0;
	private static JTable tableTransfers;
	private static DefaultTableModel tableTransfersModel;
	private static JMenuItem mntmReconnect;
	private static JPopupMenu transfersMenu;
	private static JMenuItem mntmCancelTransfer;
	
	private static void WindowClient()
	{
		frame = new JFrame();
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Client [Disconnected]");
		frame.setSize(700, 442);
		frame.setLocationRelativeTo(null);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		
		mainPanel = new JPanel();
		mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
		GridBagLayout gbl_mainPanel = new GridBagLayout();
		gbl_mainPanel.columnWidths = new int[]{300, 307, 0};
		gbl_mainPanel.rowHeights = new int[]{341, -14, 0};
		gbl_mainPanel.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gbl_mainPanel.rowWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		mainPanel.setLayout(gbl_mainPanel);
		
		panel0 = new JPanel();
		GridBagConstraints gbc_panel0 = new GridBagConstraints();
		gbc_panel0.insets = new Insets(0, 0, 5, 5);
		gbc_panel0.fill = GridBagConstraints.BOTH;
		gbc_panel0.gridx = 0;
		gbc_panel0.gridy = 0;
		mainPanel.add(panel0, gbc_panel0);
		panel0.setLayout(new GridLayout(0, 1, 0, 0));
		
		scrollPane0 = new JScrollPane();
		panel0.add(scrollPane0);
		
		tableTransfersModel = new DefaultTableModel();
		tableTransfersModel.addColumn("File name");
		tableTransfersModel.addColumn("Transfer type");
		tableTransfersModel.addColumn("Status");
		
		tableTransfers = new JTable(tableTransfersModel);
		tableTransfers.addMouseListener(new MouseListener()
		{
			@Override
			public void mousePressed(MouseEvent e)
			{
				if(tableTransfers.getSelectedRowCount() > 0)
				{
					if(e.getModifiers() == MouseEvent.BUTTON3_MASK)
					{
						transfersMenu.show(tableTransfers, e.getX(), e.getY());
					}
				}
			}

			@Override
			public void mouseReleased(MouseEvent e)
			{
				if(tableTransfers.getSelectedRowCount() > 0)
				{
					if(e.getModifiers() == MouseEvent.BUTTON3_MASK)
					{
						transfersMenu.show(tableTransfers, e.getX(), e.getY());
					}
				}
			}
			
			@Override public void mouseClicked(MouseEvent e) {}
			@Override public void mouseEntered(MouseEvent e) {}
			@Override public void mouseExited(MouseEvent e) {}
		});
		
		tableTransfers.setFillsViewportHeight(true);
		scrollPane0.setBorder(BorderFactory.createLineBorder(Color.black, 1));
		scrollPane0.setViewportView(tableTransfers);
		
		panel1 = new JPanel();
		GridBagConstraints gbc_panel1 = new GridBagConstraints();
		gbc_panel1.fill = GridBagConstraints.BOTH;
		gbc_panel1.insets = new Insets(0, 0, 5, 0);
		gbc_panel1.gridx = 1;
		gbc_panel1.gridy = 0;
		mainPanel.add(panel1, gbc_panel1);
		panel1.setLayout(new GridLayout(0, 1, 0, 0));
		
		scrollPane1 = new JScrollPane();
		panel1.add(scrollPane1);
		scrollPane1.setBorder(BorderFactory.createLineBorder(Color.black, 1));
		
		textArea = new JTextArea();
		textArea.setWrapStyleWord(true);
		textArea.setLineWrap(true);
		scrollPane1.setViewportView(textArea);
		textArea.setFont(new Font("Consolas", Font.PLAIN, 12));
		textArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		textArea.setEditable(false);
		
		panel2 = new JPanel();
		GridBagConstraints gbc_panel2 = new GridBagConstraints();
		gbc_panel2.gridwidth = 2;
		gbc_panel2.fill = GridBagConstraints.BOTH;
		gbc_panel2.gridx = 0;
		gbc_panel2.gridy = 1;
		mainPanel.add(panel2, gbc_panel2);
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
				disconnect(true);
			}
		});
		
		mntmReconnect = new JMenuItem("Reconnect");
		mntmReconnect.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				reconnect();
			}
		});
		
		mnClient.add(mntmReconnect);
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

		transfersMenu = new JPopupMenu();
		mntmCancelTransfer = new JMenuItem("Cancel transfer");
		mntmCancelTransfer.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if(tableTransfers.getSelectedRowCount() > 0)
				{
					int[] rows = tableTransfers.getSelectedRows();
					
					for(int i = rows.length - 1; i > -1; i--)
					{
						Object[] obj = tableInfo.get(rows[i]);
						
						String hash = (String) obj[1];
						String type = (String) obj[2];
						
						cancelTransfer(hash, type, true);
					}					
				}
			}
		});
		
		transfersMenu.add(mntmCancelTransfer);
		
		frame.setVisible(true);
		frame.addWindowListener(new WindowListener()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				disconnect(false);
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
			
			WindowClient();
			connectToServerDialog();
			
			new Thread(receiveMessages).start();
			new Thread(processMessages).start();
			new Thread(sendMessages).start();
			
			new Thread(receiveFiles).start();
			new Thread(processFiles).start();
			new Thread(sendFiles).start();
			
			new Thread(renderTransfers).start();
		}
		catch(Exception ex) {}
	}

	private static String srvIP = "";
	private static String localIP = "";
	
	private final static int srvMessagesPort = 2400;
	private final static int srvFilesPort = 2401;
	
	private static Socket socket_messages;
	private static Socket socket_files;

	private static int client_state = 0;
	private static String username = "Unknown";
	private static boolean enableThreads = true;
	
	private static boolean showDisconnectMessage = true;
	private static boolean disconnected = false;
	
	private static ArrayList<DataPackage> received_messages = new ArrayList<>();
	private static ArrayList<DataPackage> messagesToSend = new ArrayList<>();
	
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
					
					client_state = 0;
					enableThreads = true;
					disconnected = false;
					showDisconnectMessage = true;
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
					catch(Exception ex) {}
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
						while(received_messages.size() > 0)
						{
							try
							{
								DataPackage dp = received_messages.get(0);
								
								if(dp.OBJECT_NAME.equals("client_state"))
								{
									if(client_state == 0)
									{
										int state = (int) dp.OBJECT;
										client_state = state;
										
										if(showDisconnectMessage)
										{
											switch(state)
											{
												case 1:	JOptionPane.showMessageDialog(frame, "You have been disconnected from the server!", "Disconnected", JOptionPane.INFORMATION_MESSAGE); break;
												case 2:	JOptionPane.showMessageDialog(frame, "Server has been shut down!", "Disconnected", JOptionPane.INFORMATION_MESSAGE); break;
											}
										}
		
										if(state != 0)
										{
											disconnect(true);
										}
									}
								}
								else if(dp.OBJECT_NAME.equals("message"))
								{
									logText((Message) dp.OBJECT);
								}
								else if(dp.OBJECT_NAME.equals("send_file"))
								{
									isWaiting = false;
								}
								else if(dp.OBJECT_NAME.equals("disconnect_response"))
								{
									disconnected = true;
								}
							}
							catch(Exception ex) {}
							
							received_messages.remove(0);
							Utils.sleep(1);
						}
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
						while(messagesToSend.size() > 0)
						{
							try
							{
								DataPackage dp = messagesToSend.get(0);
								
								oos = new ObjectOutputStream(new BufferedOutputStream(socket_messages.getOutputStream()));
								oos.writeObject(dp);
								oos.flush();
							}
							catch(Exception ex) {}
							
							messagesToSend.remove(0);
						}
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
	
	private static ArrayList<FileDataPackage> received_files = new ArrayList<>();
	private static ArrayList<FileTransfer> fileTransfers = new ArrayList<>();
	private static ArrayList<File> files = new ArrayList<File>();
	
	private static ArrayList<String> fileBanned = new ArrayList<>();
	private static ArrayList<FileSaver> fileSavers = new ArrayList<>();
	private static ArrayList<Object[]> tableInfo = new ArrayList<>();
	
	private static FileDataPackage confirmReceiveFileData = null;
	private static boolean isWaiting = false;
	
	private static Runnable renderTransfers = new Runnable()
	{
		@Override
		public void run()
		{
			while(true)
			{
				if(tableInfo.size() > 0)
				{
					for(int i = 0; i < tableInfo.size(); i++)
					{
						try
						{
							Object[] obj = tableInfo.get(i);
							String hash = (String) obj[1];
							String type = (String) obj[2];
							
							if(type.equals("receive"))
							{
								int index = getFileSaverIndex(hash);
								
								FileSaver saver = fileSavers.get(index);
								String fileName = saver.getFileName();
								String transferType = "Download";
								
								long fileSize = saver.getFileSize();
								long downloaded = saver.getSavedBytes();
								
								double percent = Math.round(((double) (downloaded * 100)) / ((double) fileSize) * 10.0) / 10.0;
								String status = percent + "%";
								
								tableTransfersModel.setValueAt(fileName, i, 0);
								tableTransfersModel.setValueAt(transferType, i, 1);
								tableTransfersModel.setValueAt(status, i, 2);
							}
							else if(type.equals("send"))
							{
								int index = getFileTransferIndex(hash);
								
								FileTransfer transfer = fileTransfers.get(index);
								String fileName = transfer.getFileName();
								String transferType = "Send";
								
								long fileSize = transfer.getTotalBytes();
								long downloaded = transfer.getReadBytes();
								
								double percent = Math.round(((double) (downloaded * 100)) / ((double) fileSize) * 10.0) / 10.0;
								String status = percent + "%";
								
								tableTransfersModel.setValueAt(fileName, i, 0);
								tableTransfersModel.setValueAt(transferType, i, 1);
								tableTransfersModel.setValueAt(status, i, 2);
							}
						}
						catch(Exception ex) {}
					}
				}

				Utils.sleep(1);
			}
		}
	};
	
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

						if(dp.OBJECT_NAME.equals("file_data"))
						{
							received_files.add((FileDataPackage) dp.OBJECT);
							sendFileStatus(1);
						}
						else if(dp.OBJECT_NAME.equals("confirm_receive"))
						{
							confirmReceiveFileData = (FileDataPackage) dp.OBJECT;
						}
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
						if(confirmReceiveFileData != null)
						{
							String fHash = confirmReceiveFileData.FILE_HASH;
							String fName = confirmReceiveFileData.FILE_NAME;
							String fUser = confirmReceiveFileData.USERNAME;
							long fSize = confirmReceiveFileData.FILE_SIZE;
							
							int confirm = JOptionPane.showConfirmDialog(frame, fUser + " is sending you a file (" + fName + ").\nWould you like to accept receiving?", "Receive file", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
							if(confirm == JOptionPane.YES_OPTION)
							{
								JFileChooser jfc = new JFileChooser();
								jfc.setName(fName);
								
								if(jfc.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION)
								{
									String path = jfc.getSelectedFile().getAbsolutePath();
									fileSavers.add(new FileSaver(path, fName, fHash, fSize));
									tableTransfersModel.addRow(new Object[] {fName, "Download", "0%"});
									tableInfo.add(new Object[] {tableTransfers.getRowCount(), fHash, "receive"});
									
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
						}
						
						if(received_files.size() > 0)
						{
							while(received_files.size() > 0)
							{
								try
								{
									FileDataPackage data = received_files.get(0);
									String hash = data.FILE_HASH;
									
									if(!fileBanned.contains(hash))
									{
										int index = getFileSaverIndex(hash);
										FileSaver saver = fileSavers.get(index);
										
										long fileSize = data.FILE_SIZE;
										byte[] bytes = data.BYTES;
										
										saver.save(bytes);
										long downloaded = saver.getSavedBytes();
		
										if(downloaded >= fileSize)
										{
											fileSavers.remove(index);
											
											int inds[] = getTableInfo(hash);
											tableTransfersModel.removeRow(inds[1]);
											tableInfo.remove(inds[0]);
										}
									}
								}
								catch(Exception ex) {}
								
								received_files.remove(0);
							}
						}
					}
					catch(Exception ex) {}
				}
					
				Utils.sleep(1);
			}
		}
	};
	
	private static int getFileSaverIndex(String hash)
	{
		int index = -1;
		for(FileSaver saver : fileSavers)
		{
			index++;
			if(hash.equals(saver.getFileHash()))
			{
				break;
			}
		}
		
		return index;
	}
	
	private static int getFileTransferIndex(String UID)
	{
		int index = -1;
		for(FileTransfer transfer : fileTransfers)
		{
			index++;
			if(UID.equals(transfer.getUID()))
			{
				break;
			}
		}
		
		return index;
	}
	
	private static int[] getTableInfo(String val)
	{
		int index0 = -1;
		int index1 = -1;
		int itype = -1;
		
		for(Object[] obj : tableInfo)
		{
			index0++;
			if(val.equals((String) obj[1]))
			{
				index1 = (int) obj[0] - 1;
				
				switch(((String) obj[2]))
				{
					case "receive": 	itype = 0; 	break;
					case "send": 		itype = 1; 	break;
				}

				break;
			}
		}
		
		return new int[] {index0, index1, itype};
	}

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
					if(files.size() > 0)
					{
						while(files.size() > 0)
						{
							try
							{
								File file = files.get(0);
								String fileName = file.getName();
								String fileHash = Utils.hashSHA1(Generator.genRandomString(20) + Utils.getCurrentDate());
								
								ArrayList<FileDataPackage> fdps = new ArrayList<>();
								FileTransfer transfer = new FileTransfer(file, new FileTransferInterface()
								{
									@Override
									public void beginTransfer(FileTransfer transfer)
									{
										fileTransfers.add(transfer);
										tableTransfersModel.addRow(new Object[] {transfer.getFileName(), "Send", "0%"});
										tableInfo.add(new Object[] {tableTransfers.getRowCount(), transfer.getUID(), "send"});
									}
									
									@Override
									public void readBytes(FileTransfer transfer, FileTransfer.BytesInfo bytesInfo)
									{
										byte[] bytes = bytesInfo.getBytes();
										long totalBytes = bytesInfo.getTotalBytes();
										
										FileDataPackage fdp = new FileDataPackage(username, srvIP, fileHash, fileName, totalBytes, bytes.length).SetData(bytes);
										fdps.add(fdp);
										
										isWaiting = true;
										while(isWaiting)
										{
											Utils.sleep(1);
										}
									}

									@Override
									public void canceled(FileTransfer transfer)
									{
										int ind = -1;
										for(FileTransfer ftr : fileTransfers)
										{
											ind++;
											if(ftr.getUID().equals(transfer.getUID()))
											{
												break;
											}
										}
										
										fileTransfers.remove(ind);
										
										int inds[] = getTableInfo(transfer.getUID());
										tableTransfersModel.removeRow(inds[1]);
										tableInfo.remove(inds[0]);
									}
									
									@Override
									public void completed(FileTransfer transfer)
									{
										int ind = -1;
										for(FileTransfer ftr : fileTransfers)
										{
											ind++;
											if(ftr.getUID().equals(transfer.getUID()))
											{
												break;
											}
										}
										
										fileTransfers.remove(ind);
										
										int inds[] = getTableInfo(transfer.getUID());
										tableTransfersModel.removeRow(inds[1]);
										tableInfo.remove(inds[0]);
									}
									
									@Override public void paused(FileTransfer transfer) {}
									@Override public void proceed(FileTransfer transfer) {}
								});
								
								transfer.begin();
								while(fileTransfers.size() > 0)
								{
									while(fdps.size() > 0)
									{
										FileDataPackage fdp = fdps.get(0);
										
										try
										{
											if(client_state == 0)
											{
												oos = new ObjectOutputStream(new BufferedOutputStream(socket_files.getOutputStream()));
												oos.writeObject(new DataPackage("user_file_data", fdp));
												oos.flush();
											}
										}
										catch(Exception ex) {}
										
										fdps.remove(0);
										Utils.sleep(1);
									}
									
									Utils.sleep(1);
								}

								files.remove(0);
							}
							catch(Exception ex) {}
						}
					}
				}

				Utils.sleep(1);
			}
		}
	};

	private static void cancelTransfer(String value, String type, boolean smsg)
	{
		try
		{
			messagesToSend.add(new DataPackage("cancel_sending", 1, username, localIP));
			
			if(value.equals("all"))
			{
				fileSavers.clear();
				fileTransfers.clear();
				
				for(int i = tableTransfersModel.getRowCount(); i > -1; i--)
				{
					tableTransfersModel.removeRow(i);
				}
			}
			else
			{
				if(type.equals("receive"))
				{
					int index = getFileSaverIndex(value);
					fileSavers.remove(index);
					
					int inds[] = getTableInfo(value);
					tableTransfersModel.removeRow(inds[1]);
					tableInfo.remove(inds[0]);
				}
				else if(type.equals("send"))
				{
					int index = getFileTransferIndex(value);
					fileTransfers.remove(index);
					
					int inds[] = getTableInfo(value);
					tableTransfersModel.removeRow(inds[1]);
					tableInfo.remove(inds[0]);
				}
			}
			
			if(smsg)
			{
				JOptionPane.showMessageDialog(frame, "File transfer has been canceled!", "Canceled", JOptionPane.INFORMATION_MESSAGE);
			}
		}
		catch(Exception ex) {}
	}
	
	public static void sendFile(File f)
	{
		files.add(f);
	}
	
	private static void sendFileStatus(int s)
	{
		messagesToSend.add(new DataPackage("receive_file_data", s, username, localIP));
	}

	private static void logText(DataPackage dp)
	{
		String text = (String) dp.OBJECT;
		String time = dp.TIME;
		String user = dp.USERNAME;
		
		textArea.append("[" + user + " - " + time + "]: " + text + "\n");

		JScrollBar vertical = scrollPane1.getVerticalScrollBar();
		vertical.setValue(vertical.getMaximum());
	}
	
	private static void logText(Message msg)
	{
		String text = msg.CONTENT;
		String time = msg.TIME;
		String user = msg.USERNAME;
		
		textArea.append("[" + user + " - " + time + "]: " + text + "\n");

		JScrollBar vertical = scrollPane1.getVerticalScrollBar();
		vertical.setValue(vertical.getMaximum());
	}
	
	private static void logText(String text)
	{
		String time = Utils.getCurrentDateFormatted();
		textArea.append("[Info - " + time + "]: " + text + "\n");

		JScrollBar vertical = scrollPane1.getVerticalScrollBar();
		vertical.setValue(vertical.getMaximum());
	}
	
	private static void disconnect(boolean showMessage)
	{
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					if(!disconnected)
					{
						showDisconnectMessage = showMessage;
						sendData(new DataPackage("disconnect", 1, username, localIP));

						while(!disconnected)
						{
							Utils.sleep(1);
						}

						enableThreads = false;
						disconnected = true;
						showDisconnectMessage = true;
						
						cancelTransfer("all", "", false);
						logText("Client has been disconnected from server " + srvIP + ":" + srvMessagesPort + "!");
					}
					else
					{
						logText("Client is alredy disconnected!");
					}
				}
				catch(Exception ex) {}
			}
		}).start();
	}
	
	private static void reconnect()
	{
		disconnect(false);
		
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				while(true)
				{
					if(disconnected)
					{
						connectToServer(srvIP, srvMessagesPort, srvFilesPort);
						break;
					}
					
					Utils.sleep(1);
				}
			}
		}).start();
	}
}