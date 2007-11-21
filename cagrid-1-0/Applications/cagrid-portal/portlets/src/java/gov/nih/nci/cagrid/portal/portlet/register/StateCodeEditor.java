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
