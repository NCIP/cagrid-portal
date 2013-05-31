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
/**
 *
 */
package gov.nih.nci.cagrid.portal.portlet.query.results;

import gov.nih.nci.cagrid.portal.dao.QueryResultTableDao;
import gov.nih.nci.cagrid.portal.domain.table.QueryResultColumn;
import gov.nih.nci.cagrid.portal.domain.table.QueryResultTable;
import gov.nih.nci.cagrid.portal.portlet.util.PortletUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;

import java.util.*;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * @author <a href="mailto:manav.kher@semanticbits.com">Manav Kher</a>
 */
public class QueryResultTableToDataTableMetadataBuilder {

    private Map<String, String> columnTypeFormatterMap;
    private QueryResultTableDao queryResultTableDao;
    private Log log = LogFactory.getLog(QueryResultTableToDataTableMetadataBuilder.class);

    public JSONObject build(QueryResultTable table) {
        try {
            JSONObject meta = new JSONObject();

            Map<String, String> formatters = new HashMap<String, String>();
            List<String> allColNames = getOrderedColumnNames(table);

            for (String colName : allColNames) {
                JSONObject colDef = new JSONObject();
                colDef.put("key", colName);
                boolean sortable = false;
                try {
                    sortable = queryResultTableDao.getRowCountForColumn(table.getId(), colName) > 0;
                } catch (Exception e) {
                    // lets not break the UI because of this
                    log.error("error getting row count for column " + colName, e);
                }
                colDef.put("sortable", sortable);
                colDef.put("resizeable", true);
                String formatter = formatters.get(colName);
                if (formatter != null) {
                    colDef.put("formatter", formatter);
                }
                meta.append("columnDefs", colDef);
            }

            JSONObject responseSchema = new JSONObject();
            meta.put("responseSchema", responseSchema);
            responseSchema.put("resultsList", "rows");
            JSONObject metaFields = new JSONObject();
            responseSchema.put("metaFields", metaFields);
            metaFields.put("totalRecords", "numRows");

            for (String colName : allColNames) {
                responseSchema.append("fields", colName);
            }

            return meta;
        } catch (Exception ex) {
            throw new RuntimeException("Error building metadata: "
                    + ex.getMessage(), ex);
        }
    }

    public static List<String> getOrderedColumnNames(QueryResultTable table) {
        List<String> allColNames = new ArrayList<String>();

        if (PortletUtils.isCountQuery(table.getQueryInstance().getQuery()
                .getXml())) {
            allColNames.add("count");
        } else {
            SortedSet<String> sortedColNames = new TreeSet<String>();
            for (QueryResultColumn col : table.getColumns()) {
                sortedColNames.add(col.getName());
            }
            if (sortedColNames.contains("id")) {
                allColNames.add("id");
            }
            for (String colName : sortedColNames) {
                if (!"id".equals(colName)) {
                    allColNames.add(colName);
                }
            }
        }
        allColNames.add(ResultConstants.DATA_SERVICE_URL_COL_NAME);
        return allColNames;
    }

    protected String getFormatter(QueryResultColumn column) {
        String formatter = null;
        if (getColumnTypeFormatterMap() != null) {
            formatter = getColumnTypeFormatterMap().get(column.getName());
        }
        return formatter;
    }

    public Map<String, String> getColumnTypeFormatterMap() {
        return columnTypeFormatterMap;
    }

    public void setColumnTypeFormatterMap(
            Map<String, String> columnTypeFormatterMap) {
        this.columnTypeFormatterMap = columnTypeFormatterMap;
    }

    public QueryResultTableDao getQueryResultTableDao() {
        return queryResultTableDao;
    }

    public void setQueryResultTableDao(QueryResultTableDao queryResultTableDao) {
        this.queryResultTableDao = queryResultTableDao;
    }
}
