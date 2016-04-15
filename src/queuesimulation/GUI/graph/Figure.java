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

import java.awt.Window;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import queuesimulation.distribution.Distribution;

/**
 *
 * @author Serhan Yılmaz
 */
public class Figure {
    private final JDialog f;
    private final Canvas canvas;
    private final Window parent;
    private final ImageIcon icon;
    private boolean init = true;
            
    public Figure(String name, Window parent){
        canvas = new Canvas();
        this.parent = parent;
        f = new JDialog(this.parent, name);
        icon = new ImageIcon(this.getClass().getResource("/images/logo.png"));
        f.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent ce) {
                canvas.setCurrentDimensions(f.getContentPane().getWidth(), f.getContentPane().getHeight());
            }
        });
    }
    
    public Figure(int num, Window parent){
        canvas = new Canvas();
        this.parent = parent;
        f = new JDialog(this.parent, "Figure " + num);
        icon = new ImageIcon(this.getClass().getResource("/images/logo.png"));
    }
    
    public void show(){
        //f.setSize(800,800);
        if(init)
        {
            f.setIconImage(icon.getImage());
            f.setResizable(true);
            f.setModal(false);
            f.add(canvas);
            f.pack();
            f.setLocationRelativeTo(parent);  
            init = false;
        }
        f.setVisible(true);
        /*  f.validate();
      new Thread(new Runnable() {

            @Override
            public void run() {
                    
                    System.out.println("sdf");
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Figure.class.getName()).log(Level.SEVERE, null, ex);
                    }
                   // canvas.repaint();
            }
        }).start();*/
    }
    public void plot(ArrayList<Double> y){
        if( y == null )
            return;
        ArrayList<Double> x = new ArrayList<>();
        for(int i = 0;i < y.size() ; i ++){
            x.add((double)i);
        }
        plot(x,y);
    }
    
    public void plot(ArrayList<Double> x , ArrayList<Double> y){
        if(x == null || y == null || x.size() != y.size() || x.isEmpty()){
            return;
        }
        Number xMin = findMin(x),xMax = findMax(x);
        Number yMin = findMax(y),yMax = findMin(y);
        
        double cx = ( Canvas.DEFAULT_WIDTH * 0.8 ) / (xMax.doubleValue() - xMin.doubleValue());
        double cy = ( Canvas.DEFAULT_HEIGHT * 0.8 ) / (yMax.doubleValue() - yMin.doubleValue());
        FigurePointComparator fpc = new FigurePointComparator();
        PriorityQueue<FigurePoint> pq = new PriorityQueue<>(fpc);
        for(int i = 0;i < x.size(); i++){
            pq.add(new FigurePoint(x.get(i), y.get(i)));
        }
        
        ArrayList<Double> xr,yr;
        xr = new ArrayList<>();
        yr = new ArrayList<>();
        while(!pq.isEmpty()){
            FigurePoint fp = pq.poll();
            xr.add((fp.getX() - xMin.doubleValue()) * cx + Canvas.DEFAULT_WIDTH * 0.1);
            yr.add((fp.getY() - yMin.doubleValue()) * cy + Canvas.DEFAULT_HEIGHT * 0.1);
        }
        /*
        for(int i = 0; i< x.size() ; i ++){
            xr.add((x.get(i) - xMin.doubleValue()) * cx + Canvas.DEFAULT_WIDTH * 0.1);
            yr.add((y.get(i) - yMin.doubleValue()) * cy + Canvas.DEFAULT_HEIGHT * 0.1);
        }*/
        canvas.setDataPoints(xr, yr);
        canvas.setDataLimits(xMin.doubleValue(), xMax.doubleValue(), yMin.doubleValue(), yMax.doubleValue());
    }
    public void setTitle(String title){
        f.setTitle(title);
    }
    public void histogram(Distribution d,int n){
        histogram(d,n,250000);
    }
    public void histogram(Distribution d,int n, int k){
        if(k <= 0)
            return;
        ArrayList<Double> pq = new ArrayList<>();
        
        double min = d.getRandomNumber();
        double max = min;
        pq.add(min);
        for(int i = 1 ;i < k; i++){
            double x = d.getRandomNumber();
            pq.add(x);
            if( x < min)
                min = x;
            if( x > max)
                max = x;
        }
        double range = (max - min);
        min = min - range * Math.max(0.08 , 2 / (n));
        max = max + range * Math.max(0.08 , 2 / (n));
        range = (max - min);
        double partition = range / (n);
        double[] p_c = new double[n];
        for(int i = 0;i < n;i++){
            p_c[i] = 0;
        }
        for (Double pq1 : pq) {
            int pn = (int) ((pq1 - min) / partition);
            if(pn >= n)
                pn = n - 1;
            p_c[pn] += 1.0 / k;
        }
        ArrayList<Double> vx = new ArrayList<>();
        ArrayList<Double> vy = new ArrayList<>();
        for(int i = 0 ; i < n; i++){
            vx.add(min + partition * i);
            vy.add(p_c[i]);
        }
        plot(vx,vy);
    }
    private Number findMax(ArrayList<Double> num){
        if(num == null)
            return null;
        Number max = -1;
        for (Number num1 : num) {
            if (max.doubleValue() < num1.doubleValue() || max.doubleValue() == -1) {
                max = num1.doubleValue();
            }
        }
        return max;
    }
    private Number findMin(ArrayList<Double> num){
        if(num == null)
            return null;
        Number min = -1;
        for (Number num1 : num) {
            if (min.doubleValue() > num1.doubleValue() || min.doubleValue() == -1) {
                min = num1.doubleValue();
            }
        }
        return min;
    }
}
