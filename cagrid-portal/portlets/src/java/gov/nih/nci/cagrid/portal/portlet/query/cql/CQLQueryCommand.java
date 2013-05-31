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

import java.io.StringReader;
import java.io.StringWriter;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.fqp.common.DCQLConstants;
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
		return getDataServiceUrlError() != null || getCqlQueryError() != null
				|| getCqlQuerySubmitError() != null;
	}

	public void setCqlQuerySubmitError(String cqlQuerySubmitError) {
		this.cqlQuerySubmitError = cqlQuerySubmitError;
	}

	public String getCqlQuerySubmitError() {
		return cqlQuerySubmitError;
	}

	public boolean isDcql() {
		boolean isDcql = false;
		try {
			// First check if dcql
			gov.nih.nci.cagrid.dcql.DCQLQuery query = (gov.nih.nci.cagrid.dcql.DCQLQuery) Utils
					.deserializeObject(new StringReader(getCqlQuery()),
							gov.nih.nci.cagrid.dcql.DCQLQuery.class);
			StringWriter w = new StringWriter();
			Utils.serializeObject(query, DCQLConstants.DCQL_QUERY_QNAME, w);
			isDcql = true;
		} catch (Exception ex) {

		}
		return isDcql;
	}

}
