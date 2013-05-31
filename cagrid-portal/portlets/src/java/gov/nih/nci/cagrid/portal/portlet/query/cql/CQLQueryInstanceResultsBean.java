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
package gov.nih.nci.cagrid.portal.portlet.query.cql;

import gov.nih.nci.cagrid.portal.domain.dataservice.QueryInstance;
import gov.nih.nci.cagrid.portal.portlet.util.TableScroller;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 */
public class CQLQueryInstanceResultsBean {

    private TableScroller tableScroller;
    private QueryInstance instance;
    private String error;
    private String prettyXml;

    /**
     *
     */
    public CQLQueryInstanceResultsBean() {

    }

    public QueryInstance getInstance() {
        return instance;
    }

    public void setInstance(QueryInstance instance) {
        this.instance = instance;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public void clear() {
        setInstance(null);
        setError(null);
        setTableScroller(null);
    }

    public TableScroller getTableScroller() {
        return tableScroller;
    }

    public void setTableScroller(TableScroller tableScroller) {
        this.tableScroller = tableScroller;
    }

    public String getPrettyXml() {
        return prettyXml;
    }

    public void setPrettyXml(String prettyXml) {
        this.prettyXml = prettyXml;
    }

}
