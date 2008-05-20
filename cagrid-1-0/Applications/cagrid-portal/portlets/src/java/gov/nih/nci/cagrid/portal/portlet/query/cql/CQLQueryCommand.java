/**
 *
 */
package gov.nih.nci.cagrid.portal.portlet.query.cql;

import gov.nih.nci.cagrid.portal.portlet.query.model.SelectServiceCommand;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 */
public class CQLQueryCommand extends SelectServiceCommand {

    private String cqlQuery;
    private String dataServiceUrlError;
    private String cqlQueryError;
    private String confirmationMessage;
    private String cqlQuerySubmitError;
    private boolean dcql = false;

    /**
     *
     */
    public CQLQueryCommand() {

    }

    public void clear() {
        setCqlQuery(null);
        setDataServiceUrlError(null);
        setCqlQueryError(null);
        setCqlQuerySubmitError(null);
        setConfirmationMessage(null);
    }

    public String getConfirmationMessage() {
        return confirmationMessage;
    }

    public void setConfirmationMessage(String confirmationMessage) {
        this.confirmationMessage = confirmationMessage;
    }

    public String getCqlQuery() {
        return cqlQuery;
    }

    public void setCqlQuery(String cqlQuery) {
        this.cqlQuery = cqlQuery;
    }

    public String getCqlQueryError() {
        return cqlQueryError;
    }

    public void setCqlQueryError(String cqlQueryError) {
        this.cqlQueryError = cqlQueryError;
    }

    public String getDataServiceUrlError() {
        return dataServiceUrlError;
    }

    public void setDataServiceUrlError(String dataServiceUrlError) {
        this.dataServiceUrlError = dataServiceUrlError;
    }

    public boolean hasErrors() {
        return getDataServiceUrlError() != null || getCqlQueryError() != null || getCqlQuerySubmitError() != null;
    }

    public void setCqlQuerySubmitError(String cqlQuerySubmitError) {
        this.cqlQuerySubmitError = cqlQuerySubmitError;
    }

    public String getCqlQuerySubmitError() {
        return cqlQuerySubmitError;
    }

    public boolean isDcql() {
        return dcql;
    }

    public void setDcql(boolean dcql) {
        this.dcql = dcql;
    }
}
