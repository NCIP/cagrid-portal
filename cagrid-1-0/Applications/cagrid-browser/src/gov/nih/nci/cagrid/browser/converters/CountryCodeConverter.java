/**
 * $Id $
 */
package gov.nih.nci.cagrid.browser.converters;

import gov.nih.nci.cagrid.dorian.idp.bean.CountryCode;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class CountryCodeConverter implements Converter {

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.faces.convert.Converter#getAsObject(javax.faces.context.FacesContext,
	 *      javax.faces.component.UIComponent, java.lang.String)
	 */
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) throws ConverterException {

		CountryCode code = null;
		if (value != null && value.trim().length() > 0) {
			try {
				code = CountryCode.fromValue(value.trim());
			} catch (Exception ex) {
				throw new ConverterException("Error converting '" + value
						+ "' to String: " + ex.getMessage(), ex);
			}
		}

		return code;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.faces.convert.Converter#getAsString(javax.faces.context.FacesContext,
	 *      javax.faces.component.UIComponent, java.lang.Object)
	 */
	public String getAsString(FacesContext context, UIComponent component,
			Object code) throws ConverterException {
		String s = null;
		if (code != null) {
			try {
				s = ((CountryCode) code).getValue();
			} catch (Exception ex) {
				throw new ConverterException("Error converting '" + code
						+ "' to CountryCode: " + ex.getMessage(), ex);
			}
		}

		return s;
	}

}
