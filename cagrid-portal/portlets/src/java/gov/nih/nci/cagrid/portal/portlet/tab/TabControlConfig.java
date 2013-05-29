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
package gov.nih.nci.cagrid.portal.portlet.tab;

import org.springframework.beans.factory.annotation.Required;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class TabControlConfig {
	
	private String selectedPathParameterName;
	private String tabModelRequestAttributeName;
	private TabModel tabModel;

	/**
	 * 
	 */
	public TabControlConfig() {
		// TODO Auto-generated constructor stub
	}
	@Required
	public String getSelectedPathParameterName() {
		return selectedPathParameterName;
	}

	public void setSelectedPathParameterName(String selectPathParameterName) {
		this.selectedPathParameterName = selectPathParameterName;
	}
	@Required
	public String getTabModelRequestAttributeName() {
		return tabModelRequestAttributeName;
	}

	public void setTabModelRequestAttributeName(String tabModelRequestAttributeName) {
		this.tabModelRequestAttributeName = tabModelRequestAttributeName;
	}
	@Required
	public TabModel getTabModel() {
		return tabModel;
	}

	public void setTabModel(TabModel tabModel) {
		this.tabModel = tabModel;
	}

}
