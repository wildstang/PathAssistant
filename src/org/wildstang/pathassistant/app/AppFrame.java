package org.wildstang.pathassistant.app;


import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.KeyStroke;

import org.wildstang.pathassistant.data.WaypointModel;
import org.wildstang.pathassistant.views.DataPanel;
import org.wildstang.pathassistant.views.GraphPanel;

public class AppFrame extends JFrame
{
   
   private DataPanel m_dataPanel;
   private GraphPanel m_graphPanel;
   private boolean plusPressed;
   private boolean minusPressed;
   
   public AppFrame(String p_title)
   {
      super(p_title);
   }

   public void init()
   {
      setupWindowEvents();
      
      setSize(800, 600);
      
      initComponents();
      
      pack();
   }
   
   private void initComponents()
   {
      setLayout(new BorderLayout());
      
      plusPressed = true;
      minusPressed = true;
      
      m_dataPanel = new DataPanel();
      m_graphPanel = new GraphPanel();
      //KeyStroke.get
      add(m_dataPanel, BorderLayout.WEST);
      add(m_graphPanel, BorderLayout.CENTER);
      m_dataPanel.getInputMap().put(KeyStroke.getKeyStroke("EQUALS"),
              "+ pressed");
      m_dataPanel.getInputMap().put(KeyStroke.getKeyStroke("MINUS"),
              "- pressed");
      m_dataPanel.getActionMap().put("+ pressed",
               addRow);
      m_dataPanel.getActionMap().put("- pressed",
               removeRow);
   }
   
   
   private void setupWindowEvents()
   {
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
   }
   
   public GraphPanel getGraphPanel()
   {
      return m_graphPanel;
   }
   
   public DataPanel getDataPanel()
   {
      return m_dataPanel;
   }
   
   
   Action removeRow = new AbstractAction() {
	    public void actionPerformed(ActionEvent e) {
	    	System.out.println("remove");
	    	WaypointModel model = PathAssistant.m_applicationController.getWaypointModel();
	    	if(minusPressed) {
	    		model.deleteRow();
	    	}
	    }
	};
	
	Action addRow = new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
			System.out.println("add");
		    WaypointModel model = PathAssistant.m_applicationController.getWaypointModel();
		    if (plusPressed) {
		    		model.addRow(new double[] { 0, 0 });
		    }
		}
	};
	
}
