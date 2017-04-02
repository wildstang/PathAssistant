package org.wildstang.pathassistant.actions;

import java.awt.event.ActionEvent;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;

import org.wildstang.pathassistant.app.PathAssistant;
import org.wildstang.pathassistant.generator.FalconPathPlanner;

public class SaveTrajectoryAction extends AbstractAction
{

   public static final int WHEEL_DIA = 4;
   
   private double m_wheelCircumference = Math.PI * WHEEL_DIA;
   
   private double m_cumulativeLeftDist = 0;
   private double m_cumulativeRightDist = 0;
   
   public SaveTrajectoryAction(String p_title)
   {
      super(p_title);
   }
   
   @Override
   public void actionPerformed(ActionEvent p_arg0)
   {
	  
      File file_right = null;
      File file_left = null;
      boolean isPathBackwards = PathAssistant.m_applicationController.getAppFrame().getDataPanel().isBackwards(); 

      // Show file selection dialog
         File current = PathAssistant.m_applicationController.getCurrentPathFile();

         JFileChooser fc = null;
         if (current != null)
         {
            fc = new JFileChooser(current.getParentFile());
         }
         else
         {
            fc = new JFileChooser();
         }
         fc.setDialogTitle("Save Path");

         int returnVal = fc.showSaveDialog(PathAssistant.m_applicationController.getAppFrame());

         if (returnVal == JFileChooser.APPROVE_OPTION) {
            String selectedPath = fc.getSelectedFile().getAbsolutePath();
             file_right = new File(selectedPath + ".right");
             file_left = new File(selectedPath + ".left");
             //This is where a real application would open the file.
         } else {
         }

         
         
//      }
      
      
      // Save the file
      FileOutputStream fos = null;
      
      if (file_right != null && file_left != null)
      {
         PathAssistant.m_applicationController.setCurrentPathFile(file_right);

         try
         {
            BufferedOutputStream bos_right = new BufferedOutputStream(new FileOutputStream(file_right));
            BufferedOutputStream bos_left = new BufferedOutputStream(new FileOutputStream(file_left));
            
            String output_right = formatTrajectoryOutput(PathAssistant.m_applicationController.getPathGenerator(), true, isPathBackwards);
            String output_left = formatTrajectoryOutput(PathAssistant.m_applicationController.getPathGenerator(), false, isPathBackwards);
            bos_right.write(output_right.getBytes());
            bos_right.flush();
            bos_right.close();
            bos_left.write(output_left.getBytes());
            bos_left.flush();
            bos_left.close();
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

   
   private String formatTrajectoryOutput(FalconPathPlanner generator, boolean isRight, boolean isBackwards)
   {
      StringBuffer outputBuf = new StringBuffer();
      StringBuffer leftBuf = new StringBuffer();
      StringBuffer rightBuf = new StringBuffer();
      
      m_cumulativeLeftDist = 0;
      m_cumulativeRightDist = 0;
      
      int numPoints = generator.smoothLeftVelocity.length;
      
      // Velocity array: velocity[time][velocity]
      // Coords array:  left[x][y]
      
      // Calculate distance delta: dist = Sqrt(x^2 + y^2)  ==> then convert feet to encoder ticks
      // Dist (rotation), velocity (RPM), Duration (ms)
      
      leftBuf.append("0,0,10\n");
      rightBuf.append("0,0,10\n");
      
      for (int i = 1; i < numPoints; i++)
      {
         m_cumulativeLeftDist += distToRotations(generator.leftPath[i], generator.leftPath[i-1]);
         m_cumulativeRightDist += distToRotations(generator.rightPath[i], generator.rightPath[i-1]);
         
         //int backwards = (isBackwards == true ? -1 : 1);
         if (isBackwards) {
        	 //***Might need to make distance negative
        	 leftBuf.append(-m_cumulativeRightDist + "," + (velPerSecToRPM(generator.smoothRightVelocity[i][1]) * -1) + "," + nearest10((generator.smoothLeftVelocity[i][0] - generator.smoothLeftVelocity[i-1][0])) + "\n");
        	 rightBuf.append(-m_cumulativeLeftDist + "," + (velPerSecToRPM(generator.smoothLeftVelocity[i][1]) * -1) + "," + nearest10((generator.smoothRightVelocity[i][0] - generator.smoothRightVelocity[i-1][0])) + "\n");
      
         } else {
        	 leftBuf.append(m_cumulativeLeftDist + "," + (velPerSecToRPM(generator.smoothLeftVelocity[i][1])) + "," + nearest10((generator.smoothLeftVelocity[i][0] - generator.smoothLeftVelocity[i-1][0])) + "\n");
        	 rightBuf.append(m_cumulativeRightDist + "," + (velPerSecToRPM(generator.smoothRightVelocity[i][1])) + "," + nearest10((generator.smoothRightVelocity[i][0] - generator.smoothRightVelocity[i-1][0])) + "\n");
         }
      }
      
      // Add the two together
//      outputBuf.append(leftBuf.toString());   OLD STUFF
//      outputBuf.append("-\n");
//      outputBuf.append(rightBuf.toString());
      
      //NEW STUFF
      if (isRight) {
    	  outputBuf.append(rightBuf.toString());
      } else {
    	  outputBuf.append(leftBuf.toString());
      }
      
      return outputBuf.toString();
   }

   private int nearest10(double p_num)
   {
      double temp = p_num * 100;
      
      return (int)Math.round(temp) * 10;
   }
   
   private double velPerSecToRPM(double p_vel)
   {
      double inchesPerSecond = p_vel * 12;
      double inchesPerMin = inchesPerSecond * 60;
      
      // Convert to rotations
      return inchesPerMin / m_wheelCircumference;
   }
   
   public double distToRotations(double[] p_current, double[] p_prev)
   {
      double result = 0;
      
      // Convert delta X/Y to
      double x = p_current[0] - p_prev[0];
      double y = p_current[1] - p_prev[1];

      System.out.println("(x, y): (" + x + ", " + y + ")");
      // This is in feet
      double dist = Math.sqrt((x * x) + (y * y));
      System.out.println("dist = " + dist);
      
      // Convert to inches
      dist *= 12;
      System.out.println("dist(in) = " + dist);
      
      // Convert to rotations
      result = dist / m_wheelCircumference;

      System.out.println("rotations = " + result);
      
      return result;
   }
   
   private static void copyFileUsingFileStreams(File source, File dest)
   
           throws IOException {
   
       InputStream input = null;
   
       OutputStream output = null;
   
       try {
   
           input = new FileInputStream(source);
   
           output = new FileOutputStream(dest);
   
           byte[] buf = new byte[1024];
   
           int bytesRead;
  
           while ((bytesRead = input.read(buf)) > 0) {
   
               output.write(buf, 0, bytesRead);
   
           }
   
       } finally {
   
           input.close();
   
           output.close();
   
       }
   
   }

   
}
