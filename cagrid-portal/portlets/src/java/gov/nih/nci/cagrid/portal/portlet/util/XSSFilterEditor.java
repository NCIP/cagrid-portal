/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.util;

import gov.nih.nci.cagrid.portal.portlet.PortletConstants;
import gov.nih.nci.cagrid.portal.util.PortalUtils;

import java.beans.PropertyEditorSupport;

import org.springframework.validation.Errors;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class XSSFilterEditor extends PropertyEditorSupport {

	private HTMLInputFilter filter = new HTMLInputFilter();
	private Errors errors;
	private String field;
	private String code;
	private String defaultMessage;

	public XSSFilterEditor() {

	}

	public XSSFilterEditor(Object source) {
		super(source);
	}

	public XSSFilterEditor(Errors errors, String field, String code,
			String defaultMessage) {
		this.errors = errors;
		this.field = field;
		this.code = code;
		this.defaultMessage = defaultMessage;
	}
	
	public XSSFilterEditor(Errors errors, String field) {
		this.errors = errors;
		this.field = field;
		this.code = PortletConstants.XSS_TEXT_MSG;
		this.defaultMessage = "Invalid text!";
	}

	public void setAsText(String text) {
		if (!PortalUtils.isEmpty(text)) {
			
			String filtered = null;
			try{
				filtered = filter.filter(text);
			}catch(Exception ex){
				//Ignore
			}
			if (!text.equals(filtered) && errors != null) {
				errors.rejectValue(field, code, null, defaultMessage);
			} else {
				setValue(filtered);
			}
		}
	}

	public String getAsText() {
		String text = (String) getValue();
		if (PortalUtils.isEmpty(text)) {
			return null;
		}
		return text;
	}
	
	public static void main(String[] args){
		HTMLInputFilter f = new HTMLInputFilter(true);
		System.out.println(f.filter("ayn()r123$ABC"));
	}

}
