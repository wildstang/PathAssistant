package org.wildstang.pathassistant.data;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

public class WaypointModel extends AbstractTableModel
{
	
   private ArrayList<ArrayList<Double>> m_data = new ArrayList<ArrayList<Double>>();

   @Override
   public int getColumnCount()
   {
      return 2;
   }

   @Override
   public int getRowCount()
   {
      return m_data.size();
   }

   public boolean isCellEditable(int p_rowIndex, int p_columnIndex)
   {
      return true;
   }
   
   @Override
   public Object getValueAt(int p_row, int p_column)
   {
      if (p_row < m_data.size())
      {
         ArrayList<Double> row = m_data.get(p_row);
         if (p_column < row.size())
         {
            return row.get(p_column);
         }
      }

      // If we get here, there is nothing to return
      return null;
   }

   @Override
   public Class<?> getColumnClass(int p_columnIndex)
   {
      return Double.class;
   }

   @Override
   public String getColumnName(int p_column)
   {
      switch (p_column)
      {
         case 0:
            return "X";
         case 1:
            return "Y";
         default:
            return "??";

      }
   }

   @Override
   public void setValueAt(Object p_aValue, int p_rowIndex, int p_columnIndex)
   {
      if (p_rowIndex < m_data.size())
      {
         ArrayList<Double> row = m_data.get(p_rowIndex);
         if (p_columnIndex < row.size())
         {
            row.set(p_columnIndex, (Double)p_aValue);
         }
      }
   }

   public void addRow(double[] p_row)
   {
      ArrayList<Double> temp = new ArrayList<Double>();

      for (int i = 0; i < p_row.length; i++)
      {
         temp.add(p_row[i]);
      }

      m_data.add(temp);

      fireTableRowsInserted(0, getRowCount());
      
   }

   public void deleteRow()
   {
      m_data.remove(getRowCount() - 1);
      
      fireTableRowsDeleted(0, getRowCount());
   }
   
   public double[][] getRawData()
   {
      double[][] data = new double[m_data.size()][m_data.get(0).size()];

      for (int i = 0; i < m_data.size(); i++)
      {
         ArrayList<Double> row = m_data.get(i);
         for (int j = 0; j < row.size(); j++)
         {
            data[i][j] = row.get(j);
         }
      }
      
      return data;
   }
}
