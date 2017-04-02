package org.wildstang.pathassistant.actions;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;

import org.wildstang.pathassistant.app.PathAssistant;
import org.wildstang.pathassistant.data.WaypointModel;

public class LoadPathModelAction extends AbstractAction
{

   public LoadPathModelAction(String p_title)
   {
      super(p_title);
   }
   
   @Override
   public void actionPerformed(ActionEvent p_arg0)
   {
	  
      File file = null;

      // Show file selection dialog
      File current = PathAssistant.m_applicationController.getCurrentWaypointsFile();
      JFileChooser fc = null;
      if (current != null)
      {
         fc = new JFileChooser(current.getParentFile());
      }
      else
      {
         fc = new JFileChooser();
      }

      fc.setDialogTitle("Load path waypoints");

      int returnVal = fc.showOpenDialog(PathAssistant.m_applicationController.getAppFrame());

      if (returnVal == JFileChooser.APPROVE_OPTION) {
          file = new File(fc.getSelectedFile().getAbsolutePath());
          //This is where a real application would open the file.
      } else {
      }
      
      
      // Read the file
      if (file != null)
      {
         PathAssistant.m_applicationController.setCurrentWaypointsFile(file);
         try
         {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
            
//            WaypointModel model = (WaypointModel)ois.readObject();
//            ois.close();
//            if (model != null)
//            {
//               PathAssistant.m_applicationController.updateModel(model);
//            }
            PathAssistant.m_applicationController.getWaypointModel().readObject(ois);
            PathAssistant.m_applicationController.updateModel();
            ois.close();
         }
         catch (FileNotFoundException e1)
         {
            // TODO Auto-generated catch block
            e1.printStackTrace();
         }
         catch (IOException e1)
         {
            // TODO Auto-generated catch block
            e1.printStackTrace();
         }
         catch (ClassNotFoundException cnfe)
         {
            // TODO Auto-generated catch block
            cnfe.printStackTrace();
         }
      }      
   }   
}
