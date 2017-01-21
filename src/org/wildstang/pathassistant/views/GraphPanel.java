package org.wildstang.pathassistant.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;

import org.wildstang.pathassistant.app.PathAssistant;
import org.wildstang.pathassistant.data.Path;
import org.wildstang.pathassistant.data.Track;
import org.wildstang.pathassistant.generator.*;

import javafx.scene.layout.Border;

public class GraphPanel extends JPanel
{
   private FalconLinePlot m_pathPlot;
   private FalconLinePlot m_velocityPlot;
   
   public GraphPanel()
   {
      setPreferredSize(new Dimension(700, 600));
      init();
   }
   
   private void init()
   {
      m_pathPlot = new FalconLinePlot(new double[0][0]);
      m_velocityPlot = new FalconLinePlot(new double[0][0]);
      
      m_pathPlot.setMinimumSize(new Dimension(700, 300));
      m_velocityPlot.setMinimumSize(new Dimension(700, 300));
      m_pathPlot.setPreferredSize(new Dimension(700, 300));
      m_velocityPlot.setPreferredSize(new Dimension(700, 300));
      
      setLayout(new BorderLayout());
      
      add(m_pathPlot, BorderLayout.NORTH);
      add(m_velocityPlot, BorderLayout.SOUTH);
   }
   
   public void update()
   {
      FalconPathPlanner pathGenerator = PathAssistant.m_applicationController.getPathGenerator();
      Path plannedPath = new Path(PathAssistant.m_applicationController.getWaypointModel().getRawData());

      Track leftWheel = new Track();
      Track rightWheel = new Track();
      Track center = new Track();
      
      leftWheel.setVelocities(pathGenerator.smoothLeftVelocity);
      leftWheel.setCoords(pathGenerator.leftPath);
      
      rightWheel.setVelocities(pathGenerator.smoothRightVelocity);
      rightWheel.setCoords(pathGenerator.rightPath);
      
      center.setCoords(pathGenerator.smoothPath);
      
      plannedPath.setLeft(leftWheel);
      plannedPath.setRight(rightWheel);
      plannedPath.setSmoothPath(center);

      double[][] readPath = PathAssistant.m_applicationController.getPathFromFile(plannedPath.getSmoothPath().getCoords()[0], 0);

//      m_velocityPlot = new FalconLinePlot(pathGenerator.smoothCenterVelocity,null,Color.blue);
      
      m_velocityPlot.clearAll();
      m_velocityPlot.addData(pathGenerator.smoothCenterVelocity[0], pathGenerator.smoothCenterVelocity[1], null, Color.blue);
      m_velocityPlot.yGridOn();
      m_velocityPlot.xGridOn();
      m_velocityPlot.setYLabel("Velocity (ft/sec)");
      m_velocityPlot.setXLabel("time (seconds)");
      m_velocityPlot.setTitle("Velocity Profile for Left and Right Wheels \n Left = Cyan, Right = Magenta");
      m_velocityPlot.addData(plannedPath.getRight().getVelocities(), Color.magenta);
      m_velocityPlot.addData(plannedPath.getLeft().getVelocities(), Color.cyan);

      m_velocityPlot.updateUI();
      
//      m_pathPlot = new FalconLinePlot(pathGenerator.nodeOnlyPath,Color.blue,Color.green);
      m_pathPlot.clearAll();
      
      m_pathPlot.yGridOn();
      m_pathPlot.xGridOn();
      m_pathPlot.setYLabel("Y (feet)");
      m_pathPlot.setXLabel("X (feet)");
      m_pathPlot.setTitle("Top Down View of FRC Field (24ft x 27ft) \n shows global position of robot path, along with left and right wheel trajectories");

      //force graph to show 1/2 field dimensions of 24ft x 27 feet
      m_pathPlot.setXTic(0, 27, 1);
      m_pathPlot.setYTic(0, 24, 1);
      m_pathPlot.addData(plannedPath.getSmoothPath().getCoords(), Color.red, Color.blue);
      //Plot actual path we got (if we have one)
      if (readPath != null) {
    	  m_pathPlot.addData(readPath, Color.GREEN);
    	  //m_pathPlot.addData(readPath.getRight().getCoords(), Color.WHITE);
    	  //m_pathPlot.addData(readPath.getLeft().getCoords(), Color.WHITE);
      }
      
      //Field Blockages
      m_pathPlot.addData(new double[][]{{93.3 / 12, 12 - (70.5 / 24)},{93.3 / 12,12 + (70.5 / 24)}}, Color.ORANGE);
      m_pathPlot.addData(new double[][]{{93.3 / 12, 12 + (70.5 /24)}, {(93.3 / 12) + (70.5 * Math.sqrt(3) / 24), 12 + (70.5 /12)}},Color.ORANGE);
      m_pathPlot.addData(new double[][]{{93.3 / 12, 12 - (70.5 /24)}, {(93.3 / 12) + (70.5 * Math.sqrt(3) / 24), 12 - (70.5 /12)}},Color.ORANGE);
      m_pathPlot.addData(new double[][]{{(93.3 / 12) + (70.5 * Math.sqrt(3) / 24) , 12 + (70.5 /12)},{(93.3 / 12) + (70.5 * Math.sqrt(3) / 12) , 12 + (70.5 / 24)}}, Color.ORANGE);
      m_pathPlot.addData(new double[][]{{(93.3 / 12) + (70.5 * Math.sqrt(3) / 24) , 12 - (70.5 /12)},{(93.3 / 12) + (70.5 * Math.sqrt(3) / 12) , 12 - (70.5 / 24)}}, Color.ORANGE);
      m_pathPlot.addData(new double[][]{{(93.3 / 12) + (70.5 * Math.sqrt(3) / 12), 12 - (70.5 / 24)},{(93.3 / 12) + (70.5 * Math.sqrt(3) / 12),12 + (70.5 / 24)}}, Color.ORANGE);
      m_pathPlot.addData(new double[][]{{0,42 / Math.sqrt(2) / 12}, { 42 / Math.sqrt(2) / 12, 0}}, Color.ORANGE  );
      m_pathPlot.addData(new double[][]{{0, 73 * Math.sqrt(2) / 12},{73 * Math.sqrt(2) / 12, 0}}, Color.YELLOW);
      
      m_pathPlot.addData(leftWheel.getCoords(), Color.magenta);
      m_pathPlot.addData(rightWheel.getCoords(), Color.magenta);
      
      m_pathPlot.updateUI();
   }
}
