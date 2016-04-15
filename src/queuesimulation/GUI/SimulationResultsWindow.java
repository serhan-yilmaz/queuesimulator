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
package queuesimulation.GUI;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import queuesimulation.GUI.graph.Figure;
import queuesimulation.QueueSystemStatistics;
import queuesimulation.distribution.CustomDistribution;
import queuesimulation.distribution.Distribution;

/**
 *
 * @author Serhan Yılmaz
 */
public class SimulationResultsWindow {
    private JDialog window;
    private Window parent_window;
    private QueueSystemStatistics qss;
    
    private JPanel pane;
    private ImageIcon icon;
    private Figure figure;
    
    private boolean locked = false;
    
    public SimulationResultsWindow(Window parent, QueueSystemStatistics qss, String title){
        this.parent_window = parent;
        this.window = new JDialog(this.parent_window, title);
        this.pane = new JPanel();
        this.qss = qss;
        icon = new ImageIcon(this.getClass().getResource("/images/logo.png"));
        figure = new Figure("Figure",window);
    }
    
    public void show(){
        final SimulationResultsWindow thisObject = this;
        pane.setLayout(new BorderLayout());
        JPanel text_panel = new JPanel();
        
        JTextArea text = new JTextArea(qss.toString());
        text.setEditable(false);
        text_panel.add(text);
        JPanel button_panel = new JPanel();
        JButton OK_button = new JButton("OK");
        JButton waiting_time_hist_button = new JButton("See Waiting Time Histogram");
        JButton service_time_hist_button = new JButton("See Service Time Histogram");
        OK_button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                close();
            }
        });
        waiting_time_hist_button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if(locked)
                    return;
                showWaitingTimeHistogram();
            }
        });
        service_time_hist_button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if(locked)
                    return;
                showServiceTimeHistogram();
            }
        });
        button_panel.setLayout(new GridLayout(1,0));
        button_panel.add(waiting_time_hist_button);
        button_panel.add(OK_button);
        button_panel.add(service_time_hist_button);
        pane.add(text,BorderLayout.NORTH);
        pane.add(button_panel,BorderLayout.SOUTH);
        
        window.setLocationRelativeTo(parent_window);
        window.setModal(true);
        window.add(pane);
        window.setIconImage(icon.getImage());
        window.pack();
        window.setResizable(false);
        window.setVisible(true);
    }
    protected final void close(){
        window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
    }
    private void showWaitingTimeHistogram(){
        locked = true;
        CustomDistribution d = qss.getWaitingTimeDistribution();
        figure.setTitle("Histogram of Waiting Times");
        figure.show();
        figure.histogram(d, 100, d.getSize());
        locked = false;
    }
    private void showServiceTimeHistogram(){
        locked = true;
        CustomDistribution d = qss.getServiceTimeDistribution();
        figure.setTitle("Histogram of Service Times");
        figure.show();
        figure.histogram(d, 100, d.getSize());
        locked = false;
    }
}
