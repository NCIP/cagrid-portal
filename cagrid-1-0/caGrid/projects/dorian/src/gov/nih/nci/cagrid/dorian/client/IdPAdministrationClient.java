package gov.nih.nci.cagrid.gums.client;

import gov.nih.nci.cagrid.gums.IdPAdministration;
import gov.nih.nci.cagrid.gums.bean.GUMSInternalFault;
import gov.nih.nci.cagrid.gums.bean.PermissionDeniedFault;
import gov.nih.nci.cagrid.gums.common.FaultHelper;
import gov.nih.nci.cagrid.gums.common.FaultUtil;
import gov.nih.nci.cagrid.gums.common.GUMSFault;
import gov.nih.nci.cagrid.gums.idp.bean.IdPUser;
import gov.nih.nci.cagrid.gums.idp.bean.IdPUserFilter;
import gov.nih.nci.cagrid.gums.idp.bean.InvalidUserPropertyFault;
import gov.nih.nci.cagrid.gums.idp.bean.NoSuchUserFault;
import gov.nih.nci.cagrid.gums.wsrf.GUMSPortType;
import gov.nih.nci.cagrid.security.commstyle.CommunicationStyle;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class IdPAdministrationClient extends GUMSBaseClient implements
		IdPAdministration {

	CommunicationStyle style;
	public IdPAdministrationClient(String serviceURI, CommunicationStyle style) {
		super(serviceURI);
		this.style = style;
	}

	public IdPUser[] findUsers(IdPUserFilter filter) throws GUMSFault,
			GUMSInternalFault, PermissionDeniedFault {
		GUMSPortType port = null;
		try {
			port = this.getPort(style);
		} catch (Exception e) {
			GUMSFault fault = new GUMSFault();
			fault.setFaultString(e.getMessage());
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (GUMSFault) helper.getFault();
			throw fault;
		}
		try {
			return port.findIdPUsers(filter).getIdpUser();
		} catch (GUMSInternalFault gie) {
			throw gie;
		} catch (PermissionDeniedFault f) {
			throw f;
		} catch (Exception e) {
			FaultUtil.printFault(e);
			GUMSFault fault = new GUMSFault();
			fault.setFaultString(e.getMessage());
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (GUMSFault) helper.getFault();
			throw fault;
		}
	}

	public void removeUser(String userId) throws GUMSFault, GUMSInternalFault,
			PermissionDeniedFault {
		GUMSPortType port = null;
		try {
			port = this.getPort(style);
		} catch (Exception e) {
			GUMSFault fault = new GUMSFault();
			fault.setFaultString(e.getMessage());
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (GUMSFault) helper.getFault();
			throw fault;
		}
		try {
			port.removeIdPUser(userId);
		} catch (GUMSInternalFault gie) {
			throw gie;
		} catch (PermissionDeniedFault f) {
			throw f;
		} catch (Exception e) {
			GUMSFault fault = new GUMSFault();
			fault.setFaultString(e.getMessage());
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (GUMSFault) helper.getFault();
			throw fault;
		}

	}

	public void updateUser(IdPUser u) throws GUMSFault, GUMSInternalFault,
			PermissionDeniedFault, NoSuchUserFault, InvalidUserPropertyFault {
		GUMSPortType port = null;
		try {
			port = this.getPort(style);
		} catch (Exception e) {
			GUMSFault fault = new GUMSFault();
			fault.setFaultString(e.getMessage());
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (GUMSFault) helper.getFault();
			throw fault;
		}
		try {
			port.updateIdPUser(u);
		} catch (GUMSInternalFault gie) {
			throw gie;
		} catch (PermissionDeniedFault f) {
			throw f;
		} catch (NoSuchUserFault f) {
			throw f;
		} catch (InvalidUserPropertyFault f) {
			throw f;
		} catch (Exception e) {
			GUMSFault fault = new GUMSFault();
			fault.setFaultString(e.getMessage());
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (GUMSFault) helper.getFault();
			throw fault;
		}

	}
}
