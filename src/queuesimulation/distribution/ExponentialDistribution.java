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
public class ExponentialDistribution extends BasicDistribution{
    private static final ExponentialDistribution singleton = new ExponentialDistribution();

    private final DistributionComponent[] components;
    
    private final NumberFormat formatter = new DecimalFormat("0.0E0",DecimalFormatSymbols.getInstance(Locale.US));
    
    /**
     * Creates a ExponentialDistribution Object with default paramters.
     */
    public ExponentialDistribution(){
        components = new DistributionComponent[1];
        components[0] = new DistributionComponent("Rate",1e6);
    }
    /**
     * Creates a ExponentialDistribution Object with specified paramters.
     * @param rate 
     */
    public ExponentialDistribution(double rate){
        components = new DistributionComponent[1];
        components[0] = new DistributionComponent("Rate",rate);
    }
    @Override
    public double getRandomNumber() {
        double lambda = components[0].getValue();
        return  clip(Math.log(1-rand.nextDouble())/(-lambda));
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
    public double getRate(){
        return components[0].getValue();
    }
    public void setRate(double rate){
        this.components[0].setValue(rate);
    }
    
    @Override
    public String toString() {
        return "Exponential Distribution";
    }

    @Override
    public String getFormula() {
        return "E";
    }

    @Override
    protected Distribution getSingleton() {
        return singleton;
    }
    
    protected static Distribution getSingletonStatic() {
        return singleton;
    }

    @Override
    public String getDetailedFormula() {
        return "E("+ formatter.format(components[0].getValue()) + ")";
    }

    @Override
    public DistributionError checkForErrors() {
        return null;
    }
    
}
