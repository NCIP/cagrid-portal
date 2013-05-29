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
package gov.nih.nci.cagrid.portal.portlet.map;

import gov.nih.nci.cagrid.portal.portlet.discovery.DiscoveryType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class SelectItemsCommand {

	private DiscoveryType type;
	private List<Integer> selectedIds = new ArrayList<Integer>();
	
	/**
	 * 
	 */
	public SelectItemsCommand() {

	}

	public DiscoveryType getType() {
		return type;
	}

	public void setType(DiscoveryType type) {
		this.type = type;
	}

	public List<Integer> getSelectedIds() {
		return selectedIds;
	}

	public void setSelectedIds(List<Integer> selectedIds) {
		this.selectedIds = selectedIds;
	}

}
