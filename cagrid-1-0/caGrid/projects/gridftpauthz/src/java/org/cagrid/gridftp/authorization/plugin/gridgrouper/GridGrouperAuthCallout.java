package org.cagrid.gridftp.authorization.plugin.gridgrouper;

import gov.nih.nci.cagrid.gridgrouper.bean.MembershipExpression;

import java.net.URL;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import org.cagrid.gridftp.authorization.plugin.AbstractAuthCallout;
import org.cagrid.gridftp.authorization.plugin.GridFTPTuple;
import org.globus.wsrf.encoding.SerializationException;

/**
 * 
 * This authorization plugin implements a grid grouper check. Currently the
 * check is to verify the requester is a member of the training:trainees groups
 * in the training.cagrid.org Grid Grouper.
 * 
 * 
 * @author <A HREF="MAILTO:jpermar at bmi.osu.edu">Justin Permar</A>
 * 
 * @created Mar 20, 2007
 * @version $Id: GridGrouperAuthCallout.java,v 1.1 2007/03/22 18:54:44 jpermar
 *          Exp $
 */
public class GridGrouperAuthCallout extends AbstractAuthCallout {

	/**
	 * The xml gridgrouper auth config file that must be specified on the
	 * classpath at this location.
	 */
	public static final String RESOURCE_LOCATION = "org/cagrid/gridftp/authorization/plugin/gridgrouper/gridgrouper_auth_config.xml";
	private GridGrouperConfigurationManager _manager;

	public GridGrouperAuthCallout() throws Exception {
		// TODO test this when it throws an exception with the java_callout in
		// GridFTP
		// TODO change the config from URI type to simply string. we just want
		// user to specify paths
		super();
		URL configResource = this.getClass().getClassLoader().getResource(
				RESOURCE_LOCATION);
		_manager = new GridGrouperConfigurationManager(configResource.getPath());
		//_manager.loadConfig(configResource.getPath());
	}

	@Override
	public boolean authorizeOperation(GridFTPTuple tuple) { // String identity,
															// Operation
															// operation, String
															// target) {

		boolean authorized = false;

		MembershipExpression gridGrouperExpression = _manager
				.getMostSpecificMembershipQuery(tuple);

		// do grid grouper check
		// String gridGrouperAuthorizeCheck =
		// _entries[0].getGrouper_expression();
		if (gridGrouperExpression != null) {
			try {
				String membershipExpression = GridGrouperConfigurationManager
						.membershipExpressionToString(gridGrouperExpression);
				_logger.fine("using grid grouper membership expression: "
						+ membershipExpression);

				if (tuple.getIdentity() != null) {
					_logger.fine("calling isMember()");
					try {

						/*
						 * String gridGrouperAuthorizeCheck = "<ns1:MembershipExpression
						 * ns1:logicRelation=\"AND\"
						 * xmlns:ns1=\"http://cagrid.nci.nih.gov/1/GridGrouper\">" + "
						 * <ns1:MembershipQuery
						 * ns1:MembershipStatus=\"MEMBER_OF\">" + "
						 * <ns1:GroupIdentifier>" + "
						 * <ns1:gridGrouperURL>https://training.cagrid.org:8443/wsrf/services/cagrid/GridGrouper</ns1:gridGrouperURL>" + "
						 * <ns1:GroupName>training:trainees</ns1:GroupName>" + "
						 * </ns1:GroupIdentifier>" + " </ns1:MembershipQuery>" + "</ns1:MembershipExpression>";
						 */
						// authorized =
						// GridGrouperClientUtils.isMember(gridGrouperExpression,
						// tuple.getIdentity());
						// authorized =
						// GridGrouperClientUtils.isMember(gridGrouperAuthorizeCheck,
						// tuple.getIdentity());
					} catch (Exception e) {
						_logger.log(Level.WARNING,
								"Grid grouper check threw exception due to reason: "
										+ e.getMessage(), e);
					}
					_logger.fine("called isMember()");
					_logger.info("authorization check returned: " + authorized);
				}
			} catch (SerializationException e1) {
				// this is really an impossible exception to ever
				// get in this situation
				// this is because we already parsed the expression from a file
				// and since we are just writing out the same expression there
				// can't possibly be an error. essentially ignore
				e1.printStackTrace();
			}
		} else {
			_logger
					.info("No grid grouper configuration rule matched the GridFTP request: "
							+ tuple);
		}

		return authorized;
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		GridGrouperAuthCallout callout = new GridGrouperAuthCallout();
		callout
				.authorize(
						"/O=cagrid.org/OU=training/OU=caBIG User Group/OU=IdP [1]/CN=gridftp",
						"write", "ftp://irondale/my/test/dir/a");
	}

}
