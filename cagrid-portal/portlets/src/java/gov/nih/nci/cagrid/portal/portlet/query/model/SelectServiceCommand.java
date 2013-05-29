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
package gov.nih.nci.cagrid.portal.portlet.query.model;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class SelectServiceCommand {
	
	private String dataServiceUrl;

	/**
	 * 
	 */
	public SelectServiceCommand() {

	}

	public String getDataServiceUrl() {
		return dataServiceUrl;
	}

	public void setDataServiceUrl(String serviceUrl) {
		this.dataServiceUrl = serviceUrl;
	}

}
