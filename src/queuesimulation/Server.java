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

/**
 *
 * @author Serhan Yılmaz
 */
public class Server {
    
    // Transmission Rate In terms of Bits per second
    private final double transmission_rate; 
    private Package packet;
    // Assignment Time keeps the last time instance that the server starts
    //  to be utilized. Required for measuring utilization times.
    private Time assignment_time; 
    protected Time up_time = new Time(0);
    
    public Server(double transmission_rate){
        this.transmission_rate = transmission_rate;
    }
    public Package release(){
        Package p  = packet;
        packet = null;
        return p;
    }
    public TimedEvent assign(Package p,Time time){
        packet = p;
        assignment_time = new Time(time);
        return new ServerSentEvent(time.getSum(assign(p)),this);
    }
    protected Time assign(Package p){
        packet = p;
        return new Time(p.getSize()/transmission_rate);
    }
    protected void setAssignmentTime(Time time){
        if(this.assignment_time != null)
            this.assignment_time.setTime(time.getTime());
        else this.assignment_time = new Time(time);
    }
    public Time getAssignmentTime(){
        return assignment_time;
    }
    public boolean isIdle(){
        return packet == null;
    }
}
