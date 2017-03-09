package org.wildstang.pathassistant.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.wildstang.pathassistant.app.PathAssistant;
import org.wildstang.pathassistant.data.Path;
import org.wildstang.pathassistant.data.Track;
import org.wildstang.pathassistant.generator.*;

import javafx.scene.layout.Border;

public class GraphPanel extends JPanel
{
   private FalconLinePlot m_pathPlot;
   private FalconLinePlot m_velocityPlot;
   private FalconLinePlot m_diffPlot;
   
   public GraphPanel()
   {
      setPreferredSize(new Dimension(874, 768));
      init();
   }
   
   private void init()
   {
      m_pathPlot = new FalconLinePlot(new double[0][0]);
      m_velocityPlot = new FalconLinePlot(new double[0][0]);
      m_diffPlot = new FalconLinePlot(new double[0][0]);
      
      m_pathPlot.setMinimumSize(new Dimension(874, 768));
      m_velocityPlot.setMinimumSize(new Dimension(874, 768));
      m_diffPlot.setMinimumSize(new Dimension(874, 768));
      m_pathPlot.setPreferredSize(new Dimension(874, 768));
      m_velocityPlot.setPreferredSize(new Dimension(874, 768));
      m_diffPlot.setPreferredSize(new Dimension(874, 768));

      m_pathPlot.setFocusable(false);
      m_velocityPlot.setFocusable(false);
      m_diffPlot.setFocusable(false);
      
      setLayout(new BorderLayout());
      
      JTabbedPane tabs = new JTabbedPane();
      
      JPanel pathPanel = new JPanel();
      pathPanel.add(m_pathPlot);
      tabs.addTab("Path", pathPanel);
      
      JPanel velocityPanel = new JPanel();
      velocityPanel.add(m_velocityPlot);
      tabs.addTab("Velocity", velocityPanel);

      JPanel diffPanel = new JPanel();
      diffPanel.add(m_diffPlot);
      tabs.addTab("Compare", diffPanel);

      add(tabs, BorderLayout.CENTER);
   }
   
   public void updateGraphs(boolean loadRealPath)
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
      

      m_pathPlot.clearAll();
      m_pathPlot.yGridOn();
      m_pathPlot.xGridOn();
      m_pathPlot.setYLabel("Y (feet)");
      m_pathPlot.setXLabel("X (feet)");
      m_pathPlot.setTitle("Top Down View of FRC Field (24ft x 27ft) \n shows global position of robot path, along with left and right wheel trajectories");

      //force graph to show 1/2 field dimensions of 24ft x 27 feet
      m_pathPlot.setXTic(0, 27, 1);
      m_pathPlot.setYTic(0, 27, 1);
      m_pathPlot.addData(plannedPath.getSmoothPath().getCoords(), Color.red, Color.blue);
     
      //Field Blockages
      addFieldLines(m_pathPlot);
      
      m_pathPlot.addData(leftWheel.getCoords(), Color.magenta);
      m_pathPlot.addData(rightWheel.getCoords(), Color.magenta);
      
      m_pathPlot.updateUI();

   
   
      if (loadRealPath)
      {
         double[][] realPath = PathAssistant.m_applicationController.getPathFromFile(plannedPath.getSmoothPath().getCoords()[0], 0);
         
         m_diffPlot.addData(realPath, Color.GREEN);

         m_diffPlot.clearAll();
         m_diffPlot.yGridOn();
         m_diffPlot.xGridOn();
         m_diffPlot.setYLabel("Y (feet)");
         m_diffPlot.setXLabel("X (feet)");
         m_diffPlot.setTitle("Top Down View of FRC Field (24ft x 27ft) \n shows global position of robot path, along with left and right wheel trajectories");

         //force graph to show 1/2 field dimensions of 24ft x 27 feet
         m_diffPlot.setXTic(0, 27, 1);
         m_diffPlot.setYTic(0, 27, 1);
         //Field Blockages
         addFieldLines(m_diffPlot);

         m_diffPlot.addData(plannedPath.getSmoothPath().getCoords(), Color.red, Color.blue);
         m_diffPlot.addData(leftWheel.getCoords(), Color.magenta);
         m_diffPlot.addData(rightWheel.getCoords(), Color.magenta);
         
         m_diffPlot.updateUI();
      }
   }
   
   
   private void addFieldLines(FalconLinePlot p_plot)
   {
      p_plot.addData(new double[][]{{93.3 / 12, 13.5 - (70.5 / 24)},{93.3 / 12,13.5 + (70.5 / 24)}}, Color.GREEN);
      p_plot.addData(new double[][]{{93.3 / 12, 13.5 + (70.5 /24)}, {(93.3 / 12) + (70.5 * Math.sqrt(3) / 24), 13.5 + (70.5 /12)}},Color.ORANGE);
      p_plot.addData(new double[][]{{93.3 / 12, 13.5 - (70.5 /24)}, {(93.3 / 12) + (70.5 * Math.sqrt(3) / 24), 13.5 - (70.5 /12)}},Color.ORANGE);
      p_plot.addData(new double[][]{{(93.3 / 12) + (70.5 * Math.sqrt(3) / 24) , 13.5 + (70.5 /12)},{(93.3 / 12) + (70.5 * Math.sqrt(3) / 12), 13.5 + (70.5 / 24)}}, Color.ORANGE);
      p_plot.addData(new double[][]{{(93.3 / 12) + (70.5 * Math.sqrt(3) / 24) , 13.5 - (70.5 /12)},{(93.3 / 12) + (70.5 * Math.sqrt(3) / 12), 13.5 - (70.5 / 24)}}, Color.ORANGE);
      p_plot.addData(new double[][]{{(93.3 / 12) + (70.5 * Math.sqrt(3) / 12), 13.5 - (70.5 / 24)},{(93.3 / 12) + (70.5 * Math.sqrt(3) / 12), 13.5 + (70.5 / 24)}}, Color.ORANGE);

      p_plot.addData(new double[][]{{0,42 / Math.sqrt(2) / 12}, { 42 / Math.sqrt(2) / 12, 0}}, Color.ORANGE  );
      p_plot.addData(new double[][]{{0, 73 * Math.sqrt(2) / 12},{73 * Math.sqrt(2) / 12, 0}}, Color.YELLOW);
      p_plot.addData(new double[][]{{0,(21 * Math.sqrt(3) / 12) + 27 - 165 /(12 * Math.sqrt(3))}, {102.5 / 12, 27}}, Color.ORANGE);
      p_plot.addData(new double[][]{{0, 27 - 165 /(12 * Math.sqrt(3))},{165.5 / 12, 27}}, Color.YELLOW);
      p_plot.addData(new double[][]{{6.5417, 0}, {8.75, 0}}, new Color(231, 23, 112));

   }
}
