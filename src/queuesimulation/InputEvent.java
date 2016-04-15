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
public class InputEvent extends TimedEvent{

    public InputEvent(Time event_time) {
        super(event_time);
    }

    @Override
    public void run(QueueSystem qs) {
        if(qs.isDataReady())
            qs.statistic.input_packet_count++;
        qs.statistic.real_input_packet_count++;
        Package p = new Package(qs.getRandomPacketSize(),qs.getCurrentTime());
        try {
            qs.queue.addPackage(p);
        } catch (QueueIsFullException ex) {
            if(qs.isDataReady())
                qs.statistic.lost_packet_count++;
        }
        setEventTime(qs.getArrivalTime().getSum(qs.getCurrentTime()));
        qs.events.add(this);
    }
}
