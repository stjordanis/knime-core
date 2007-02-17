/* 
 * -------------------------------------------------------------------
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
 *   Feb 15, 2007 (wiswedel): created
 */
package org.knime.core.data.container;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.knime.core.data.DataTableSpec;
import org.knime.core.data.RowIterator;
import org.knime.core.data.RowKey;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.BufferedDataTable.KnowsRowCountTable;

/**
 * Class that realizes a join table of two {@link BufferedDataTable} arguments.
 * <p><b>This class is not intended to be used in any node implementation, it
 * is public only because some KNIME framework classes access it.</b>
 * <p>This class is used to represent the {@link BufferedDataTable} that is
 * returned by the {@link org.knime.core.node.ExecutionContext}s 
 * {@link org.knime.core.node.ExecutionContext#
 * createJoinedTable(BufferedDataTable, BufferedDataTable, ExecutionMonitor)}
 * method.
 * @author Bernd Wiswedel, University of Konstanz
 */
public final class JoinedTable implements KnowsRowCountTable {
    
    private final BufferedDataTable m_leftTable;
    private final BufferedDataTable m_rightTable;
    private final DataTableSpec m_spec;
    /** We use the {@link JoinTableIterator}, which needs a map and flags
     * arguments. */
    private final int[] m_map;
    private final boolean[] m_flags;
    
    /**
     * Creates new object. No checks are done.
     * @param left The left table.
     * @param right The right table.
     * @param spec The proper spec.
     */
    private JoinedTable(final BufferedDataTable left, 
            final BufferedDataTable right, final DataTableSpec spec) {
        m_leftTable = left;
        m_rightTable = right;
        final int colsLeft = m_leftTable.getDataTableSpec().getNumColumns();
        final int colsRight = m_rightTable.getDataTableSpec().getNumColumns();
        m_map = new int[colsLeft + colsRight];
        m_flags = new boolean[colsLeft + colsRight];
        for (int i = 0; i < colsLeft; i++) {
            m_map[i] = i;
            m_flags[i] = true;
        }
        for (int i = 0; i < colsRight; i++) {
            m_map[i + colsLeft] = i;
            m_flags[i + colsLeft] = false;
        }
        m_spec = spec;
    }

    /**
     * @see org.knime.core.data.DataTable#getDataTableSpec()
     */
    public DataTableSpec getDataTableSpec() {
        return m_spec;
    }
    
    /**
     * @see org.knime.core.data.DataTable#iterator()
     */
    public RowIterator iterator() {
        return new JoinTableIterator(m_leftTable.iterator(), 
                m_rightTable.iterator(), m_map, m_flags);
    }
    
    /**
     * Does nothing. 
     * @see KnowsRowCountTable#clear()
     */
    public void clear() {
        // left empty, it's up to the node to clear our underlying tables.
    }

    /**
     * @see KnowsRowCountTable#getReferenceTables()
     */
    public BufferedDataTable[] getReferenceTables() {
        return new BufferedDataTable[]{m_leftTable, m_rightTable};
    }

    /**
     * @see KnowsRowCountTable#getRowCount()
     */
    public int getRowCount() {
        return m_leftTable.getRowCount();
    }

    /**
     * @see KnowsRowCountTable#putIntoTableRepository(HashMap)
     */
    public void putIntoTableRepository(
            final HashMap<Integer, ContainerTable> rep) {
    }

    /**
     * @see KnowsRowCountTable#removeFromTableRepository(HashMap)
     */
    public void removeFromTableRepository(
            final HashMap<Integer, ContainerTable> rep) {
    }
    
    private static final String CFG_INTERNAL_META = "meta_internal";
    private static final String CFG_LEFT_TABLE_ID = "leftTableID";
    private static final String CFG_RIGHT_TABLE_ID = "rightTableID";

    /**
     * @see KnowsRowCountTable#saveToFile(
     *  File, NodeSettingsWO, ExecutionMonitor)
     */
    public void saveToFile(final File f, final NodeSettingsWO settings,
            final ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {
        NodeSettingsWO internals = settings.addNodeSettings(CFG_INTERNAL_META);
        internals.addInt(CFG_LEFT_TABLE_ID, m_leftTable.getBufferedTableId());
        internals.addInt(CFG_RIGHT_TABLE_ID, m_leftTable.getBufferedTableId());
    }
    
    /** Method being called when the workflow is restored and the table shall
     * recreated.
     * @param s The settings object, contains tables ids.
     * @param spec The final spec.
     * @param loadID The current load ID.
     * @return The restored table.
     * @throws InvalidSettingsException If the settings can't be read.
     */
    public static JoinedTable load(final NodeSettingsRO s, 
            final DataTableSpec spec, final int loadID) 
        throws InvalidSettingsException {
        NodeSettingsRO subSettings = s.getNodeSettings(CFG_INTERNAL_META);
        int leftID = subSettings.getInt(CFG_LEFT_TABLE_ID);
        int rightID = subSettings.getInt(CFG_RIGHT_TABLE_ID);
        BufferedDataTable leftTable = 
            BufferedDataTable.getDataTable(loadID, leftID);
        BufferedDataTable rightTable = 
            BufferedDataTable.getDataTable(loadID, rightID);
        return new JoinedTable(leftTable, rightTable, spec);
    }
    
    /**
     * Creates new join table, does the sanity checks. Called from the 
     * {@link org.knime.core.node.ExecutionContext#createJoinedTable(
     * BufferedDataTable, BufferedDataTable, ExecutionMonitor)} method.
     * @param left The left table.
     * @param right The right table.
     * @param prog For progress/cancel.
     * @return A joined table.
     * @throws CanceledExecutionException When canceled.
     * @throws IllegalArgumentException If row keys don't match or there are
     * duplicate columns.
     */
    public static JoinedTable create(final BufferedDataTable left,
            final BufferedDataTable right, final ExecutionMonitor prog)
            throws CanceledExecutionException {
        if (left.getRowCount() != right.getRowCount()) {
            throw new IllegalArgumentException("Tables can't be joined, non "
                    + "matching row counts: " + left.getRowCount() + " vs. "
                    + right.getRowCount());
        }
        // throws exception when duplicates encountered.
        DataTableSpec joinSpec = new DataTableSpec(
                left.getDataTableSpec(), right.getDataTableSpec());
        // check if rows come in same order
        RowIterator leftIt = left.iterator();
        RowIterator rightIt = right.iterator();
        int rowIndex = 0;
        while (leftIt.hasNext()) {
            prog.checkCanceled();
            RowKey leftKey = leftIt.next().getKey();
            RowKey rightKey = rightIt.next().getKey();
            if (!leftKey.equals(rightKey)) {
                throw new IllegalArgumentException(
                        "Tables contain non-matching rows or are sorted "
                        + "differently, keys in row " + rowIndex 
                        + " do not match: \"" + leftKey 
                        + "\" vs. \"" + rightKey + "\"");
            }
            rowIndex++;
        }
        return new JoinedTable(left, right, joinSpec);
    }


}
