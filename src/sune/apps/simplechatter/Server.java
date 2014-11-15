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
					new FileSelector(new FileSelectorActions()
					{
						@Override
						public void Send(File f)
						{
							Server.sendFile(f);
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
						new Thread(processMessages).start();
						new Thread(sendMessages).start();
						new Thread(sendFiles).start();

						logText("Server has been started on " + srvIP + ":" + srvMessagesPort + "!");
					}
					catch(Exception ex)
					{
						logText("Server could not be started! Port is already in use!");
					}
				}
				catch(Exception ex) {}
			}
		});
	}
	
	private static String srvIP = "";

	private static int srvMessagesPort = 2400;
	private static int srvFilesPort = 2401;
	
	private static ServerSocket srvMessages;
	private static ServerSocket srvFiles;
	
	private static ArrayList<ClientThread> clients = new ArrayList<ClientThread>();
	private static boolean shutdown = false;
	
	private static ArrayList<Message> messagesToSend = new ArrayList<Message>();	
	private static ArrayList<File> files = new ArrayList<File>();

	private static Runnable acceptMessages = new Runnable()
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

						clients.add(new ClientThread(socket0, socket1, srvIP, clientAddr, clientName));
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

	private static Runnable processMessages = new Runnable()
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
	
	private static Runnable sendMessages = new Runnable()
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
						for(ClientThread client : clients)
						{
							client.addMessage(msg);
							logText(msg);
						}
					}

					messagesToSend.clear();
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
		messagesToSend.add((Message) dp.OBJECT);
	}
	
	private static Runnable sendFiles = new Runnable()
	{
		@Override
		public void run()
		{
			while(true)
			{
				if(clients.size() > 1)
				{
					for(ClientThread client : clients)
					{
						ArrayList<FileDataPackage> sent_files = client.getSentFiles();
						
						if(sent_files.size() > 0)
						{
							for(int i = 0; i < sent_files.size(); i++)
							{
								FileDataPackage fdp = (FileDataPackage) sent_files.get(i);
								
								for(ClientThread cli : clients)
								{
									if(!cli.getIP().equals(client.getIP()))
									{
										cli.addDataPackage(new DataPackage("file_data", fdp));
									}
								}
							
								for(ClientThread cli : clients)
								{
									int to = 0;
									while(cli.isWaiting() && to < 15000)
									{
										to++;											
										Utils.sleep(1);
									}
								}
								
								client.addDataPackage(new DataPackage("send_file", 1));
								client.removeSentFile(i);
								i--;
							}
						}
					}		
				}
				
				if(files.size() > 0)
				{
					for(File file : files)
					{
						try
						{
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
								
								FileDataPackage fdp = new FileDataPackage("Server", srvIP, fileHash, fileName, fileSize, read).SetData(bytes);
								
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
									break;
								}
								
								Utils.sleep(1);
							}
							
							bis.close();
						}
						catch(Exception ex) {}
					}
					
					files.clear();
				}
				
				Utils.sleep(1);
			}
		}
	};
	
	public static int getClientIndexByIP(String ip)
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
	
	public static void sendFile(File f)
	{
		files.add(f);
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
			ClientThread client = clients.get(index);
			String client_ip = client.getIP();
			String username = client.getUsername();
			client.setClientState(shutdown ? 2 : 1);
			
			clients.remove(index);
			list_clients_model.removeElementAt(index);
			
			logText(username + " with IP " + client_ip + " has been disconnected!");
		}
		catch(Exception ex) {}
	}
	
	private static void disconnect(int index)
	{
		disconnectClient(index);
	}
	
	private static void shutdown()
	{
		shutdown = true;
		for(ClientThread client : clients)
		{
			client.setClientState(2);
		}
	}
}