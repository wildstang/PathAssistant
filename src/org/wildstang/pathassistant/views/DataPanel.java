package org.wildstang.pathassistant.views;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.wildstang.pathassistant.actions.GeneratePathAction;
import org.wildstang.pathassistant.actions.SaveTrajectoryAction;
import org.wildstang.pathassistant.app.PathAssistant;

public class DataPanel extends JPanel
{
   
   private JTable m_waypointTable;
   private JCheckBox pathBackwards;
   
   public DataPanel()
   {
      setMinimumSize(new Dimension(150, 600));
      setPreferredSize(new Dimension(150, 600));
      init();
   }
   
   private void init()
   {
      m_waypointTable = new JTable(PathAssistant.m_applicationController.getWaypointModel());
      m_waypointTable.setFocusable(false);
      PathAssistant.m_applicationController.getWaypointModel().addTableModelListener(m_waypointTable);
      
      setLayout(new BorderLayout());
      
      add(new JScrollPane(m_waypointTable), BorderLayout.CENTER);
      m_waypointTable.setFillsViewportHeight(true);
      
      JPanel buttonPanel = new JPanel();
      pathBackwards = new JCheckBox(" Backwards");
      JButton generateButton = new JButton(new GeneratePathAction("Generate path"));
      JButton saveButton = new JButton(new SaveTrajectoryAction("Save trajectory"));
      generateButton.setFocusable(false);
      saveButton.setFocusable(false);
      buttonPanel.setLayout(new GridLayout(3,1));
      buttonPanel.add(pathBackwards);
      buttonPanel.add(generateButton);
      buttonPanel.add(saveButton);
      add(buttonPanel, BorderLayout.SOUTH);
   }
   
   public boolean isBackwards() {
	   return pathBackwards.isSelected();
   }
   
   public JTable getWaypointTable() {
	   return m_waypointTable;
   }

}
