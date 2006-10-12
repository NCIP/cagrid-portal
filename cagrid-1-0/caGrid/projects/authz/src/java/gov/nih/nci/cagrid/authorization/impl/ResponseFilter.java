package gov.nih.nci.cagrid.authorization.impl;

import gov.nih.nci.cagrid.gridgrouper.grouper.GroupI;

import java.util.Iterator;
import java.util.Set;

import javax.security.auth.Subject;

import org.apache.axis.AxisFault;
import org.apache.axis.Message;
import org.apache.axis.MessageContext;
import org.apache.axis.handlers.BasicHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.globus.wsrf.security.SecurityManager;

public class ResponseFilter extends BasicHandler {

	static Log logger = LogFactory.getLog(ResponseFilter.class.getName());

	public void invoke(MessageContext context) throws AxisFault {

		String identity = SecurityManager.getManager().getCaller();
		Subject subject = (Subject) context
				.getProperty(org.globus.wsrf.impl.security.authentication.Constants.PEER_SUBJECT);
		if (subject == null) {
			logger.debug("No subject found in context.");
		} else {
			Set creds = subject.getPublicCredentials();
			for (Iterator i = creds.iterator(); i.hasNext();) {
				Object o = i.next();
				if (o instanceof GroupI) {
					GroupI group = (GroupI) o;
					logger.debug(identity + " has group " + group.getName());
				}
			}
		}

	}

}
