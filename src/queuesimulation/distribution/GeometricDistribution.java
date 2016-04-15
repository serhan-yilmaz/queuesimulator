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
public class GeometricDistribution extends BasicDistribution{
    private static final GeometricDistribution singleton = new GeometricDistribution();
    
    DistributionComponent[] components;
    
    private final NumberFormat formatter = new DecimalFormat("0.0E0",DecimalFormatSymbols.getInstance(Locale.US));
    /**
     * Creates the GeometricDistribution Object with specified parameters.
     * @param probability 
     * @param scale 
     */
    public GeometricDistribution(double probability, double scale){
        this.components = new DistributionComponent[2];
        components[0] = new DistributionComponent("Probability",probability);
        components[1] = new DistributionComponent("Scale",scale);
    }
    /**
     * Creates the GeometricDistribution Object with default parameters.
     */
    public GeometricDistribution(){
        this.components = new DistributionComponent[2];
        components[0] = new DistributionComponent("Probability",0.5);
        components[1] = new DistributionComponent("Scale",1);
    }
    
    public double getProbability(){
        return components[0].getValue();
    }
    
    public void setProbability(double probability){
       components[0].setValue(probability);
    }
    
    public double getScale(){
        return components[1].getValue();
    }
    
    public void setScale(double scale){
       components[1].setValue(scale);
    }
    @Override
    public String toString() {
        return "Geometric Distribution";
    }

    @Override
    public DistributionComponent[] getComponents() {
        return components;
    }

    @Override
    protected void setComponent(int index, double value) {
        if(index >= 0 && index < components.length)
            components[index].setValue(value);
    }

    @Override
    protected Distribution getSingleton() {
        return singleton;
    }

    @Override
    public String getDetailedFormula(){
       return "Ge("+ formatter.format(components[0].getValue()) + ", " + formatter.format(components[1].getValue()) + ")";
    }

    @Override
    public double getRandomNumber() {
        double p = components[0].getValue();
        if( p <= 0 ) {
            return 1e40;
        }
        if( p >= 1) {
            return 0;
        }
        return clip(Math.floor(Math.log(rand.nextDouble()) / Math.log(1 - p)) * components[1].getValue());
    }

    @Override
    public String getFormula() {
        return "Ge";
    }
    
    protected static Distribution getSingletonStatic() {
        return singleton;
    }

    @Override
    public DistributionError checkForErrors() {
        if(components[0].getValue() <= 0)
            return new DistributionError("Probability parameter should be greater than 0.", "Parameter Error");
        if(components[0].getValue() > 1)
            return new DistributionError("Probability parameter should be less than or equal to 1.", "Parameter Error");
        return null;
    }
    
}
