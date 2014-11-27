package sune.apps.simplechatter;

import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

public class DataTableModel extends DefaultTableModel
{
	private static final long serialVersionUID = 1L;
	
	private final ArrayList<String> names;
	private final ArrayList<RowData> rowsData;
	
	public DataTableModel()
	{
		super();
		
		this.names = new ArrayList<>();
		this.rowsData = new ArrayList<>();
	}
	
	public void addRow(String n, Object[] rd, RowData d)
	{
		super.addRow(rd);
		
		names.add(n);
		rowsData.add(d);
	}
	
	@Override
	public void removeRow(int i)
	{
		super.removeRow(i);
		names.remove(i);
		rowsData.remove(i);
	}
	
	public void removeRow(String n)
	{
		int i = names.indexOf(n);
		
		super.removeRow(i);
		names.remove(i);
		rowsData.remove(i);
	}
	
	public void setValue(String n, int c, String val)
	{
		int i = names.indexOf(n);
		super.setValueAt(val, i, c);
	}
	
	public RowData getAdditionalData(int i)
	{
		return rowsData.get(i);
	}
	
	public RowData getAdditionalData(String n)
	{
		int i = names.indexOf(n);
		return rowsData.get(i);
	}
	
	public void setRowsData(int i, RowData data)
	{
		rowsData.set(i, data);
	}
	
	public ArrayList<RowData> getRowsData()
	{
		return rowsData;
	}
	
	@Override
    public boolean isCellEditable(int r, int c)
	{
		return false;
	}
	
	public final class RowData
	{
		private int index;
		private String hash;
		private String type;
		
		public RowData(int index, String hash, String type)
		{
			this.index = index;
			this.hash = hash;
			this.type = type;
		}
		
		public void setIndex(int i)
		{
			index = i;
		}
		
		public int getIndex()
		{
			return index;
		}
		
		public String getHash()
		{
			return hash;
		}
		
		public String getType()
		{
			return type;
		}
	}
}