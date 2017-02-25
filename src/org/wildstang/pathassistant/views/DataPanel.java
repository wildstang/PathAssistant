package org.wildstang.pathassistant.views;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import org.wildstang.pathassistant.actions.GeneratePathAction;
import org.wildstang.pathassistant.actions.LoadPathAction;
import org.wildstang.pathassistant.actions.SaveTrajectoryAction;
import org.wildstang.pathassistant.app.PathAssistant;

public class DataPanel extends JPanel
{
   
   private JTable m_waypointTable;
   private JCheckBox pathBackwards;
   private JLabel totalTimeLabel;
   private JLabel deltaTimeLabel;
   private JTextField totalTime;
   private JTextField deltaTime;
   
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
      JButton loadButton = new JButton(new LoadPathAction("Load path"));
      totalTimeLabel = new JLabel("Total time");
      deltaTimeLabel = new JLabel("Delta time (ms)");
      totalTime = new JTextField(8);
      deltaTime = new JTextField(8);
      totalTime.setText("5");
      deltaTime.setText("20");
      generateButton.setFocusable(false);
      saveButton.setFocusable(false);
      buttonPanel.setLayout(new GridLayout(8,1));
      buttonPanel.add(totalTimeLabel);
      buttonPanel.add(totalTime);
      buttonPanel.add(deltaTimeLabel);
      buttonPanel.add(deltaTime);
      buttonPanel.add(pathBackwards);
      buttonPanel.add(generateButton);
      buttonPanel.add(loadButton);
      buttonPanel.add(saveButton);
      add(buttonPanel, BorderLayout.SOUTH);
   }
   
   public boolean isBackwards() {
	   return pathBackwards.isSelected();
   }
   
   public double getTotalTime() {
	   if (!totalTime.getText().equals("")) {
		   return Double.parseDouble(totalTime.getText());
	   } else {
		   return 0;
	   }
   }
   
   public double getDeltaTime() {
	   if (!deltaTime.getText().equals("")) {
		   return Double.parseDouble(deltaTime.getText());
	   } else {
		   return 0;
	   }
   }
   
   public JTable getWaypointTable() {
	   return m_waypointTable;
   }

}
