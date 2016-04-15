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
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import queuesimulation.GUI.graph.Figure;

/**
 *
 * @author Serhan Yılmaz
 */
public class DistributionContainer {
    private final static Distribution[] DISTRIBUTION_NAME_LIST;
    static {
        DISTRIBUTION_NAME_LIST = new Distribution[8];
        DISTRIBUTION_NAME_LIST[0] = ExponentialDistribution.getSingletonStatic();
        DISTRIBUTION_NAME_LIST[1] = GaussianDistribution.getSingletonStatic();
        DISTRIBUTION_NAME_LIST[2] = UniformDistribution.getSingletonStatic();
        DISTRIBUTION_NAME_LIST[3] = DeterministicDistribution.getSingletonStatic();
        DISTRIBUTION_NAME_LIST[4] = GeometricDistribution.getSingletonStatic();
        DISTRIBUTION_NAME_LIST[5] = SummedDistribution.getSingletonStatic();
        DISTRIBUTION_NAME_LIST[6] = ORredDistribution.getSingletonStatic();
        DISTRIBUTION_NAME_LIST[7] = MultipliedDistribution.getSingletonStatic();
    }
    
    protected Distribution distribution;
    private final Window parent;
    private final JPanel pane;
    
    private final JButton formula_button;
    private final JButton histogram_button;
    private final JButton mean_button;
    private final JButton deviation_button;
    private final JButton edit_button;
    private final JComboBox list;
    
    private boolean locked = false;
    
    private final JLabel distribution_label = new JLabel("None");
    
    protected String last_selected = null;
    
    private Figure figure;
    
    private NumberFormat formatter = new DecimalFormat("0.000E0");
    
    public DistributionContainer(Window parent, String title){
        final DistributionContainer thisObject = this;
        this.parent = parent;
        this.pane = new JPanel();
        pane.setLayout(new GridLayout(0,1));
        //pane.setPreferredSize(new Dimension(200,200));
        //JButton button = new JButton("Button 1");
        list = new JComboBox(DISTRIBUTION_NAME_LIST);
        list.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(locked)
                    return;
                JComboBox cb = (JComboBox)e.getSource();
                Distribution d = (Distribution)cb.getSelectedItem();
                DistributionWindow dw = d.createWindow(thisObject);
                dw.show();
            }
        });
        edit_button = new JButton("Edit");
        edit_button.setEnabled(false);
        edit_button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                DistributionWindow dw = distribution.createWindow(thisObject);
                dw.show();
            }
        });
        
        JPanel list_panel = new JPanel();
        list_panel.add(list);
        distribution_label.setForeground(Color.red);
        JPanel select_panel = new JPanel();
        JLabel select_label = new JLabel("Selected : ");
        select_panel.add(select_label);
        select_panel.add(distribution_label);
        JLabel title_label = new JLabel(title,SwingConstants.CENTER);
        title_label.setFont(new Font("Arial",2,20));
        JPanel button_panel = new JPanel(new GridLayout(0,2));
        JPanel button_panel2 = new JPanel();
        formula_button = new JButton("See Formula");
        histogram_button = new JButton("See Histogram");
        mean_button = new JButton("See Moments");
        deviation_button = new JButton("See L-Moments");
        button_panel.add(formula_button);
        button_panel.add(histogram_button);
        button_panel.add(mean_button);
        button_panel.add(deviation_button);
        select_panel.add(edit_button);
        formula_button.setEnabled(false);
        histogram_button.setEnabled(false);
        mean_button.setEnabled(false);
        deviation_button.setEnabled(false);
        formula_button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                thisObject.seeDetailedFormula();
            }
        });
        histogram_button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if(locked)
                    return;
                thisObject.seeHistogram();
            }
        });
        
        mean_button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                seeMoments();
                //seeMean();
            }
        });
        
        deviation_button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                seeLMoments();
            }
        });
        
        pane.add(title_label);
        pane.add(list_panel);
        pane.add(select_panel);
       // pane.add(button_panel2);
        pane.add(button_panel);
        pane.setBorder(BorderFactory.createLineBorder(Color.blue));
        figure = new Figure("Figure",parent);
    }
    
    public JPanel getPane(){
        return pane;
    }
    protected Window getParentWindow(){
        return parent;
    }
    protected void seeFormula(){
        //seeHistogram();
        JOptionPane.showMessageDialog(parent, distribution.getFormula(), "Distribution Formula", JOptionPane.INFORMATION_MESSAGE);
    }
    protected void seeDetailedFormula(){
        String eol = System.getProperty("line.separator");
        String s = distribution.getDetailedFormula();
        String s2 = distribution.getFormula();
        String r = "";
        r = r + "Basic Formula : " + eol;
        String[] slist2 = splitIntoLine(s2,60);
        for(int i = 0; i < slist2.length ; i++){
            r = r + slist2[i] + eol;
        }
        r = r + "Detailed Formula : " + eol;
        String[] slist = splitIntoLine(s,60);
        for(int i = 0; i < slist.length ; i++){
            r = r + slist[i] + eol;
        }
        JOptionPane.showMessageDialog(parent, r, "Distribution Formula", JOptionPane.INFORMATION_MESSAGE);
    }
    
    protected void seeMean(){
        JOptionPane.showMessageDialog(parent, distribution.getMean(), "Distribution Mean", JOptionPane.INFORMATION_MESSAGE);
    }
    protected void seeStandartDeviation(){
        JOptionPane.showMessageDialog(parent, distribution.getStandardDeviation(), "Distribution Standart Deviation", JOptionPane.INFORMATION_MESSAGE);
    }
    protected void seeMoments(){
        String eol = System.getProperty("line.separator");
        String r = "";
        DistributionAnalysis moments = distribution.getMoments();
        r = r + "Mean : " + formatter.format(moments.getMean()) + eol;
        r = r + "Standart Deviation : " + formatter.format(moments.getStandardDeviation()) + eol;
        r = r + "Skewness : " + formatter.format(moments.getSkewness()) + eol;
        r = r + "Kurtosis : " + formatter.format(moments.getKurtosis()) + eol;
        r = r + "Excess Kurtosis : " + formatter.format(moments.getExcessKurtosis()) + eol;
        JOptionPane.showMessageDialog(parent, r, "Distribution Moments", JOptionPane.INFORMATION_MESSAGE);
    }
    protected void seeLMoments(){
        String eol = System.getProperty("line.separator");
        String r = "";
        DistributionAnalysis Lmoments = distribution.getLMoments();
        r = r + "Mean : " + formatter.format(Lmoments.getMean()) + eol;
        r = r + "L-Scale : " + formatter.format(Lmoments.getStandardDeviation()) + eol;
        r = r + "L-Skewness : " + formatter.format(Lmoments.getSkewness()) + eol;
        r = r + "L-Kurtosis : " + formatter.format(Lmoments.getKurtosis()) + eol;
        JOptionPane.showMessageDialog(parent, r, "Distribution L-Moments", JOptionPane.INFORMATION_MESSAGE);
    }
    public void setDistribution(Distribution d){
        this.locked = true;
        this.list.setSelectedItem(d.getSingleton());
        this.locked = false;
        this.distribution = d;
        this.distribution_label.setText(d.toString());
        this.distribution_label.setForeground(new Color(0,200,0));
        this.formula_button.setEnabled(true);
        this.edit_button.setEnabled(true);
        this.histogram_button.setEnabled(true);
        this.mean_button.setEnabled(true);
        this.deviation_button.setEnabled(true);
    }
    public Distribution getDistribution(){
        return distribution;
    }
    public boolean isEmpty(){
        return distribution == null;
    }
    protected void seeHistogram(){
        locked = true;
        figure.setTitle("Histogram of " + distribution.toString());
        figure.show();
        figure.histogram(distribution, 100);
        locked = false;
    }
    
    private String[] splitIntoLine(String input, int maxCharInLine){

    StringTokenizer tok = new StringTokenizer(input, " ");
    StringBuilder output = new StringBuilder(input.length());
    int lineLen = 0;
    while (tok.hasMoreTokens()) {
        String word = tok.nextToken();

        while(word.length() > maxCharInLine){
            output.append(word.substring(0, maxCharInLine-lineLen)).append("\n");
            word = word.substring(maxCharInLine-lineLen);
            lineLen = 0;
        }

        if (lineLen + word.length() > maxCharInLine) {
            output.append("\n");
            lineLen = 0;
        }
        output.append(word).append(" ");

        lineLen += word.length() + 1;
    }
    // output.split();
    // return output.toString();
    return output.toString().split("\n");
}
    
}
