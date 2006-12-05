/**
 * $Id $
 */
package gov.nih.nci.cagrid.browser.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import org.apache.axis.message.addressing.Address;
import org.apache.axis.message.addressing.EndpointReferenceType;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class EPRConverter implements Converter {

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.faces.convert.Converter#getAsObject(javax.faces.context.FacesContext,
	 *      javax.faces.component.UIComponent, java.lang.String)
	 */
	public Object getAsObject(FacesContext ctx, UIComponent component,
			String value) throws ConverterException {
		EndpointReferenceType epr = null;
		if (value != null) {
			try {
				epr = new EndpointReferenceType();
				epr.setAddress(new Address(value));
			} catch (Exception ex) {
				throw new ConverterException("Error converting '" + value
						+ "' to EndpointReferenceType: " + ex.getMessage(), ex);
			}
		}
		return epr;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.faces.convert.Converter#getAsString(javax.faces.context.FacesContext,
	 *      javax.faces.component.UIComponent, java.lang.Object)
	 */
	public String getAsString(FacesContext ctx, UIComponent component,
			Object epr) {
		String str = null;
		if (epr != null) {
			try {
				str = ((EndpointReferenceType)epr).getAddress().toString();
			} catch (Exception ex) {
				throw new ConverterException("Error converting " + epr
						+ " to String: " + ex.getMessage(), ex);
			}
		}
		return str;
	}

}
