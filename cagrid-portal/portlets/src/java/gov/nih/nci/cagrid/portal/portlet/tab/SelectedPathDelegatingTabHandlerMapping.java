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
package gov.nih.nci.cagrid.portal.portlet.tab;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.PortletRequest;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class SelectedPathDelegatingTabHandlerMapping extends TabHandlerMapping {
	
	private List<SelectedPathHandler> selectedPathHandlers = new ArrayList<SelectedPathHandler>();

	/**
	 * 
	 */
	public SelectedPathDelegatingTabHandlerMapping() {

	}
	
	protected String getSelectedPath(PortletRequest request) {
		String selectedPath = null;
		
		for(SelectedPathHandler handler : getSelectedPathHandlers()){
			selectedPath = handler.getSelectedPath(request);
			if(selectedPath != null){
				break;
			}
		}
		
		if(selectedPath == null){
			selectedPath = super.getSelectedPath(request);
		}
		return selectedPath;
	}

	public List<SelectedPathHandler> getSelectedPathHandlers() {
		return selectedPathHandlers;
	}

	public void setSelectedPathHandlers(
			List<SelectedPathHandler> selectedPathHandlers) {
		this.selectedPathHandlers = selectedPathHandlers;
	}

}
