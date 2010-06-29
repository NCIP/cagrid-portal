/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.gss;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.DataServiceConstants;
import gov.nih.nci.cagrid.portal.domain.catalog.GridServiceEndPointCatalogEntry;
import gov.nih.nci.cagrid.portal.portlet.util.PortletUtils;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

public class SummaryQueryWithLocations {
    private String caption;
    private String query;
    private Set<String> urls = new HashSet<String>();
    private Map<String, Long> counters = new HashMap<String, Long>();
    private String packageName;
    private String shortClassName;

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

    public void setCounter(String url, String s) {
        long l = 0;
        try {
            l = Long.parseLong(s);
        } catch (NumberFormatException e) {
            l = 0;
        }
        counters.put(url, new Long(l));
    }
    
    static public String queryResultAsString(CQLQueryResults queryResult) throws Exception {
        StringWriter writer = new StringWriter();
        Utils.serializeObject(queryResult, DataServiceConstants.CQL_RESULT_SET_QNAME, writer);
        String out = writer.getBuffer().toString();
        out = out.replace(" ", "");
        out = out.replace("\n", "");
        out = out.replace("targetClassname", " targetClassname");
        out = out.replace("xmlns", " xmlns");
        out = out.replace("count", " count");
        return out;
    }

    public void setCounterFromFullAnswer(String url, String answer) throws Exception {
        // 
        // long l = result.getCountResult().getCount();
        //
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = documentBuilderFactory.newDocumentBuilder();
        InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(answer));
        Document document = builder.parse(is);
       
        Node firstChild = document.getFirstChild();
        Node firstGrandChild = firstChild.getFirstChild();

        NamedNodeMap nm = firstGrandChild.getAttributes();
        Node namedItem = nm.getNamedItem("count");

        String resStr = namedItem.getTextContent();

        this.setCounter(url, resStr);
    }

    public Long getSum() {
        long result = 0;
        Iterator<String> i = counters.keySet().iterator();
        while (i.hasNext()) {
            result = result + counters.get(i.next()).longValue();
        }
        return result;
    }

    public void resetCounters() {
        counters.clear();
    }

}