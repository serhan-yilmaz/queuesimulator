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

import java.util.PriorityQueue;


/**
 *
 * @author Serhan Yılmaz
 */
public abstract class Distribution {
    
    protected double clip_min = 0;
    protected double clip_max = 0;
    protected boolean clip_min_enable = false;
    protected boolean clip_max_enable = false;
    
    public abstract double getRandomNumber();
    
    public abstract String getFormula();
    
    public abstract String getDetailedFormula();
    
    public abstract DistributionComponent[] getComponents();
    
    protected abstract DistributionWindow createWindow(DistributionContainer dc);
    
    protected abstract void setComponent(int index, double value);
    
    protected final double clip(double num){
        if(num < clip_min && clip_min_enable)
            num = clip_min;
        if(num > clip_max && clip_max_enable)
            num = clip_max;
        return num;
    }
    
    protected abstract Distribution getSingleton();
    
    @Override
    public abstract String toString();
    /**
     * Checks the distribution object for possible errors.
     * Returns DistributionError if there is one, or returns NULL if
     * there are no errors.
     * @return DistributionError which specifies the error type and message
     */
    
    public abstract DistributionError checkForErrors();
    
    /**
     * Estimates the mean of the random distribution 
     * using the Monte Carlo method.
     * @return 
     */
    public final double getMean(){
        int n = 10000;
        double t = 0;
        for(int i = 0; i < n ; i++){
            t += getRandomNumber();
        }
        return t / n;
    }
    /**
     * Estimates the stardard deviation of the random 
     * distribution using the Monte Carlo method.
     * @return 
     */
    public final double getStandardDeviation(){
        int n = 10000;
        double t1 = 0, t2 = 0;
        for(int i = 0; i < n ; i++){
            double x = getRandomNumber();
            t1 += x * x;
            t2 += x;
        }
        t1 /= n;
        t2 /= n;
        return Math.sqrt(t1 - t2 * t2);
    }
    
    public final DistributionAnalysis getMoments(){
        DistributionAnalysis result;
        int n = 250000;
        double t1 = 0, t2 = 0, t3 = 0, t4 = 0;
        for(int i = 0; i < n ; i++){
            double x = getRandomNumber();
            double temp = x;
            t1 += temp;
            temp *= x;
            t2 += temp;
            temp *= x;
            t3 += temp;
            temp *= x;
            t4 += temp;
        }
        t1 /= n;
        t2 /= n;
        t3 /= n;
        t4 /= n;
        double mean = t1;
        double m2 = mean * mean;
        double m3 = m2 * mean;
        double m4 = m3 * mean;
        double d2 = t2 - mean * mean;
        double deviation = Math.sqrt(d2);
        double d3 = d2 * deviation;
        double d4 = d3 * deviation;
        double skewness = (t3 - 3 * mean * d2 - m3) / d3;
        double kurtosis = (t4 - 4 * t3 * mean + 6 * t2 * m2 - 4 * t1 * m3 + m4) / d4;
        result = new DistributionAnalysis(mean,deviation,skewness,kurtosis);
        return result;
    }
    public final DistributionAnalysis getLMoments(){
        PriorityQueue<Double> pq = new PriorityQueue<>();
        DistributionAnalysis result;
        int n = 250000;
        for(int i = 0; i < n ; i++){
            pq.add(getRandomNumber());
        }
        double b0 = 0;
        double b1 = 0;
        double b2 = 0;
        double b3 = 0;
        for(int i = 1 ; i <= n ; i++){
            double x = pq.poll();
            double r1 = (i - 1.0) / (n - 1);
            double r2 = r1 * (i - 2.0) / (n - 2);
            double r3 = r2 * (i - 3.0) / (n - 3);
            b0 += x;
            b1 += x * r1;
            b2 += x * r2;
            b3 += x * r3;
        }
        b0 /= n;
        b1 /= n;
        b2 /= n;
        b3 /= n;
        double l1 = b0;
        double l2 = 2 * b1 - b0;
        double l3 = 6 * b2 - 6 * b1 + b0;
        double l4 = 20 * b3 - 30 * b2 + 12 * b1 - b0;
        double mean = l1;
        double lscale = l2 / l1;
        double lskewness = l3 / l2;
        double lkurtosis = l4 / l2;
        result = new DistributionAnalysis(mean, lscale, lskewness, lkurtosis);
        return result;
    }
}
