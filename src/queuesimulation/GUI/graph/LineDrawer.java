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

import java.awt.Graphics;
import java.awt.Rectangle;

/**
 *
 * @author Serhan Yılmaz
 */
public abstract class LineDrawer {
    private double cx = 1, cy = 1;
    protected Rectangle window;
    protected double size;
    
    public LineDrawer(double size,Rectangle rec) {
        this.size = size;
        this.window = rec;
    }
    public abstract void draw(Graphics g, double x1, double y1, double x2, double y2);
    
    public final void setCorrectionRatios(double cx,double cy){
        this.cx = cx;
        this.cy = cy;
    }
    public final void setActiveWindow(Rectangle rec){
        this.window = rec;
    }
    protected final int cX(double x){
        return (int) Math.round(x * cx);
    }
    protected final int cY(double y){
        return (int) Math.round(y * cy);
    }
}
