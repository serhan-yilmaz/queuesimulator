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
public class UniformDistribution extends BasicDistribution{
    private static final UniformDistribution singleton = new UniformDistribution();
    
    private final DistributionComponent[] components;
    
    private final NumberFormat formatter = new DecimalFormat("0.0E0",DecimalFormatSymbols.getInstance(Locale.US));
    
    public UniformDistribution(double min, double max){
        components = new DistributionComponent[2];
        components[0] = new DistributionComponent("Min",min);
        components[1] = new DistributionComponent("Max",max);
    }
    
    public UniformDistribution(){
        components = new DistributionComponent[2];
        components[0] = new DistributionComponent("Min",10);
        components[1] = new DistributionComponent("Max",100);
    }
    public double getMin(){
        return components[0].getValue();
    }
    public void setMin(double min){
        components[0].setValue(min);
    }
    public double getMax(){
        return components[1].getValue();
    }
    public void setMax(double max){
        components[1].setValue(max);
    }
    @Override
    public String toString() {
        return "Uniform Distribution";
    }
    
    @Override
    public DistributionComponent[] getComponents() {
        return components;
    }

    @Override
    protected void setComponent(int index, double value) {
        components[index].setValue(value);
    }

    @Override
    public double getRandomNumber() {
        return clip(rand.nextDouble() * (components[1].getValue() - components[0].getValue()) + components[0].getValue());
    }

    @Override
    public String getFormula() {
        return "U";
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
        return "U("+ formatter.format(components[0].getValue()) + ", " + formatter.format(components[1].getValue()) + ")";
    }

    @Override
    public DistributionError checkForErrors() {
        if(components[0].getValue() > components[1].getValue())
            return new DistributionError("Minimum parameter should be lesser than Maximum.", "Parameter Error");
        return null;
    }
}
