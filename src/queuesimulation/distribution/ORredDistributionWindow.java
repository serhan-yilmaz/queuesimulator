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
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Hashtable;
import java.util.Locale;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author Serhan Yılmaz
 */
public class ORredDistributionWindow extends CombinedDistributionWindow{
    private final NumberFormat formatter = new DecimalFormat("00.0",DecimalFormatSymbols.getInstance(Locale.US));
    
    private double ratio = 0.5;
    
    public ORredDistributionWindow(DistributionContainer parent, ORredDistribution d) {
        super(parent, d);
    }

    @Override
    protected void initialize(boolean f) {
        super.initialize(f);
        
        final JLabel label1 = new JLabel(" 0.05 ");
        final JLabel label2 = new JLabel(" 0.95 ");
        
        JSlider slider = new JSlider();
        JPanel slider_pane = new JPanel();
        slider.setMaximum(1000);
        slider.setMinimum(0);
        slider.setMinorTickSpacing(10);
        slider.setMajorTickSpacing(250);
      //  slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setSnapToTicks(true);
        Hashtable labelTable = new Hashtable();
        for(int i = 0 ; i <= 1000 ; i+= 250){
            labelTable.put( new Integer( i ), new JLabel(" %" + formatter.format(i/10)) );
        }
        slider.setLabelTable( labelTable );
        slider.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                 JSlider source = (JSlider)e.getSource();
                 double rat = source.getValue() / 1000.0;
                 label1.setText("  A : %" + formatter.format(100 - 100 * rat)  + "  ");
                 label2.setText("  B : %" + formatter.format(100 * rat)  + "  ");
                 ratio = rat;
            }
        });
        if(f){
            slider.setValue((int) (distribution.getComponents()[0].getValue() * 1000));
        }else
            slider.setValue(500);
        slider_pane.setLayout(new BorderLayout());
        
        JPanel cn = new JPanel();
        JLabel labela = new JLabel("Probability Weight");
        
        cn.add(labela);
        slider_pane.add(label1,BorderLayout.WEST);
        slider_pane.add(cn,BorderLayout.CENTER);
        slider_pane.add(label2,BorderLayout.EAST);
        upper_panel.add(slider_pane);
        upper_panel.add(slider);
    }
    
    @Override
    protected void checkAndSetBelow(Distribution d) {
        super.checkAndSetBelow(d);
        d.setComponent(0, ratio);
    }

}
