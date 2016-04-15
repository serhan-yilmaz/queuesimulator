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

/**
 *
 * @author Serhan Yılmaz
 */
public abstract class Pointer {
    
    private double cx = 1,cy = 1;
    protected double size;
            
    public Pointer(double size){
        this.size = size;
    }

    public abstract void draw(Graphics g, double x , double y);
    
    public final void setCorrectionRatios(double cx,double cy){
        this.cx = cx;
        this.cy = cy;
    }
    
    protected final int cX(double x){
        return (int) Math.round(x * cx);
    }
    protected final int cY(double y){
        return (int) Math.round(y * cy);
    }
    
}
