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

import java.util.logging.Level;
import java.util.logging.Logger;
import static queuesimulation.QueueSystem.MAXIMUM_ARRAY_LENGTH_DISTRIBUTION;

/**
 *
 * @author Serhan Yılmaz
 */
public class ServerSentEvent extends TimedEvent{

    Server owner;
    
    public ServerSentEvent(Time event_time, Server owner) {
        super(event_time);
        this.owner = owner;
    }

    @Override
    public void run(QueueSystem qs) {
        if(qs.isDataReady())
            qs.statistic.sent_packet_count++;
        if (qs.queue.getCurrentSize() > 0){
            try {
                Package p = qs.queue.getPackage();
                Package p2 = owner.release();
                if(qs.isDataReady()){
                    qs.statistic.total_service_time.addTime(qs.getCurrentTime().getDifference(p2.birth_time));
                    qs.statistic.total_waiting_time.addTime(qs.getCurrentTime().getDifference(p.birth_time));
                    qs.statistic.total_waiting_time_counter ++;
                    double time = qs.getCurrentTime().getDifference(p.birth_time).getTime();
                    double time2 = qs.getCurrentTime().getDifference(p2.birth_time).getTime();
                    qs.statistic.total_waiting_time_sqr.addTime(time * time);
                    qs.statistic.total_service_time_sqr.addTime(time2 * time2);
                    if(qs.statistic.maximum_waiting_time.getTime() < time)
                        qs.statistic.maximum_waiting_time.setTime(qs.getCurrentTime().getDifference(p.birth_time));
                    if(qs.statistic.maximum_service_time.getTime() < qs.getCurrentTime().getDifference(p2.birth_time).getTime())
                        qs.statistic.maximum_service_time.setTime(qs.getCurrentTime().getDifference(p2.birth_time));
                    if(qs.statistic.waiting_times.size() < MAXIMUM_ARRAY_LENGTH_DISTRIBUTION)
                            qs.statistic.waiting_times.add(time);
                    if(qs.statistic.service_times.size() < MAXIMUM_ARRAY_LENGTH_DISTRIBUTION)
                            qs.statistic.service_times.add(time2);
                }
                setEventTime(owner.assign(p).getSum(qs.getCurrentTime()));
                qs.events.add(this);
            } catch (QueueIsEmptyException ex) {
                Logger.getLogger(ServerSentEvent.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            
            Package p = owner.release();
            
            if(qs.isDataReady()){
                owner.up_time.addTime(qs.getCurrentTime().getDifference(owner.getAssignmentTime()));
                qs.statistic.total_service_time.addTime(qs.getCurrentTime().getDifference(p.birth_time));
                double time2 = qs.getCurrentTime().getDifference(p.birth_time).getTime();
                qs.statistic.total_service_time_sqr.addTime(time2 * time2);
                if(qs.statistic.maximum_service_time.getTime() < qs.getCurrentTime().getDifference(p.birth_time).getTime())
                    qs.statistic.maximum_service_time.setTime(qs.getCurrentTime().getDifference(p.birth_time));
                if(qs.statistic.service_times.size() < MAXIMUM_ARRAY_LENGTH_DISTRIBUTION)
                            qs.statistic.service_times.add(time2);
            }
            qs.idle_servers.add(owner);
        }
    }
    
}
