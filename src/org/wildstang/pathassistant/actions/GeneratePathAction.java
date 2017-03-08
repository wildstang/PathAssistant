package org.wildstang.pathassistant.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.wildstang.pathassistant.app.ApplicationController;
import org.wildstang.pathassistant.app.PathAssistant;
import org.wildstang.pathassistant.data.Path;
import org.wildstang.pathassistant.data.WaypointModel;
import org.wildstang.pathassistant.generator.FalconPathPlanner;


public class GeneratePathAction extends AbstractAction
{
	private boolean hasPathToRead = false;
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
      
      // Now that we're generating the model, set the total time and step time on the model as part of the path generation state
      model.setTotalTime(PathAssistant.m_applicationController.getAppFrame().getDataPanel().getTotalTime());
      model.setTimeStep(PathAssistant.m_applicationController.getAppFrame().getDataPanel().getDeltaTime());
      model.setBackwards(PathAssistant.m_applicationController.getAppFrame().getDataPanel().isBackwards());
      
      Path path = new Path(model.getRawData());
      generator.reset();
      generator.init(path);      

      // Default times
      double totalTime = 5; //seconds
      double timeStep = 0.02; //period of control loop on Rio, seconds
      double robotTrackWidth = 2.5; //distance between left and right wheels, feet

      totalTime = model.getTotalTime();
      timeStep = model.getTimeStep() / 1000;
      
      if (totalTime == 0) {
    	  totalTime = 5;
      }
      if (timeStep == 0) {
    	  timeStep = .02;
      }
      
      
      generator.calculate(totalTime, timeStep, robotTrackWidth);
      
      PathAssistant.m_applicationController.getGraphPanel().updateGraphs(false);
     
   }

}
