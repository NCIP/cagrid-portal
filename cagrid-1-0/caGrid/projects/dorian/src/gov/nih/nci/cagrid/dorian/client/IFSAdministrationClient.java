package gov.nih.nci.cagrid.dorian.client;

import gov.nih.nci.cagrid.dorian.IFSAdministration;
import gov.nih.nci.cagrid.dorian.bean.DorianInternalFault;
import gov.nih.nci.cagrid.dorian.bean.PermissionDeniedFault;
import gov.nih.nci.cagrid.dorian.common.FaultHelper;
import gov.nih.nci.cagrid.dorian.common.FaultUtil;
import gov.nih.nci.cagrid.dorian.common.DorianFault;
import gov.nih.nci.cagrid.dorian.common.IOUtils;
import gov.nih.nci.cagrid.dorian.ifs.bean.IFSUser;
import gov.nih.nci.cagrid.dorian.ifs.bean.IFSUserFilter;
import gov.nih.nci.cagrid.dorian.ifs.bean.IFSUserPolicy;
import gov.nih.nci.cagrid.dorian.ifs.bean.InvalidTrustedIdPFault;
import gov.nih.nci.cagrid.dorian.ifs.bean.InvalidUserFault;
import gov.nih.nci.cagrid.dorian.ifs.bean.TrustedIdP;
import gov.nih.nci.cagrid.dorian.wsrf.DorianPortType;
import gov.nih.nci.cagrid.dorian.wsrf.IFSFindTrustedIdPs;
import gov.nih.nci.cagrid.dorian.wsrf.IFSGetUserPolicies;
import gov.nih.nci.cagrid.security.commstyle.CommunicationStyle;


/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class IFSAdministrationClient extends DorianBaseClient implements IFSAdministration {
	CommunicationStyle style;


	public IFSAdministrationClient(String serviceURI, CommunicationStyle style) {
		super(serviceURI);
		this.style = style;
	}


	public TrustedIdP addTrustedIdP(TrustedIdP idp) throws DorianFault, PermissionDeniedFault, InvalidUserFault,
		InvalidTrustedIdPFault, DorianInternalFault {
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
			return port.addTrustedIdP(idp);
		} catch (DorianInternalFault gie) {
			throw gie;
		} catch (PermissionDeniedFault f) {
			throw f;
		} catch (InvalidUserFault f) {
			throw f;
		} catch (InvalidTrustedIdPFault f) {
			throw f;
		} catch (Exception e) {
			FaultUtil.printFault(e);
			DorianFault fault = new DorianFault();
			fault.setFaultString(simplifyMessage(IOUtils.getExceptionMessage(e)));
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianFault) helper.getFault();
			throw fault;
		}
	}


	public void removeTrustedIdP(TrustedIdP idp) throws DorianFault, PermissionDeniedFault, InvalidUserFault,
		InvalidTrustedIdPFault, DorianInternalFault {
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
			port.removeTrustedIdP(idp);
		} catch (DorianInternalFault gie) {
			throw gie;
		} catch (PermissionDeniedFault f) {
			throw f;
		} catch (InvalidUserFault f) {
			throw f;
		} catch (InvalidTrustedIdPFault f) {
			throw f;
		} catch (Exception e) {
			FaultUtil.printFault(e);
			DorianFault fault = new DorianFault();
			fault.setFaultString(simplifyMessage(IOUtils.getExceptionMessage(e)));
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianFault) helper.getFault();
			throw fault;
		}

	}


	public void updateTrustedIdP(TrustedIdP idp) throws DorianFault, PermissionDeniedFault, InvalidUserFault,
		InvalidTrustedIdPFault, DorianInternalFault {
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
			port.updateTrustedIdP(idp);
		} catch (DorianInternalFault gie) {
			throw gie;
		} catch (PermissionDeniedFault f) {
			throw f;
		} catch (InvalidUserFault f) {
			throw f;
		} catch (InvalidTrustedIdPFault f) {
			throw f;
		} catch (Exception e) {
			FaultUtil.printFault(e);
			DorianFault fault = new DorianFault();
			fault.setFaultString(simplifyMessage(IOUtils.getExceptionMessage(e)));
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianFault) helper.getFault();
			throw fault;
		}

	}


	public IFSUserPolicy[] getUserPolicies() throws DorianFault, PermissionDeniedFault, InvalidUserFault,
		DorianInternalFault {
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
			return port.getIFSUserPolicies(new IFSGetUserPolicies()).getPolicies();
		} catch (DorianInternalFault gie) {
			throw gie;
		} catch (PermissionDeniedFault f) {
			throw f;
		} catch (InvalidUserFault f) {
			throw f;
		} catch (Exception e) {
			FaultUtil.printFault(e);
			DorianFault fault = new DorianFault();
			fault.setFaultString(simplifyMessage(IOUtils.getExceptionMessage(e)));
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianFault) helper.getFault();
			throw fault;
		}
	}


	public IFSUser renewUserCredentials(IFSUser usr) throws DorianFault, PermissionDeniedFault, InvalidUserFault,
		DorianInternalFault {
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
			return port.renewIFSUserCredentials(usr);
		} catch (DorianInternalFault gie) {
			throw gie;
		} catch (PermissionDeniedFault f) {
			throw f;
		} catch (InvalidUserFault f) {
			throw f;
		} catch (Exception e) {
			FaultUtil.printFault(e);
			DorianFault fault = new DorianFault();
			fault.setFaultString(simplifyMessage(IOUtils.getExceptionMessage(e)));
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianFault) helper.getFault();
			throw fault;
		}
	}


	public TrustedIdP[] getTrustedIdPs() throws DorianFault, PermissionDeniedFault, InvalidUserFault, DorianInternalFault {

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
			return port.getTrustedIdPs(new IFSFindTrustedIdPs()).getIdps();
		} catch (DorianInternalFault gie) {
			throw gie;
		} catch (PermissionDeniedFault f) {
			throw f;
		} catch (InvalidUserFault f) {
			throw f;
		} catch (Exception e) {
			FaultUtil.printFault(e);
			DorianFault fault = new DorianFault();
			fault.setFaultString(simplifyMessage(IOUtils.getExceptionMessage(e)));
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianFault) helper.getFault();
			throw fault;
		}
	}


	public IFSUser[] findUsers(IFSUserFilter filter) throws DorianFault, PermissionDeniedFault, InvalidUserFault,
		DorianInternalFault {

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
			return port.findIFSUsers(filter).getUsers();
		} catch (DorianInternalFault gie) {
			throw gie;
		} catch (PermissionDeniedFault f) {
			throw f;
		} catch (InvalidUserFault f) {
			throw f;
		} catch (Exception e) {
			FaultUtil.printFault(e);
			DorianFault fault = new DorianFault();
			fault.setFaultString(simplifyMessage(IOUtils.getExceptionMessage(e)));
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianFault) helper.getFault();
			throw fault;
		}
	}


	public void removeUser(IFSUser usr) throws DorianFault, PermissionDeniedFault, InvalidUserFault, DorianInternalFault {

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
			port.removeIFSUser(usr);
		} catch (DorianInternalFault gie) {
			throw gie;
		} catch (PermissionDeniedFault f) {
			throw f;
		} catch (InvalidUserFault f) {
			throw f;
		} catch (Exception e) {
			FaultUtil.printFault(e);
			DorianFault fault = new DorianFault();
			fault.setFaultString(simplifyMessage(IOUtils.getExceptionMessage(e)));
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianFault) helper.getFault();
			throw fault;
		}

	}


	public void updateUser(IFSUser usr) throws DorianFault, PermissionDeniedFault, InvalidUserFault, DorianInternalFault {
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
			port.updateIFSUser(usr);
		} catch (DorianInternalFault gie) {
			throw gie;
		} catch (PermissionDeniedFault f) {
			throw f;
		} catch (InvalidUserFault f) {
			throw f;
		} catch (Exception e) {
			FaultUtil.printFault(e);
			DorianFault fault = new DorianFault();
			fault.setFaultString(simplifyMessage(IOUtils.getExceptionMessage(e)));
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianFault) helper.getFault();
			throw fault;
		}

	}

}
