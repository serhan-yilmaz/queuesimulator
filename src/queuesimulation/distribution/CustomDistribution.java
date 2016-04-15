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
package queuesimulation.distribution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Serhan Yılmaz
 */
public class CustomDistribution extends Distribution{
    private final List<Double> list;
    private int current = 0;
    public CustomDistribution(Double[] list){
        this.list = new ArrayList<>(Arrays.asList(list));
    }
    public CustomDistribution(List<Double> list){
        this.list = new ArrayList<>(list);
    }
    @Override
    public double getRandomNumber(){
        current++;
        if(current >= list.size())
            current = 0;
        return list.get(current);
    }

    @Override
    public String getFormula() {
        return "C";
    }
    @Override
    public String getDetailedFormula() {
        return "C";
    }
    /**
     * Adds another number to the list.
     * @param d number to be added to the list.
     */
    public void add(double d){
        list.add(d);
    }
    public int getSize(){
        return list.size();
    }
    @Override
    public DistributionComponent[] getComponents() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected DistributionWindow createWindow(DistributionContainer dc) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void setComponent(int index, double value) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected Distribution getSingleton() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String toString() {
        return "Custom Distribution";
    }

    @Override
    public DistributionError checkForErrors() {
        if(list.isEmpty() )
            return new DistributionError("Number list is empty!","Custom Distribution Error");
        return null;
    }
}
