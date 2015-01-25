package sune.apps.simplechatter;

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
import java.io.BufferedOutputStream;
import java.io.File;
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
	public final int SERVER_MESSAGES_PORT = 2400;
	public final int SERVER_FILES_PORT = 2401;
	
	private JFrame frame;
	private JPanel contentPane;
	private JPanel panel0;
	private JPanel panel1;
	private JPanel subPanel0;
	private JPanel subPanel1;
	private JList<String> list_clients;
	private DefaultListModel<String> list_clients_model;
	private JButton btnDisconnect;
	private JLabel lblConnectedClients;
	private JTextField txtMessage;
	private JScrollPane scrollPane;
	private JTextArea textArea;
	private JPanel panelList;
	private JPanel panel2;
	private JPanel subPanel2;
	private JButton btnSend;
	private JLabel lblConsole;
	private JButton btnSendFiles;

	private String srvIP = "";

	private ServerSocket srvMessages;
	private ServerSocket srvFiles;
	
	private ArrayList<ClientThread> clients = new ArrayList<ClientThread>();
	private boolean shutdown = false;
	
	private ArrayList<Message> messagesToSend = new ArrayList<Message>();	
	private ArrayList<File> files = new ArrayList<File>();
	
	private ArrayList<FileTransfer> fileTransfers = new ArrayList<>();
	private ArrayList<FileDataPackage> filesUsers = new ArrayList<>();

	public Server(InetAddress addr)
	{
		srvIP = addr.getHostAddress();
		init();
		
		try
		{
			srvMessages = new ServerSocket(SERVER_MESSAGES_PORT, 0, addr);
			srvFiles = new ServerSocket(SERVER_FILES_PORT, 0, addr);

			new Thread(acceptMessages).start();
			new Thread(processMessages).start();
			new Thread(sendMessages).start();
			new Thread(sendFiles).start();
			new Thread(sendUserFiles).start();

			logText("Server has been started on " + srvIP + ":" + SERVER_MESSAGES_PORT + "!");
		}
		catch(Exception ex)
		{
			logText("Server could not be started! Port is already in use!");
		}
	}
	
	public void init()
	{
		frame = new JFrame();
		frame.setTitle("Server - " + srvIP + ":" + SERVER_MESSAGES_PORT);
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
					new FileSelector(new FileSelectorActions()
					{
						@Override
						public void Send(File f)
						{
							sendFile(f);
						}
					});
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
							JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
						txtMessage.setText("");
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
						txtMessage.setText("");
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
				while(!shutdown)
				{
					Utils.sleep(1);
				}
				
				System.exit(0);
			}

			@Override public void windowActivated(WindowEvent e) {}
			@Override public void windowClosed(WindowEvent e) {}
			@Override public void windowDeactivated(WindowEvent e) {}
			@Override public void windowDeiconified(WindowEvent e) {}
			@Override public void windowIconified(WindowEvent e) {}
			@Override public void windowOpened(WindowEvent e) {}
		});
		
		frame.setVisible(true);
	}

	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					new Server(InetAddress.getLocalHost());
				}
				catch(Exception ex) {}
			}
		});
	}

	private Runnable acceptMessages = new Runnable()
	{
		@Override
		public void run()
		{
			while(true)
			{
				try
				{
					Socket socket0 = srvMessages.accept();
					Socket socket1 = srvFiles.accept();

					InetAddress addr = socket0.getInetAddress();
					String clientAddr = addr.getHostAddress();
					String clientName = addr.getHostName();
					boolean validSocket = true;
					
					if(clients.size() > 0)
					{
						for(ClientThread client : clients)
						{
							if(client.getIP().equals(clientAddr))
							{
								validSocket = false;
							}
						}
					}
					
					if(validSocket)
					{
						ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(socket0.getInputStream()));
						DataPackage dp = (DataPackage) ois.readObject();
						
						String username = "";
						if(dp.OBJECT_NAME.equals("username"))
						{
							username = (String) dp.OBJECT;
						}

						ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(socket0.getOutputStream()));
						oos.writeObject(new DataPackage("message", "Welcome to the server, " + username + "!"));
						oos.flush();

						clients.add(createClientThread(socket0, socket1, clientAddr, clientName));
						list_clients_model.addElement(username + " - " + clientAddr + " - " + clientName);

						sendMessage(username + " with IP " + clientAddr + " connected to the server!");
					}
					else
					{
						ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(socket0.getOutputStream()));
						oos.writeObject(new DataPackage("message", "Client is already connected!"));
						oos.flush();
					}
				}
				catch(Exception ex) {}
				
				Utils.sleep(1);
			}
		}
	};

	private Runnable processMessages = new Runnable()
	{
		@Override
		public void run()
		{
			while(true)
			{
				for(int i = 0; i < clients.size(); i++)
				{
					ClientThread client = clients.get(i);
					
					if(client.getClientState() == 0)
					{
						ArrayList<DataPackage> messages = client.getReceivedMessages();
						
						if(messages.size() > 0)
						{
							for(DataPackage dp : messages)
							{
								if(dp.OBJECT_NAME.equals("message"))
								{
									sendUserMessage(dp);
								}
								
								if(dp.OBJECT_NAME.equals("disconnect"))
								{
									client.addMessage(new DataPackage("disconnect_response", 1));
									
									disconnectClient(i);
									i--;
									
									break;
								}
							}
							
							client.clearMessages();
						}
					}
					else
					{
						disconnectClient(i);
						i--;
					}
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
			while(true)
			{
				if(messagesToSend.size() > 0)
				{
					for(Message msg : messagesToSend)
					{
						DataPackage dp = new DataPackage("message", msg);
						logText(msg);
						
						for(ClientThread client : clients)
						{
							client.addMessage(dp);
						}
					}

					messagesToSend.clear();
				}
				
				Utils.sleep(1);
			}
		}
	};	
	
	private void sendMessage(String msg)
	{
		messagesToSend.add(new Message("Server", Utils.getCurrentDateFormatted(), msg, srvIP));
	}
	
	private void sendUserMessage(DataPackage dp)
	{
		messagesToSend.add((Message) dp.OBJECT);
	}

	public void addUserFile(FileDataPackage fdp)
	{
		filesUsers.add(fdp);
	}
	
	private Runnable sendFiles = new Runnable()
	{
		@Override
		public void run()
		{			
			while(true)
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
								}
								
								@Override
								public void readBytes(FileTransfer transfer, FileTransfer.BytesInfo bytesInfo)
								{
									byte[] bytes = bytesInfo.getBytes();
									long totalBytes = bytesInfo.getTotalBytes();
									
									FileDataPackage fdp = new FileDataPackage("Server", srvIP, fileHash, fileName, totalBytes, bytes.length).SetData(bytes);
									fdps.add(fdp);
									
									if(clients.size() > 0)
									{
										for(ClientThread client : clients)
										{
											client.addDataPackage(new DataPackage("file_data", fdp));
										}
										
										for(ClientThread client : clients)
										{
											int to = 0;
											while(client.isWaiting() && to < 15000)
											{
												to++;											
												Utils.sleep(1);
											}
										}
									}
									else
									{
										transfer.cancel();
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
								}
								
								@Override public void paused(FileTransfer transfer) {}
								@Override public void proceed(FileTransfer transfer) {}
							});
							
							transfer.begin();
							files.remove(0);
						}
						catch(Exception ex) {}
					}
				}
				
				Utils.sleep(1);
			}
		}
	};
	
	private Runnable sendUserFiles = new Runnable()
	{
		@Override
		public void run()
		{			
			while(true)
			{
				if(clients.size() > 1)
				{
					while(filesUsers.size() > 0)
					{
						FileDataPackage fdp = filesUsers.get(0);
						
						if(fdp != null)
						{
							ClientThread client = null;
							
							for(ClientThread cli : clients)
							{
								try
								{
									if(!cli.getIP().equals(fdp.IP))	cli.addDataPackage(new DataPackage("file_data", fdp));
									else							client = cli;
								}
								catch(Exception ex) {}
							}
							
							if(client != null)
							{
								client.addMessage(new DataPackage("send_file", 1));
							}
						}
						
						filesUsers.remove(0);
					}
				}
				else
				{
					if(clients.size() > 0)
					{
						if(filesUsers.size() > 0)
						{
							ClientThread client = clients.get(0);
							client.addMessage(new DataPackage("send_file", filesUsers.get(0).FILE_HASH));
							
							filesUsers.clear();
						}
					}
				}
				
				Utils.sleep(1);
			}
		}
	};
	
	public int getClientIndexByIP(String ip)
	{
		for(int x = 0; x < clients.size(); x++)
		{
			ClientThread cli = clients.get(x);
			
			if(cli.getIP().equals(ip))
			{
				return x;
			}
		}
		
		return -1;
	}
	
	public void sendFile(File f)
	{
		files.add(f);
	}

	private void logText(Message msg)
	{
		String text = msg.CONTENT;
		String time = msg.TIME;
		String user = msg.USERNAME;
		
		textArea.append("[" + user + " - " + time + "]: " + text + "\n");

		JScrollBar vertical = scrollPane.getVerticalScrollBar();
		vertical.setValue(vertical.getMaximum());
	}
	
	private void logText(String text)
	{
		logText(text, "Info");
	}
	
	private void logText(String text, String name)
	{
		String time = Utils.getCurrentDateFormatted();
		textArea.append("[" + name + " - " + time + "]: " + text + "\n");

		JScrollBar vertical = scrollPane.getVerticalScrollBar();
		vertical.setValue(vertical.getMaximum());
	}
	
	private void disconnectClient(int index)
	{
		try
		{
			ClientThread client = clients.get(index);
			String client_ip = client.getIP();
			String username = client.getUsername();
			client.setClientState(shutdown ? 2 : 1);
			
			clients.remove(index);
			list_clients_model.removeElementAt(index);
			
			String message = username + " with IP " + client_ip + " has been disconnected!";
			
			sendMessage(message);
			logText(message);
		}
		catch(Exception ex) {}
	}
	
	private void disconnect(int index)
	{
		disconnectClient(index);
	}
	
	private void shutdown()
	{
		for(ClientThread client : clients)
		{
			client.setClientState(2);
		}
		
		clients.clear();
		shutdown = true;
	}
	
	public ServerSocket getSocketMessages()
	{
		return srvMessages;
	}
	
	public ServerSocket getSocketFiles()
	{
		return srvFiles;
	}
	
	private ClientThread createClientThread(Socket socket0, Socket socket1, String clientAddr, String clientName)
	{
		return new ClientThread(this, socket0, socket1, clientAddr, clientName);
	}
}