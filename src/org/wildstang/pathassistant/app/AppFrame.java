package org.wildstang.pathassistant.app;


import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

import org.wildstang.pathassistant.data.WaypointModel;
import org.wildstang.pathassistant.views.DataPanel;
import org.wildstang.pathassistant.views.GraphPanel;

public class AppFrame extends JFrame implements KeyListener
{
   
   private DataPanel m_dataPanel;
   private GraphPanel m_graphPanel;
   
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
      
      m_dataPanel = new DataPanel();
      m_graphPanel = new GraphPanel();
      
      add(m_dataPanel, BorderLayout.WEST);
      add(m_graphPanel, BorderLayout.CENTER);
      
      m_dataPanel.setFocusable(true);
      m_dataPanel.addKeyListener(this);
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
   
   
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		System.out.println("abrgbraeub");
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		System.out.println("Button Pressed LOLOLO");
		WaypointModel model = PathAssistant.m_applicationController.getWaypointModel();

		if (e.getKeyCode() == KeyEvent.VK_EQUALS) {
			model.addRow(new double[] { 0, 0 });
		} else if (e.getKeyCode() == KeyEvent.VK_MINUS) {
			model.deleteRow();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}
   
}
