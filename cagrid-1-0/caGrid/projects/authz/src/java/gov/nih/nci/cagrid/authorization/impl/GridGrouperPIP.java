package gov.nih.nci.cagrid.authorization.impl;

import edu.internet2.middleware.grouper.GroupNotFoundException;
import edu.internet2.middleware.grouper.GrouperRuntimeException;
import gov.nih.nci.cagrid.gridgrouper.client.GridGrouper;
import gov.nih.nci.cagrid.gridgrouper.grouper.GroupI;
import gov.nih.nci.cagrid.gridgrouper.grouper.GrouperI;
import gov.nih.nci.cagrid.gridgrouper.grouper.StemI;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.security.auth.Subject;
import javax.xml.namespace.QName;
import javax.xml.rpc.handler.MessageContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.globus.wsrf.impl.security.authorization.exceptions.AttributeException;
import org.globus.wsrf.impl.security.authorization.exceptions.CloseException;
import org.globus.wsrf.impl.security.authorization.exceptions.InitializeException;
import org.globus.wsrf.security.SecurityManager;
import org.globus.wsrf.security.authorization.PDPConfig;

public class GridGrouperPIP implements org.globus.wsrf.security.authorization.PIP {
	
	private static Log logger = LogFactory.getLog(GridGrouperPIP.class.getName());

	private Set searchStems = new HashSet();

	private GrouperI grouper;

	public void collectAttributes(Subject subject, MessageContext context,
			QName operation) throws AttributeException {

		String identity = SecurityManager.getManager().getCaller();
		for (Iterator i = this.searchStems.iterator(); i.hasNext();) {
			String stemStr = (String) i.next();
			StemI stem = null;
			try {
				stem = this.grouper.findStem(stemStr);
			} catch (Exception ex) {
				throw new AttributeException("Error finding stem '" + stemStr
						+ "': " + ex.getMessage());
			}
			if (stem == null) {
				throw new AttributeException("Couldn't find stem '" + stemStr
						+ "'");
			}
			Set childGroups = stem.getChildGroups();
			for (Iterator j = childGroups.iterator(); j.hasNext();) {
				GroupI group = (GroupI) j.next();
				try {
					if (this.grouper.isMemberOf(identity, group.getName())) {
						subject.getPublicCredentials().add(group);
					}
				} catch (GrouperRuntimeException ex) {
					throw new AttributeException(
							"Error checking group membership: "
									+ ex.getMessage());
				} catch (GroupNotFoundException ex) {
					throw new AttributeException("Bad group '"
							+ group.getName() + "': " + ex.getMessage());
				}

			}

		}

	}

	public void close() throws CloseException {

	}

	public void initialize(PDPConfig config, String name, String id)
			throws InitializeException {
		
	}

	public GrouperI getGrouper() {
		return grouper;
	}

	public void setGrouper(GrouperI grouper) {
		this.grouper = grouper;
	}

	public Set getSearchStems() {
		return searchStems;
	}

	public void setSearchStems(Set searchStems) {
		this.searchStems = searchStems;
	}

}
