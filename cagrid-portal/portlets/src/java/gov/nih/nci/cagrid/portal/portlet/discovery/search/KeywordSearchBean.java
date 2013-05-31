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
package gov.nih.nci.cagrid.portal.portlet.discovery.search;

import gov.nih.nci.cagrid.portal.portlet.discovery.DiscoveryType;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class KeywordSearchBean {
	
	private DiscoveryType discoveryType;
	private String keywords;
	private String[] searchFields;
	private boolean activeServicesOnly;

	/**
	 * 
	 */
	public KeywordSearchBean() {

	}

	public DiscoveryType getDiscoveryType() {
		return discoveryType;
	}

	public void setDiscoveryType(DiscoveryType discoveryType) {
		this.discoveryType = discoveryType;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String[] getSearchFields() {
		return searchFields;
	}

	public void setSearchFields(String[] searchFields) {
		this.searchFields = searchFields;
	}

	public boolean isActiveServicesOnly() {
		return activeServicesOnly;
	}

	public void setActiveServicesOnly(boolean activeServicesOnly) {
		this.activeServicesOnly = activeServicesOnly;
	}

}
