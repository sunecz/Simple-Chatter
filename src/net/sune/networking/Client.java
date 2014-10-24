package net.sune.networking;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

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
	
	public static void WindowClient(String ip)
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
		gbl_subPanel2.columnWidths = new int[]{240, 240, 0};
		gbl_subPanel2.rowHeights = new int[]{27, 0};
		gbl_subPanel2.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gbl_subPanel2.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		subPanel2.setLayout(gbl_subPanel2);
		
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
						socket.close();
					}
					catch(Exception ex) {}
				}
			}
		});
		
		btnConnect = new JButton("Connect");
		btnConnect.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if(e.getModifiers() == MouseEvent.BUTTON1_MASK)
				{
					String strIP = JOptionPane.showInputDialog(frame, "Insert server IP", "Connect to a server", JOptionPane.PLAIN_MESSAGE);
					String strPort = JOptionPane.showInputDialog(frame, "Insert server port", "Connect to a server", JOptionPane.PLAIN_MESSAGE);

					if(!strIP.isEmpty() && !strPort.isEmpty())
					{
						int intPort = Integer.parseInt(strPort);
						
						new Thread(new Runnable()
						{
							public void run()
							{
								connectToServer(strIP, intPort);
							}
						}).start();						
					}
				}
			}
		});
		
		btnConnect.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black, 1), BorderFactory.createEmptyBorder(4, 10, 6, 10)));
		GridBagConstraints gbc_btnConnect = new GridBagConstraints();
		gbc_btnConnect.fill = GridBagConstraints.BOTH;
		gbc_btnConnect.insets = new Insets(0, 0, 0, 5);
		gbc_btnConnect.gridx = 0;
		gbc_btnConnect.gridy = 0;
		subPanel2.add(btnConnect, gbc_btnConnect);
		
		btnDisconnect.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black, 1), BorderFactory.createEmptyBorder(4, 10, 6, 10)));
		GridBagConstraints gbc_btnDisconnect = new GridBagConstraints();
		gbc_btnDisconnect.fill = GridBagConstraints.BOTH;
		gbc_btnDisconnect.gridx = 1;
		gbc_btnDisconnect.gridy = 0;
		subPanel2.add(btnDisconnect, gbc_btnDisconnect);
		
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
					sendMessage(txtMessage.getText());
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
			public void actionPerformed(ActionEvent e)
			{
				if(e.getModifiers() == MouseEvent.BUTTON1_MASK)
				{
					sendMessage(txtMessage.getText());
				}
			}
		});
		
		subPanel1.add(btnSend);
		btnSend.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black, 1), BorderFactory.createEmptyBorder(4, 10, 6, 10)));
		
		frame.setVisible(true);

		frame.addWindowListener(new WindowListener()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				try
				{
					socket.close();
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
			connectToServer(localIP, 2406);
			
			new Thread(receive).start();
			new Thread(send).start();
		}
		catch(Exception e) {}
	}
	
	public static void connectToServer(String ip, int port)
	{
		try
		{
			ip = InetAddress.getLocalHost().getHostAddress();
			logText("Connecting to " + ip + ":" + port + "...");
			
			if(socket != null)
			{
				socket.close();
			}
			
			InetSocketAddress sa = new InetSocketAddress(ip, port);

			socket = new Socket();
			socket.connect(sa, 8000);
			
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			oos.writeObject(new DataPackage("username", username));

			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			DataPackage data = (DataPackage) ois.readObject();
			
			if(data.getObjectName().equals("message"))
			{
				logText(data);
			}
		}
		catch(Exception ex)
		{
			logText("Cannot connect to the server!");
		}
	}
	
	public static Socket socket;
	
	public static int port = 2406;
	public static String ip = "";
	
	public static String localIP = "";

	public static int state = 0;
	public static String username = "Unknown";
	
	public static ArrayList<DataPackage> received = new ArrayList<DataPackage>();
	private static ArrayList<Message> messagesToSend = new ArrayList<Message>();
	
	public static Runnable send = new Runnable()
	{
		@Override
		public void run()
		{
			ObjectOutputStream oos;
			
			while(true)
			{
				try
				{
					if(messagesToSend.size() > 0)
					{
						for(Message msg : messagesToSend)
						{
							oos = new ObjectOutputStream(socket.getOutputStream());
							oos.writeObject(new DataPackage("message", msg.getContent(), msg.getUsername(), msg.getIP()));
						}

						txtMessage.setText("");
					}
				}
				catch(Exception e) {}

				messagesToSend.clear();
				Utils.sleep(1);
			}
		}
	};
	
	public static Runnable receive = new Runnable()
	{
		@Override
		public void run()
		{
			new Thread(process).start();
			ObjectInputStream ois;
			
			while(true)
			{
				try
				{
					ois = new ObjectInputStream(socket.getInputStream());
					received.add((DataPackage) ois.readObject());
				}
				catch(Exception e) {}

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
						DataPackage data = received.get(i);
						
						if(data.getObjectName().equals("client_state"))
						{
							int receive_state = (int) data.getValue();
							
							if(receive_state == 0)
							{
								state = receive_state;
							}
							else
							{
								JOptionPane.showMessageDialog(null, "You have been disconnected!", "Disconnected", JOptionPane.INFORMATION_MESSAGE);
								System.exit(0);
							}
						}
						
						if(data.getObjectName().equals("message"))
						{
							logText(data);
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
	
	public static void sendMessage(String msg)
	{
		messagesToSend.add(new Message(username, Utils.getCurrentDateFormatted(), msg, localIP));
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
		String time = Utils.getCurrentDateFormatted();
		textArea.append("[Info - " + time + "]: " + text + "\n");

		JScrollBar vertical = scrollPane.getVerticalScrollBar();
		vertical.setValue(vertical.getMaximum());
	}
}