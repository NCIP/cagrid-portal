package gov.nih.nci.cagrid.portal.portlet.impromptu;

import java.util.UUID;

public class ImpromptuQuery {

    private UUID uuid;
    private String query = "";
    private String endpointUrl = "";
    private boolean clearPrevious = false;
    private long accessedOn = 0;
    private boolean htmlSuccessPage = false;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

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

    public boolean isClearPrevious() {
        return clearPrevious;
    }

    public void setClearPrevious(boolean clearPrevious) {
        this.clearPrevious = clearPrevious;
    }

    public long getAccessedOn() {
        return accessedOn;
    }

    public void setAccessedOn(long accessedOn) {
        this.accessedOn = accessedOn;
    }

    public boolean isHtmlSuccessPage() {
        return htmlSuccessPage;
    }

    public void setHtmlSuccessPage(boolean htmlSuccessPage) {
        this.htmlSuccessPage = htmlSuccessPage;
    }

    @Override
    public String toString() {
        return "ImpromptuQuery [clearPrevious=" + clearPrevious + ", endpointUrl=" + endpointUrl + ", query=" + query + ", accessedOn=" + accessedOn + ", uuid=" + uuid + ", htmlSuccessPage="
                + htmlSuccessPage + "]";
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
