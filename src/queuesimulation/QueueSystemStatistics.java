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
package queuesimulation;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import queuesimulation.distribution.CustomDistribution;

/**
 *
 * @author Serhan Yılmaz
 */
public class QueueSystemStatistics {
    protected ArrayList<Time> state_times;
    protected ArrayList<Double> state_utilizations;
    protected ArrayList<Double> waiting_times;
    protected ArrayList<Double> service_times;
    
    protected int sent_packet_count = 0;
    protected int input_packet_count = 0;
    protected int real_input_packet_count = 0;
    protected int lost_packet_count = 0;
    protected int maximum_queue_size;
    protected int maximum_queue_size_bits;
    protected long total_packet_size = 0;
    protected Time simulation_time;
    protected Time total_simulation_time;
    protected Time total_waiting_time = new Time(0);
    protected Time total_service_time = new Time(0);
    protected Time total_waiting_time_sqr = new Time(0);
    protected Time total_service_time_sqr = new Time(0);
    protected Time maximum_waiting_time = new Time(0);
    protected Time maximum_service_time = new Time(0);
    protected double total_processing_packages = 0;
    protected double total_queue_length_bits = 0;
    protected int total_waiting_time_counter = 0;
    protected double real_time;

    public QueueSystemStatistics(){
        state_times = new ArrayList<>();
        waiting_times = new ArrayList<>();
        service_times = new ArrayList<>();
    }
    public CustomDistribution getWaitingTimeDistribution(){
        return new CustomDistribution(waiting_times);
    }
    public CustomDistribution getServiceTimeDistribution(){
        return new CustomDistribution(service_times);
    }
    public double getPacketLoss(){
        return lost_packet_count / (input_packet_count + 0.0);
    }
    public double getAveragePacketSize(){
        return total_packet_size/ (input_packet_count + 0.0);
    }
    public double getAverageQueueSizeBits(){
        return total_queue_length_bits / simulation_time.getTime();
    }
    public double getMaximumQueueSizeBits(){
        return maximum_queue_size_bits;
    }
    public double getMaximumQueueLength(){
        return maximum_queue_size;
    }
    public double getAverageServiceTime(){
        return total_service_time.getTime() / sent_packet_count;
    }
    public double getAverageWaitingTime(){
        return total_waiting_time.getTime() / total_waiting_time_counter;
    }
    public double getMaximumServiceTime(){
        return maximum_service_time.getTime();
    }
    public double getMaximumWaitingTime(){
        return maximum_waiting_time.getTime();
    }
    public double getServiceTimeStandardDeviation(){
        double a = getAverageServiceTime();
        return Math.sqrt(total_service_time_sqr.getTime() / sent_packet_count - a * a);
    }
    public double getWaitingTimeStandardDeviation(){
        double a = getAverageWaitingTime();
        return Math.sqrt(total_waiting_time_sqr.getTime() / total_waiting_time_counter - a * a);
    }
    public double getAverageQueueLength(){
        double total = 0;
        for(int i = 0; i < state_times.size(); i++){
            total += i * state_times.get(i).getTime();
        }   
        return total / simulation_time.getTime();
    }
    public double getAveragePackagesInSystem(){
        return getAverageQueueLength() + total_processing_packages / simulation_time.getTime();
    }
    public double getAverageUtilizationRate(){
        double avg_util = 0;
        for(Double d : state_utilizations ){
            avg_util += d / state_utilizations.size();
        }
        return avg_util;
    }
    public ArrayList<Double> getStateTimes(){
        ArrayList<Double> st = new ArrayList<>();
        for (Time state_time : state_times) {
            st.add(20 * Math.log10(state_time.getTime() / simulation_time.getTime()));
            //st.add(state_time.getTime() / simulation_time.getTime());
           // System.out.println(state_time.getTime() / simulation_time.getTime());
        }
        return st;
    }
    
    public void print(){
        QueueSystemStatistics statistic = this;
        
        System.out.println("Simulation Time = " + statistic.simulation_time.getTime() + " s");
        System.out.println("Input Packet Count = " + statistic.input_packet_count);
        System.out.println("Average Packet Size = " + statistic.getAveragePacketSize() + " bits");
        
        System.out.println("Packet Loss = %" + 100 * statistic.getPacketLoss());
        System.out.println("Average Utilization Factor = %" + 100 * statistic.getAverageUtilizationRate());
        System.out.println("Average Packages In System = " + statistic.getAveragePackagesInSystem() + " packages");
        System.out.println("Maximum Queue Length = " + statistic.getMaximumQueueLength() + " packages");
        System.out.println("Average Queue Length = " + statistic.getAverageQueueLength() + " packages");
        System.out.println("Average Queue Length = " + statistic.getAverageQueueSizeBits() + " bits");
        System.out.println("Average Waiting Time = " + statistic.getAverageWaitingTime() + " s");
        System.out.println("Average Service Time = " + statistic.getAverageServiceTime() + " s");
        
        /*
        for ( int i = 0 ; i < statistic.state_utilizations.size() ; i ++){
            System.out.println("Utilization Factor of Server " + (i + 1) + " = %" + 100 * statistic.state_utilizations.get(i));
        }*/
    }
    
    @Override
    public String toString(){
        String eol = System.getProperty("line.separator");
        String r = "";
        String[] s = new String[17];
        int ind = 0;
        NumberFormat formatter = new DecimalFormat("#0.000");     
        NumberFormat formatter2 = new DecimalFormat("0.000E0");  
        s[ind++] = "Simulation Time = " + formatter2.format(simulation_time.getTime()) + " s";
        s[ind++] = ("Input Packet Count = " + input_packet_count);
        s[ind++] = ("Average Packet Size = " + formatter.format(getAveragePacketSize()) + " bits");
        s[ind++] = ("Packet Loss = %" + formatter.format((100 * getPacketLoss())));
        s[ind++] = ("Average Utilization Factor = %" + formatter.format(100 * getAverageUtilizationRate()));
        s[ind++] = ("Average Packages In System = " + formatter.format(getAveragePackagesInSystem()) + " packages");
        s[ind++] = ("Maximum Queue Length = " + formatter.format(getMaximumQueueLength()) + " packages");
        s[ind++] = ("Maximum Queue Length = " + formatter.format(getMaximumQueueSizeBits()) + " bits");
        s[ind++] = ("Average Queue Length = " + formatter.format(getAverageQueueLength()) + " packages");
        s[ind++] = ("Average Queue Length = " + formatter.format(getAverageQueueSizeBits()) + " bits");
        s[ind++] = ("Average Waiting Time = " + formatter2.format(getAverageWaitingTime()) + " s");
        s[ind++] = ("Average Service Time = " + formatter2.format(getAverageServiceTime()) + " s");
        s[ind++] = ("Maximum Waiting Time = " + formatter2.format(getMaximumWaitingTime()) + " s");
        s[ind++] = ("Maximum Service Time = " + formatter2.format(getMaximumServiceTime()) + " s");
        s[ind++] = ("Waiting Time Standard Deviation = " + formatter2.format(getWaitingTimeStandardDeviation()) + " s");
        s[ind++] = ("Service Time Standard Deviation = " + formatter2.format(getServiceTimeStandardDeviation()) + " s");
        s[ind++] = "It took " + formatter.format(real_time) + " ms to simulate the system!";
        for(String st : s){
            r = r + st + eol;
        }
        return r;
    }
}
