package org.cagrid.gridftp.authorization.plugin.gridgrouper;

import java.net.MalformedURLException;

import gov.nih.nci.cagrid.gridgrouper.bean.GroupIdentifier;
import gov.nih.nci.cagrid.gridgrouper.bean.LogicalOperator;
import gov.nih.nci.cagrid.gridgrouper.bean.MembershipExpression;
import gov.nih.nci.cagrid.gridgrouper.bean.MembershipQuery;
import gov.nih.nci.cagrid.gridgrouper.bean.MembershipStatus;

import org.apache.axis.types.URI;
import org.apache.axis.types.URI.MalformedURIException;
import org.cagrid.gridftp.authorization.plugin.GridFTPOperation;
import org.cagrid.gridftp.authorization.plugin.GridFTPTuple;
import org.cagrid.www._1.gridftpauthz.GridFTP_Grouper_Config;
import org.cagrid.www._1.gridftpauthz.Grouper_Config_Entry;
import org.cagrid.www._1.gridftpauthz.Grouper_Config_EntryAction;

import junit.framework.TestCase;


/**
 * This test case tests various configuration files against various GridFTP
 * requests, checking that the correct GridGrouper expression is used in each
 * scenario.
 * 
 * @author <A HREF="MAILTO:jpermar at bmi.osu.edu">Justin Permar</A>
 * @created Mar 30, 2007
 * @version $Id: GridGrouperConfigurationTestCase.java,v 1.1 2007-03-30 20:00:46 jpermar Exp $
 */
public class GridGrouperConfigurationTestCase extends TestCase {

	/**
	 * this tests the case where the GridFTP request doesn't match any
	 * configured rule
	 * 
	 * @throws MalformedURIException
	 * @throws GridGrouperAuthorizationConfigurationException
	 * @throws MalformedURLException
	 */
	public void testNoMatchFound() throws MalformedURIException, GridGrouperAuthorizationConfigurationException,
		MalformedURLException {
		Grouper_Config_Entry[] entries = new Grouper_Config_Entry[1];
		Grouper_Config_EntryAction action = Grouper_Config_EntryAction.fromString("*");
		GroupIdentifier groupIdentifier = new GroupIdentifier(
			"https://training.cagrid.org:8443/wsrf/services/cagrid/GridGrouper", "demo:groupz");
		MembershipQuery query = new MembershipQuery(groupIdentifier, MembershipStatus.MEMBER_OF);
		MembershipQuery[] queries = new MembershipQuery[]{query};
		MembershipExpression expression = new MembershipExpression(LogicalOperator.AND, new MembershipExpression[0],
			queries);
		URI uri = new URI("ftp://irondale/my/test/dir");
		entries[0] = new Grouper_Config_Entry(action, expression, uri);
		GridFTP_Grouper_Config config = new GridFTP_Grouper_Config(entries);

		GridGrouperConfigurationManager manager = new GridGrouperConfigurationManager(config);
		GridFTPOperation requestAction = GridFTPOperation.read;
		String requestURL = "ftp://irondale/usr";
		GridFTPTuple tuple = new GridFTPTuple(null, requestAction, requestURL);
		MembershipExpression result = manager.getMostSpecificMembershipQuery(tuple);
		assertNull(result);
	}


	/**
	 * This tests the case where a GridFTP request for a file matches a rule
	 * specifically for that file e.g., request for read /tmp/foo matches read
	 * /tmp/foo
	 * 
	 * @throws MalformedURIException
	 * @throws GridGrouperAuthorizationConfigurationException
	 * @throws MalformedURLException
	 */
	public void testMatchRuleForSpecificFile() throws MalformedURIException,
		GridGrouperAuthorizationConfigurationException, MalformedURLException {
		Grouper_Config_Entry[] entries = new Grouper_Config_Entry[1];
		Grouper_Config_EntryAction action = Grouper_Config_EntryAction.fromString("*");
		GroupIdentifier groupIdentifier = new GroupIdentifier(
			"https://training.cagrid.org:8443/wsrf/services/cagrid/GridGrouper", "demo:groupz");
		MembershipQuery query = new MembershipQuery(groupIdentifier, MembershipStatus.MEMBER_OF);
		MembershipQuery[] queries = new MembershipQuery[]{query};
		MembershipExpression expression = new MembershipExpression(LogicalOperator.AND, new MembershipExpression[0],
			queries);
		URI uri = new URI("ftp://irondale/my/test/file");
		entries[0] = new Grouper_Config_Entry(action, expression, uri);
		GridFTP_Grouper_Config config = new GridFTP_Grouper_Config(entries);

		GridGrouperConfigurationManager manager = new GridGrouperConfigurationManager(config);
		GridFTPOperation requestAction = GridFTPOperation.read;
		String requestURL = "ftp://irondale/my/test/file";
		GridFTPTuple tuple = new GridFTPTuple(null, requestAction, requestURL);
		MembershipExpression result = manager.getMostSpecificMembershipQuery(tuple);
		assertEquals(expression, result);
	}


	/**
	 * This tests the case where a GridFTP request for a directory matches a
	 * rule specifically for that directory e.g., request for lookup /tmp
	 * matches lookup /tmp/*
	 */
	public void testMatchRuleForSpecificDirectory() throws MalformedURIException,
		GridGrouperAuthorizationConfigurationException, MalformedURLException {
		Grouper_Config_Entry[] entries = new Grouper_Config_Entry[1];
		Grouper_Config_EntryAction action = Grouper_Config_EntryAction.fromString("*");
		GroupIdentifier groupIdentifier = new GroupIdentifier(
			"https://training.cagrid.org:8443/wsrf/services/cagrid/GridGrouper", "demo:groupz");
		MembershipQuery query = new MembershipQuery(groupIdentifier, MembershipStatus.MEMBER_OF);
		MembershipQuery[] queries = new MembershipQuery[]{query};
		MembershipExpression expression = new MembershipExpression(LogicalOperator.AND, new MembershipExpression[0],
			queries);
		URI uri = new URI("ftp://irondale/usr/local/*");
		entries[0] = new Grouper_Config_Entry(action, expression, uri);
		GridFTP_Grouper_Config config = new GridFTP_Grouper_Config(entries);

		GridGrouperConfigurationManager manager = new GridGrouperConfigurationManager(config);
		GridFTPOperation requestAction = GridFTPOperation.read;
		String requestURL = "ftp://irondale/usr/local/myfile";
		GridFTPTuple tuple = new GridFTPTuple(null, requestAction, requestURL);
		MembershipExpression result = manager.getMostSpecificMembershipQuery(tuple);
		assertEquals(expression, result);
	}


	/**
	 * This tests the case where a GridFTP request for a file matches a rule for
	 * a directory up from the file e.g., request for /tmp/foo matches rule /*
	 */
	public void testMatchRuleForGeneralDirectory() throws MalformedURIException,
		GridGrouperAuthorizationConfigurationException, MalformedURLException {
		Grouper_Config_Entry[] entries = new Grouper_Config_Entry[1];
		Grouper_Config_EntryAction action = Grouper_Config_EntryAction.fromString("*");
		GroupIdentifier groupIdentifier = new GroupIdentifier(
			"https://training.cagrid.org:8443/wsrf/services/cagrid/GridGrouper", "demo:groupz");
		MembershipQuery query = new MembershipQuery(groupIdentifier, MembershipStatus.MEMBER_OF);
		MembershipQuery[] queries = new MembershipQuery[]{query};
		MembershipExpression expression = new MembershipExpression(LogicalOperator.AND, new MembershipExpression[0],
			queries);
		URI uri = new URI("ftp://irondale/usr/*");
		entries[0] = new Grouper_Config_Entry(action, expression, uri);
		GridFTP_Grouper_Config config = new GridFTP_Grouper_Config(entries);

		GridGrouperConfigurationManager manager = new GridGrouperConfigurationManager(config);
		GridFTPOperation requestAction = GridFTPOperation.read;
		String requestURL = "ftp://irondale/usr/local/myfile";
		GridFTPTuple tuple = new GridFTPTuple(null, requestAction, requestURL);
		MembershipExpression result = manager.getMostSpecificMembershipQuery(tuple);
		assertEquals(expression, result);

	}


	/**
	 * This tests the case where a GridFTP request for a file doesn't match a
	 * rule for a file that is up from the requested file e.g., request for
	 * /tmp/foo doesn't match rule /tmp
	 */
	public void testNoMatchRuleForGeneralFile() throws MalformedURIException,
		GridGrouperAuthorizationConfigurationException, MalformedURLException {
		Grouper_Config_Entry[] entries = new Grouper_Config_Entry[1];
		Grouper_Config_EntryAction action = Grouper_Config_EntryAction.fromString("*");
		GroupIdentifier groupIdentifier = new GroupIdentifier(
			"https://training.cagrid.org:8443/wsrf/services/cagrid/GridGrouper", "demo:groupz");
		MembershipQuery query = new MembershipQuery(groupIdentifier, MembershipStatus.MEMBER_OF);
		MembershipQuery[] queries = new MembershipQuery[]{query};
		MembershipExpression expression = new MembershipExpression(LogicalOperator.AND, new MembershipExpression[0],
			queries);
		URI uri = new URI("ftp://irondale/foo");
		entries[0] = new Grouper_Config_Entry(action, expression, uri);
		GridFTP_Grouper_Config config = new GridFTP_Grouper_Config(entries);

		GridGrouperConfigurationManager manager = new GridGrouperConfigurationManager(config);
		GridFTPOperation requestAction = GridFTPOperation.read;
		String requestURL = "ftp://irondale/foo/bar";
		GridFTPTuple tuple = new GridFTPTuple(null, requestAction, requestURL);
		MembershipExpression result = manager.getMostSpecificMembershipQuery(tuple);
		assertNull(result);

	}


	/**
	 * This tests the cae where a specific file is requested that is conained
	 * within a directory that is up from the file e.g., request for read
	 * /usr/local/foo matches read /usr/*
	 */
	public void testMatchRuleForSpecificFileContainedWithinDirectory() throws MalformedURIException,
		GridGrouperAuthorizationConfigurationException, MalformedURLException {
		Grouper_Config_Entry[] entries = new Grouper_Config_Entry[1];
		Grouper_Config_EntryAction action = Grouper_Config_EntryAction.fromString("*");
		GroupIdentifier groupIdentifier = new GroupIdentifier(
			"https://training.cagrid.org:8443/wsrf/services/cagrid/GridGrouper", "demo:groupz");
		MembershipQuery query = new MembershipQuery(groupIdentifier, MembershipStatus.MEMBER_OF);
		MembershipQuery[] queries = new MembershipQuery[]{query};
		MembershipExpression expression = new MembershipExpression(LogicalOperator.AND, new MembershipExpression[0],
			queries);
		URI uri = new URI("ftp://irondale/usr/*");
		entries[0] = new Grouper_Config_Entry(action, expression, uri);
		GridFTP_Grouper_Config config = new GridFTP_Grouper_Config(entries);

		GridGrouperConfigurationManager manager = new GridGrouperConfigurationManager(config);
		GridFTPOperation requestAction = GridFTPOperation.read;
		String requestURL = "ftp://irondale/usr/local/foo";
		GridFTPTuple tuple = new GridFTPTuple(null, requestAction, requestURL);
		MembershipExpression result = manager.getMostSpecificMembershipQuery(tuple);
		assertEquals(expression, result);

	}


	public void testNoMatchForDifferingActionType() throws MalformedURIException,
		GridGrouperAuthorizationConfigurationException, MalformedURLException {
		Grouper_Config_Entry[] entries = new Grouper_Config_Entry[1];
		Grouper_Config_EntryAction action = Grouper_Config_EntryAction.fromString("write");
		GroupIdentifier groupIdentifier = new GroupIdentifier(
			"https://training.cagrid.org:8443/wsrf/services/cagrid/GridGrouper", "demo:groupz");
		MembershipQuery query = new MembershipQuery(groupIdentifier, MembershipStatus.MEMBER_OF);
		MembershipQuery[] queries = new MembershipQuery[]{query};
		MembershipExpression expression = new MembershipExpression(LogicalOperator.AND, new MembershipExpression[0],
			queries);
		URI uri = new URI("ftp://irondale/foo/*");
		entries[0] = new Grouper_Config_Entry(action, expression, uri);
		GridFTP_Grouper_Config config = new GridFTP_Grouper_Config(entries);

		GridGrouperConfigurationManager manager = new GridGrouperConfigurationManager(config);
		GridFTPOperation requestAction = GridFTPOperation.read;
		String requestURL = "ftp://irondale/foo/bar";
		GridFTPTuple tuple = new GridFTPTuple(null, requestAction, requestURL);
		MembershipExpression result = manager.getMostSpecificMembershipQuery(tuple);
		assertNull(result);

	}


	public void testInvalidRuleActionInvalid() throws MalformedURIException,
		GridGrouperAuthorizationConfigurationException, MalformedURLException {
		try {
			Grouper_Config_Entry[] entries = new Grouper_Config_Entry[1];
			String actionName = "yack";
			Grouper_Config_EntryAction action = Grouper_Config_EntryAction.fromString(actionName);
			GroupIdentifier groupIdentifier = new GroupIdentifier(
				"https://training.cagrid.org:8443/wsrf/services/cagrid/GridGrouper", "demo:groupz");
			MembershipQuery query = new MembershipQuery(groupIdentifier, MembershipStatus.MEMBER_OF);
			MembershipQuery[] queries = new MembershipQuery[]{query};
			MembershipExpression expression = new MembershipExpression(LogicalOperator.AND,
				new MembershipExpression[0], queries);
			URI uri = new URI("ftp://irondale/foo/*");
			entries[0] = new Grouper_Config_Entry(action, expression, uri);
			GridFTP_Grouper_Config config = new GridFTP_Grouper_Config(entries);

			GridGrouperConfigurationManager manager = new GridGrouperConfigurationManager(config);
			GridFTPOperation requestAction = GridFTPOperation.read;
			String requestURL = "ftp://irondale/foo/bar";
			GridFTPTuple tuple = new GridFTPTuple(null, requestAction, requestURL);
			MembershipExpression result = manager.getMostSpecificMembershipQuery(tuple);
			fail("Expected to fail due to invalid action " + actionName + " specified in config");
		} catch (Exception e) {
			assertTrue(true);
		}

	}


	public void testInvalidRuleInvalidURIBadFormatWildcardNotAtEnd() throws MalformedURIException,
		GridGrouperAuthorizationConfigurationException, MalformedURLException {
		try {
			Grouper_Config_Entry[] entries = new Grouper_Config_Entry[1];
			String actionName = "read";
			Grouper_Config_EntryAction action = Grouper_Config_EntryAction.fromString(actionName);
			GroupIdentifier groupIdentifier = new GroupIdentifier(
				"https://training.cagrid.org:8443/wsrf/services/cagrid/GridGrouper", "demo:groupz");
			MembershipQuery query = new MembershipQuery(groupIdentifier, MembershipStatus.MEMBER_OF);
			MembershipQuery[] queries = new MembershipQuery[]{query};
			MembershipExpression expression = new MembershipExpression(LogicalOperator.AND,
				new MembershipExpression[0], queries);
			URI uri = new URI("ftp://irondale/*foo/*");
			entries[0] = new Grouper_Config_Entry(action, expression, uri);
			GridFTP_Grouper_Config config = new GridFTP_Grouper_Config(entries);

			GridGrouperConfigurationManager manager = new GridGrouperConfigurationManager(config);
			GridFTPOperation requestAction = GridFTPOperation.read;
			String requestURL = "ftp://irondale/foo/bar";
			GridFTPTuple tuple = new GridFTPTuple(null, requestAction, requestURL);
			MembershipExpression result = manager.getMostSpecificMembershipQuery(tuple);
			fail("Expected to fail due to wildcard found in the middle of the URI: " + uri);
		} catch (Exception e) {
			assertTrue(true);
		}
	}


	public void testInvalidRuleInvalidURIBadFormatMultipleWildcardsAtEnd() throws MalformedURIException,
		GridGrouperAuthorizationConfigurationException, MalformedURLException {
		try {
			Grouper_Config_Entry[] entries = new Grouper_Config_Entry[1];
			String actionName = "read";
			Grouper_Config_EntryAction action = Grouper_Config_EntryAction.fromString(actionName);
			GroupIdentifier groupIdentifier = new GroupIdentifier(
				"https://training.cagrid.org:8443/wsrf/services/cagrid/GridGrouper", "demo:groupz");
			MembershipQuery query = new MembershipQuery(groupIdentifier, MembershipStatus.MEMBER_OF);
			MembershipQuery[] queries = new MembershipQuery[]{query};
			MembershipExpression expression = new MembershipExpression(LogicalOperator.AND,
				new MembershipExpression[0], queries);
			URI uri = new URI("ftp://irondale/foo/**");
			entries[0] = new Grouper_Config_Entry(action, expression, uri);
			GridFTP_Grouper_Config config = new GridFTP_Grouper_Config(entries);

			GridGrouperConfigurationManager manager = new GridGrouperConfigurationManager(config);
			GridFTPOperation requestAction = GridFTPOperation.read;
			String requestURL = "ftp://irondale/foo/bar";
			GridFTPTuple tuple = new GridFTPTuple(null, requestAction, requestURL);
			MembershipExpression result = manager.getMostSpecificMembershipQuery(tuple);
			fail("Expected to fail due to * at place other than end of the configured URI");
		} catch (Exception e) {
			assertTrue(true);
		}
	}


	public void testRuleOverrideOnlyGeneralMatches() throws MalformedURIException,
		GridGrouperAuthorizationConfigurationException, MalformedURLException {
		// entry 1
		String actionName = "read";
		Grouper_Config_EntryAction action = Grouper_Config_EntryAction.fromString(actionName);
		GroupIdentifier groupIdentifier = new GroupIdentifier(
			"https://training.cagrid.org:8443/wsrf/services/cagrid/GridGrouper", "demo:groupz");
		MembershipQuery query = new MembershipQuery(groupIdentifier, MembershipStatus.MEMBER_OF);
		MembershipQuery[] queries = new MembershipQuery[]{query};
		MembershipExpression expression1 = new MembershipExpression(LogicalOperator.AND, new MembershipExpression[0],
			queries);
		URI uri = new URI("ftp://irondale/tmp/*");
		Grouper_Config_Entry entry1 = new Grouper_Config_Entry(action, expression1, uri);

		// entry 2
		actionName = "read";
		action = Grouper_Config_EntryAction.fromString(actionName);
		groupIdentifier = new GroupIdentifier("https://training.cagrid.org:8443/wsrf/services/cagrid/GridGrouper",
			"demo:groupz");
		query = new MembershipQuery(groupIdentifier, MembershipStatus.MEMBER_OF);
		queries = new MembershipQuery[]{query};
		MembershipExpression expression2 = new MembershipExpression(LogicalOperator.AND, new MembershipExpression[0],
			queries);
		uri = new URI("ftp://irondale/tmp/my/test/file2");
		Grouper_Config_Entry entry2 = new Grouper_Config_Entry(action, expression2, uri);

		Grouper_Config_Entry[] entries = new Grouper_Config_Entry[]{entry1, entry2};

		GridFTP_Grouper_Config config = new GridFTP_Grouper_Config(entries);

		// request
		GridGrouperConfigurationManager manager = new GridGrouperConfigurationManager(config);
		GridFTPOperation requestAction = GridFTPOperation.read;
		String requestURL = "ftp://irondale/tmp/my/test/file";
		GridFTPTuple tuple = new GridFTPTuple(null, requestAction, requestURL);

		// check that only entry 1 matches
		MembershipExpression result = manager.getMostSpecificMembershipQuery(tuple);
		assertEquals(expression1, result);
	}


	public void testRuleOverrideOnlySpecificMatches() throws MalformedURIException,
		GridGrouperAuthorizationConfigurationException, MalformedURLException {
		// entry 1
		String actionName = "read";
		Grouper_Config_EntryAction action = Grouper_Config_EntryAction.fromString(actionName);
		GroupIdentifier groupIdentifier = new GroupIdentifier(
			"https://training.cagrid.org:8443/wsrf/services/cagrid/GridGrouper", "demo:groupz");
		MembershipQuery query = new MembershipQuery(groupIdentifier, MembershipStatus.MEMBER_OF);
		MembershipQuery[] queries = new MembershipQuery[]{query};
		MembershipExpression expression1 = new MembershipExpression(LogicalOperator.AND, new MembershipExpression[0],
			queries);
		URI uri = new URI("ftp://irondale/tmp/*");
		Grouper_Config_Entry entry1 = new Grouper_Config_Entry(action, expression1, uri);

		// entry 2
		actionName = "read";
		action = Grouper_Config_EntryAction.fromString(actionName);
		groupIdentifier = new GroupIdentifier("https://training.cagrid.org:8443/wsrf/services/cagrid/GridGrouper",
			"demo:groupz");
		query = new MembershipQuery(groupIdentifier, MembershipStatus.MEMBER_OF);
		queries = new MembershipQuery[]{query};
		MembershipExpression expression2 = new MembershipExpression(LogicalOperator.AND, new MembershipExpression[0],
			queries);
		uri = new URI("ftp://irondale/tmp/my/test/file2");
		Grouper_Config_Entry entry2 = new Grouper_Config_Entry(action, expression2, uri);

		Grouper_Config_Entry[] entries = new Grouper_Config_Entry[]{entry1, entry2};

		GridFTP_Grouper_Config config = new GridFTP_Grouper_Config(entries);

		// request
		GridGrouperConfigurationManager manager = new GridGrouperConfigurationManager(config);
		GridFTPOperation requestAction = GridFTPOperation.read;
		String requestURL = "ftp://irondale/tmp/my/test/file2";
		GridFTPTuple tuple = new GridFTPTuple(null, requestAction, requestURL);

		// check that only entry 1 matches
		MembershipExpression result = manager.getMostSpecificMembershipQuery(tuple);
		assertEquals(expression2, result);
	}

	public void testNoMatchWrongAction() throws MalformedURIException,
	GridGrouperAuthorizationConfigurationException, MalformedURLException {
	// entry 1
	String actionName = "write";
	Grouper_Config_EntryAction action = Grouper_Config_EntryAction.fromString(actionName);
	GroupIdentifier groupIdentifier = new GroupIdentifier(
		"https://training.cagrid.org:8443/wsrf/services/cagrid/GridGrouper", "demo:groupz");
	MembershipQuery query = new MembershipQuery(groupIdentifier, MembershipStatus.MEMBER_OF);
	MembershipQuery[] queries = new MembershipQuery[]{query};
	MembershipExpression expression1 = new MembershipExpression(LogicalOperator.AND, new MembershipExpression[0],
		queries);
	URI uri = new URI("ftp://irondale/tmp/*");
	Grouper_Config_Entry entry1 = new Grouper_Config_Entry(action, expression1, uri);

	// entry 2
	actionName = "write";
	action = Grouper_Config_EntryAction.fromString(actionName);
	groupIdentifier = new GroupIdentifier("https://training.cagrid.org:8443/wsrf/services/cagrid/GridGrouper",
		"demo:groupz");
	query = new MembershipQuery(groupIdentifier, MembershipStatus.MEMBER_OF);
	queries = new MembershipQuery[]{query};
	MembershipExpression expression2 = new MembershipExpression(LogicalOperator.AND, new MembershipExpression[0],
		queries);
	uri = new URI("ftp://irondale/tmp/my/test/file2");
	Grouper_Config_Entry entry2 = new Grouper_Config_Entry(action, expression2, uri);

	Grouper_Config_Entry[] entries = new Grouper_Config_Entry[]{entry1, entry2};

	GridFTP_Grouper_Config config = new GridFTP_Grouper_Config(entries);

	// request
	GridGrouperConfigurationManager manager = new GridGrouperConfigurationManager(config);
	GridFTPOperation requestAction = GridFTPOperation.read;
	String requestURL = "ftp://irondale/tmp/my/test/file2";
	GridFTPTuple tuple = new GridFTPTuple(null, requestAction, requestURL);

	// check that only entry 1 matches
	MembershipExpression result = manager.getMostSpecificMembershipQuery(tuple);
	assertNull(result);
}

}
