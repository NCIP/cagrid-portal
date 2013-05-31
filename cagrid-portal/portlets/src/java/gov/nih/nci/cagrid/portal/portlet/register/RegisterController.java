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
package gov.nih.nci.cagrid.portal.portlet.register;

import gov.nih.nci.cagrid.dorian.client.DorianClient;
import gov.nih.nci.cagrid.dorian.idp.bean.Application;
import gov.nih.nci.cagrid.dorian.idp.bean.CountryCode;
import gov.nih.nci.cagrid.dorian.idp.bean.StateCode;
import gov.nih.nci.cagrid.dorian.stubs.types.DorianInternalFault;
import gov.nih.nci.cagrid.dorian.stubs.types.InvalidUserPropertyFault;
import gov.nih.nci.cagrid.portal.portlet.util.XSSFilterEditor;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.axis.types.URI.MalformedURIException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.bind.PortletRequestDataBinder;
import org.springframework.web.portlet.mvc.SimpleFormController;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class RegisterController extends SimpleFormController implements
		InitializingBean {

	private List<String> stateCodes = new ArrayList<String>();
	private List<String> countryCodes = new ArrayList<String>();
	private String ifsUrl;

	/**
	 * 
	 */
	public RegisterController() {

	}

	protected void initBinder(PortletRequest request,
			PortletRequestDataBinder binder) throws Exception {
		

		binder.registerCustomEditor(StateCode.class, "state",
				new StateCodeEditor());
		binder.registerCustomEditor(CountryCode.class, "country",
				new CountryCodeEditor());

		binder.registerCustomEditor(String.class, "userId", new XSSFilterEditor(binder.getBindingResult(), "userId"));
		binder.registerCustomEditor(String.class, "email", new XSSFilterEditor(binder.getBindingResult(), "email"));
		binder.registerCustomEditor(String.class, "firstName", new XSSFilterEditor(binder.getBindingResult(), "firstName"));
		binder.registerCustomEditor(String.class, "lastName", new XSSFilterEditor(binder.getBindingResult(), "lastName"));
		binder.registerCustomEditor(String.class, "organization", new XSSFilterEditor(binder.getBindingResult(), "organization"));
		binder.registerCustomEditor(String.class, "address", new XSSFilterEditor(binder.getBindingResult(), "address"));
		binder.registerCustomEditor(String.class, "addres2", new XSSFilterEditor(binder.getBindingResult(), "address2"));
		binder.registerCustomEditor(String.class, "city", new XSSFilterEditor(binder.getBindingResult(), "city"));
		binder.registerCustomEditor(String.class, "phoneNumber", new XSSFilterEditor(binder.getBindingResult(), "phoneNumber"));
	}

	protected Object formBackingObject(PortletRequest request) throws Exception {
		return new Application();
	}

	protected Map referenceData(PortletRequest request, Object command,
			Errors errors) throws Exception {
		Map data = new HashMap();
		data.put("stateCodes", getStateCodes());
		data.put("countryCodes", getCountryCodes());
		return data;
	}

	protected void onSubmitAction(ActionRequest request,
			ActionResponse response, Object command, BindException errors)
			throws Exception {
		Application application = (Application) command;
		String confirmationMessage = null;
		DorianClient client = null;
		try {
			client = new DorianClient(getIfsUrl());
		} catch (MalformedURIException ex) {
			errors.reject("badDorianUrl", new String[] { getIfsUrl() },
					"Bad Dorian URL: " + getIfsUrl());
		} catch (RemoteException ex) {
			errors.reject("dorianClientError",
					new String[] { ex.getMessage() },
					"Can't instantiate Dorian client: " + ex.getMessage());
		}
		if (!errors.hasErrors()) {
			try {
				confirmationMessage = client.registerWithIdP(application);
				if("Your account was approved, your current account status is Active.".equals(confirmationMessage)){
					confirmationMessage = "Your account was approved. Your current account status is Active.";
				}
			} catch (DorianInternalFault ex) {
				errors.reject("dorianServiceError", new String[] { ex
						.getMessage() }, "Error contacting Dorian service: "
						+ ex.getFaultString());
			} catch (InvalidUserPropertyFault ex) {
				errors.reject("dorianInvalidUserPropertyError",
						new String[] { ex.getFaultString() }, ex
								.getFaultString());
			} catch (RemoteException ex) {
				errors.reject("dorianServiceError", new String[] { ex
						.getMessage() }, "Error contacting Dorian service: "
						+ ex.getMessage());
			}
		}
		if (!errors.hasErrors()) {
			response.setRenderParameter("confirmationMessage",
					confirmationMessage);
		}
	}

	protected ModelAndView onSubmitRender(RenderRequest request,
			RenderResponse response, Object command, BindException errors)
			throws Exception {
		ModelAndView mav = new ModelAndView(getSuccessView(), errors.getModel());
		if (!errors.hasErrors()) {
			mav.addObject("confirmationMessage", request
					.getParameter("confirmationMessage"));
		}
		mav.addObject("stateCodes", getStateCodes());
		mav.addObject("countryCodes", getCountryCodes());
		return mav;
	}

	public List<String> getStateCodes() {
		return stateCodes;
	}

	public void setStateCodes(List<String> stateCodes) {
		this.stateCodes = stateCodes;
	}

	public List<String> getCountryCodes() {
		return countryCodes;
	}

	public void setCountryCodes(List<String> countryCodes) {
		this.countryCodes = countryCodes;
	}

	public String getIfsUrl() {
		return ifsUrl;
	}

	public void setIfsUrl(String ifsUrl) {
		this.ifsUrl = ifsUrl;
	}

	public void afterPropertiesSet() throws Exception {
		setStateCodes(sort(getStateCodes()));
		setCountryCodes(sort(getCountryCodes()));
	}

	private List<String> sort(List<String> values) {
		return new ArrayList<String>(new TreeSet<String>(values));
	}

}
