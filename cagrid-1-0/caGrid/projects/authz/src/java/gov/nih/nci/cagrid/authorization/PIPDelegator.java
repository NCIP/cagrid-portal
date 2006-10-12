package gov.nih.nci.cagrid.authorization;

import gov.nih.nci.cagrid.authorization.impl.AuthzUtils;

import javax.security.auth.Subject;
import javax.xml.namespace.QName;
import javax.xml.rpc.handler.MessageContext;

import org.globus.wsrf.impl.security.authorization.exceptions.AttributeException;
import org.globus.wsrf.impl.security.authorization.exceptions.CloseException;
import org.globus.wsrf.impl.security.authorization.exceptions.InitializeException;
import org.globus.wsrf.security.authorization.PDPConfig;
import org.globus.wsrf.security.authorization.PIP;

public class PIPDelegator implements PIP {
	
	private PIP pip;

	public void collectAttributes(Subject subject, MessageContext context, QName operation)
			throws AttributeException {
		this.pip.collectAttributes(subject, context, operation);
	}

	public void close() throws CloseException {
		this.pip.close();
	}

	public void initialize(PDPConfig config, String pipName, String id)
			throws InitializeException {
		this.pip = (PIP)AuthzUtils.getBean(config, pipName, id);
	}

}
