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
public abstract class BasicDistribution extends Distribution{
    protected Random rand = new Random();
    
    @Override
    public abstract String toString();
    
    @Override
    public abstract DistributionComponent[] getComponents();
    
    @Override
    protected abstract void setComponent(int index, double value);
    
    @Override
    public DistributionWindow createWindow(DistributionContainer dc) {
        return new BasicDistributionWindow(dc,this);
    }
    
    @Override
    protected abstract Distribution getSingleton();
    
    @Override
    public abstract String getDetailedFormula();
    
    @Override
    public abstract DistributionError checkForErrors();
}
