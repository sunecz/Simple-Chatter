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
						new Thread(new Runnable()
						{
							@Override
							public void run()
							{
								File f = fc.getSelectedFile();
								byte[] buffer = new byte[8192];
								
								try
								{
									FileInputStream fis = new FileInputStream(f);
									
									while(fis.read(buffer) > 0)
									{
										FileDataPackage fdp = new FileDataPackage("Server", "0.0.0.0", "file.txt").SetData(buffer);
										sendData(fdp);
										
										Utils.sleep(1);
									}

									fis.close();
								}
								catch(Exception e) {}
							}
						}).start();
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
					ip = InetAddress.getLocalHost().getHostAddress() + ":" + port;
					server = new ServerSocket(port, 0, InetAddress.getLocalHost());
					new Thread(accept).start();

					WindowServer(ip);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}
	
	public static int port = 2406;
	public static String ip = "";
	
	public static ServerSocket server;
	
	public static ArrayList<Socket> sockets = new ArrayList<Socket>();
	public static ArrayList<Integer> client_states = new ArrayList<Integer>();
	public static ArrayList<DataPackage> data = new ArrayList<DataPackage>();
	
	private static ArrayList<Message> messagesToSend = new ArrayList<Message>();
	private static ArrayList<Message> messagesUserToSend = new ArrayList<Message>();
	private static ArrayList<FileDataPackage> fileDataToSend = new ArrayList<FileDataPackage>();
	public static ArrayList<DataPackage> received = new ArrayList<DataPackage>();
	
	public static void sendMessage(String msg)
	{
		messagesToSend.add(new Message("Server", Utils.getCurrentDateFormatted(), msg, "0.0.0.0"));
	}
	
	public static void sendUserMessage(DataPackage dp)
	{
		messagesUserToSend.add(new Message(dp.getUsername(), dp.getTime(), (String) dp.getValue(), dp.getIP()));
	}
	
	public static void sendData(FileDataPackage fdp)
	{
		fileDataToSend.add(fdp);
	}
	
	private static Runnable accept = new Runnable()
	{
		@Override
		public void run()
		{
			new Thread(send).start();
			new Thread(receive).start();
			new Thread(process).start();
			
			while(true)
			{
				try
				{
					Socket socket = server.accept();
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
					list_clients_model.addElement(username + " - " + addr.getHostAddress() + " - " + addr.getHostName());

					client_states.add(0);
					data.add(new DataPackage("empty", ""));
					sockets.add(socket);
				}
				catch(Exception ex) {}
				
				Utils.sleep(1);
			}
		}
	};
	
	private static Runnable send = new Runnable()
	{
		@Override
		public void run()
		{
			ObjectOutputStream oos;
			
			while(true)
			{
				for(int i = 0; i < sockets.size(); i++)
				{
					try
					{					
						Socket socket = sockets.get(i);
						oos = new ObjectOutputStream(socket.getOutputStream());
						
						int client_state = client_states.get(i);
						oos.writeObject(new DataPackage("client_state", client_state));
						
						if(client_state == 0)
						{
							if(fileDataToSend.size() > 0)
							{
								for(FileDataPackage dat : fileDataToSend)
								{
									oos = new ObjectOutputStream(socket.getOutputStream());
									oos.writeObject(new DataPackage("file_package", dat.getBytes()));
									logText("sent file data");
								}
							}
							
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
								
								txtMessage.setText("");
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
				
				fileDataToSend.clear();
				messagesToSend.clear();
				messagesUserToSend.clear();
				
				Utils.sleep(1);
			}
		}
	};

	private static Runnable receive = new Runnable()
	{
		@Override
		public void run()
		{
			ObjectInputStream ois;
			
			while(true)
			{
				for(int i = 0; i < sockets.size(); i++)
				{
					try
					{
						ois = new ObjectInputStream(sockets.get(i).getInputStream());
						DataPackage dp = (DataPackage) ois.readObject();
						
						received.add(dp);
					}
					catch(Exception ex)
					{
						// Client disconnected (not notifying the server)
						disconnectClient(i);
						i--;
					}
				}
				
				Utils.sleep(1);
			}
		}
	};
	
	public static Runnable process = new Runnable()
	{
		@Override
		public void run()
		{
			while(true)
			{
				try
				{
					for(int i = 0; i < received.size(); i++)
					{
						DataPackage dp = received.get(i);
						
						if(dp.getObjectName().equals("client_state"))
						{
							int receive_state = (int) dp.getValue();
							
							if(receive_state == 0)
							{
								data.set(i, dp);
							}
							else
							{
								// Client disconnected by server
								disconnectClient(i);
								i--;
							}
						}
						
						if(dp.getObjectName().equals("message"))
						{
							logText(dp);
							sendUserMessage(dp);
						}
						
						received.remove(i);
						i--;
					}
				}
				catch(Exception e) {}

				Utils.sleep(1);
			}
		}
	};
	private static JPanel subPanel2;
	private static JButton btnSend;
	private static JLabel lblConsole;
	private static JButton btnSendFile;
	
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
			Socket socket = sockets.get(index);
			String cip = socket.getInetAddress().getHostAddress();
			
			client_states.remove(index);
			data.remove(index);
			sockets.remove(index);

			list_clients_model.removeElementAt(index);
			logText("Client with IP=" + cip + " has been disconnected!");
		}
		catch(Exception ex) {}
	}
	
	public static void disconnect(int index)
	{
		client_states.set(index, 1);
	}
	
	public static void shutdown()
	{
		for(int i = 0; i < client_states.size(); i++)
		{
			try
			{
				client_states.set(i, 2);
			}
			catch(Exception ex) {}
		}
	}
}