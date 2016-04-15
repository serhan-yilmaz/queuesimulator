/*
 * Copyright (C) 2016 Serhan Yılmaz
 *
 * This file is part of QueueSimulator
 * 
 * QueueSimulator is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * QueueSimulator is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package queuesimulation.GUI.graph;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author Serhan Yılmaz
 */
public class Canvas extends JPanel{
    protected static final int DEFAULT_WIDTH = 640;
    protected static final int DEFAULT_HEIGHT = 480;
    private int currentWidth;
    private int currentHeight;
    
    private ArrayList<Double> x_points, y_points;
    private double xMin,xMax,yMin,yMax;
    
    private Pointer pointer = new CrossPointer(7);
    
    //private LineDrawer line = new BasicLineDrawer(2);
    private LineDrawer line;
    
    public Canvas(){
        Rectangle rec = new Rectangle(cX(DEFAULT_WIDTH * 0.1),cY(DEFAULT_HEIGHT * 0.9),cX(DEFAULT_WIDTH * 0.8),cY(DEFAULT_HEIGHT * 0.8));
        line = new BarDrawer(1,rec);
    }
    
    @Override
    public void paintComponent(Graphics g){
        super.paintComponents(g);
        g.setColor(new Color(228,228,228));
        g.fillRect(0, 0, currentWidth, currentHeight);
        
        g.setColor(Color.white);
        g.fillRect(cX(DEFAULT_WIDTH * 0.1), cY(DEFAULT_HEIGHT * 0.1), cX(DEFAULT_WIDTH * 0.8), cY(DEFAULT_HEIGHT * 0.8));
        g.setColor(Color.black);
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(1));
        g2.drawRect(cX(DEFAULT_WIDTH * 0.1), cY(DEFAULT_HEIGHT * 0.1), cX(DEFAULT_WIDTH * 0.8), cY(DEFAULT_HEIGHT * 0.8));
        g2.setStroke(new BasicStroke(3));
        g2.drawLine(cX(DEFAULT_WIDTH * 0.1), cY(DEFAULT_HEIGHT * 0.1), cX(DEFAULT_WIDTH * 0.1), cY(DEFAULT_HEIGHT * 0.9));
        g2.drawLine(cX(DEFAULT_WIDTH * 0.1), cY(DEFAULT_HEIGHT * 0.9), cX(DEFAULT_WIDTH * 0.9), cY(DEFAULT_HEIGHT * 0.9));
        
        g2.setFont(new Font("Arial", 0 ,cX(15)));

        int yMinS = (int) Math.log10(yMin) +1;
        int xMaxS = (int) Math.log10(xMax) +1;
        g2.drawString(String.format("%-7.3f", yMin / Math.pow(10,yMinS)), cX(DEFAULT_WIDTH * 0.1 - 40), cY(DEFAULT_HEIGHT * 0.13));
        g2.drawString(String.format("%-7.3f", yMax / Math.pow(10,yMinS)), cX(DEFAULT_WIDTH * 0.1 - 40), cY(DEFAULT_HEIGHT * 0.87));

        g2.drawString("" + String.format("%-7.3f", xMin / Math.pow(10,xMaxS)) , cX(DEFAULT_WIDTH * 0.1), cY(DEFAULT_HEIGHT * 0.9 + 22));
        g2.drawString("" + String.format("%-7.3f", xMax / Math.pow(10,xMaxS)), cX(DEFAULT_WIDTH * 0.85), cY(DEFAULT_HEIGHT * 0.9 + 22));
        g2.setFont(new Font("Arial", 1 ,cX(12)));
        g2.drawString("x 10 ^ " + yMinS, cX(DEFAULT_WIDTH * 0.1 - 15), cY(DEFAULT_HEIGHT * 0.1 - 5));
        g2.drawString("x 10 ^ " + xMaxS, cX(DEFAULT_WIDTH * 0.9), cY(DEFAULT_HEIGHT * 0.9 + 35));
        if(x_points != null){
            if(pointer != null){
                for(int i = 0;i < x_points.size();i++){
                    pointer.draw(g,x_points.get(i), y_points.get(i));
                }
            }
            if(line != null){
                for(int i = 0;i < x_points.size() - 1;i++){
                    line.draw(g,x_points.get(i), y_points.get(i),x_points.get(i + 1),y_points.get(i + 1));
                }
            }
        }
        //g.drawOval(cX(400), cY(400), cX(400), cY(400));
    }
    
    @Override
    public Dimension getPreferredSize(){
        return new Dimension(DEFAULT_WIDTH,DEFAULT_HEIGHT);
    }
    private int cX(double x){
        return (int) Math.round(x * currentWidth / DEFAULT_WIDTH);
    }
    private int cXY(double xy){
        double n = (currentWidth + 0.0) / DEFAULT_WIDTH;
        n *= (currentHeight + 0.0) / DEFAULT_HEIGHT;
        n = Math.sqrt(n);
        return (int) Math.round(xy * n);
    }
    private int cY(double y){
        return (int) Math.round(y * currentHeight / DEFAULT_HEIGHT);
    }
    public final void setCurrentDimensions(int width, int height){
        currentWidth = width;
        currentHeight = height;
        if(pointer != null){
            pointer.setCorrectionRatios((double) currentWidth / DEFAULT_WIDTH, (double) currentHeight / DEFAULT_HEIGHT);
        }
        if(line != null){
            line.setCorrectionRatios((double) currentWidth / DEFAULT_WIDTH, (double) currentHeight / DEFAULT_HEIGHT);
            Rectangle rec = new Rectangle(cX(DEFAULT_WIDTH * 0.1),cY(DEFAULT_HEIGHT * 0.9),cX(DEFAULT_WIDTH * 0.9),cY(DEFAULT_HEIGHT * 0.9));
            line.setActiveWindow(rec);
        }
        //System.out.println(width + "-" + height);
    }
    public void setDataPoints(ArrayList<Double> x,ArrayList<Double> y){
        this.x_points = x;
        this.y_points = y;
        pointer = null;
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                repaint();
            }
        });
        //repaint();
    }
    
    public void setDataLimits(double xMin,double xMax, double yMin, double yMax){
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;
    }
    
}
