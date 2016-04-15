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
import java.awt.GridLayout;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;

/**
 *
 * @author Serhan Yılmaz
 */
public class CombinedDistributionWindow extends DistributionWindow{

    DistributionContainer distributionA, distributionB;
    protected JPanel upper_panel;
    
    public CombinedDistributionWindow(DistributionContainer parent, CombinedDistribution d) {
        super(parent, d);
    }

    @Override
    protected void initialize(boolean f) {
        final DistributionWindow thisObject = this;
        distributionA = new DistributionContainer(window,"Distribution A");
        distributionB = new DistributionContainer(window,"Distribution B");
        
        if(f){
            CombinedDistribution cd = (CombinedDistribution) parent.getDistribution();
            distributionA.setDistribution(cd.distributionA);
            distributionB.setDistribution(cd.distributionB);
            distributionA.last_selected = cd.distributionA.toString();
            distributionB.last_selected = cd.distributionB.toString();
        }
        
        JPanel distribution_panel = new JPanel();
        distribution_panel.setLayout(new FlowLayout());
        CombinedDistribution cd = (CombinedDistribution) distribution;
        JLabel symbol = new JLabel(cd.getSymbol(), SwingConstants.CENTER);
        symbol.setFont(new Font("Arial",1,40));
        
        distribution_panel.add(distributionA.getPane());
        distribution_panel.add(symbol);
        distribution_panel.add(distributionB.getPane());
        
        upper_panel = new JPanel();
        upper_panel.setLayout(new BoxLayout(upper_panel, BoxLayout.Y_AXIS));
        upper_panel.add(distribution_panel);      
        
        pane.add(upper_panel,BorderLayout.NORTH);
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
                displayErrorMessage("Maximum Clip is lesser than Minimum.");
                return;
            }
            if(distributionA.isEmpty()){
                displayErrorMessage("Distribution A is not Specified.");
                return;
            }
            if(distributionB.isEmpty()){
                displayErrorMessage("Distribution B is not Specified.");
                return;
            }
            checkAndSetBelow(d);
            CombinedDistribution cd = (CombinedDistribution) d;
            cd.distributionA = distributionA.distribution;
            cd.distributionB = distributionB.distribution;
            DistributionError err = cd.checkForErrors();
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
    
    protected void checkAndSetBelow(Distribution d){
        
    }

    
}
