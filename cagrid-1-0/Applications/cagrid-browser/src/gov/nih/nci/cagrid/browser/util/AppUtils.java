/**
 * $Id $
 */

package gov.nih.nci.cagrid.browser.util;

import java.io.InputStream;
import java.util.Map;
import java.util.ResourceBundle;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.el.PropertyNotFoundException;

import org.apache.log4j.Logger;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class AppUtils {

	private static Logger logger = Logger.getLogger(AppUtils.class);

	private static final String INVALID_STATE_CODE = "registrationFailed.invalidStateCode";

	private static final String INVALID_COUNTRY_CODE = "registrationFailed.invalidCountryCode";

	private static ResourceBundle messages = ResourceBundle
			.getBundle("messages");

	private static ResourceBundle labels = ResourceBundle.getBundle("labels");

	public static String getMessage(String name) {
		return messages.getString(name);
	}

	public static String getLabel(String name) {
		return labels.getString(name);
	}

	public static InputStream loadResourceAsStream(String fileName) {
		ExternalContext cont = FacesContext.getCurrentInstance()
				.getExternalContext();
		InputStream stream = cont.getResourceAsStream(fileName);

		return stream;
	}

	public static Object getBean(String beanName)
			throws PropertyNotFoundException {
		FacesContext ctx = FacesContext.getCurrentInstance();
		return ctx.getApplication().getVariableResolver().resolveVariable(ctx,
				beanName);
	}

	public static Object getParameter(String paramName) {
		Map reqMap = FacesContext.getCurrentInstance().getExternalContext()
				.getRequestMap();
		return reqMap.get(paramName);
	}

//	public static void validateStateCode(FacesContext context,
//			UIComponent toValidate, Object value) {
//		
//		try {
//			StateCode.fromValue((String) value);
//		} catch (Exception ex) {
//			String msg = AppUtils.getMessage(INVALID_STATE_CODE);
//			logger.error(msg, ex);
//			((UIInput) toValidate).setValid(false);
//			context.addMessage(toValidate.getClientId(context),
//					new FacesMessage(msg));
//		}
//	}
//
//	public static void validateCountryCode(FacesContext context,
//			UIComponent toValidate, Object value) {
//		try {
//			CountryCode.fromValue((String) value);
//		} catch (Exception ex) {
//			String msg = AppUtils.getMessage(INVALID_COUNTRY_CODE);
//			logger.error(msg, ex);
//			((UIInput) toValidate).setValid(false);
//			context.addMessage(toValidate.getClientId(context),
//					new FacesMessage(msg));
//		}
//	}

}
