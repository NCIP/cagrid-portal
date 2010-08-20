/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.gss;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.portal.domain.catalog.GridServiceEndPointCatalogEntry;
import gov.nih.nci.cagrid.portal.portlet.util.PortletUtils;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class SummaryQueryWithLocations {
    private UUID id;
    private String caption;
    private String query;
    private Set<String> urls = new HashSet<String>();
    private String packageName;
    private String shortClassName;

    public UUID getId() {
        return id;
    }

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

    public Iterator<String> getUrlsIterator() {
        return urls.iterator();
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.id = UUID.randomUUID();
        this.query = query;
        String umlClassName = PortletUtils.getTargetUMLClassName(this.query);
        int idx = umlClassName.lastIndexOf(".");
        this.packageName = umlClassName.substring(0, idx);
        this.shortClassName = umlClassName.substring(idx + 1);
    }

    public String getPackage() {
        return this.packageName;
    }

    public String getShortClassName() {
        return this.shortClassName;
    }

    public Set<String> getUrls() {
        return urls;
    }

    public void setUrls(Set<String> urls) {
        this.urls = urls;
    }

    public String getPackageName() {
        return packageName;
    }

    public void addUrls(List<GridServiceEndPointCatalogEntry> endpointCes) {
        if (endpointCes != null) {
            for (GridServiceEndPointCatalogEntry p : endpointCes) {
                this.urls.add(p.getAbout().getUrl());
            }
        }
    }

}