package gov.nih.nci.cagrid.portal.portlet.gss;

public class ImpromptuQuery {
    
    private String query = "<ns1:CQLQuery xmlns:ns1=\"http://CQL.caBIG/1/gov.nih.nci.cagrid.CQLQuery\"><ns1:Target name=\"gov.nih.nci.caarray.domain.array.Array\"/><ns1:QueryModifier countOnly=\"true\"/></ns1:CQLQuery>";
    private String endpointUrl = "http://cagrid1.duhs.duke.edu:18080/wsrf/services/cagrid/CaArraySvc";
    private String result;

    public String getQuery() {
        return query;
    }
    
    public void setQuery(String query) {
        this.query = query;
    }
    
    public String getEndpointUrl() {
        return endpointUrl;
    }
    
    public void setEndpointUrl(String endpointUrl) {
        this.endpointUrl = endpointUrl;
    }
    
    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "ImpromptuQuery [endpointUrl=" + endpointUrl + ", query=" + query + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((endpointUrl == null) ? 0 : endpointUrl.hashCode());
        result = prime * result + ((query == null) ? 0 : query.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ImpromptuQuery other = (ImpromptuQuery) obj;
        if (endpointUrl == null) {
            if (other.endpointUrl != null)
                return false;
        } else if (!endpointUrl.equals(other.endpointUrl))
            return false;
        if (query == null) {
            if (other.query != null)
                return false;
        } else if (!query.equals(other.query))
            return false;
        return true;
    }
    
}
