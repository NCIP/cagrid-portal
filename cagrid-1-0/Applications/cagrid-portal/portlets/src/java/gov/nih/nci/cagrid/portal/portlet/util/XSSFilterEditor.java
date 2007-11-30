/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.util;

import java.beans.PropertyEditorSupport;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class XSSFilterEditor extends PropertyEditorSupport {

	private HTMLInputFilter filter = new HTMLInputFilter();
	
	public XSSFilterEditor() {

	}

	public XSSFilterEditor(Object source) {
		super(source);
	}
	
	public void setAsText(String text){
		setValue(filter.filter(text));
	}

}
