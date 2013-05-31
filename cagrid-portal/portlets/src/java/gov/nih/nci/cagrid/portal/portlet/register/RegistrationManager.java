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

import java.rmi.RemoteException;

import org.apache.axis.types.URI.MalformedURIException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class RegistrationManager {

	private static final Log logger = LogFactory
			.getLog(RegistrationManager.class);

	private String firstName;
	private String lastName;
	private String username;
	private String password;
	private String email;
	private String phone;
	private String street1;
	private String street2;
	private String locality;
	private String stateProvince;
	private String country;
	private String organization;
	private String postalCode;

	private String ifsUrl;

	public String submit() {
		try {
			String response = null;

			Application application = new Application();
			application.setFirstName(firstName);
			application.setLastName(lastName);
			application.setUserId(username);
			application.setPassword(password);
			application.setEmail(email);
			application.setPhoneNumber(phone);
			application.setAddress(street1);
			application.setAddress2(street2);
			application.setCity(locality);
			application.setState(StateCode.fromString(stateProvince));
			application.setCountry(CountryCode.fromString(country));
			application.setOrganization(organization);
			application.setZipcode(postalCode);

			DorianClient client = null;
			try {
				client = new DorianClient(getIfsUrl());
			} catch (MalformedURIException ex) {
				String msg = "Error: Dorian URL: " + getIfsUrl();
				logger.error(msg, ex);
				throw new RuntimeException(msg);
			} catch (RemoteException ex) {
				String msg = "Error: Dorian Client: " + ex.getMessage();
				logger.error(msg, ex);
				throw new RuntimeException(msg);
			}

			try {
				response = client.registerWithIdP(application);
			} catch (DorianInternalFault ex) {
				String msg = "Error: Dorian Service Internal: "
						+ ex.getFaultString();
				logger.error(msg, ex);
				throw new RuntimeException(msg);
			} catch (InvalidUserPropertyFault ex) {
				String msg = "Error: Invalid User Property: " + ex.getFaultString();
				logger.error(msg, ex);
				throw new RuntimeException(msg);
			} catch (RemoteException ex) {
				String msg = "Error: Remote: " + ex.getMessage();
				logger.error(msg, ex);
				throw new RuntimeException(msg);
			}

			return response;
		} catch (Exception ex) {
			String msg = "Error encountered: " + ex.getMessage();
			logger.error(msg, ex);
			throw new RuntimeException(msg, ex);
		}
	}

	public String validate() {
		String response = null;
		return response;
	}

	public String setFirstName(String firstName) {
		String response = null;
		this.firstName = firstName;
		return response;
	}

	public String setLastName(String lastName) {
		String response = null;
		this.lastName = lastName;
		return response;
	}

	public String setUsername(String username) {
		String response = null;
		this.username = username;
		return response;
	}

	public String setPassword(String password) {
		String response = null;
		this.password = password;
		return response;
	}

	public String setEmail(String email) {
		String response = null;
		this.email = email;
		return response;
	}

	public String setPhone(String phone) {
		String response = null;
		this.phone = phone;
		return response;
	}

	public String setStreet1(String street1) {
		String response = null;
		this.street1 = street1;
		return response;
	}

	public String setStreet2(String street2) {
		String response = null;
		this.street2 = street2;
		return response;
	}

	public String setLocality(String locality) {
		String response = null;
		this.locality = locality;
		return response;
	}

	public String setStateProvince(String stateProvince) {
		String response = null;
		this.stateProvince = stateProvince;
		return response;
	}

	public String setCountry(String country) {
		String response = null;
		this.country = country;
		return response;
	}

	public String setOrganization(String organization) {
		String response = null;
		this.organization = organization;
		return response;
	}

	public String setPostalCode(String postalCode) {
		String response = null;
		this.postalCode = postalCode;
		return response;
	}

	public String getIfsUrl() {
		return ifsUrl;
	}

	public void setIfsUrl(String ifsUrl) {
		this.ifsUrl = ifsUrl;
	}

}
