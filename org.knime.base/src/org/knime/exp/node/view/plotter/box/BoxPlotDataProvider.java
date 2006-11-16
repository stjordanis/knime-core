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

import java.util.Map;

import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.RowKey;
import org.knime.exp.node.view.plotter.DataProvider;

/**
 * 
 * @author Fabian Dill, University of Konstanz
 */
public interface BoxPlotDataProvider extends DataProvider {
    
    /**
     * 
     * @return a map of the column name and a double array containing
     * the minimum, the lower quartile, the median, the upper quatile and
     * the maximum value for that column.
     */
    public Map<DataColumnSpec, double[]>getStatistics();
    
    /**
     * Mild outliers are values > q1 - 3 * iqr and < q1 - 1.5 * iqr and
     * < q3 + 3 * iqr and > q3 + 1.5 * iqr.
     * @return a list of mild outliers for each column.
     */
    public Map<String, Map<Double, RowKey>> getMildOutliers();
    
    /**
     * Extreme outliers are values < q1 - 3 * iqr and > q3 + 3 * iqr.
     * @return a list of extreme outliers for each column.
     */
    public Map<String, Map<Double, RowKey>> getExtremeOutliers();
    

}
