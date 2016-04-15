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

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author Serhan Yılmaz
 */
public class BasicDistributionWindow extends DistributionWindow{

    JFormattedTextField[] component_tf;

    public BasicDistributionWindow(DistributionContainer parent, BasicDistribution d) {
        super(parent, d);
    }
    

    @Override
    protected void initialize(boolean f) {
        final DistributionWindow thisObject = this;
        
        final DistributionComponent[] components = distribution.getComponents();
        final JPanel component_all_pane = new JPanel();
        component_all_pane.setLayout(new GridLayout(0,1));
        if(components != null){
            final JPanel[] component_pane = new JPanel[components.length];
            final JFormattedTextField[] component_tf = new JFormattedTextField[components.length];
            this.component_tf = component_tf;
            FlowLayout fl = new FlowLayout();
            fl.setAlignment(FlowLayout.LEFT);
            JLabel param = new JLabel(" Parameters : ");
            param.setFont(new Font("Arial",Font.BOLD,14));
            component_all_pane.add(param);

            for (int i = 0 ; i < components.length ; i++) {
                component_pane[i] = new JPanel();
                component_pane[i].setLayout(fl);
                JLabel label = new JLabel(components[i].getName() + " : ");
                component_pane[i].add(label);
                NumberFormat format = new DecimalFormat("0.######E0");
                component_tf[i] = new JFormattedTextField(format);
                component_tf[i].setColumns(10);
                if(f){
                    component_tf[i].setValue(parent.getDistribution().getComponents()[i].getValue());
                }else
                    component_tf[i].setValue(components[i].getValue());
                component_pane[i].add(component_tf[i]);
                component_all_pane.add(component_pane[i]);
            }
        }
        pane.add(component_all_pane,BorderLayout.NORTH);
        
    }

    @Override
    protected void checkAndSet() {
        try {
            Distribution d = distribution.getClass().newInstance();
            if(clip_max_enable.isSelected())
                d.clip_max = ((Number) clip_max.getValue()).doubleValue();
            if(clip_min_enable.isSelected())
                d.clip_min = ((Number) clip_min.getValue()).doubleValue();
            d.clip_max_enable = clip_max_enable.isSelected();
            d.clip_min_enable = clip_min_enable.isSelected();
            if(clip_min_enable.isSelected() && clip_max_enable.isSelected() && d.clip_max < d.clip_min){
                displayErrorMessage("Maximum Clip is lesser than Minimum");
                return;
            }
            if(component_tf != null){
                for(int i = 0; i < component_tf.length ; i++){
                    d.setComponent(i, ((Number)component_tf[i].getValue()).doubleValue());
                }
            }
            DistributionError err = d.checkForErrors();
            if(err != null){
                displayErrorMessage(err.getErrorMessage(),err.getTitle());
                return;
            }
            parent.setDistribution(d);
            parent_window.pack();
            parent.last_selected = distribution.toString();
            this.close();
        } catch (InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(BasicDistributionWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
