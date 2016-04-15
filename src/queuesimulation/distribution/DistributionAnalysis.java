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
public class DistributionAnalysis {
    private final double mean;
    private final double standard_deviation;
    private final double skewness;
    private final double kurtosis;
    public DistributionAnalysis(double mean, double deviation, double skewness, double kurtosis){
        this.mean = mean;
        this.standard_deviation = deviation;
        this.skewness = skewness;
        this.kurtosis = kurtosis;
    }
    public double getMean(){
        return mean;
    }
    public double getStandardDeviation(){
        return standard_deviation;
    }
    public double getSkewness(){
        return skewness;
    }
    public double getKurtosis(){
        return kurtosis;
    }
    public double getExcessKurtosis(){
        return kurtosis - 3;
    }
}
