package org.wildstang.pathassistant.app;


import org.wildstang.pathassistant.data.WaypointModel;
import org.wildstang.pathassistant.generator.FalconPathPlanner;
import org.wildstang.pathassistant.views.GraphPanel;

public class ApplicationController
{
   private AppFrame m_frame;
   private WaypointModel m_waypointModel;
   private FalconPathPlanner m_pathGenerator = new FalconPathPlanner();
   
   public ApplicationController()
   {
      
   }
   
   public void init()
   {
      m_waypointModel = new WaypointModel();

      m_frame = new AppFrame("Path Assistant");
      m_frame.init();
      
      m_frame.setVisible(true);
      

   }
   
   public WaypointModel getWaypointModel()
   {
      return m_waypointModel;
   }

   public FalconPathPlanner getPathGenerator()
   {
      return m_pathGenerator;
   }

   public GraphPanel getGraphPanel()
   {
      return m_frame.getGraphPanel();
   }

   public AppFrame getAppFrame()
   {
      return m_frame;
   }
   
   
}
