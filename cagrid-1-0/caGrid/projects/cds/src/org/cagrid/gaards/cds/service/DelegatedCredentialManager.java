package org.cagrid.gaards.cds.service;

import gov.nih.nci.cagrid.common.FaultHelper;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cagrid.gaards.cds.common.DelegationPolicy;
import org.cagrid.gaards.cds.conf.CDSConfiguration;
import org.cagrid.gaards.cds.conf.PolicyHandlerConfiguration;
import org.cagrid.gaards.cds.conf.PolicyHandlerDescription;
import org.cagrid.gaards.cds.conf.PolicyHandlers;
import org.cagrid.gaards.cds.stubs.types.CDSInternalFault;
import org.cagrid.tools.database.Database;
import org.cagrid.tools.database.DatabaseException;
import org.cagrid.tools.events.EventManager;


public class DelegatedCredentialManager {

	private final static String TABLE = "delegated_credentials";
	private final static String DELEGATION_ID = "DELEGATION_ID";
	private final static String GRID_IDENTITY = "GRID_IDENTITY";
	private final static String STATUS = "STATUS";
	private final static String POLICY_TYPE = "POLICY_TYPE";
	private final static String EXPIRATION = "EXPIRATION";

	private Database db;
	private boolean dbBuilt = false;
	private CDSConfiguration conf;
	private EventManager eventManager;
	private Map<String, PolicyHandler> handlers;
	private Log log;


	public DelegatedCredentialManager(CDSConfiguration conf, EventManager eventManager, Database db)
		throws CDSInternalFault {
		this.db = db;
		this.conf = conf;
		this.log = LogFactory.getLog(this.getClass().getName());
		this.eventManager = eventManager;
		this.handlers = new HashMap<String, PolicyHandler>();
		PolicyHandlers ph = this.conf.getPolicyHandlers();
		if (ph != null) {
			PolicyHandlerDescription[] list = ph.getPolicyHandlerDescription();
			if (list != null) {
				for (int i = 0; i < list.length; i++) {
					try {
						Class[] types = new Class[]{PolicyHandlerConfiguration.class, Database.class};
						Constructor c = Class.forName(list[i].getClassName()).getConstructor(types);
						Object[] objs = new Object[]{list[i].getPolicyHandlerConfiguration(), db};
						PolicyHandler handler = (PolicyHandler) c.newInstance(objs);
						this.handlers.put(list[i].getPolicyClassName(), handler);
					} catch (Exception e) {
						log.error(e.getMessage(), e);
						CDSInternalFault f = new CDSInternalFault();
						f.setFaultString("Error loading the handler " + list[i].getClassName() + " for the policy "
							+ list[i].getPolicyClassName() + ".");
						FaultHelper helper = new FaultHelper(f);
						helper.addFaultCause(e);
						f = (CDSInternalFault) helper.getFault();
						throw f;
					}
				}
			}
		}
	}


	public void delegateCredential(String callerGridIdentity, DelegationPolicy policy) {

	}


	public void clearDatabase() throws DatabaseException {
		buildDatabase();
		try {
			Iterator<PolicyHandler> itr = this.handlers.values().iterator();
			while (itr.hasNext()) {
				itr.next().removeAllStoredPolicies();
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		db.update("DROP TABLE IF EXISTS " + TABLE);
		dbBuilt = false;
	}


	private void buildDatabase() throws DatabaseException {
		if (!dbBuilt) {
			if (!this.db.tableExists(TABLE)) {
				String trust = "CREATE TABLE " + TABLE + " (" + DELEGATION_ID
					+ " INT NOT NULL AUTO_INCREMENT PRIMARY KEY," + GRID_IDENTITY + " VARCHAR(255) NOT NULL,"
					+ POLICY_TYPE + " VARCHAR(255) NOT NULL," + STATUS + " VARCHAR(50) NOT NULL," + EXPIRATION
					+ " BIGINT NOT NULL, INDEX document_index (" + DELEGATION_ID + "));";
				db.update(trust);
			}
			dbBuilt = true;
		}
	}

}
