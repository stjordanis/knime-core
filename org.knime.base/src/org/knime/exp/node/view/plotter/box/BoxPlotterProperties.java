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
 *   29.09.2006 (Fabian Dill): created
 */
package org.knime.exp.node.view.plotter.box;

import javax.swing.JCheckBox;

import org.knime.core.data.DoubleValue;
import org.knime.exp.node.view.plotter.columns.MultiColumnPlotterProperties;
import org.knime.exp.node.view.plotter.props.BoxPlotAppearanceTab;

/**
 * 
 * @author Fabian Dill, University of Konstanz
 */
public class BoxPlotterProperties extends MultiColumnPlotterProperties {
    
    private final BoxPlotAppearanceTab m_normalizeTab;
    
    /**
     * 
     *
     */
    public BoxPlotterProperties() {
        super(DoubleValue.class);
        m_normalizeTab = new BoxPlotAppearanceTab();
        addTab(m_normalizeTab.getDefaultName(), m_normalizeTab);
    }
    
    /**
     * 
     * @return the checkbox to force normalized presentation.
     */
    public JCheckBox getNormalizeCheckBox() {
        return m_normalizeTab.getNormalizeCheckBox();
    }
    

}
