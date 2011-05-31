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