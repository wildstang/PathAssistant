package org.wildstang.pathassistant.actions;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;

import org.wildstang.pathassistant.app.PathAssistant;

public class SavePathModelAction extends AbstractAction
{

   public SavePathModelAction(String p_title)
   {
      super(p_title);
   }
   
   @Override
   public void actionPerformed(ActionEvent p_arg0)
   {
	  
      File file = null;
//      if (m_controller.getCurrentViewFile() == null)
//      {
         // Show file selection dialog
//         File current = m_controller.getCurrentFile();
         JFileChooser fc = null;
//         if (current != null)
//         {
//            fc = new JFileChooser(current.getParentFile());
//         }
//         else
//         {
            fc = new JFileChooser();
            fc.setDialogTitle("Save path waypoints");
//         }
         int returnVal = fc.showSaveDialog(PathAssistant.m_applicationController.getAppFrame());

         if (returnVal == JFileChooser.APPROVE_OPTION) {
             file = new File(fc.getSelectedFile().getAbsolutePath());
             //This is where a real application would open the file.
         } else {
         }
//      }
      
      
      // Save the file
      if (file != null)
      {
         try
         {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
            PathAssistant.m_applicationController.getWaypointModel().writeObject(oos);
            oos.flush();
            oos.close();
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
      }      
   }   
}
