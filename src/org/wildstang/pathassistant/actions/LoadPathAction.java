package org.wildstang.pathassistant.actions;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;

import org.wildstang.pathassistant.app.PathAssistant;
import org.wildstang.pathassistant.data.Path;
import org.wildstang.pathassistant.data.WaypointModel;
import org.wildstang.pathassistant.generator.FalconPathPlanner;

public class LoadPathAction extends AbstractAction
{
	private boolean hasPathToRead = false;
	
   public LoadPathAction(String p_title)
   {
      super(p_title);
   }

   @Override
   public void actionPerformed(ActionEvent p_arg0)
   {
      System.out.println("Generating path");

      File file = null;
      JFileChooser fc = new JFileChooser();
      fc.setDialogTitle("Get Robot Path");
      int returnVal = fc.showOpenDialog(PathAssistant.m_applicationController.getAppFrame());
      if (returnVal == JFileChooser.APPROVE_OPTION) {
           file = fc.getSelectedFile();
           //This is where a real application would open the file.
       } else {
         file = null;
       }
      
      PathAssistant.m_applicationController.setRuntimePathFile(file);
      
      PathAssistant.m_applicationController.getGraphPanel().updateGraphs(true);
     
   }


}
