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

import java.util.Comparator;

/**
 *
 * @author Serhan Yılmaz
 */
public class FigurePointComparator implements Comparator<FigurePoint>{

    @Override
    public int compare(FigurePoint o1, FigurePoint o2) {
        if ( o1.getX() > o2.getX() ) 
            return 1;
        if ( o1.getX() < o2.getX() ) 
           return -1;
        return 0;
    }
    
}
