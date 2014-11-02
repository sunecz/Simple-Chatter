package net.sune.networking;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class FileSelector
{
	private JFrame frame;
	private JPanel contentPane;
	private JPanel panel;
	private JPanel subPanel0;
	private JTable tableFiles;
	private JButton btnAddFile;
	private JButton btnClear;
	private JScrollPane scrollPane;
	private DefaultTableModel tableModel;
	private JButton btnRemoveFile;
	private JPanel subPanel2;
	private JButton btnSend;
	private JPanel subPanel1;
	private JPanel subPanel3;
	
	private ArrayList<File> files = new ArrayList<File>();
	
	/**
	 * @wbp.parser.entryPoint
	 */
	public FileSelector()
	{
		frame = new JFrame();
		frame.setTitle("Send files");
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setBounds(100, 100, 475, 360);
		frame.setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		contentPane.setLayout(new BorderLayout(5, 5));
		frame.setContentPane(contentPane);

		panel = new JPanel();
		contentPane.add(panel, BorderLayout.SOUTH);
		panel.setLayout(new GridLayout(0, 1, 0, 0));
		
		subPanel0 = new JPanel();
		panel.add(subPanel0);
		subPanel0.setLayout(new GridLayout(0, 2, 0, 0));
		
		subPanel1 = new JPanel();
		subPanel0.add(subPanel1);
		subPanel1.setLayout(new GridLayout(0, 1, 0, 0));
		
		subPanel3 = new JPanel();
		subPanel1.add(subPanel3);
		GridBagLayout gbl_subPanel3 = new GridBagLayout();
		gbl_subPanel3.columnWidths = new int[]{59, 79, 47, 0};
		gbl_subPanel3.rowHeights = new int[]{27, 0};
		gbl_subPanel3.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_subPanel3.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		subPanel3.setLayout(gbl_subPanel3);
		
		btnAddFile = new JButton("Add file");
		GridBagConstraints gbc_btnAddFile = new GridBagConstraints();
		gbc_btnAddFile.anchor = GridBagConstraints.NORTHWEST;
		gbc_btnAddFile.insets = new Insets(0, 0, 0, 5);
		gbc_btnAddFile.gridx = 0;
		gbc_btnAddFile.gridy = 0;
		subPanel3.add(btnAddFile, gbc_btnAddFile);
		btnAddFile.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black, 1), BorderFactory.createEmptyBorder(4, 10, 6, 10)));
		
		btnAddFile.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if(e.getModifiers() == MouseEvent.BUTTON1_MASK)
				{
					JFileChooser fc = new JFileChooser();
					fc.setMultiSelectionEnabled(true);
					
					if(fc.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION)
					{
						File[] fs = fc.getSelectedFiles();
						
						for(File f : fs)
						{
							tableModel.addRow(new Object[] {f.getAbsolutePath(), f.getName()});
							files.add(f);
						}
					}
				}
			}
		});
		
		btnRemoveFile = new JButton("Remove file");
		GridBagConstraints gbc_btnRemoveFile = new GridBagConstraints();
		gbc_btnRemoveFile.anchor = GridBagConstraints.NORTHWEST;
		gbc_btnRemoveFile.insets = new Insets(0, 0, 0, 5);
		gbc_btnRemoveFile.gridx = 1;
		gbc_btnRemoveFile.gridy = 0;
		subPanel3.add(btnRemoveFile, gbc_btnRemoveFile);
		btnRemoveFile.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black, 1), BorderFactory.createEmptyBorder(4, 10, 6, 10)));
		
		btnRemoveFile.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if(e.getModifiers() == MouseEvent.BUTTON1_MASK)
				{
					int[] indexes = tableFiles.getSelectedRows();
					
					for(int i = indexes.length - 1; i > -1; i--)
					{
						int ind = indexes[i];
						
						tableModel.removeRow(ind);
						files.remove(ind);
					}
				}
			}
		});
		
		btnClear = new JButton("Clear");
		GridBagConstraints gbc_btnClear = new GridBagConstraints();
		gbc_btnClear.anchor = GridBagConstraints.NORTHWEST;
		gbc_btnClear.gridx = 2;
		gbc_btnClear.gridy = 0;
		subPanel3.add(btnClear, gbc_btnClear);
		btnClear.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black, 1), BorderFactory.createEmptyBorder(4, 10, 6, 10)));
		
		btnClear.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if(e.getModifiers() == MouseEvent.BUTTON1_MASK)
				{
					for(int i = tableModel.getRowCount() - 1; i > -1; i--)
					{
						tableModel.removeRow(i);
					}
					
					files.clear();
				}
			}
		});
		
		subPanel2 = new JPanel();
		subPanel0.add(subPanel2);
		subPanel2.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
		subPanel2.setLayout(new BorderLayout(0, 0));
		
		btnSend = new JButton("Send");
		btnSend.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black, 1), BorderFactory.createEmptyBorder(4, 10, 6, 10)));
		subPanel2.add(btnSend, BorderLayout.EAST);

		btnSend.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if(e.getModifiers() == MouseEvent.BUTTON1_MASK)
				{
					if(files.size() > 0)
					{
						for(File f : files)
						{
							Server.sendFile(f);
						}
					}

					close();
				}
			}
		});
		
		scrollPane = new JScrollPane();
		scrollPane.setBorder(BorderFactory.createLineBorder(Color.black, 1));
		contentPane.add(scrollPane, BorderLayout.CENTER);
		
		tableModel = new DefaultTableModel();
		tableModel.addColumn("File path");
		tableModel.addColumn("File name");		
		
		tableFiles = new JTable(tableModel);
		tableFiles.setFillsViewportHeight(true);
		scrollPane.setViewportView(tableFiles);
		
		frame.setVisible(true);
	}
	
	public void close()
	{
		frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
	}
}