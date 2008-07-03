package org.cagrid.gaards.dorian.client;

import gov.nih.nci.cagrid.common.FaultHelper;
import gov.nih.nci.cagrid.common.FaultUtil;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.opensaml.SAMLAssertion;

import java.rmi.RemoteException;

import org.apache.axis.types.URI.MalformedURIException;
import org.cagrid.gaards.authentication.BasicAuthentication;
import org.cagrid.gaards.authentication.Credential;
import org.cagrid.gaards.authentication.faults.AuthenticationProviderFault;
import org.cagrid.gaards.authentication.faults.CredentialNotSupportedFault;
import org.cagrid.gaards.authentication.faults.InvalidCredentialFault;
import org.cagrid.gaards.dorian.common.DorianFault;
import org.cagrid.gaards.dorian.idp.Application;
import org.cagrid.gaards.dorian.stubs.types.DorianInternalFault;
import org.cagrid.gaards.dorian.stubs.types.InvalidUserPropertyFault;
import org.cagrid.gaards.dorian.stubs.types.PermissionDeniedFault;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class LocalUserClient {

	private DorianClient client;

	public LocalUserClient(String serviceURI) throws MalformedURIException,
			RemoteException {
		client = new DorianClient(serviceURI);
	}

	public boolean doesUserExist(String userId) throws DorianFault,
			DorianInternalFault {
		try {
			return client.doesLocalUserExist(userId);
		} catch (DorianInternalFault f) {
			throw f;
		} catch (Exception e) {
			FaultUtil.printFault(e);
			DorianFault fault = new DorianFault();
			fault.setFaultString(Utils.getExceptionMessage(e));
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianFault) helper.getFault();
			throw fault;
		}
	}

	public SAMLAssertion authenticate(Credential cred) throws DorianFault,
			AuthenticationProviderFault, InvalidCredentialFault,
			CredentialNotSupportedFault, AuthenticationProviderFault {

		try {
			return client.authenticateUser(cred);
		} catch (InvalidCredentialFault f) {
			throw f;
		} catch (CredentialNotSupportedFault f) {
			throw f;
		} catch (AuthenticationProviderFault f) {
			throw f;
		} catch (Exception e) {
			FaultUtil.printFault(e);
			DorianFault fault = new DorianFault();
			fault.setFaultString(Utils.getExceptionMessage(e));
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianFault) helper.getFault();
			throw fault;
		}
	}

	public void changePassword(BasicAuthentication cred, String newPassword)
			throws DorianFault, DorianInternalFault, PermissionDeniedFault,
			InvalidUserPropertyFault {
		try {
			client.changeLocalUserPassword(cred, newPassword);
		} catch (DorianInternalFault f) {
			throw f;
		} catch (PermissionDeniedFault f) {
			throw f;
		} catch (InvalidUserPropertyFault f) {
			throw f;
		} catch (Exception e) {
			FaultUtil.printFault(e);
			DorianFault fault = new DorianFault();
			fault.setFaultString(Utils.getExceptionMessage(e));
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianFault) helper.getFault();
			throw fault;
		}
	}
	
	public String register(Application a) throws DorianFault, DorianInternalFault, InvalidUserPropertyFault {
		try {
			return client.registerWithIdP(a);
		} catch (DorianInternalFault gie) {
			throw gie;
		} catch (InvalidUserPropertyFault f) {
			throw f;
		} catch (Exception e) {
			FaultUtil.printFault(e);
			DorianFault fault = new DorianFault();
			fault.setFaultString(Utils.getExceptionMessage(e));
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianFault) helper.getFault();
			throw fault;
		}
	}

}
