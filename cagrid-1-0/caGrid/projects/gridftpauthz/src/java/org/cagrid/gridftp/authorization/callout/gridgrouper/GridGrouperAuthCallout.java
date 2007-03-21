package org.cagrid.gridftp.authorization.callout.gridgrouper;

import gov.nih.nci.cagrid.gridgrouper.client.GridGrouperClientUtils;

import java.util.logging.Level;

import org.cagrid.gridftp.authorization.callout.AbstractAuthCallout;
import org.cagrid.gridftp.authorization.callout.GridFTPTuple;

/**
 * 
 * This authorization plugin implements a grid grouper check. Currently the check
 * is to verify the requester is a member of the training:trainees groups
 * in the training.cagrid.org Grid Grouper.
 * 
 * 
 * @author <A HREF="MAILTO:jpermar at bmi.osu.edu">Justin Permar</A>
 * 
 * @created Mar 20, 2007 
 * @version $Id: GridGrouperAuthCallout.java,v 1.1 2007-03-21 13:59:19 jpermar Exp $
 */
public class GridGrouperAuthCallout extends AbstractAuthCallout {

	@Override
	public boolean authorizeOperation(GridFTPTuple tuple) { //String identity, Operation operation, String target) {

		boolean authorized = false;
		
		// do grid grouper check
		String gridGrouperAuthorizeCheck = "<ns1:MembershipExpression ns1:logicRelation=\"AND\" xmlns:ns1=\"http://cagrid.nci.nih.gov/1/GridGrouper\">"
			+ " <ns1:MembershipQuery ns1:MembershipStatus=\"MEMBER_OF\">"
			+ "  <ns1:GroupIdentifier>"
			+ "   <ns1:gridGrouperURL>https://training.cagrid.org:8443/wsrf/services/cagrid/GridGrouper</ns1:gridGrouperURL>"
			+ "   <ns1:GroupName>training:trainees</ns1:GroupName>"
			+ "  </ns1:GroupIdentifier>"
			+ " </ns1:MembershipQuery>" + "</ns1:MembershipExpression>";

		_logger.fine("grid grouper check: " + gridGrouperAuthorizeCheck);
		if (tuple.getIdentity() != null) {
			_logger.fine("calling isMember()");
			try {
				authorized = GridGrouperClientUtils.isMember(gridGrouperAuthorizeCheck, tuple.getIdentity());
			} catch (Exception e) {
				_logger.log(Level.WARNING, "Grid grouper check threw exception due to reason: " + e.getMessage(), e);
			}
			_logger.fine("called isMember()");
			_logger.info("authorization check returned: " + authorized);
		}

		return authorized;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		GridGrouperAuthCallout callout = new GridGrouperAuthCallout();
		callout.authorize("/O=cagrid.org/OU=training/OU=caBIG User Group/OU=IdP [1]/CN=gridftp", "test2", "test3");

	}

}
