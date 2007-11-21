/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.register;

import gov.nih.nci.cagrid.dorian.idp.bean.CountryCode;

import java.beans.PropertyEditorSupport;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class CountryCodeEditor extends PropertyEditorSupport {

	/**
	 * 
	 */
	public CountryCodeEditor() {

	}

	/**
	 * @param source
	 */
	public CountryCodeEditor(Object source) {
		super(source);

	}
	
	public void setAsText(String text){
		setValue(CountryCode.fromString(text));
	}

}
