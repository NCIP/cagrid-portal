package gov.nih.nci.cagrid.gums.client;

import gov.nih.nci.cagrid.gums.IFSAdministration;
import gov.nih.nci.cagrid.gums.bean.GUMSInternalFault;
import gov.nih.nci.cagrid.gums.bean.PermissionDeniedFault;
import gov.nih.nci.cagrid.gums.common.FaultHelper;
import gov.nih.nci.cagrid.gums.common.FaultUtil;
import gov.nih.nci.cagrid.gums.common.GUMSFault;
import gov.nih.nci.cagrid.gums.common.IOUtils;
import gov.nih.nci.cagrid.gums.ifs.bean.IFSUser;
import gov.nih.nci.cagrid.gums.ifs.bean.IFSUserFilter;
import gov.nih.nci.cagrid.gums.ifs.bean.InvalidUserFault;
import gov.nih.nci.cagrid.gums.ifs.bean.TrustedIdP;
import gov.nih.nci.cagrid.gums.wsrf.GUMSPortType;
import gov.nih.nci.cagrid.gums.wsrf.IFSFindTrustedIdPs;
import gov.nih.nci.cagrid.security.commstyle.CommunicationStyle;


/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class IFSAdministrationClient extends GUMSBaseClient implements IFSAdministration {
	CommunicationStyle style;


	public IFSAdministrationClient(String serviceURI, CommunicationStyle style) {
		super(serviceURI);
		this.style = style;
	}


	public IFSUser renewUserCredentials(IFSUser usr) throws GUMSFault, PermissionDeniedFault, InvalidUserFault,
		GUMSInternalFault {
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
			return port.renewIFSUserCredentials(usr);
		} catch (GUMSInternalFault gie) {
			throw gie;
		} catch (PermissionDeniedFault f) {
			throw f;
		} catch (InvalidUserFault f) {
			throw f;
		} catch (Exception e) {
			FaultUtil.printFault(e);
			GUMSFault fault = new GUMSFault();
			fault.setFaultString(simplifyMessage(IOUtils.getExceptionMessage(e)));
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (GUMSFault) helper.getFault();
			throw fault;
		}
	}


	public TrustedIdP[] getTrustedIdPs() throws GUMSFault, PermissionDeniedFault, InvalidUserFault, GUMSInternalFault {

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
			return port.getTrustedIdPs(new IFSFindTrustedIdPs()).getIdps();
		} catch (GUMSInternalFault gie) {
			throw gie;
		} catch (PermissionDeniedFault f) {
			throw f;
		} catch (InvalidUserFault f) {
			throw f;
		} catch (Exception e) {
			FaultUtil.printFault(e);
			GUMSFault fault = new GUMSFault();
			fault.setFaultString(simplifyMessage(IOUtils.getExceptionMessage(e)));
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (GUMSFault) helper.getFault();
			throw fault;
		}
	}


	public IFSUser[] findUsers(IFSUserFilter filter) throws GUMSFault, PermissionDeniedFault, InvalidUserFault,
		GUMSInternalFault {

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
			return port.findIFSUsers(filter).getUsers();
		} catch (GUMSInternalFault gie) {
			throw gie;
		} catch (PermissionDeniedFault f) {
			throw f;
		} catch (InvalidUserFault f) {
			throw f;
		} catch (Exception e) {
			FaultUtil.printFault(e);
			GUMSFault fault = new GUMSFault();
			fault.setFaultString(simplifyMessage(IOUtils.getExceptionMessage(e)));
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (GUMSFault) helper.getFault();
			throw fault;
		}
	}


	public void removeUser(IFSUser usr) throws GUMSFault, PermissionDeniedFault, InvalidUserFault, GUMSInternalFault {

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
			port.removeIFSUser(usr);
		} catch (GUMSInternalFault gie) {
			throw gie;
		} catch (PermissionDeniedFault f) {
			throw f;
		} catch (InvalidUserFault f) {
			throw f;
		} catch (Exception e) {
			FaultUtil.printFault(e);
			GUMSFault fault = new GUMSFault();
			fault.setFaultString(simplifyMessage(IOUtils.getExceptionMessage(e)));
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (GUMSFault) helper.getFault();
			throw fault;
		}

	}


	public void updateUser(IFSUser usr) throws GUMSFault, PermissionDeniedFault, InvalidUserFault, GUMSInternalFault {
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
			port.updateIFSUser(usr);
		} catch (GUMSInternalFault gie) {
			throw gie;
		} catch (PermissionDeniedFault f) {
			throw f;
		} catch (InvalidUserFault f) {
			throw f;
		} catch (Exception e) {
			FaultUtil.printFault(e);
			GUMSFault fault = new GUMSFault();
			fault.setFaultString(simplifyMessage(IOUtils.getExceptionMessage(e)));
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (GUMSFault) helper.getFault();
			throw fault;
		}

	}

}
