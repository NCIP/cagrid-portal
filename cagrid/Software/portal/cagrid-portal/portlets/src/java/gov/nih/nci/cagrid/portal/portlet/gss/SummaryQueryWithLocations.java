/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.gss;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;

import java.io.StringReader;

public class SummaryQueryWithLocations {
    private String caption;
    private String query;

    public String getCaption() {
        return caption;
    }

    public void setCaption(String name) {
        this.caption = name;
    }

    public CQLQuery getCqlQuery() {
        try {
            return (CQLQuery) Utils.deserializeObject(new StringReader(this.query), CQLQuery.class);
        } catch (Exception e) {
            return null;
        }
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }



}