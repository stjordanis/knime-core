/* -------------------------------------------------------------------
 * This source code, its documentation and all appendant files
 * are protected by copyright law. All rights reserved.
 * 
 * Copyright, 2003 - 2006
 * Universitaet Konstanz, Germany.
 * Lehrstuhl fuer Angewandte Informatik
 * Prof. Dr. Michael R. Berthold
 * 
 * You may not modify, publish, transmit, transfer or sell, reproduce,
 * create derivative works from, distribute, perform, display, or in
 * any way exploit any of the content, in whole or in part, except as
 * otherwise expressly permitted in writing by the copyright owner.
 * -------------------------------------------------------------------
 * 
 * History
 *   24.08.2006 (Fabian Dill): created
 */
package org.knime.exp.node.view.plotter;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JTabbedPane;

import org.knime.exp.node.view.plotter.props.DefaultTab;

/**
 * 
 * @author Fabian Dill, University of Konstanz
 */
public class AbstractPlotterProperties extends JTabbedPane {
    
    private final DefaultTab m_defaultTab;
    /**
     * 
     *
     */
    public AbstractPlotterProperties() {
        m_defaultTab = new DefaultTab();
        addTab(m_defaultTab.getDefaultName(), m_defaultTab);
    }
    
    /**
     * 
     * @return the button triggering the color chooser dialog.
     */
    public JButton getChooseBackgroundButton() {
        return m_defaultTab.getChooseBackgroundButton();
    }
    
    /**
     * 
     * @return the color chooser for the background color.
     */
    public JColorChooser getColorChooser() {
        return m_defaultTab.getColorChooser();
    }
    
    /**
     * 
     * @return the combo box for the mouse mode.
     */
    public JComboBox getMouseSelectionBox() {
        return m_defaultTab.getMouseSelectionBox();
    }
    
    /**
     * 
     * @return the button to trigger fit to screen
     */
    public JButton getFitToScreenButton() {
        return m_defaultTab.getFitToScreenButton();
    }
    

}
