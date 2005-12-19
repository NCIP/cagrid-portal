package gov.nih.nci.cagrid.dorian.client;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.dorian.IdPAdministration;
import gov.nih.nci.cagrid.dorian.bean.DorianInternalFault;
import gov.nih.nci.cagrid.dorian.bean.PermissionDeniedFault;
import gov.nih.nci.cagrid.dorian.common.DorianFault;
import gov.nih.nci.cagrid.dorian.common.FaultHelper;
import gov.nih.nci.cagrid.dorian.common.FaultUtil;
import gov.nih.nci.cagrid.dorian.idp.bean.IdPUser;
import gov.nih.nci.cagrid.dorian.idp.bean.IdPUserFilter;
import gov.nih.nci.cagrid.dorian.idp.bean.InvalidUserPropertyFault;
import gov.nih.nci.cagrid.dorian.idp.bean.NoSuchUserFault;
import gov.nih.nci.cagrid.dorian.wsrf.DorianPortType;
import gov.nih.nci.cagrid.security.commstyle.CommunicationStyle;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class IdPAdministrationClient extends DorianBaseClient implements
		IdPAdministration {

	CommunicationStyle style;
	public IdPAdministrationClient(String serviceURI, CommunicationStyle style) {
		super(serviceURI);
		this.style = style;
	}

	public IdPUser[] findUsers(IdPUserFilter filter) throws DorianFault,
			DorianInternalFault, PermissionDeniedFault {
		DorianPortType port = null;
		try {
			port = this.getPort(style);
		} catch (Exception e) {
			DorianFault fault = new DorianFault();
			fault.setFaultString(e.getMessage());
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianFault) helper.getFault();
			throw fault;
		}
		try {
			return port.findIdPUsers(filter).getIdpUser();
		} catch (DorianInternalFault gie) {
			throw gie;
		} catch (PermissionDeniedFault f) {
			throw f;
		}catch (Exception e) {
			FaultUtil.printFault(e);
			DorianFault fault = new DorianFault();
			fault.setFaultString(simplifyMessage(Utils.getExceptionMessage(e)));
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianFault) helper.getFault();
			throw fault;
		}
	}

	public void removeUser(String userId) throws DorianFault, DorianInternalFault,
			PermissionDeniedFault {
		DorianPortType port = null;
		try {
			port = this.getPort(style);
		} catch (Exception e) {
			DorianFault fault = new DorianFault();
			fault.setFaultString(e.getMessage());
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianFault) helper.getFault();
			throw fault;
		}
		try {
			port.removeIdPUser(userId);
		} catch (DorianInternalFault gie) {
			throw gie;
		} catch (PermissionDeniedFault f) {
			throw f;
		}catch (Exception e) {
			FaultUtil.printFault(e);
			DorianFault fault = new DorianFault();
			fault.setFaultString(simplifyMessage(Utils.getExceptionMessage(e)));
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianFault) helper.getFault();
			throw fault;
		}

	}

	public void updateUser(IdPUser u) throws DorianFault, DorianInternalFault,
			PermissionDeniedFault, NoSuchUserFault, InvalidUserPropertyFault {
		DorianPortType port = null;
		try {
			port = this.getPort(style);
		} catch (Exception e) {
			DorianFault fault = new DorianFault();
			fault.setFaultString(e.getMessage());
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianFault) helper.getFault();
			throw fault;
		}
		try {
			port.updateIdPUser(u);
		} catch (DorianInternalFault gie) {
			throw gie;
		} catch (PermissionDeniedFault f) {
			throw f;
		} catch (NoSuchUserFault f) {
			throw f;
		} catch (InvalidUserPropertyFault f) {
			throw f;
		}catch (Exception e) {
			FaultUtil.printFault(e);
			DorianFault fault = new DorianFault();
			fault.setFaultString(simplifyMessage(Utils.getExceptionMessage(e)));
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianFault) helper.getFault();
			throw fault;
		}

	}
}
