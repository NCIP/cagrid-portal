/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-portal/LICENSE.txt for details.
*============================================================================
**/
package gov.nih.nci.cagrid.portal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dbunit.dataset.datatype.*;

import java.sql.Types;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */

public class HsqlDataTypeFactory
        extends DefaultDataTypeFactory {
    private static final Log log = LogFactory.getLog(HsqlDataTypeFactory.class);

    public DataType createDataType(int sqlType, String sqlTypeName)
            throws DataTypeException {
        if (sqlType == Types.BOOLEAN) {
            return DataType.BOOLEAN;
        }

        return super.createDataType(sqlType, sqlTypeName);
    }
}