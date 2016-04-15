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

/**
 *
 * @author Serhan Yılmaz
 */
public class MultipliedDistribution extends CombinedDistribution{
    private static final MultipliedDistribution singleton = new MultipliedDistribution();

    public MultipliedDistribution(Distribution A,Distribution B){
        super(A,B);
    }
    
    public MultipliedDistribution(){
        
    }
    
    
    @Override
    public String toString() {
        return "Multiplied Distribution";
    }

    @Override
    public String getSymbol() {
        return " x ";
    }

    @Override
    public DistributionComponent[] getComponents() {
        return null;
    }

    @Override
    protected void setComponent(int index, double value) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double getRandomNumber() {
        return clip(distributionA.getRandomNumber() * distributionB.getRandomNumber());
    }

    @Override
    protected Distribution getSingleton() {
        return singleton;
    }
    protected static Distribution getSingletonStatic(){
        return singleton;
    }

    @Override
    public DistributionError checkForErrors() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
