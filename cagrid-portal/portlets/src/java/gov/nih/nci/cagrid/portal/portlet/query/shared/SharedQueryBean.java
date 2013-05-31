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
package gov.nih.nci.cagrid.portal.portlet.query.shared;

import gov.nih.nci.cagrid.portal.domain.dataservice.SharedCQLQuery;
import gov.nih.nci.cagrid.portal.portlet.query.cql.CQLQueryCommand;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class SharedQueryBean {
	
	private CQLQueryCommand queryCommand;
	private SharedCQLQuery query;
	private String prettyXml;

	/**
	 * 
	 */
	public SharedQueryBean() {

	}

	public CQLQueryCommand getQueryCommand() {
		return queryCommand;
	}

	public void setQueryCommand(CQLQueryCommand queryCommand) {
		this.queryCommand = queryCommand;
	}

	public SharedCQLQuery getQuery() {
		return query;
	}

	public void setQuery(SharedCQLQuery query) {
		this.query = query;
	}

	public String getPrettyXml() {
		return prettyXml;
	}

	public void setPrettyXml(String prettyXml) {
		this.prettyXml = prettyXml;
	}

}
