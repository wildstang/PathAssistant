package org.wildstang.pathassistant.actions;

import java.awt.event.ActionEvent;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

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
//         }
         int returnVal = fc.showSaveDialog(PathAssistant.m_applicationController.getAppFrame());

         if (returnVal == JFileChooser.APPROVE_OPTION) {
             file = fc.getSelectedFile();
             //This is where a real application would open the file.
         } else {
         }
//      }
      
      
      // Save the file
      FileOutputStream fos = null;
      
      if (file != null)
      {
         try
         {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            
            String output = formatTrajectoryOutput(PathAssistant.m_applicationController.getPathGenerator());
            bos.write(output.getBytes());
            bos.flush();
            bos.close();
//            fos = new FileOutputStream(file);
//            ObjectOutputStream oos = new ObjectOutputStream(fos);
//            oos.writeObject(m_controller.getViewConfig());
//            oos.flush();
//            oos.close();
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

   
   private String formatTrajectoryOutput(FalconPathPlanner generator)
   {
      StringBuffer outputBuf = new StringBuffer();
      StringBuffer leftBuf = new StringBuffer();
      StringBuffer rightBuf = new StringBuffer();
      
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
         leftBuf.append(m_cumulativeLeftDist + "," + velPerSecToRPM(generator.smoothLeftVelocity[i][1]) + "," + nearest10((generator.smoothLeftVelocity[i][0] - generator.smoothLeftVelocity[i-1][0])) + "\n");
         rightBuf.append(m_cumulativeRightDist + "," + velPerSecToRPM(generator.smoothRightVelocity[i][1]) + "," + nearest10((generator.smoothRightVelocity[i][0] - generator.smoothRightVelocity[i-1][0])) + "\n");
      }
      
      // Add the two together
      outputBuf.append(leftBuf.toString());
      outputBuf.append("-\n");
      outputBuf.append(rightBuf.toString());
      
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
   
}
