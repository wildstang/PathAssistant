package org.wildstang.pathassistant.app;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFileChooser;

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
   
   public double[][] getPathFromFile(double[] initialPoint, double initialHeading) {
	   double heading = initialHeading;
	   ArrayList<double[]> coords = new ArrayList<double[]>();
	   coords.add(initialPoint);
	   File file = null;
	   JFileChooser fc = new JFileChooser();
	   fc.setDialogTitle("Get Robot Path");
	   int returnVal = fc.showSaveDialog(PathAssistant.m_applicationController.getAppFrame());
	   if (returnVal == JFileChooser.APPROVE_OPTION) {
           file = fc.getSelectedFile();
           //This is where a real application would open the file.
       } else {
    	   file = null;
    	   return null;
       }
	   String line = null;
	   try {
		BufferedReader bis = new BufferedReader(new FileReader(file));

		while((line = bis.readLine()) != null) {

			//dTheta is the last value, radius is the second to last value, straightDistance is third
            double deltaTheta = Double.parseDouble(line.substring(line.lastIndexOf(",") + 2, line.length()));
            String tempLine = line.substring(0, line.lastIndexOf(","));
            double radius = Double.parseDouble(line.substring(tempLine.lastIndexOf(",") + 2, tempLine.length()));
            String tempLine2 = tempLine.substring(0, tempLine.lastIndexOf(","));
            double straightInches = Double.parseDouble(line.substring(tempLine2.lastIndexOf(",") + 2, tempLine2.length()));
            
            double dx = 0;
            double dy = 0;
            double oldX = (coords.get(coords.size() - 1))[0];
            double oldY = (coords.get(coords.size() - 1))[1];
            System.out.println(oldX + " " + oldY);
            double RADIANS = Math.PI / 180;
            if (straightInches != 0) {
            	dx = straightInches * Math.cos(RADIANS * heading);
            	dy = straightInches * Math.sin(RADIANS * heading);
            } else {
            	dx = 2 * radius * Math.sin(RADIANS * (Math.abs(deltaTheta) / 2)) * Math.cos(RADIANS * (deltaTheta + heading));
            	dy = 2 * radius * Math.sin(RADIANS * (Math.abs(deltaTheta) / 2)) * Math.sin(RADIANS * (deltaTheta + heading));
            }
            System.out.println(dx + " " + dy);
            
            dx /= 12; //Convert from inches to feet (It plots the points in feet)
            dy /= 12; 
            
            coords.add(new double[]{oldX + dx, oldY + dy});
            heading += deltaTheta;
            if (heading >= 360) {
            	heading = heading % 360;
            } else if (heading < 0){
            	heading += 360; // Should only need to do this once, we will not have a dTheta of >360
            }
            //System.out.println(deltaTheta + " " + radius);
        }
		bis.close();
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	   
	   double[][] temp = new double[coords.size()][2];
	   for (int i = 0; i < coords.size(); i++) {
		   temp[i] = coords.get(i);
	   }
	   return temp;
   }
   
   public WaypointModel getWaypointModel()
   {
      return m_waypointModel;
   }
   
   public void updateModel()//WaypointModel p_model)
   {
//      m_waypointModel = p_model;
      m_frame.getDataPanel().modelUpdated();
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
