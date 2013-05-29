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
package gov.nih.nci.cagrid.portal.portlet.register;

import gov.nih.nci.cagrid.dorian.idp.bean.StateCode;

import java.beans.PropertyEditorSupport;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class StateCodeEditor extends PropertyEditorSupport {
	
	/**
	 * 
	 */
	public StateCodeEditor() {

	}


	public StateCodeEditor(Object source) {
		super(source);
	}
	
	public void setAsText(String text){
		setValue(StateCode.fromString(text));
	}

}
