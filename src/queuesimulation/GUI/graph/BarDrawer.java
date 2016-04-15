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
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 *
 * @author Serhan Yılmaz
 */
public class BarDrawer extends LineDrawer{

    public BarDrawer(double size, Rectangle rec) {
        super(size, rec);
    }

    @Override
    public void draw(Graphics g, double x1, double y1, double x2, double y2) {

        g.setColor(Color.blue);
        g.fillRect(cX(x1), cY(y1), cX(x2 - x1), window.y - cY(y1));
        if(size > 0){
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke((float) size));
            g.setColor(Color.black);
            g.drawRect(cX(x1), cY(y1), cX(x2 - x1), window.y - cY(y1));
        }
    }
    
}
