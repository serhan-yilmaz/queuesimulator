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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import queuesimulation.distribution.Distribution;

/**
 *
 * @author Serhan Yılmaz
 */
public class QueueSystem {
    protected static final int MAXIMUM_ARRAY_LENGTH_DISTRIBUTION = 1000000;
    
    public static final int TERMINATION_OPTION_INPUT_PACKAGE = 0;
    public static final int TERMINATION_OPTION_SIMULATION_TIME = 1;
    public static final int TERMINATION_OPTION_REAL_TIME = 2;
    
    protected final PriorityQueue<TimedEvent> events;
    protected final Queue queue;
    protected final QueueSystemStatistics statistic;
    protected final LinkedList<Server> idle_servers;
    
    private final ArrayList<Server> servers;
    private final Time current_time = new Time(0);
    private final Time delayed_start_time = new Time(0);
    private final Distribution input_time;
    private final Distribution input_size;
    
    private volatile boolean stop_simulation = false;
    private volatile boolean interrupt_simulation = false;
    private boolean packet_size_error = false;
    private boolean simulation_started = false;
    private volatile boolean data_ready = false;
    private volatile boolean make_data_ready = false;
    
    private double simulation_delay_percentage = 0;
    
    /**
     * Creates the specified QueueSystem object.
     * 
     * @param server_amount Number of parallel transmission servers.
     * @param queue_length Buffer length in terms of bits.
     * @param transmission_rate Transmission rate per server in terms of
     * bits per second.
     * @param input_time Disribution of Package Arrival Time in bits per second
     * @param input_size Distribution of Package Size in bits
     */
    
    public QueueSystem(int server_amount,int queue_length ,double transmission_rate,Distribution input_time,Distribution input_size){
        events = new PriorityQueue(server_amount + 1,new TimedEventComparator());
        servers = new ArrayList<>();
        idle_servers = new LinkedList<>();
        statistic = new QueueSystemStatistics();
        this.input_time = input_time;
        this.input_size = input_size;
        for(int i = 0; i < server_amount ; i++){
            Server server = new Server(transmission_rate);
            servers.add(server);
            idle_servers.add(server);
        }
        this.queue = new Queue(queue_length);
    }
    
    /**
     * Simulates the queue system until the condition specified by 
     * the option and Number n parameters are reached. 
     * <p>
     * The option parameter can only be options provided in the class 
     * definition which are TERMINATION_OPTION_INPUT_PACKAGE,
     * TERMINATION_OPTION_SIMULATION_TIME, TERMINATION_OPTION_REAL_TIME.
     * <p>
     * Otherwise a NULL value will be returned.
     * <p>
     * For Number n, a Long value is expected for input package option whereas
     * for simulation time and real time options a Double value is expected.
     * 
     * @param option Simulation termination condition provided 
     * in the class definition
     * @param n Number specifying when the simulation will be terminated.

     * @return A QueueSystemStatistics object which contains information about
     * the simulation results or NULL if simulation is unsuccessful.
     * @throws queuesimulation.PacketArrivalTimeIsNegativeException
     * @throws queuesimulation.PacketSizeNotPositiveException
     * @throws queuesimulation.QueueSystemInterruptedException
     */
    public QueueSystemStatistics simulate(int option, Number n) throws PacketArrivalTimeIsNegativeException, PacketSizeNotPositiveException, QueueSystemInterruptedException{
        if(option != TERMINATION_OPTION_REAL_TIME && option != TERMINATION_OPTION_SIMULATION_TIME && option != TERMINATION_OPTION_INPUT_PACKAGE)
            return null;
        data_ready = false;
        if( simulation_delay_percentage == 0)
            data_ready = true;
        if(option == TERMINATION_OPTION_REAL_TIME){
            stop_simulation = false;
            make_data_ready = false;
            final Timer myTimer = new Timer();
            TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        stop_simulation = true;
                        myTimer.cancel();
                    }
                };
            myTimer.schedule(task,(long)(n.doubleValue() * 1000));
            if(!data_ready){
                final Timer myTimer2 = new Timer();
                TimerTask task2 = new TimerTask() {
                        @Override
                        public void run() {
                            make_data_ready = true;
                            myTimer2.cancel();
                        }
                    };
                myTimer2.schedule(task2,(long)(n.doubleValue() * 1000 * simulation_delay_percentage));
            }
        }
        simulation_started = true;
        interrupt_simulation = false;
        packet_size_error = false;
        long start_time = System.nanoTime();
        events.add(new InputEvent(current_time.getSum(getArrivalTime())));
        while(true){
            TimedEvent te = events.poll();
            Time delta_time = te.getEventTime().getDifference(current_time);
            if(delta_time.getTime() < 0){
                throw new PacketArrivalTimeIsNegativeException();
            }
            if(packet_size_error){
                throw new PacketSizeNotPositiveException();
            }
            if(interrupt_simulation)
                throw new QueueSystemInterruptedException();
            if(isDataReady()){
                if(statistic.state_times.size() > queue.getCurrentSize()){
                    statistic.state_times.set(queue.getCurrentSize(), statistic.state_times.get(queue.getCurrentSize()).getSum(delta_time));
                }else{
                    statistic.state_times.add(delta_time);
                }
                if(statistic.maximum_queue_size < queue.getCurrentSize())
                    statistic.maximum_queue_size = queue.getCurrentSize();
                if(statistic.maximum_queue_size_bits < queue.getCurrentSizeInBits())
                    statistic.maximum_queue_size_bits = queue.getCurrentSizeInBits();
                statistic.total_processing_packages += (servers.size() - idle_servers.size()) * delta_time.getTime();
                statistic.total_queue_length_bits += queue.getCurrentSizeInBits() * delta_time.getTime();
            }
            current_time.setTime(te.getEventTime());
            te.run(this);
            while(idle_servers.size() > 0 && queue.getCurrentSize() > 0 ){
                try {
                    Package p = queue.getPackage();
                    events.add(idle_servers.removeFirst().assign(p, current_time));
                    if(isDataReady()){
                        statistic.total_waiting_time.addTime(getCurrentTime().getDifference(p.birth_time));
                        double time = getCurrentTime().getDifference(p.birth_time).getTime();
                        statistic.total_waiting_time_sqr.addTime(time * time);
                        if(statistic.maximum_waiting_time.getTime() < getCurrentTime().getDifference(p.birth_time).getTime())
                            statistic.maximum_waiting_time.setTime(getCurrentTime().getDifference(p.birth_time));
                        statistic.total_waiting_time_counter ++;
                        if(statistic.waiting_times.size() < MAXIMUM_ARRAY_LENGTH_DISTRIBUTION)
                            statistic.waiting_times.add(time);
                    }
                } catch (QueueIsEmptyException ex) {
                    Logger.getLogger(QueueSystem.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            boolean c1 = option == TERMINATION_OPTION_INPUT_PACKAGE && statistic.input_packet_count >= n.longValue();
            boolean c2 = option == TERMINATION_OPTION_SIMULATION_TIME && getCurrentTime().getTime() >= n.doubleValue();
            boolean c3 = stop_simulation;
            
            if(!isDataReady()){
                boolean cx1 = option == TERMINATION_OPTION_INPUT_PACKAGE && statistic.real_input_packet_count >= n.longValue() * simulation_delay_percentage;
                boolean cx2 = option == TERMINATION_OPTION_SIMULATION_TIME && getCurrentTime().getTime() >= n.doubleValue() * simulation_delay_percentage;
                boolean cx3 = option == TERMINATION_OPTION_REAL_TIME && make_data_ready;
                if( cx1 || cx2 || cx3 ){
                    setDataReady();
                }
            }
            
            if(c1 || c2 || c3){
                for(Server s : servers){
                    if(!s.isIdle()){
                        s.up_time.addTime(getCurrentTime().getDifference(s.getAssignmentTime()));
                        s.setAssignmentTime(getCurrentTime());
                    }
                }
                break;
            }
        }
        
        statistic.state_utilizations = new ArrayList<>();
        for(Server s : servers){
            statistic.state_utilizations.add(s.up_time.getTime() / getActiveTime().getTime());
        }
        statistic.simulation_time = getActiveTime();
        statistic.total_simulation_time = new Time(getCurrentTime());
        long end_time = System.nanoTime();
        statistic.real_time = (end_time - start_time) * 1e-6;
        return statistic;
    }
    /**
     * Stops the simulation if it has started. Otherwise has no effect.
     * 
     */
    public void stopSimulation(){
        this.interrupt_simulation = true;
    }
    /**
     * Specifies the percentage delay amount before statistical data starts to
     * be saved. The delay amount can be in terms of input packages, simulation time or 
     * real time depending on the simulation option paramter.
     * <p>
     * Cannot be set after the simulation starts.
     * @param delay Percentage delay between %0 and %50
     */
    public void setPercentageDataDelay(double delay){
        if(!simulation_started && delay >= 0 && delay <= 50){
            this.simulation_delay_percentage = delay / 100;
        }
    }
    private void setDataReady(){
        data_ready = true;
        delayed_start_time.setTime(getCurrentTime());
      //  System.out.println(statistic.real_input_packet_count);
        for(Server s : servers){
            s.setAssignmentTime(getCurrentTime());
            s.up_time.setTime(0);
         }
    }
    protected Time getArrivalTime(){
        return new Time(input_time.getRandomNumber());
    }
    protected int getRandomPacketSize(){
        int size = (int) Math.round(input_size.getRandomNumber());
        if(isDataReady())
            statistic.total_packet_size += size;
        if(size <= 0 )
            packet_size_error = true;
        return size;
    }
    protected Time getCurrentTime(){
        return current_time;
    }
    protected Time getActiveTime(){
        return new Time(current_time).getDifference(delayed_start_time);
    }
    protected boolean isDataReady(){
        return data_ready;
    }
}
