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
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import queuesimulation.GUI.graph.Figure;
import queuesimulation.PacketArrivalTimeIsNegativeException;
import queuesimulation.PacketSizeNotPositiveException;
import queuesimulation.QueueSystem;
import queuesimulation.QueueSystemInterruptedException;
import queuesimulation.QueueSystemStatistics;
import queuesimulation.distribution.Distribution;
import queuesimulation.distribution.DistributionContainer;

/**
 *
 * @author Serhan Yılmaz
 */
public class MainFrame {
    private final static String version = "v1.05";
    
    private JFrame f;
    private JPanel main_pane;
    private DistributionContainer p_time;
    private DistributionContainer p_size;
    
    private JFormattedTextField sim_delay;
    private JFormattedTextField sim_length;
    private JFormattedTextField buffer_size;
    private JFormattedTextField transmission_rate;
    private JFormattedTextField server_amount;
    private int simulation_option = QueueSystem.TERMINATION_OPTION_INPUT_PACKAGE;
    private JButton stop_button;
    private JButton simulate_button;
    
    private volatile QueueSystem system;
    
    private ImageIcon icon;
    
    public MainFrame(){
        f = new JFrame("Queue Simulator " + version);
        initialize();
    }
    private void initialize(){
        main_pane = new JPanel();
        icon = new ImageIcon(this.getClass().getResource("/images/logo.png"));
        f.setIconImage(icon.getImage());
        //main_pane.setLayout(new GridBagLayout());
        //main_pane.setPreferredSize(new Dimension(640,480));
        //main_pane.setSize(new Dimension(640,480));
        main_pane.setLayout(new BoxLayout(main_pane, BoxLayout.PAGE_AXIS));
        main_pane.setBorder(BorderFactory.createLineBorder(Color.black));
        simulate_button = new JButton("Simulate");
        stop_button = new JButton("Stop");
        stop_button.setEnabled(false);
        simulate_button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        simulate();
                    }
                }).start();
            }
        });
        stop_button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                stopSimulation();
            }
        });
        JPanel button_pane = new JPanel();
        button_pane.add(stop_button);
        button_pane.add(simulate_button);
        
        p_time = new DistributionContainer(f,"Packet Arrival Time Distribution");
        p_size = new DistributionContainer(f,"Packet Size Distribution");
        JPanel distribution_pane = new JPanel();
        distribution_pane.setLayout(new GridLayout(1,0));
        distribution_pane.add(p_time.getPane());
        distribution_pane.add(p_size.getPane());
        
        
        JPanel sim_length_pane = new JPanel();
        JLabel sim_length_label1 = new JLabel("Simulation Length : ");
        final JLabel sim_length_label2 = new JLabel(" seconds");
        sim_length_pane.add(sim_length_label1);
        
        NumberFormat general = new DecimalFormat("0.#####E0");
        sim_length = new JFormattedTextField(general);
        sim_length.setColumns(6);
        sim_length.setValue(1);
        sim_length_pane.add(sim_length);
        sim_length_pane.add(sim_length_label2);
        SimulationLengthOption[] sim_length_options = new SimulationLengthOption[3];
        sim_length_options[0] = new SimulationLengthOption("Input Packages", QueueSystem.TERMINATION_OPTION_INPUT_PACKAGE," packages");
        sim_length_options[1] = new SimulationLengthOption("Simulation Time", QueueSystem.TERMINATION_OPTION_SIMULATION_TIME, " seconds");
        sim_length_options[2] = new SimulationLengthOption("Real Time", QueueSystem.TERMINATION_OPTION_REAL_TIME," seconds");
        JComboBox sim_length_list = new JComboBox(sim_length_options);
        
        sim_length_list.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox cb = (JComboBox)e.getSource();
                SimulationLengthOption opt = (SimulationLengthOption)cb.getSelectedItem();
                simulation_option = opt.getOption();
                sim_length_label2.setText(opt.getUnit());
            }
        });
        sim_length_list.setSelectedIndex(2);
        sim_length_pane.add(sim_length_list);
        
        JPanel sim_delay_pane = new JPanel();
        JLabel sim_delay_label = new JLabel("Simulation Result Data Delay : %");
        NumberFormat general3 = new DecimalFormat("#0.00#");
        sim_delay = new JFormattedTextField(general3);
        sim_delay.setColumns(6);
        sim_delay.setValue(10);
        sim_delay_pane.add(sim_delay_label);
        sim_delay_pane.add(sim_delay);
        
        JLabel buffer_label = new JLabel("Queue Buffer Size : ");
        
        
        
        NumberFormat df2 = NumberFormat.getIntegerInstance();
        df2.setGroupingUsed(false);
        buffer_size = new JFormattedTextField(df2);
        buffer_size.setColumns(6);
        buffer_size.setValue(1e5);
        JLabel buffer_label2 = new JLabel(" bits ");
        JPanel buffer_panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buffer_panel.add(buffer_label);
        buffer_panel.add(buffer_size);
        buffer_panel.add(buffer_label2);
        
        JPanel transmission_panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel transmission_label = new JLabel("Transmission Rate : ");
        JLabel transmission_label2 = new JLabel(" bits per second");
        NumberFormat df3 = new DecimalFormat("0.#####E0");
        transmission_rate = new JFormattedTextField(df3);
        transmission_rate.setColumns(6);
        transmission_rate.setValue(1e6);
        transmission_panel.add(transmission_label);
        transmission_panel.add(transmission_rate);
        transmission_panel.add(transmission_label2);
        
        JPanel server_panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel server_label = new JLabel("Server amount : ");
        NumberFormat df4 = NumberFormat.getIntegerInstance();
        df4.setGroupingUsed(false);
        server_amount = new JFormattedTextField(df4);
        server_amount.setColumns(6);
        server_amount.setValue(1);
        server_panel.add(server_label);
        server_panel.add(server_amount);
        
        JPanel param_panel = new JPanel();
        JLabel param = new JLabel(" Parameters :");
        param.setFont(new Font("Arial",Font.BOLD,14));
        param_panel.setLayout(new BorderLayout());
        param_panel.add(param,BorderLayout.WEST);
        
        JPanel about_panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton about_button = new JButton("About");
        about_panel.add(about_button);
        param_panel.add(about_panel,BorderLayout.EAST);
        
        about_button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                displayAboutButtonMessage();
            }
        });
        
        main_pane.add(param_panel);
        main_pane.add(server_panel);
        main_pane.add(transmission_panel);
        main_pane.add(buffer_panel);
        main_pane.add(distribution_pane);
        main_pane.add(sim_length_pane);
        main_pane.add(sim_delay_pane);
        main_pane.add(button_pane);
    }
    
    public void show(){
        f.add(main_pane);
        f.setResizable(false);
        f.setUndecorated(false);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
    protected void simulate(){
        if(p_time.isEmpty()){
            displayErrorMessage("Packet Arrival Time Distribution is Empty!", "Distribution Error");
            return;
        }
        if(p_size.isEmpty()){
            displayErrorMessage("Packet Size Distribution is Empty!", "Distribution Error");
            return;
        }
        Number n = (Number) sim_length.getValue();
        if(n == null || n.doubleValue() <= 0 ){
            displayErrorMessage("Simulation Length should be greater than 0.", "Simulation Length Error");
            return;
        }
        Number n2 = (Number) server_amount.getValue();
        if(n2 == null || n2.intValue() <= 0 ){
            displayErrorMessage("Server amount should be greater than 0.", "Server Amount Error");
            return;
        }
        Number n3 = (Number) transmission_rate.getValue();
        if(n3 == null || n3.doubleValue() <= 0 ){
            displayErrorMessage("Transmission rate should be greater than 0.", "Transmission Rate Error");
            return;
        }
        Number n4 = (Number) buffer_size.getValue();
        if(n4 == null || n4.intValue() <= 0 ){
            displayErrorMessage("Buffer size should be greater than 0.", "Buffer Size Error");
            return;
        }
        Number n5 = (Number) sim_delay.getValue();
        if(n5 == null || n5.doubleValue() < 0 || n5.doubleValue() > 50){
            displayErrorMessage("Percentage Simulation Result Data Delay should be between %0 and %50.", "Simulation Delay Error");
            return;
        }
        system = new QueueSystem(n2.intValue(), n4.intValue(), n3.doubleValue() , p_time.getDistribution(), p_size.getDistribution());
        QueueSystemStatistics qss;
        try {
            system.setPercentageDataDelay(n5.doubleValue());
            this.simulate_button.setEnabled(false);
            this.stop_button.setEnabled(true);
            qss = system.simulate(simulation_option, n);
            if(qss == null){
                displayErrorMessage("Simulation has failed.", "Unknown Error");
                return;
            }
            SimulationResultsWindow srw = new SimulationResultsWindow(f,qss,"Simulation Results Report");
            srw.show();
            /*
            Figure figure = new Figure("Figure",f);
            Distribution d = qss.getServiceTimeDistribution();
            figure.setTitle("Histogram of " + d.toString());
            figure.show();
            figure.histogram(d, 100);*/
            //displayMessage(qss.toString(),"Simulation Results Report");
        } catch (PacketArrivalTimeIsNegativeException ex) {
            String eol = System.getProperty("line.separator");
            String r = "";
            r = r + "Negative packet arrival times are not accepted." + eol;
            r = r + "Consider clipping the distribution at 0." + eol;
            displayErrorMessage(r, "Packet Arrival Time Distribution Error");
        } catch (PacketSizeNotPositiveException ex) {
            String eol = System.getProperty("line.separator");
            String r = "";
            r = r + "Non-positive packet sizes are not accepted." + eol;
            r = r + "Consider clipping the distribution at 1." + eol;
            displayErrorMessage(r, "Packet Size Distribution Error");
        } catch (QueueSystemInterruptedException ex) {
            String eol = System.getProperty("line.separator");
            String r = "";
            r = r + "The simulation has been terminated by the user.";
            displayMessage(r, "Simulation Interrupted");
        }
        finally{
          this.stop_button.setEnabled(false);
          this.simulate_button.setEnabled(true);
        }
    
        //qss.print();
    }
    protected void stopSimulation(){
        if(system != null)
            system.stopSimulation();
    }
    protected final void displayErrorMessage(String error, String title){
        //JOptionPane.showMessageDialog(f, error, title, JOptionPane.ERROR_MESSAGE);
        JOptionPane jp = new JOptionPane(error, JOptionPane.ERROR_MESSAGE);
        JDialog dialog = jp.createDialog(f, title);
        dialog.setResizable(false);
        dialog.setIconImage(((ImageIcon)icon).getImage());  
        dialog.pack();
        dialog.setVisible(true); 
    }
    protected final void displayMessage(String message, String title){
        //JOptionPane.showMessageDialog(f, message, title, JOptionPane.INFORMATION_MESSAGE);
        JOptionPane jp = new JOptionPane(message, JOptionPane.INFORMATION_MESSAGE);
        JDialog dialog = jp.createDialog(f, title);
        dialog.setResizable(false);
        dialog.setIconImage(((ImageIcon)icon).getImage());  
        dialog.pack();
        dialog.setVisible(true); 
    }
    protected final void displayAboutButtonMessage(){
       String eol = System.getProperty("line.separator");
       String title = "QueueSimulator " + version + " About";
       String r = "";
       
       r = r + "QueueSimulator Copyright (C) 2016 Serhan Yılmaz" + eol;
       r = r + "Contact : " + "yilmazserhan@yahoo.com" + eol;
       r = r + eol;
       r = r + "QueueSimulator is a free software which aims to simulate primarily" + eol;
       r = r + "G/G/k queueing systems that are used in computer networks. But" + eol;
       r = r + "it can be used to simulate practically any queue system." + eol;
       r = r + eol;
       r = r + "You are more than welcome to share your comments, suggestions" + eol;
       r = r + "and bugs that you may have encountered through e-mail." + eol;
       r = r + eol;
       r = r + "License Information :" + eol;
       r = r + "This program is free software: you can redistribute it and/or modify" + eol;
       r = r + "it under the terms of the GNU General Public License as published by" + eol;
       r = r + "the Free Software Foundation, either version 3 of the License, or" + eol;
       r = r + "(at your option) any later version." + eol;
       r = r + eol;
       r = r + "This program is distributed in the hope that it will be useful," + eol;
       r = r + "but WITHOUT ANY WARRANTY; without even the implied warranty of" + eol;
       r = r + "MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the" + eol;
       r = r + "GNU General Public License for more details." + eol;
       r = r + eol;
       r = r + "You should have received a copy of the GNU General Public License" + eol;
       r = r + "along with this program.  If not, see <http://www.gnu.org/licenses/>." + eol;
       displayMessage(r,title);
    }
}
