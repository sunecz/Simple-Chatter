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
import javax.swing.JFileChooser;
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
	public static JFrame frame;
	public static JPanel contentPane;
	public static JPanel panel0;
	public static JPanel panel1;
	public static JPanel subPanel0;
	public static JPanel subPanel1;
	
	public static JList<String> list_clients;
	public static DefaultListModel<String> list_clients_model;
	public static JButton btnDisconnect;
	public static JLabel lblConnectedClients;
	
	public static JTextField txtMessage;
	private static JScrollPane scrollPane;
	private static JTextArea textArea;
	private static JPanel panelList;
	private static JPanel panel2;
	
	private static JPanel subPanel2;
	private static JButton btnSend;
	private static JLabel lblConsole;
	private static JButton btnSendFile;

	public static void WindowServer(String ip)
	{
		frame = new JFrame();
		frame.setTitle("Server - " + ip);
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
		
		btnSendFile = new JButton("Send file");
		btnSendFile.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if(e.getModifiers() == MouseEvent.BUTTON1_MASK)
				{
					JFileChooser fc = new JFileChooser();
					fc.setMultiSelectionEnabled(false);
					
					if(fc.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION)
					{
						File f = fc.getSelectedFile();
						sendFile(f);
					}
				}
			}
		});
		
		panel1.add(btnSendFile, BorderLayout.WEST);
		
		frame.setContentPane(contentPane);

		panel2 = new JPanel();
		contentPane.add(panel2, BorderLayout.EAST);
		panel2.setLayout(new GridLayout(0, 1, 0, 0));
		
		scrollPane = new JScrollPane();
		panel2.add(scrollPane);
		scrollPane.setBorder(new EmptyBorder(0, 5, 0, 0));
		scrollPane.setLayout(new ScrollPaneLayout());
		scrollPane.setViewportView(textArea);
		
		textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		textArea.setWrapStyleWord(true);
		textArea.setLineWrap(true);
		textArea.setColumns(50);
		textArea.setFont(new Font("Consolas", Font.PLAIN, 12));
		textArea.setEditable(false);
		textArea.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black, 1), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		
		frame.setVisible(true);
		
		btnDisconnect.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
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
		});
		
		txtMessage.addKeyListener(new KeyListener()
		{
			@Override
			public void keyPressed(KeyEvent e)
			{
				if(e.getKeyCode() == KeyEvent.VK_ENTER)
				{
					sendMessage(txtMessage.getText());
				}
			}

			@Override public void keyReleased(KeyEvent e) {}
			@Override public void keyTyped(KeyEvent e) {}
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
					
					srvMessages = new ServerSocket(srvMessagesPort, 0, addr);
					srvFiles = new ServerSocket(srvFilesPort, 0, addr);

					new Thread(acceptMessages).start();
					new Thread(acceptFiles).start();
					
					WindowServer(srvIP);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}
	
	public static String srvIP = "";

	public static int srvMessagesPort = 2400;
	public static int srvFilesPort = 2401;
	
	public static ServerSocket srvMessages;
	public static ServerSocket srvFiles;
	
	public static ArrayList<Socket> sockets_messages = new ArrayList<Socket>();
	public static ArrayList<Integer> clients_states = new ArrayList<Integer>();
	public static ArrayList<DataPackage> received_messages = new ArrayList<DataPackage>();
	
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
					
					ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
					DataPackage dp = (DataPackage) ois.readObject();
					
					String username = "";
					if(dp.getObjectName().equals("username"))
					{
						username = (String) dp.getValue();
					}

					ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
					oos.writeObject(new DataPackage("message", "Welcome to the server, " + username + "!"));
					logText(username + " with IP " + socket.getInetAddress().getHostAddress() + " connected to the server!");

					InetAddress addr = socket.getInetAddress();
					String clientAddr = addr.getHostAddress();
					String clientName = addr.getHostName();
					
					list_clients_model.addElement(username + " - " + clientAddr + " - " + clientName);

					clients_states.add(0);
					sockets_messages.add(socket);
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
				for(int i = 0; i < sockets_messages.size(); i++)
				{
					try
					{
						ois = new ObjectInputStream(sockets_messages.get(i).getInputStream());
						DataPackage dp = (DataPackage) ois.readObject();
						
						received_messages.add(dp);
					}
					catch(Exception ex)
					{
						disconnectClient(i);
						i--;
					}
				}
				
				Utils.sleep(1);
			}
		}
	};
	
	public static Runnable processMessages = new Runnable()
	{
		@Override
		public void run()
		{
			while(true)
			{
				try
				{
					for(int i = 0; i < received_messages.size(); i++)
					{
						DataPackage dp = received_messages.get(i);
						
						if(dp.getObjectName().equals("client_state"))
						{
							int receive_state = (int) dp.getValue();
							
							if(receive_state != 0)
							{
								disconnectClient(i);
								i--;
							}
						}
						
						if(dp.getObjectName().equals("message"))
						{
							logText(dp);
							sendUserMessage(dp);
						}
						
						received_messages.remove(i);
						i--;
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
				for(int i = 0; i < sockets_messages.size(); i++)
				{
					try
					{					
						Socket socket = sockets_messages.get(i);
						oos = new ObjectOutputStream(socket.getOutputStream());
						
						int client_state = clients_states.get(i);
						oos.writeObject(new DataPackage("client_state", client_state));
						
						if(client_state == 0)
						{
							if(messagesToSend.size() > 0)
							{
								for(Message msg : messagesToSend)
								{
									oos = new ObjectOutputStream(socket.getOutputStream());
									oos.writeObject(new DataPackage("message", msg.getContent()));
									
									logText(msg.getContent(), "Server");
								}

								txtMessage.setText("");
							}
							
							if(messagesUserToSend.size() > 0)
							{
								for(Message msg : messagesUserToSend)
								{
									oos = new ObjectOutputStream(socket.getOutputStream());
									oos.writeObject(new DataPackage("message", msg.getContent(), msg.getUsername(), msg.getIP()));
								}
							}
						}
						else
						{
							disconnectClient(i);
							i--;
						}
					}
					catch(Exception ex) {}
				}

				messagesToSend.clear();
				messagesUserToSend.clear();
				
				Utils.sleep(1);
			}
		}
	};	
	
	public static void sendMessage(String msg)
	{
		messagesToSend.add(new Message("Server", Utils.getCurrentDateFormatted(), msg, "0.0.0.0"));
	}
	
	public static void sendUserMessage(DataPackage dp)
	{
		messagesUserToSend.add(new Message(dp.getUsername(), dp.getTime(), (String) dp.getValue(), dp.getIP()));
	}
	
	public static ArrayList<Socket> sockets_files = new ArrayList<Socket>();
	public static ArrayList<FileDataPackage> received_files = new ArrayList<FileDataPackage>();

	private static ArrayList<FileDataPackage> filesToSend = new ArrayList<FileDataPackage>();
	private static ArrayList<FileDataPackage> filesUserToSend = new ArrayList<FileDataPackage>();
	
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
	
	private static Runnable receiveFiles = new Runnable()
	{
		@Override
		public void run()
		{
			ObjectInputStream ois;
			
			while(true)
			{
				for(int i = 0; i < sockets_files.size(); i++)
				{
					try
					{
						Socket socket = sockets_files.get(i);
						
						ois = new ObjectInputStream(socket.getInputStream());
						Object o = ois.readObject();
						
						if(o instanceof FileDataPackage)
						{
							FileDataPackage fdp = (FileDataPackage) o;
							received_files.add(fdp);
						}
						else if(o instanceof DataPackage)
						{
							DataPackage dp = (DataPackage) o;
							
							if(dp.getObjectName().equals("receive_file_data"))
							{
								int val = (int) dp.getValue();
								
								if(val == 1)
									canContinueSending = true;
							}
						}
					}
					catch(Exception ex)
					{
						disconnectClient(i);
						i--;
					}
				}
				
				Utils.sleep(1);
			}
		}
	};
	
	public static Runnable processFiles = new Runnable()
	{
		@Override
		public void run()
		{
			while(true)
			{
				try
				{
					for(int i = 0; i < received_files.size(); i++)
					{
						FileDataPackage dp = received_files.get(i);
						sendUserFileData(dp);

						received_files.remove(i);
						i--;
					}
				}
				catch(Exception e) {}

				Utils.sleep(1);
			}
		}
	};
	
	public static boolean canContinueSending = false;
	public static boolean canSend = false;
	
	private static Runnable sendFiles = new Runnable()
	{
		@Override
		public void run()
		{
			ObjectOutputStream oos;
			
			while(true)
			{
				if(canSend)
				{
					for(int i = 0; i < sockets_files.size(); i++)
					{
						try
						{
							Socket socket = sockets_files.get(i);
							int client_state = clients_states.get(i);
							
							if(client_state == 0)
							{
								if(filesToSend.size() > 0)
								{
									for(FileDataPackage fdp : filesToSend)
									{
										oos = new ObjectOutputStream(socket.getOutputStream());
										oos.writeObject(new DataPackage("file_data", fdp));
										canContinueSending = false;
										
										while(!canContinueSending)
										{
											Utils.sleep(1);
										}
									}
								}
							}
							else
							{
								disconnectClient(i);
								i--;
							}
						}
						catch(Exception ex) {}
					}
	
					filesToSend.clear();
					filesUserToSend.clear();
				}
				
				Utils.sleep(1);
			}
		}
	};
	
	public static void sendFile(File f)
	{
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				byte[] buffer = new byte[8192];
				String fileName = f.getName();
				long fileSize = f.length();
				
				try
				{
					FileInputStream fis = new FileInputStream(f);
					BufferedInputStream bis = new BufferedInputStream(fis);
					
					int read = 0;
					canSend = false;
					
					while((read = bis.read(buffer)) != -1)
					{
						byte[] bytes = new byte[read];
						System.arraycopy(buffer, 0, bytes, 0, read);
						
						FileDataPackage fdp = new FileDataPackage("Server", "0.0.0.0", fileName, fileSize, read).SetData(bytes);
						sendFileData(fdp);
					}

					fis.close();
					canSend = true;
				}
				catch(Exception e) {}
			}
		}).start();
	}
	
	public static void sendFileData(FileDataPackage fdp)
	{
		filesToSend.add(fdp);
	}
	
	public static void sendUserFileData(FileDataPackage fdp)
	{
		filesUserToSend.add(fdp);
	}

	public static void logText(DataPackage dp)
	{
		String text = (String) dp.getValue();
		String time = dp.getTime();
		String user = dp.getUsername();
		
		textArea.append("[" + user + " - " + time + "]: " + text + "\n");

		JScrollBar vertical = scrollPane.getVerticalScrollBar();
		vertical.setValue(vertical.getMaximum());
	}
	
	public static void logText(String text)
	{
		logText(text, "Info");
	}
	
	public static void logText(String text, String name)
	{
		String time = Utils.getCurrentDateFormatted();
		textArea.append("[" + name + " - " + time + "]: " + text + "\n");

		JScrollBar vertical = scrollPane.getVerticalScrollBar();
		vertical.setValue(vertical.getMaximum());
	}
	
	public static void disconnectClient(int index)
	{
		try
		{
			Socket socket = sockets_messages.get(index);
			String client_ip = socket.getInetAddress().getHostAddress();
			
			clients_states.remove(index);
			sockets_messages.remove(index);
			sockets_files.remove(index);

			list_clients_model.removeElementAt(index);
			logText("Client with IP=" + client_ip + " has been disconnected!");
		}
		catch(Exception ex) {}
	}
	
	public static void disconnect(int index)
	{
		clients_states.set(index, 1);
	}
	
	public static void shutdown()
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