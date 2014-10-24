package net.sune.networking;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
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
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class Server
{
	public static JFrame frame;
	public static JPanel contentPane;
	public static JPanel panel0;
	public static JPanel panel1;
	public static JPanel panel2;
	public static JPanel panel3;
	
	public static JList<String> list_clients;
	public static DefaultListModel<String> list_clients_model;
	public static JButton btnDisconnect;
	public static JLabel lblServerIP;
	public static JLabel lblConnectedClients;
	public static JLabel lblMessage;
	
	public static JTextField txtMessage;
	private static JScrollPane scrollPane;
	private static JTextArea textArea;
	private static JPanel panelc;
	private static JPanel panel;

	public static void WindowServer(String ip)
	{
		try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}
		
		frame = new JFrame();
		frame.setTitle("Server");
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(100, 100, 730, 430);
		frame.setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		contentPane.setLayout(new BorderLayout(0, 0));
		
		list_clients_model = new DefaultListModel<String>();
		
		panel0 = new JPanel();
		panel0.setBorder(new EmptyBorder(0, 0, 5, 0));
		panel0.setLayout(new BorderLayout(0, 0));
		
		lblConnectedClients = new JLabel("Connected clients");
		lblConnectedClients.setHorizontalAlignment(SwingConstants.LEFT);
		
		panel1 = new JPanel();
		panel1.setBorder(new EmptyBorder(5, 0, 0, 0));
		panel1.setLayout(new BorderLayout(0, 0));
		
		lblServerIP = new JLabel("Server IP: " + ip);
		btnDisconnect = new JButton("Disconnect");
		
		panel2 = new JPanel();
		panel2.setBorder(new EmptyBorder(0, 0, 5, 0));
		panel2.setLayout(new BorderLayout(0, 0));

		panel3 = new JPanel();
		panel3.setBorder(new EmptyBorder(0, 5, 0, 0));
		panel3.setLayout(new BorderLayout(0, 0));
		
		lblMessage = new JLabel("Message:");
		txtMessage = new JTextField();
		txtMessage.setColumns(100);
		txtMessage.setFont(new Font("Consolas", Font.PLAIN, 12));

		panel0.add(lblConnectedClients);
		panel1.add(btnDisconnect, BorderLayout.EAST);
		panel1.add(lblServerIP, BorderLayout.WEST);
		panel1.add(panel2, BorderLayout.NORTH);
		panel2.add(lblMessage, BorderLayout.WEST);
		panel2.add(panel3, BorderLayout.CENTER);
		panel3.add(txtMessage);
		
		panelc = new JPanel();
		contentPane.add(panelc, BorderLayout.CENTER);
		panelc.setLayout(new GridLayout(0, 1, 0, 0));

		list_clients = new JList<String>();
		panelc.add(list_clients);
		list_clients.setBorder(new LineBorder(new Color(0, 0, 0)));
		list_clients.setModel(list_clients_model);
		contentPane.add(panel0, BorderLayout.NORTH);
		contentPane.add(panel1, BorderLayout.SOUTH);
		
		frame.setContentPane(contentPane);
		
		Border border = BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black, 1), BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		panel = new JPanel();
		contentPane.add(panel, BorderLayout.EAST);
		panel.setLayout(new GridLayout(0, 1, 0, 0));
		
		scrollPane = new JScrollPane();
		panel.add(scrollPane);
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
		textArea.setBorder(border);
		
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
	private static ArrayList<DataPackage> dataToSend = new ArrayList<DataPackage>();
	public static ArrayList<DataPackage> received = new ArrayList<DataPackage>();
	
	public static void sendMessage(String msg)
	{
		messagesToSend.add(new Message("Server", Utils.getCurrentDateFormatted(), msg, "0.0.0.0"));
	}
	
	public static void sendUserMessage(DataPackage dp)
	{
		messagesUserToSend.add(new Message(dp.getUsername(), dp.getTime(), (String) dp.getValue(), dp.getIP()));
	}
	
	public static void sendData(DataPackage dp)
	{
		dataToSend.add(dp);
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
							if(dataToSend.size() > 0)
							{
								for(DataPackage dat : dataToSend)
								{
									oos = new ObjectOutputStream(socket.getOutputStream());
									oos.writeObject(dat);
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
				
				dataToSend.clear();
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