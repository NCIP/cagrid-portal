package gov.nih.nci.cagrid.gums.ifs;

import gov.nih.nci.cagrid.gums.bean.GUMSInternalFault;
import gov.nih.nci.cagrid.gums.common.GUMSObject;
import gov.nih.nci.cagrid.gums.ifs.bean.CredentialsFault;
import gov.nih.nci.cagrid.gums.ifs.bean.ExpiredAssertionFault;
import gov.nih.nci.cagrid.gums.ifs.bean.IFSUser;
import gov.nih.nci.cagrid.gums.ifs.bean.IFSUserRole;
import gov.nih.nci.cagrid.gums.ifs.bean.IFSUserStatus;
import gov.nih.nci.cagrid.gums.ifs.bean.InvalidAssertionFault;
import gov.nih.nci.cagrid.gums.ifs.bean.InvalidProxyFault;
import gov.nih.nci.cagrid.gums.ifs.bean.InvalidTrustedIdPFault;
import gov.nih.nci.cagrid.gums.ifs.bean.InvalidUserFault;
import gov.nih.nci.cagrid.gums.ifs.bean.ProxyValid;
import gov.nih.nci.cagrid.gums.ifs.bean.TrustedIdP;
import gov.nih.nci.cagrid.gums.ifs.bean.UntrustedAssertionFault;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;

import org.globus.wsrf.utils.FaultHelper;
import org.opensaml.SAMLAssertion;
import org.opensaml.SAMLAttribute;
import org.opensaml.SAMLAttributeStatement;
import org.opensaml.SAMLAuthenticationStatement;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class IFS extends GUMSObject {

	public final static String EMAIL_NAMESPACE = "http://cagrid.nci.nih.gov/email";

	public final static String EMAIL_NAME = "email";

	public IFS() {
	
	}

	public synchronized TrustedIdP addTrustedIdP(TrustedIdP idp)
			throws GUMSInternalFault, InvalidTrustedIdPFault {
		// TODO: Verify User is an administrator etc.
		return IFSManager.getInstance().getTrustManager().addTrustedIdP(idp);
	}

	public void createProxy(SAMLAssertion saml, ProxyValid valid) throws GUMSInternalFault,
			ExpiredAssertionFault, InvalidAssertionFault,
			UntrustedAssertionFault, CredentialsFault,InvalidProxyFault {
		if (!saml.isSigned()) {
			UntrustedAssertionFault fault = new UntrustedAssertionFault();
			fault
					.setFaultString("The assertion specified is invalid, it MUST be signed by a trusted IdP");
			throw fault;
		}

		// Determine whether or not the assertion is expired
		Calendar cal = new GregorianCalendar();
		Date now = cal.getTime();
		if ((now.before(saml.getNotBefore()))
				|| (now.after(saml.getNotOnOrAfter()))) {
			ExpiredAssertionFault fault = new ExpiredAssertionFault();
			fault.setFaultString("The Assertion is not valid at " + now
					+ ", the assertion is valid from " + saml.getNotBefore()
					+ " to " + saml.getNotOnOrAfter());
			throw fault;
		}

		// Make sure the assertion is trusted
		TrustedIdP idp = IFSManager.getInstance().getTrustManager()
				.getTrustedIdP(saml);

		SAMLAuthenticationStatement auth = getAuthenticationStatement(saml);

		// We need to verify the authentication method now
		boolean allowed = false;
		for (int i = 0; i < idp.getAuthenticationMethod().length; i++) {
			if (idp.getAuthenticationMethod(i).getValue().equals(
					auth.getAuthMethod())) {
				allowed = true;
			}
		}
		if (!allowed) {
			InvalidAssertionFault fault = new InvalidAssertionFault();
			fault.setFaultString("The authentication method "
					+ auth.getAuthMethod() + " is not acceptable for the IdP "
					+ idp.getName());
			throw fault;
		}

		UserManager um = IFSManager.getInstance().getUserManager();

		// If the user does not exist, add them
		String email = this.getEmail(saml);
		IFSUser usr = null;
		if (!um.determineIfUserExists(idp.getId(), auth.getSubject().getName())) {
			try {
				usr = new IFSUser();
				usr.setIdPId(idp.getId());
				usr.setUID(auth.getSubject().getName());
				usr.setEmail(email);
				usr.setUserRole(IFSUserRole.Non_Administrator);
				usr.setUserStatus(IFSUserStatus.Pending);
				usr = um.addUser(usr);
			} catch (InvalidUserFault e) {
				logError(e.getMessage(), e);
				GUMSInternalFault fault = new GUMSInternalFault();
				fault
						.setFaultString("An unexpected error occurred in adding the user "
								+ usr.getUID()
								+ " from the IdP "
								+ idp.getName());
				FaultHelper helper = new FaultHelper(fault);
				helper.addFaultCause(e);
				fault = (GUMSInternalFault) helper.getFault();
				throw fault;
			}
		} else {
			try {
				usr = um.getUser(idp.getId(), auth.getSubject().getName());
				if (!usr.getEmail().equals(email)) {
					usr.setEmail(email);
					um.updateUser(usr);
				}
			} catch (Exception e) {
				logError(e.getMessage(), e);
				GUMSInternalFault fault = new GUMSInternalFault();
				fault
						.setFaultString("An unexpected error occurred in obtaining the user "
								+ usr.getUID()
								+ " from the IdP "
								+ idp.getName());
				FaultHelper helper = new FaultHelper(fault);
				helper.addFaultCause(e);
				fault = (GUMSInternalFault) helper.getFault();
				throw fault;
			}
		}
		
		IFSConfiguration conf = IFSManager.getInstance().getConfiguration();
		//Validate that the proxy is of valid length
		if(getProxyValid(valid).after(conf.getMaxProxyValid())){
			InvalidProxyFault fault = new InvalidProxyFault();
			fault.setFaultString("The proxy valid length exceeds the maximum proxy valid length (hrs="+conf.getMaxProxyValidHours()+", mins="+conf.getMaxProxyValidMinutes()+", sec="+conf.getMaxProxyValidSeconds()+")");
			throw fault;
		}
		
		
		

		// Run the policy

		// Auto renew policy should renew credentials if they are not valid
		// until the max proxy time frame

		// Check to see if authorized

		// Check to see if the user's credentials are ok, includes checking if
		// the credentials are expired and if the proxy time is ok in regards to
		// the credentials time

		// create the proxy
	}
	
	private Date getProxyValid(ProxyValid valid){
		Calendar c = new GregorianCalendar();
		c.add(Calendar.HOUR_OF_DAY, valid.getHours());
		c.add(Calendar.MINUTE, valid.getMinutes());
		c.add(Calendar.SECOND, valid.getSeconds());
		return c.getTime();
	}

	private String getEmail(SAMLAssertion saml) {
		Iterator itr = saml.getStatements();
		while (itr.hasNext()) {
			Object o = itr.next();
			if (o instanceof SAMLAttributeStatement) {
				SAMLAttributeStatement att = (SAMLAttributeStatement) o;
				Iterator attItr = att.getAttributes();
				while (attItr.hasNext()) {
					SAMLAttribute a = (SAMLAttribute) attItr.next();
					if ((a.getNamespace().equals(EMAIL_NAMESPACE))
							&& (a.getName().equals(EMAIL_NAME))) {
						Iterator vals = a.getValues();
						while (vals.hasNext()) {
							return (String) vals.next();
						}
					}
				}
			}
		}

		return null;
	}

	private SAMLAuthenticationStatement getAuthenticationStatement(
			SAMLAssertion saml) throws InvalidAssertionFault {
		Iterator itr = saml.getStatements();
		SAMLAuthenticationStatement auth = null;
		while (itr.hasNext()) {
			Object o = itr.next();
			if (o instanceof SAMLAuthenticationStatement) {
				if (auth != null) {
					InvalidAssertionFault fault = new InvalidAssertionFault();
					fault
							.setFaultString("The assertion specified contained more that one authentication statement.");
					throw fault;
				}
				auth = (SAMLAuthenticationStatement) o;
			}
		}
		if (auth == null) {
			InvalidAssertionFault fault = new InvalidAssertionFault();
			fault
					.setFaultString("No authentication statement specified in the assertion provided.");
			throw fault;
		}
		return auth;
	}
}
