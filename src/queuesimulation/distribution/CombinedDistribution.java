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
public abstract class CombinedDistribution extends Distribution{
    
    Distribution distributionA;
    Distribution distributionB;
    
    public CombinedDistribution(Distribution A,Distribution B){
        this.distributionA = A;
        this.distributionB = B;
    }
    
    public CombinedDistribution(){
        
    }
    
    @Override
    public abstract String toString();
    
    public abstract String getSymbol();
    
    @Override
    public abstract DistributionComponent[] getComponents();
    
    @Override
    protected abstract void setComponent(int index, double value);

    @Override
    public DistributionWindow createWindow(DistributionContainer dc) {
        return new CombinedDistributionWindow(dc,this);
    }
    
    @Override
    public String getFormula() {
        return "(" + distributionA.getFormula() + getSymbol() + distributionB.getFormula() + ")";
    }
    
    @Override
    public String getDetailedFormula() {
        return "(" + distributionA.getDetailedFormula() + getSymbol() + distributionB.getDetailedFormula() + ")";
    }
    
    @Override
    public DistributionError checkForErrors() {
        if(distributionA == null)
            return new DistributionError("Distribution A is not specified.", "Distribution Parameter Error");
        if(distributionB == null)
            return new DistributionError("Distribution B is not specified.", "Distribution Parameter Error");
        return null;
    }
    

    
    
}
