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
package queuesimulation;

import java.util.LinkedList;

/**
 *
 * @author Serhan Yılmaz
 */
public class Queue {
    
    private final int total_length;   // Queue Length in bits
    private int current_length = 0;   // Current Length in bits
    private final LinkedList<Package> packages;
    
    public Queue(int total_length){
        this.total_length = total_length;
        this.packages = new LinkedList<>();
    }
    public Package getPackage() throws QueueIsEmptyException{
        if(packages.size() == 0)
            throw new QueueIsEmptyException();
        Package p = packages.removeFirst();
        current_length -= p.getSize();
        return p;
    }
    public void addPackage(Package p) throws QueueIsFullException{
        if(current_length + p.getSize() > total_length)
            throw new QueueIsFullException();
        packages.add(p);
        current_length += p.getSize();
    }
    public int getCurrentSize(){
        return packages.size();
    }
    public int getCurrentSizeInBits(){
        return current_length;
    }
}
