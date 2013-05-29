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

import gov.nih.nci.cagrid.portal.dao.QueryResultTableDao;
import gov.nih.nci.cagrid.portal.domain.table.*;
import gov.nih.nci.cagrid.portal.service.PortalFileService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * @author <a href="mailto:manav.kher@semanticbits.com>Manav Kher</a>
 */
public class XMLQueryResultToQueryResultTableHandler extends
        BaseQueryResultHandler {

    private static final Log logger = LogFactory
            .getLog(XMLQueryResultToQueryResultTableHandler.class);

    private QueryResultTableDao queryResultTableDao;
    private QueryResultColumnNameResolver queryResultColumnNameResolver;
    private QueryResultTable table = new QueryResultTable();
    private Map<String, QueryResultColumn> cols = new HashMap<String, QueryResultColumn>();
    private QueryResultRow currentRow;
    private int maxValueLength = 256;
    private String dataServiceUrl;
    private boolean persist = true;

    private PortalFileService portalFileService;

    /**
     *
     */
    public XMLQueryResultToQueryResultTableHandler() {

    }

    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {

        elementStack.push(new ElementInfo(uri, localName, qName, attributes));

        if (queryType == null) {
            if (persist) {
                getQueryResultTableDao().save(table);
            }
            if ("DCQLQueryResultsCollection".equals(localName)) {
                queryType = QueryType.DCQL;
            } else {
                queryType = QueryType.CQL;
            }
        } else if (resultType == null) {

            int size = elementStack.size();
            if (size == 2 && QueryType.CQL.equals(queryType) || size == 4
                    && QueryType.DCQL.equals(queryType)) {
                if ("ObjectResult".equals(localName)) {
                    resultType = ResultType.OBJECT;
                } else if ("AttributeResult".equals(localName)) {
                    resultType = ResultType.ATTRIBUTE;
                } else if ("CountResult".equals(localName)) {
                    resultType = ResultType.COUNT;
                }
            }
        }

        if ("DCQLResult".equals(localName)) {
            for (int i = 0; i < attributes.getLength(); i++) {
                if ("targetServiceURL".equals(attributes.getLocalName(i))) {
                    dataServiceUrl = attributes.getValue(i);
                    break;
                }
            }
        }

        if (resultType != null) {
            if (ResultType.COUNT.equals(resultType)) {
            	QueryResultColumn col = new QueryResultColumn();
                col.setName("count");
                col.setTable(table);
                if (persist) {
                    getQueryResultTableDao().getHibernateTemplate().save(col);
                }
                table.getColumns().add(col);

                QueryResultRow row = new QueryResultRow();
                row.setTable(table);
                if (dataServiceUrl == null) {
                    throw new RuntimeException(
                            "Couldn't determine source URL");
                }
                row.setServiceUrl(dataServiceUrl);
                if (persist) {
                    getQueryResultTableDao().getHibernateTemplate().save(row);
                }
                table.getRows().add(row);

                QueryResultCell cell = new QueryResultCell();
                cell.setValue(attributes.getValue("count"));
                cell.setRow(row);
                cell.setColumn(col);
                row.getCells().add(cell);
                if (persist) {
                    getQueryResultTableDao().getHibernateTemplate().save(cell);
                }                

            } else if (ResultType.ATTRIBUTE.equals(resultType)) {            	
                if ("AttributeResult".equals(localName)) {                	
                    currentRow = new QueryResultRow();
                    currentRow.setTable(table);

                    if (dataServiceUrl == null) {
                        throw new RuntimeException(
                                "Couldn't determine source URL");
                    }
                    currentRow.setServiceUrl(dataServiceUrl);

                    if (persist) {
                        getQueryResultTableDao().getHibernateTemplate().save(
                                currentRow);
                    }
                    table.getRows().add(currentRow);

                } else if ("Attribute".equals(localName)) {
                	String name = attributes.getValue("name");
                    String value = attributes.getValue("value");
                    QueryResultColumn col = cols.get(name);
                    if (col == null) {
                        col = new QueryResultColumn();
                        col.setName(name);
                        col.setTable(table);
                        table.getColumns().add(col);
                        if (persist) {
                            getQueryResultTableDao().getHibernateTemplate()
                                    .save(col);
                        }
                        cols.put(name, col);
                    }
                    QueryResultCell cell = new QueryResultCell();
                    cell.setRow(currentRow);
                    cell.setColumn(col);
                    cell.setValue(value);
                    if (persist) {
                        getQueryResultTableDao().getHibernateTemplate().save(
                                cell);
                    }
                    currentRow.getCells().add(cell);
                }
            } else if (ResultType.OBJECT.equals(resultType)) {   	

                if ("ObjectResult".equals(localName)) {                	
                    currentRow = new QueryResultRow();
                    currentRow.setTable(table);

                    if (dataServiceUrl == null) {
                        throw new RuntimeException(
                                "Couldn't determine source URL");
                    }
                    currentRow.setServiceUrl(dataServiceUrl);
                    if (persist) {
                        getQueryResultTableDao().getHibernateTemplate().save(
                                currentRow);
                    }
                    table.getRows().add(currentRow);

                } else if ("ObjectResult".equals(elementStack.get(elementStack
                        .size() - 2).localName)) {
                	 for (int i = 0; i < attributes.getLength(); i++) {
                        String name = attributes.getLocalName(i);
                        String value = attributes.getValue(i);
                        QueryResultColumn col = cols.get(name);
                        if (col == null) {
                            col = new QueryResultColumn();
                            col.setName(name);
                            col.setTable(table);
                            table.getColumns().add(col);
                            if (persist) {
                                getQueryResultTableDao().getHibernateTemplate()
                                        .save(col);
                            }
                            cols.put(name, col);
                        }
                        QueryResultCell cell = new QueryResultCell();
                        cell.setRow(currentRow);
                        cell.setColumn(col);
                        cell.setValue(value);
                        if (persist) {
                            getQueryResultTableDao().getHibernateTemplate()
                                    .save(cell);
                        }
                        currentRow.getCells().add(cell);

                    }
                }
            }
        }

    }

    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        elementStack.pop();
    }

    public void characters(char[] ch, int start, int length)
            throws org.xml.sax.SAXException {

    }

    public void endDocument() {

        if (ResultType.OBJECT.equals(resultType)) {
            List<String> colNames = getColumnNames();
            if (colNames != null && colNames.size() > 0) {
                for (String colName : colNames) {
                    if (!cols.containsKey(colName)) {
                        QueryResultColumn col = new QueryResultColumn();
                        col.setName(colName);
                        col.setTable(table);
                        if (persist) {
                            getQueryResultTableDao().getHibernateTemplate()
                                    .save(col);
                        }
                        cols.put(colName, col);
                    }
                }
            }
        }
        try {

            File file = getPortalFileService().write(table.getQueryInstance().getResult().getBytes());
            table.getQueryInstance().setResult(null);
            QueryResultData data = new QueryResultData();
            data.setFileName(file.getName());
            if (persist) {
                getQueryResultTableDao().getHibernateTemplate().save(data);
            }
            table.setData(data);
            if (persist) {
                getQueryResultTableDao().save(table);
            }
        } catch (Exception ex) {
            throw new RuntimeException("Error saving data: " + ex.getMessage(),
                    ex);
        }
    }

    @Override
    public List<String> getColumnNames() {
        if (super.getColumnNames() == null
                || super.getColumnNames().size() == 0) {
            return getQueryResultColumnNameResolver().getColumnNames(
                    getTable().getQueryInstance().getId());
        }
        return super.getColumnNames();
    }

    public QueryResultTable getTable() {
        return table;
    }

    public static void main(String[] args) throws Exception {
        SAXParserFactory fact = SAXParserFactory.newInstance();
        fact.setNamespaceAware(true);
        SAXParser parser = fact.newSAXParser();
        XMLQueryResultToQueryResultTableHandler handler = new XMLQueryResultToQueryResultTableHandler();
        parser.parse(new FileInputStream("tissueQueryResults_dcql_atts.xml"),
                handler);
        System.out.println(handler.getTable());
    }

    public int getMaxValueLength() {
        return maxValueLength;
    }

    public void setMaxValueLength(int maxValueLength) {
        this.maxValueLength = maxValueLength;
    }

    public QueryResultTableDao getQueryResultTableDao() {
        return queryResultTableDao;
    }

    public void setQueryResultTableDao(QueryResultTableDao queryResultTableDao) {
        this.queryResultTableDao = queryResultTableDao;
    }

    public QueryResultColumnNameResolver getQueryResultColumnNameResolver() {
        return queryResultColumnNameResolver;
    }

    public void setQueryResultColumnNameResolver(
            QueryResultColumnNameResolver queryResultColumnNameResolver) {
        this.queryResultColumnNameResolver = queryResultColumnNameResolver;
    }

    public String getDataServiceUrl() {
        return dataServiceUrl;
    }

    public void setDataServiceUrl(String dataServiceUrl) {
        this.dataServiceUrl = dataServiceUrl;
    }

    public boolean isPersist() {
        return persist;
    }

    public void setPersist(boolean persist) {
        this.persist = persist;
    }

    public PortalFileService getPortalFileService() {
        return portalFileService;
    }

    public void setPortalFileService(PortalFileService portalFileService) {
        this.portalFileService = portalFileService;
    }
}
