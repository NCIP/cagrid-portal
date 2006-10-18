package gov.nih.nci.cagrid.authorization.impl;

import gov.nih.nci.cagrid.authorization.GridAuthorizationManager;
import gov.nih.nci.cagrid.authorization.GridGroupName;
import gov.nih.nci.cagrid.gridgrouper.client.GridGrouper;
import gov.nih.nci.cagrid.gridgrouper.grouper.GrouperI;
import gov.nih.nci.security.AuthenticationManager;
import gov.nih.nci.security.AuthorizationManager;
import gov.nih.nci.security.authentication.CommonAuthenticationManager;
import gov.nih.nci.security.authorization.domainobjects.ApplicationContext;
import gov.nih.nci.security.authorization.domainobjects.Group;
import gov.nih.nci.security.authorization.domainobjects.ProtectionElement;
import gov.nih.nci.security.authorization.domainobjects.User;
import gov.nih.nci.security.authorization.jaas.AccessPermission;
import gov.nih.nci.security.exceptions.CSConfigurationException;
import gov.nih.nci.security.exceptions.CSException;
import gov.nih.nci.security.exceptions.CSInputException;
import gov.nih.nci.security.exceptions.CSInsufficientAttributesException;
import gov.nih.nci.security.exceptions.CSLoginException;
import gov.nih.nci.security.exceptions.CSObjectNotFoundException;
import gov.nih.nci.security.exceptions.CSTransactionException;
import gov.nih.nci.security.provisioning.UserProvisioningManagerImpl;

import java.net.URL;
import java.security.Principal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.security.auth.Subject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CSMGridAuthorizationManager implements GridAuthorizationManager,
		AuthorizationManager, AuthenticationManager {

	private static Log logger = LogFactory
			.getLog(CSMGridAuthorizationManager.class.getName());
	
	private Map gridGrouperMap = new HashMap();

	private GrouperI grouper;

	private AuthorizationManager authorizationManager;
	
	private AuthenticationManager authenticationManager;

	
	public CSMGridAuthorizationManager(){
		
	}
	
	public CSMGridAuthorizationManager(String appCtxName){
		initialize(appCtxName);
	}
	
	public AuthenticationManager getAuthenticationManager() {
		return authenticationManager;
	}

	public void setAuthenticationManager(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	public boolean isAuthorized(String identity, String objectId,
			String privilege) {
		
//		logger.debug("identity=" + identity + ", objectId=" + objectId + ", privilege=" + privilege);
		System.out.println("identity=" + identity + ", objectId=" + objectId + ", privilege=" + privilege);

		boolean isAuthorized = false;

		try {
			isAuthorized = this.authorizationManager.checkPermission(identity, objectId, privilege);
			if (!isAuthorized) { // then, check groups

				String groupName = null;

				List groups = getAccessibleGroups(objectId, privilege);
				
				if (groups != null) {
					//logger.debug("got " + groups.size() + " groups");
					System.out.println("got " + groups.size() + " groups");
					for (Iterator i = groups.iterator(); i.hasNext();) {
						Group group = (Group) i.next();
						String name = group.getGroupName();
						//logger.debug("Checking group '" + name + "'");
						System.out.println("Checking group '" + name + "'");
						if (GridGroupName.isGridGroupName(name)) {
//							logger.debug("is grid group name");
							System.out.println("is grid group name");
							GridGroupName gName = new GridGroupName(name);
							GrouperI client = getGridGrouper(gName.getUrl());
							if (client.isMemberOf(identity, gName.getName())) {
								groupName = name;
								break;
							}
						}else{
//							logger.debug("is NOT grid group name");
							System.out.println("is NOT grid group name");
						}
					}
				}else{
//					logger.debug("groups is null");
					System.out.println("groups is null");
				}
//				logger.debug("groupName=" + groupName);
				System.out.println("groupName=" + groupName);
				if (groupName != null) {
					isAuthorized = checkPermissionForGroup(groupName, objectId,
							privilege);
				}
			}
		} catch (Exception ex) {
			throw new RuntimeException("Error checking authorization: "
					+ ex.getMessage(), ex);
		}

		return isAuthorized;
	}

	private AuthorizationManager newAuthorizationManager() {
		AuthorizationManager mgr = null;
		try {
			mgr = new UserProvisioningManagerImpl(getApplicationContextName());
		} catch (CSConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mgr;
	}

	protected GrouperI getGridGrouper(String url) {
		GrouperI grouper = null;
		Map map = getGridGrouperMap();
		if(map == null){
			map = new HashMap();
			setGridGrouperMap(map);
		}
		grouper = (GrouperI) map.get(url);
		if(grouper == null){
			grouper = new GridGrouper(url);
			map.put(url, grouper);
		}
		return grouper;
	}

	public GrouperI getGrouper() {
		return grouper;
	}

	public void setGrouper(GrouperI grouper) {
		this.grouper = grouper;
	}

	public void assignProtectionElement(String arg0, String arg1)
			throws CSTransactionException {
		this.authorizationManager.assignProtectionElement(arg0, arg1);
	}

	public void assignProtectionElement(String arg0, String arg1, String arg2)
			throws CSTransactionException {
		this.authorizationManager.assignProtectionElement(arg0, arg1, arg2);
	}

	public boolean checkOwnership(String arg0, String arg1) {
		return this.authorizationManager.checkOwnership(arg0, arg1);
	}

	public boolean checkPermission(AccessPermission arg0, Subject arg1)
			throws CSException {
		return this.authorizationManager.checkPermission(arg0, arg1);
	}

	public boolean checkPermission(AccessPermission arg0, String arg1)
			throws CSException {
		return this.authorizationManager.checkPermission(arg0, arg1);
	}

	public boolean checkPermission(String arg0, String arg1, String arg2)
			throws CSException {
		return isAuthorized(arg0, arg1, arg2);
	}

	public boolean checkPermission(String arg0, String arg1, String arg2,
			String arg3) throws CSException {
		return this.authorizationManager
				.checkPermission(arg0, arg1, arg2, arg3);
	}

	public boolean checkPermissionForGroup(String group, String objectId, String privilege)
			throws CSException {
//		logger.debug("group=" + group + ", " + objectId + ", " + privilege);
		System.out.println("group=" + group + ", " + objectId + ", " + privilege);
		return this.authorizationManager.checkPermissionForGroup(group, objectId, privilege);
	}

	public boolean checkPermissionForGroup(String arg0, String arg1,
			String arg2, String arg3) throws CSException {
		return this.checkPermissionForGroup(arg0, arg1, arg2, arg3);
	}

	public void createProtectionElement(ProtectionElement arg0)
			throws CSTransactionException {
		this.authorizationManager.createProtectionElement(arg0);
	}

	public void deAssignProtectionElements(String arg0, String arg1)
			throws CSTransactionException {
		this.authorizationManager.deAssignProtectionElements(arg0, arg1);
	}

	public List getAccessibleGroups(String arg0, String arg1)
			throws CSException {
		return this.authorizationManager.getAccessibleGroups(arg0, arg1);
	}

	public List getAccessibleGroups(String arg0, String arg1, String arg2)
			throws CSException {
		return this.authorizationManager.getAccessibleGroups(arg0, arg1, arg2);
	}

	public ApplicationContext getApplicationContext() {
		return this.authorizationManager.getApplicationContext();
	}

	public Principal[] getPrincipals(String arg0) {
		return this.authorizationManager.getPrincipals(arg0);
	}

	public Collection getPrivilegeMap(String arg0, Collection arg1)
			throws CSException {
		return this.getPrivilegeMap(arg0, arg1);
	}

	public ProtectionElement getProtectionElement(String arg0)
			throws CSObjectNotFoundException {
		return this.authorizationManager.getProtectionElement(arg0);
	}

	public ProtectionElement getProtectionElement(String arg0, String arg1) {
		return this.authorizationManager.getProtectionElement(arg0, arg1);
	}

	public ProtectionElement getProtectionElementById(String arg0)
			throws CSObjectNotFoundException {
		return this.authorizationManager.getProtectionElementById(arg0);
	}

	public List getProtectionGroups() {
		return this.authorizationManager.getProtectionGroups();
	}

	public Set getProtectionGroups(String arg0)
			throws CSObjectNotFoundException {
		return this.authorizationManager.getProtectionGroups(arg0);
	}

	public User getUser(String arg0) {
		return this.authorizationManager.getUser(arg0);
	}

	public void initialize(String appCtxName) {

		if (this.authorizationManager == null) {
			try {
				this.authorizationManager = new UserProvisioningManagerImpl(
						appCtxName);
			} catch (Exception ex) {
				throw new RuntimeException("Error initializing: "
						+ ex.getMessage(), ex);
			}
			
		}
		this.authorizationManager.initialize(appCtxName);
		
		if(this.authenticationManager == null){
			try {
				this.authenticationManager = new CommonAuthenticationManager();
			} catch (Exception ex) {
				throw new RuntimeException("Error initializing: "
						+ ex.getMessage(), ex);
			}
		}
		this.authenticationManager.initialize(appCtxName);
	}


	public Collection secureCollection(String arg0, Collection arg1)
			throws CSException {
		return this.authorizationManager.secureCollection(arg0, arg1);
	}

	public Object secureObject(String arg0, Object arg1) throws CSException {
		return this.authorizationManager.secureObject(arg0, arg1);
	}

	public Object secureUpdate(String arg0, Object arg1, Object arg2)
			throws CSException {
		return this.authorizationManager.secureUpdate(arg0, arg1, arg2);
	}

	public void setAuditUserInfo(String arg0, String arg1) {
		this.authorizationManager.setAuditUserInfo(arg0, arg1);
	}

	public void setOwnerForProtectionElement(String arg0, String[] arg1)
			throws CSTransactionException {
		this.authorizationManager.setOwnerForProtectionElement(arg0, arg1);
	}

	public void setOwnerForProtectionElement(String arg0, String arg1,
			String arg2) throws CSTransactionException {
		this.authorizationManager
				.setOwnerForProtectionElement(arg0, arg1, arg2);
	}

	public AuthorizationManager getAuthorizationManager() {
		return authorizationManager;
	}

	public void setAuthorizationManager(
			AuthorizationManager authorizationManager) {
		this.authorizationManager = authorizationManager;
	}

	public Map getGridGrouperMap() {
		return gridGrouperMap;
	}

	public void setGridGrouperMap(Map gridGrouperMap) {
		this.gridGrouperMap = gridGrouperMap;
	}

	public Subject authenticate(String arg0, String arg1) throws CSException, CSLoginException, CSInputException, CSConfigurationException, CSInsufficientAttributesException {
		return this.authenticationManager.authenticate(arg0, arg1);
	}

	public String getApplicationContextName() {
		return this.authenticationManager.getApplicationContextName();
	}

	public Object getAuthenticatedObject() {
		return this.authenticationManager.getAuthenticatedObject();
	}

	public Subject getSubject() {
		return this.authenticationManager.getSubject();
	}

	public boolean login(String usr, String pwd) throws CSException, CSLoginException, CSInputException, CSConfigurationException {

		logger.debug("usr=" + usr + ", pwd=" + pwd);
		
		boolean authenticated = false;
		CSException toThrow = null;
		try{
			authenticated = this.authenticationManager.login(usr, pwd); 
		}catch(CSException ex){
			toThrow = ex;
		}
		if(!authenticated && isURL(pwd)){
			logger.debug("Checking grid identity...");
			authenticated = this.checkPermission(usr, pwd, "ACCESS");
			logger.debug("..." + authenticated);
		}
		if(!authenticated && toThrow != null){
			throw toThrow;
		}
		return authenticated;
	}

	private boolean isURL(String pwd) {
		boolean isUrl = false;
		try{
			new URL(pwd);
			isUrl = true;
		}catch(Exception ex){
			
		}
		return isUrl;
	}

	public void logout(String arg0) throws CSException {
		this.authenticationManager.logout(arg0);
	}

	public void setApplicationContextName(String arg0) {
		this.authenticationManager.setApplicationContextName(arg0);
	}
	
}
