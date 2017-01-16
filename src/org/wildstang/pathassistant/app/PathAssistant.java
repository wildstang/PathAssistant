package org.wildstang.pathassistant.app;


import org.wildstang.pathassistant.data.WaypointModel;

public class PathAssistant
{
   public static ApplicationController m_applicationController;
   
   public static void main(String[] args)
   {
      new PathAssistant();
   }
   
   public PathAssistant()
   {
      m_applicationController = new ApplicationController();
      m_applicationController.init();
      
      WaypointModel model = m_applicationController.getWaypointModel();
      
//      {1, 1},
//      {5, 1},
//      {8, 9},
//      {15, 6},
//      {19, 12}

      System.out.println("App created - adding data");
      model.addRow(new double[]{1, 1});
      model.addRow(new double[]{5, 1});
      model.addRow(new double[]{8, 9});
      model.addRow(new double[]{15, 6});
      model.addRow(new double[]{19, 12});
      System.out.println("App created - finished adding data");
      model.fireTableDataChanged();
      
   }
   
}
