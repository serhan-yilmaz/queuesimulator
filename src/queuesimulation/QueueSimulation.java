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

import queuesimulation.GUI.MainFrame;
import queuesimulation.GUI.graph.Figure;
import queuesimulation.distribution.Distribution;
import queuesimulation.distribution.ExponentialDistribution;

/**
 *
 * @author Serhan Yılmaz
 */
public class QueueSimulation {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        /*
        QueueSystem system;
        ExponentialDistribution input_rate = new ExponentialDistribution(2e6);
        ExponentialDistribution input_size = new ExponentialDistribution(1/210.0);
        
        system = new QueueSystem(50, (int) 5e5, 1e7 , input_rate,input_size);
        QueueSystemStatistics statistic = system.simulate(QueueSystem.TERMINATION_OPTION_INPUT_PACKAGE,(long)1e6);
        
        statistic.print();
        */
        MainFrame main = new MainFrame();
        main.show();
        /*
        Figure figure = new Figure("Figure 1");
        figure.show();
        figure.plot(statistic.getStateTimes());
        */
    }
    
}
