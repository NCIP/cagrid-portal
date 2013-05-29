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
package gov.nih.nci.cagrid.portal.portlet.gss;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;

import java.io.StringReader;

public class SummaryQueryWithLocations {
    private String caption;
    private String query;
    private String url;

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
    
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }



}