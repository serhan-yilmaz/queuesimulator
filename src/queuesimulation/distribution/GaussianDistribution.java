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
public class GaussianDistribution extends BasicDistribution{
    private static final GaussianDistribution singleton = new GaussianDistribution();
    
    private final DistributionComponent[] components;
    
    private final NumberFormat formatter = new DecimalFormat("0.0E0",DecimalFormatSymbols.getInstance(Locale.US));
    /**
     * Creates a GaussianDistribution Object with specified paramters.
     * @param mean  Mean value
     * @param deviation Standard Deviation value
     */
    public GaussianDistribution(double mean, double deviation){
        components = new DistributionComponent[2];
        components[0] = new DistributionComponent("Mean",mean);
        components[1] = new DistributionComponent("Standard Deviation",deviation);
    }
    /**
     * Creates a GaussianDistribution Object with default parameters.
     */
    public GaussianDistribution(){
        components = new DistributionComponent[2];
        components[0] = new DistributionComponent("Mean",1e-3);
        components[1] = new DistributionComponent("Standard Deviation",1e-3);
    }
    
    public void setMean(double mean){
        components[0].setValue(mean);
    }
    
    public void setStandardDeviation(double deviation){
        components[1].setValue(deviation);
    }
    
    @Override
    public double getRandomNumber() {
        return clip(rand.nextGaussian() * components[1].getValue() + components[0].getValue());
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
    public String toString() {
        return "Gaussian Distribution";
    }

    @Override
    public String getFormula() {
        return "G";
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
        return "G("+ formatter.format(components[0].getValue()) + ", " + formatter.format(components[1].getValue()) + ")";
    }

    @Override
    public DistributionError checkForErrors() {
        if(components[1].getValue() < 0)
            return new DistributionError("Standard Deviation parameter cannot be negative.", "Parameter Error");
        return null;
    }
}
