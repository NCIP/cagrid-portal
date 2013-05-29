/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-core/LICENSE.txt for details.
*============================================================================
**/
/**
 *
 */
package gov.nih.nci.cagrid.portal.portlet.query.results;

import gov.nih.nci.cagrid.portal.domain.table.QueryResultCell;
import gov.nih.nci.cagrid.portal.domain.table.QueryResultRow;
import gov.nih.nci.cagrid.portal.domain.table.QueryResultTable;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 */
public class QueryResultTableToJSONObjectBuilder {

    public JSONObject build(List<QueryResultRow> rows, Integer numRows) {
        JSONObject tableJO = new JSONObject();
        try {
            QueryResultTable table = null;
            tableJO.put("numRows", numRows);
            for (QueryResultRow row : rows) {
                if (table == null) {
                    table = row.getTable();
                }
                Map<String, String> rowMap = new HashMap<String, String>();
                for (String colName : QueryResultTableToDataTableMetadataBuilder
                        .getOrderedColumnNames(table)) {
                    String value = "";
                    for (QueryResultCell cell : row.getCells()) {
                        if (cell.getColumn().getName().equals(colName)) {
                            value = cell.getValue();
                            break;
                        }
                    }
                    rowMap.put(colName, value);
                }
                rowMap.put(ResultConstants.DATA_SERVICE_URL_COL_NAME, row
                        .getServiceUrl());
                tableJO.append("rows", rowMap);
            }
        } catch (Exception ex) {
            throw new RuntimeException("Error building JSON: "
                    + ex.getMessage(), ex);
        }
        return tableJO;
    }

}
