package org.wildstang.pathassistant.app;


import java.awt.BorderLayout;

import javax.swing.JFrame;

import org.wildstang.pathassistant.views.DataPanel;
import org.wildstang.pathassistant.views.GraphPanel;

public class AppFrame extends JFrame
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
   }
   
   private void setupWindowEvents()
   {
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
   }
   
   public GraphPanel getGraphPanel()
   {
      return m_graphPanel;
   }
}
