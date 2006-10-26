package gov.nih.nci.cagrid.data.cql.cacore;

import gov.nih.nci.cagrid.authorization.GridAuthorizationManager;
import gov.nih.nci.cagrid.authorization.impl.CSMGridAuthorizationManager;
import gov.nih.nci.cagrid.cqlquery.Association;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlquery.Object;

import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/** 
 *  CsmSecurityCheck
 *  Check for CSM security authorization
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Oct 26, 2006 
 * @version $Id$ 
 */
public class CsmSecurityCheck {

	public static final String CSM_CONFIG_PROPERTY = "gov.nih.nci.security.configFile";
	public static final String CSM_PRIVILEGE = "READ";

	public static synchronized boolean isAuthorized(String csmConfigFile, String callerId, String appserviceUrl, CQLQuery query) 
		throws RemoteException {
		
		try {
			if (csmConfigFile == null || csmConfigFile.trim().length() == 0) {
				throw new java.rmi.RemoteException("No CSM Configuration file was specified.");
			} else {
				// here's why its synchronized...
				System.setProperty(CSM_CONFIG_PROPERTY, csmConfigFile);
			}
			GridAuthorizationManager mgr = new CSMGridAuthorizationManager(appserviceUrl);
			List authObjects = new LinkedList();
			populateObjectsToAuthorize(query.getTarget(), authObjects);
			Iterator authObjectIter = authObjects.iterator();
			while (authObjectIter.hasNext()) {
				String objectName = (String) authObjectIter.next();
				// TODO: verify I can call this multiple times without problems
				if (!mgr.isAuthorized(callerId, objectName, CSM_PRIVILEGE)) {
					return false;
				}
			}
		} catch (Exception e) {
			throw new RemoteException("Error determining if caller is authorized to perform request", e);
		}		
		return true;
	}
	
	
	private static void populateObjectsToAuthorize(Object queryObject, List objects) {
		objects.add(queryObject.getName());
		if (queryObject.getAssociation() != null) {
			populateObjectsToAuthorize(queryObject.getAssociation(), objects);
		}
		if (queryObject.getGroup() != null && queryObject.getGroup().getAssociation() != null) {
			Association[] associations = queryObject.getGroup().getAssociation();
			for (int i = 0; i < associations.length; i++) {
				populateObjectsToAuthorize(associations[i], objects);
			}
		}
	}
}
