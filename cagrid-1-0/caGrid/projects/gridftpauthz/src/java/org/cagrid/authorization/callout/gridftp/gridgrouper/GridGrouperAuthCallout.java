package org.cagrid.authorization.callout.gridftp.gridgrouper;

import gov.nih.nci.cagrid.gridgrouper.client.GridGrouperClientUtils;

import java.util.logging.Level;

import org.cagrid.authorization.callout.gridftp.AbstractAuthCallout;
import org.cagrid.authorization.callout.gridftp.GridFTPOperation.Operation;


public class GridGrouperAuthCallout extends AbstractAuthCallout {

	@Override
	public boolean authorizeOperation(String identity, Operation operation, String target) {

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
		if (identity != null) {
			_logger.fine("calling isMember()");
			try {
				authorized = GridGrouperClientUtils.isMember(gridGrouperAuthorizeCheck, identity);
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
