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

import java.util.Random;

/**
 *
 * @author Serhan Yılmaz
 */
public class ORredDistribution extends CombinedDistribution{
    private static final ORredDistribution singleton = new ORredDistribution();
    
    private final DistributionComponent[] components;
    
    public ORredDistribution(Distribution A,Distribution B){
        super(A,B);
        this.components = new DistributionComponent[1];
        components[0] = new DistributionComponent("B/A Probabilty Weight Ratio",0.5);
    }
    
    public ORredDistribution(){
        this.components = new DistributionComponent[1];
        components[0] = new DistributionComponent("B/A Probabilty Weight Ratio",0.5);
    }
    
    protected Random rand = new Random();
    
    @Override
    public String toString() {
        return "ORed Distribution";
    }

    @Override
    public String getSymbol() {
        return " OR ";
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
    public double getRandomNumber() {
        double p = components[0].getValue();
        if(rand.nextDouble() >= p )
            return clip(distributionA.getRandomNumber());
        else
            return clip(distributionB.getRandomNumber());
    }

    @Override
    public String getFormula() {
        return "(" + distributionA.getFormula() + getSymbol() + distributionB.getFormula() + ")";
    }
    
    @Override
    protected Distribution getSingleton() {
        return singleton;
    }
    protected static Distribution getSingletonStatic() {
        return singleton;
    }
    
    @Override
    public DistributionWindow createWindow(DistributionContainer dc) {
        return new ORredDistributionWindow(dc,this);
    }
    
    @Override
    public String getDetailedFormula() {
        return "(" + distributionA.getDetailedFormula() + " OR(" + ((int) (100 * components[0].getValue())) + ") " + distributionB.getDetailedFormula() + ")";
    }
    
    @Override
    public DistributionError checkForErrors(){
        DistributionError err = super.checkForErrors();
        if(err != null)
            return err;
        if(components[0].getValue() < 0 || components[0].getValue() > 1)
            return new DistributionError("Ratio should be between 0 and 1.","Parameter Error");
        return null;
    }
}
