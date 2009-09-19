/* ------------------------------------------------------------------
 * This source code, its documentation and all appendant files
 * are protected by copyright law. All rights reserved.
 *
 * Copyright, 2008 - 2009
 * KNIME.com, Zurich, Switzerland
 *
 * You may not modify, publish, transmit, transfer or sell, reproduce,
 * create derivative works from, distribute, perform, display, or in
 * any way exploit any of the content, in whole or in part, except as
 * otherwise expressly permitted in writing by the copyright owner or
 * as specified in the license file distributed with this product.
 *
 * If you have any questions please contact the copyright holder:
 * website: www.knime.com
 * email: contact@knime.com
 * ---------------------------------------------------------------------
 * 
 * History
 *   14.09.2009 (Fabian Dill): created
 */
package org.knime.core.data.date;

import org.knime.core.data.DataValue;
import org.knime.core.data.DataValueComparator;

/**
 * Comapres to {@link TimestampValue}s by comparing their UTC time.
 * 
 * @author Fabian Dill, KNIME.com, Zurich, Switzerland
 */
public class TimestampComparator extends DataValueComparator {

    /**
     * {@inheritDoc}
     */
    @Override
    protected int compareDataValues(final DataValue v1, final DataValue v2) {
        TimestampValue t1 = (TimestampValue)v1;
        TimestampValue t2 = (TimestampValue)v2;
        return t1.getUTCCalendarClone().compareTo(t2.getUTCCalendarClone());
    }

}
