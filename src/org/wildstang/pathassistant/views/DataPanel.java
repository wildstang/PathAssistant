package org.wildstang.pathassistant.views;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.wildstang.pathassistant.actions.GeneratePathAction;
import org.wildstang.pathassistant.actions.SaveTrajectoryAction;
import org.wildstang.pathassistant.app.PathAssistant;
import org.wildstang.pathassistant.data.WaypointModel;

public class DataPanel extends JPanel
{
   
   private JTable m_waypointTable;
   
   public DataPanel()
   {
      setMinimumSize(new Dimension(150, 600));
      setPreferredSize(new Dimension(150, 600));
      //setFocusable(true);
      //addKeyListener(this);
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
      JButton generateButton = new JButton(new GeneratePathAction("Generate path"));
      JButton saveButton = new JButton(new SaveTrajectoryAction("Save trajectory"));
      generateButton.setFocusable(false);
      saveButton.setFocusable(false);
      buttonPanel.setLayout(new BorderLayout());
      buttonPanel.add(generateButton, BorderLayout.NORTH);
      buttonPanel.add(saveButton, BorderLayout.SOUTH);
      add(buttonPanel, BorderLayout.SOUTH);
   }

}
