/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.gss;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.portal.domain.catalog.GridServiceEndPointCatalogEntry;
import gov.nih.nci.cagrid.portal.portlet.util.PortletUtils;

import java.io.StringReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

class SummaryQueryWithLocations {
    private String id;
    private String query;
    private Set<String> urls = new HashSet<String>();
    private Map<String, Long> counters = new HashMap<String, Long>();
    private String packageName;
    private String shortClassName;

    public SummaryQueryWithLocations(String _id, String _query) {
        this.id = _id;
        this.query = _query;
        String umlClassName = PortletUtils.getTargetUMLClassName(this.query);
        int idx = umlClassName.lastIndexOf(".");
        this.packageName = umlClassName.substring(0, idx);
        this.shortClassName = umlClassName.substring(idx + 1);
    }

    public SummaryQueryWithLocations(String _id, String _query, String _defaultLocation) {
        this(_id, _query);
        this.urls.add(_defaultLocation);
    }

    public CQLQuery getCqlQuery() {
        try {
            return (CQLQuery) Utils.deserializeObject(new StringReader(this.query), CQLQuery.class);
        } catch (Exception e) {
            return null;
        }
    }

    public String getPackage() {
        return this.packageName;
    }

    public String getShortClassName() {
        return this.shortClassName;
    }

    public Iterator<String> getUrlsIterator() {
        return urls.iterator();
    }

    public void addUrls(List<GridServiceEndPointCatalogEntry> endpointCes) {
        if (endpointCes != null) {
            for (GridServiceEndPointCatalogEntry p : endpointCes) {
                this.urls.add(p.getAbout().getUrl());
            }
        }
    }

    public void setCounter(String url, String s) {
        long l = 0;
        try {
            l = Long.parseLong(s);
        } catch (NumberFormatException e) {
            l = 0;
        }
        counters.put(url, new Long(l));
    }

    public Long getSum() {
        long result = 0;
        Iterator<String> i = counters.keySet().iterator();
        while (i.hasNext()) {
            result = result + counters.get(i.next()).longValue();
        }
        return result;
    }

    public String getId() {
        return this.id;
    }

}