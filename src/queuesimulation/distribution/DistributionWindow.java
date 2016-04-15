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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.NumberFormat;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

/**
 *
 * @author Serhan Yılmaz
 */
public abstract class DistributionWindow {
    protected JDialog window;
    protected Distribution distribution;
    protected DistributionContainer parent;
    protected Window parent_window;
    protected JPanel pane;
    
    protected JFormattedTextField clip_min,clip_max;
    protected JCheckBox clip_min_enable,clip_max_enable;
    
    protected ImageIcon icon;
    
    public DistributionWindow(DistributionContainer parent, Distribution d){
        this.parent_window = parent.getParentWindow();
        this.parent = parent;
        this.window = new JDialog(this.parent_window, d.toString());
        this.distribution = d;
        this.pane = new JPanel();
        icon = new ImageIcon(this.getClass().getResource("/images/logo.png"));
    }
    public final void show(){
        final DistributionWindow thisObject = this;
        
        boolean f = !parent.isEmpty() && (parent.last_selected != null && distribution.toString().equals(parent.last_selected));
        
        pane.setLayout(new BorderLayout());
        //pane.setPreferredSize(new Dimension(350,250));
        JPanel button_pane = new JPanel();
        //button_pane.setBorder(BorderFactory.createLineBorder(Color.black));
        JButton cancel = new JButton("Cancel");
        pane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"),"esc_pressed");
        pane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ENTER"),"enter_pressed");
       
        pane.getActionMap().put("esc_pressed", new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                thisObject.cancel();
            }
        });
        pane.getActionMap().put("enter_pressed", new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                thisObject.checkAndSet();
            }
        });
        //cancel.setMnemonic(KeyEvent.VK_ESCAPE);
        cancel.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                thisObject.cancel();
            }
        });
        JButton ok = new JButton("OK");
        //ok.setMnemonic(KeyEvent.VK_ENTER);
        
        ok.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                thisObject.checkAndSet();
            }
        });
        
        JPanel common_pane = new JPanel();
        common_pane.setLayout(new GridLayout(0,1));
        //button_pane.setPreferredSize(new Dimension(340,40));
        button_pane.add(cancel);
        button_pane.add(ok);
        
        JPanel clip_pane = new JPanel();
        //clip_pane.setPreferredSize(new Dimension(340,40));
        //clip_pane.setBorder(BorderFactory.createLineBorder(Color.black));
        JLabel clip = new JLabel("Clipping : ");
        final JLabel label_min = new JLabel("min :");
        JCheckBox min_check = new JCheckBox();
        final JLabel label_max = new JLabel("max :");
        label_max.setEnabled(false);
        JCheckBox max_check = new JCheckBox();
        final JFormattedTextField min,max;
        NumberFormat general = new DecimalFormat("0.#####E0");
        min = new JFormattedTextField(general);
        max = new JFormattedTextField(general);
        min.setColumns(6);
        max.setColumns(6);
        min.setValue(distribution.clip_min);
        max.setValue(distribution.clip_max);
        max.setEnabled(false);
        min_check.setSelected(true);
        clip_min_enable = min_check;
        clip_max_enable = max_check;
        clip_min = min;
        clip_max = max;
        
        if(f){
            label_min.setEnabled(parent.getDistribution().clip_min_enable);
            label_max.setEnabled(parent.getDistribution().clip_max_enable);
            clip_min.setEnabled(parent.getDistribution().clip_min_enable);
            clip_max.setEnabled(parent.getDistribution().clip_max_enable);
            min_check.setSelected(parent.getDistribution().clip_min_enable);
            max_check.setSelected(parent.getDistribution().clip_max_enable);
            clip_min.setValue(parent.getDistribution().clip_min);
            clip_max.setValue(parent.getDistribution().clip_max);
        }
        
        min_check.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
               if(e.getStateChange() == ItemEvent.DESELECTED){
                   label_min.setEnabled(false);
                   min.setEnabled(false);
               } else{
                   label_min.setEnabled(true);
                   min.setEnabled(true);
               }
            }
        });
        max_check.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
               if(e.getStateChange() == ItemEvent.DESELECTED){
                   label_max.setEnabled(false);
                   max.setEnabled(false);
               } else{
                   label_max.setEnabled(true);
                   max.setEnabled(true);
               }
            }
        });
        
        clip_pane.add(clip);
        clip_pane.add(label_min);
        clip_pane.add(min);
        clip_pane.add(min_check);
        clip_pane.add(label_max);
        clip_pane.add(max);
        clip_pane.add(max_check);
        
        common_pane.add(clip_pane);
        common_pane.add(button_pane);
        
        pane.add(common_pane,BorderLayout.SOUTH);
        Point lo = parent.getPane().getLocationOnScreen();
        lo.x += 35;
        lo.y += 20;
        //window.setLocationRelativeTo(parent.getPane());
        window.setLocation(lo);
        window.setModal(true);
        initialize(f);
        window.add(pane);
        window.setIconImage(icon.getImage());
        window.pack();
        window.setResizable(false);
        window.setVisible(true);
        
    }
    protected final void cancel(){
        window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
    }
    
    protected final void displayErrorMessage(String error, String title){
        //JOptionPane.showMessageDialog(window, error, "Wrong Parameter", JOptionPane.ERROR_MESSAGE);
        JOptionPane jp = new JOptionPane(error, JOptionPane.ERROR_MESSAGE);
        JDialog dialog = jp.createDialog(window, title);
        dialog.setResizable(false);
        dialog.setIconImage(((ImageIcon)icon).getImage());  
        dialog.pack();
        dialog.setVisible(true); 
    }
    protected final void displayErrorMessage(String error){
        displayErrorMessage(error,"Wrong Parameter");
    }
    protected abstract void initialize(boolean f);
    
    protected final void close(){
        window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
    }
    
    protected abstract void checkAndSet();
    
}
