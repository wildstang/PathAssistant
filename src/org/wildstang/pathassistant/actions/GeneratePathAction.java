package org.wildstang.pathassistant.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.wildstang.pathassistant.app.PathAssistant;
import org.wildstang.pathassistant.data.Path;
import org.wildstang.pathassistant.data.WaypointModel;
import org.wildstang.pathassistant.generator.FalconPathPlanner;


public class GeneratePathAction extends AbstractAction
{
   
   public GeneratePathAction(String p_title)
   {
      super(p_title);
   }

   @Override
   public void actionPerformed(ActionEvent p_arg0)
   {
      System.out.println("Generating path");
      FalconPathPlanner generator = PathAssistant.m_applicationController.getPathGenerator();
      WaypointModel model = PathAssistant.m_applicationController.getWaypointModel();
      
      
      Path path = new Path(model.getRawData());
      generator.reset();
      generator.init(path);
      
      // TODO: Get these params from UI
      double totalTime = 5; //seconds
      double timeStep = 0.03; //period of control loop on Rio, seconds
      double robotTrackWidth = 2; //distance between left and right wheels, feet

      generator.calculate(totalTime, timeStep, robotTrackWidth);
      
      PathAssistant.m_applicationController.getGraphPanel().update();
   }

}
