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

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

/**
 *
 * @author Serhan Yılmaz
 */
public class DeterministicDistribution extends BasicDistribution{
    private static final DeterministicDistribution singleton = new DeterministicDistribution();
    
    private final DistributionComponent[] components;
    
    private final NumberFormat formatter = new DecimalFormat("0.0E0",DecimalFormatSymbols.getInstance(Locale.US));
        
    public DeterministicDistribution(){
        components = new DistributionComponent[1];
        components[0] = new DistributionComponent("Value",1);
    }
    
    @Override
    public String toString() {
        return "Deterministic";
    }

    @Override
    public DistributionComponent[] getComponents() {
        return components;
    }

    @Override
    protected void setComponent(int index, double value){
        if(index < components.length && index >= 0){
            components[index].setValue(value);
        }
    }

    @Override
    protected Distribution getSingleton() {
        return singleton;
    }

    @Override
    public double getRandomNumber() {
        return clip(components[0].getValue());
    }
    @Override
    public String getFormula() {
        return "D";
    }
    protected static Distribution getSingletonStatic() {
        return singleton;
    }

    @Override
    public String getDetailedFormula() {
        return "D("+ formatter.format(components[0].getValue())+")";
    }

    @Override
    public DistributionError checkForErrors() {
        return null;
    }
    
}
