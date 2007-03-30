package org.cagrid.gridftp.authorization.plugin.gridgrouper;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.gridgrouper.bean.MembershipExpression;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.apache.axis.types.URI;
import org.cagrid.gridftp.authorization.plugin.GridFTPOperation;
import org.cagrid.gridftp.authorization.plugin.GridFTPTuple;
import org.cagrid.www._1.gridftpauthz.GridFTP_Grouper_Config;
import org.cagrid.www._1.gridftpauthz.Grouper_Config_Entry;
import org.cagrid.www._1.gridftpauthz.Grouper_Config_EntryAction;
import org.globus.wsrf.encoding.ObjectSerializer;
import org.globus.wsrf.encoding.SerializationException;


public class GridGrouperConfigurationManager {

	private static String WILDCARD = "*";

	private List<GrouperConfig> _config;

	public GridGrouperConfigurationManager(GridFTP_Grouper_Config xmlConfig) throws GridGrouperAuthorizationConfigurationException {
		Grouper_Config_Entry[] entries = xmlConfig.getEntry();
		try {
			validateConfig(entries);
		} catch (Exception e) {
			String msg = "Could not load grid grouper authorization configuration file due to: " + e.getMessage();
			throw new GridGrouperAuthorizationConfigurationException(msg, e);
		}
		
	}
	
	public GridGrouperConfigurationManager(String configFilePath) throws GridGrouperAuthorizationConfigurationException {
		try {
			GridFTP_Grouper_Config xmlConfig = (GridFTP_Grouper_Config) Utils.deserializeDocument(configFilePath,
				GridFTP_Grouper_Config.class);
			Grouper_Config_Entry[] entries = xmlConfig.getEntry();
			validateConfig(entries);
		} catch (Exception e) {
			String msg = "Could not load grid grouper authorization configuration file due to: " + e.getMessage();
			throw new GridGrouperAuthorizationConfigurationException(msg, e);
		}
	}
	/*
	public void setConfig(GridFTP_Grouper_Config xmlConfig) throws GridGrouperAuthorizationConfigurationException {
		Grouper_Config_Entry[] entries = xmlConfig.getEntry();
		try {
			validateConfig(entries);
		} catch (Exception e) {
			String msg = "Could not load grid grouper authorization configuration file due to: " + e.getMessage();
			throw new GridGrouperAuthorizationConfigurationException(msg, e);
		}

	}
	*/


	private void validateConfig(Grouper_Config_Entry[] entries) throws Exception {
		_config = new ArrayList<GrouperConfig>();

		// validate all entries
		for (Grouper_Config_Entry entry : entries) {
			// the action is valid since it must be valid according to the
			// schema,
			// which has an enum for the actions
			Grouper_Config_EntryAction action = entry.getAction();
			// action is essentially validated by the schema's enum
			// specification

			// the only part of the URI that we care about is the path. Simply
			// create a
			// GridFTPTuple for this identity, action, and URI
			URI uri = entry.getUri();
			String path = uri.getPath();
			// make sure no * in the path except possibly at the very end
			int index = path.indexOf(WILDCARD);
			if ((index != -1) && (index < path.length() - 1)) {
				// wildcard was found at a spot other than at the end
				// complain
				// TODO modify exception reason when the config type changes
				// (URI to path)
				throw new Exception("Invalid URI: " + uri + ". " + WILDCARD + " can only appear at the end of a URI");
			}

			// make sure path doesn't end in "/"
			// TODO Need to document that URIs use "/" only
			String pathSeparator = "/";
			if (path.endsWith(pathSeparator)) {
				throw new Exception("Invalid URI: " + uri + ". URI cannot end with " + pathSeparator);
			}

			// parse the grouper expression to be sure the XML is ok
			MembershipExpression exp = entry.getMembershipExpression();
			// MembershipExpression exp =
			// GridGrouperClientUtils.xmlToExpression(xmlExpression);

			// GrouperConfig config = new
			// GrouperConfig(GridFTPOperation.valueOf(action.getValue()),
			GrouperConfig config = new GrouperConfig(action.getValue(), uri.getPath(), exp);

			System.out.println(config);
			_config.add(config);

		}

	}

	/*
	public void loadConfig(String configFilePath) throws GridGrouperAuthorizationConfigurationException {

		try {
			GridFTP_Grouper_Config xmlConfig = (GridFTP_Grouper_Config) Utils.deserializeDocument(configFilePath,
				GridFTP_Grouper_Config.class);
			Grouper_Config_Entry[] entries = xmlConfig.getEntry();
			validateConfig(entries);
		} catch (Exception e) {
			String msg = "Could not load grid grouper authorization configuration file due to: " + e.getMessage();
			throw new GridGrouperAuthorizationConfigurationException(msg, e);
		}
	}
	*/


	/**
	 * Given a path, this method finds the grid grouper membership expression
	 * that best matches. That is, it finds the most specific rule from the
	 * config file that matches and returns the membership expression for it
	 * 
	 * @param tuple the GridFTPTuple representing the request. Note that
	 * identity is not used in the tuple, so that can be null. Furthermore,
	 * a tuple is used here since we only care about the path of the URL
	 * and not the complete URL (and GridFTPTuple only keeps the path from the
	 * URL)
	 * @return the grid grouper MembershipExpression that matched, or null if no
	 *         match was found
	 */
	//public MembershipExpression getMostSpecificMembershipQuery(GridFTPOperation action, String uri) {
	public MembershipExpression getMostSpecificMembershipQuery(GridFTPTuple tuple) {
		// TODO add in action to this
		MembershipExpression expression = null;
		//know that URL is not same as URI but here it doesn't matter since every URL is a URI
		String uri = tuple.getURL();
		int maxLength = -1;
		for (GrouperConfig config : _config) {
			// check that action matches this one
			if (operationMatches(tuple.getOperation(), config)) {
				
				// compare path to each of the paths from the configuration
				// note that for configured paths that end in WILDCARD, use the
				// parent path as the path to match against
				// 1. check if configured path ends in WILDCARD
				String configPath = config.get_path();

				String finalConfigPath = null;
				boolean directory = false;
				if (configPath.endsWith(WILDCARD)) {
					// 2. if so, use the path minus the WILDCARD
					int index = configPath.indexOf(WILDCARD);
					finalConfigPath = configPath.substring(0, index);
					directory = true;
				} else {
					// 3. if not, use the whole path
					finalConfigPath = configPath;
				}

				// TODO document that any URI like /my/test/dir is a file
				// TODO document that any URI like /my/test/dir/* is referring
				// to any file in /my/test/dir directory
				System.out.println("comparing " + uri + " against " + finalConfigPath);

				// check if the given path matches the configured path
				// this includes:
				// check that the configured path is a file or a directory
				// if directory then the directory of the given path must match
				// e.g., if configured path is /tmp/* and the given path is
				// /tmp/foo then the /tmp parts must match
				// e.g., if configured path is /tmp/* and the given path is
				// /usr/tmp/foo then no match
				// e.g., if configured path is /tmp/* and the given path is
				// /usr/foo then no match
				// test for match is that the configured path minus /* must
				// match the given path entirely
				// that is, remove the /* and then check that the paths match
				// (checking from the beginning)
				// if file then the entire given path must match the configured
				// path
				// e.g., if configured path is /tmp/foo then the only match is
				// for given path /tmp/foo (must match exactly)

				// 4. check if the configured path is contained within the given
				// path
				if (!directory) {
					// must match completely
					if (uri.equals(finalConfigPath)) {
						expression = config.get_expression();
						maxLength = uri.length();
					}
				} else {
					int length = finalConfigPath.length();
					if (uri.subSequence(0, length).equals(finalConfigPath)) {
						// 5. if it is, then that means this grid grouper
						// expression applies for the given path. add it to the
						// list
						System.out.println("matched");
						// the length of the match is equal to
						// finalConfigPath.length()
						// 1. get the length of the path that matched
						if (length > maxLength) {
							System.out.println("using new expression");
							// 2. if the length > max length then set new max to
							// this length and set expressionToUse as the
							// current expression
							// this is more specific match
							// 3. use the final expression as the grouper
							// expression for the isMemberOf check
							expression = config.get_expression();
							maxLength = length;
						}
					}
				}

				// 6. if it is not, then move on to next configured path
			}
		}

		return expression;
	}


	private boolean operationMatches(GridFTPOperation action, GrouperConfig config) {
		// check that this action matches the action in the config
		// either action matches exactly or the config has WILDCARD as its
		// action, in which case it matches
		if (config.get_operation().equals(WILDCARD)) {
			return true;
		} else {
			return action.name().equals(config.get_operation());
		}
	}


	public static String membershipExpressionToString(MembershipExpression exp) throws SerializationException {
		StringWriter writer = new StringWriter();
		// TODO can get this name MembershipExpression from the schema somehow?
		ObjectSerializer.serialize(writer, exp, new QName("MembershipExpression"));
		return writer.getBuffer().toString();
	}

}
