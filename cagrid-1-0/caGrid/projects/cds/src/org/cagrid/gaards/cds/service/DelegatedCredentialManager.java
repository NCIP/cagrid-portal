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
import org.cagrid.gaards.cds.conf.KeyManagerDescription;
import org.cagrid.gaards.cds.conf.PolicyHandlerConfiguration;
import org.cagrid.gaards.cds.conf.PolicyHandlerDescription;
import org.cagrid.gaards.cds.conf.PolicyHandlers;
import org.cagrid.gaards.cds.stubs.types.CDSInternalFault;
import org.cagrid.gaards.cds.stubs.types.InvalidPolicyFault;
import org.cagrid.tools.database.Database;
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
	private KeyManager keyManager;

	public DelegatedCredentialManager(CDSConfiguration conf,
			EventManager eventManager, Database db) throws CDSInternalFault {
		this.db = db;
		this.conf = conf;
		this.log = LogFactory.getLog(this.getClass().getName());
		this.eventManager = eventManager;
		this.handlers = new HashMap<String, PolicyHandler>();
		PolicyHandlers ph = this.conf.getPolicyHandlers();
		
		//TODO: LOCK TO SINGLE KEY MANAGER
		KeyManagerDescription des = conf.getKeyManagerDescription();
		try {
			Class kmc = Class.forName(des.getClassName());
			Constructor con = kmc.getConstructor(new Class[] {
					KeyManagerDescription.class, Database.class });
			this.keyManager = (KeyManager) con.newInstance(new Object[] { des,
					this.db });
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			CDSInternalFault f = new CDSInternalFault();
			f.setFaultString("Error instantiating the key manager "
					+ des.getClassName() + ".");
			FaultHelper helper = new FaultHelper(f);
			helper.addFaultCause(e);
			f = (CDSInternalFault) helper.getFault();
			throw f;
		}
		if (ph != null) {
			PolicyHandlerDescription[] list = ph.getPolicyHandlerDescription();
			if (list != null) {
				for (int i = 0; i < list.length; i++) {
					try {
						Class[] types = new Class[] {
								PolicyHandlerConfiguration.class,
								Database.class };
						Constructor c = Class.forName(list[i].getClassName())
								.getConstructor(types);
						Object[] objs = new Object[] {
								list[i].getPolicyHandlerConfiguration(), db };
						PolicyHandler handler = (PolicyHandler) c
								.newInstance(objs);
						this.handlers
								.put(list[i].getPolicyClassName(), handler);
					} catch (Exception e) {
						log.error(e.getMessage(), e);
						CDSInternalFault f = new CDSInternalFault();
						f.setFaultString("Error loading the handler "
								+ list[i].getClassName() + " for the policy "
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

	public void delegateCredential(String callerGridIdentity,
			DelegationPolicy policy) throws CDSInternalFault,
			InvalidPolicyFault {
		this.buildDatabase();
		String policyType = policy.getClass().getName();
		if (!this.handlers.containsKey(policyType)) {
			InvalidPolicyFault f = new InvalidPolicyFault();
			f.setFaultString("The policy " + policyType
					+ " is not a supported policy.");
			throw f;
		}

		if ((policy.getKeyLength() < conf.getProxyPolicy().getKeyLength()
				.getMin())
				|| (policy.getKeyLength() > conf.getProxyPolicy()
						.getKeyLength().getMax())) {
			InvalidPolicyFault f = new InvalidPolicyFault();
			f.setFaultString("Invalid key length specified.");
			throw f;
		}
		
		

	}

	public void clearDatabase() throws CDSInternalFault {
		buildDatabase();
		try {
			Iterator<PolicyHandler> itr = this.handlers.values().iterator();
			while (itr.hasNext()) {
				itr.next().removeAllStoredPolicies();
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		this.keyManager.deleteAll();
		try {
			db.update("DELETE FROM " + TABLE);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			CDSInternalFault f = new CDSInternalFault();
			f.setFaultString("Unexpected Database Error.");
			FaultHelper helper = new FaultHelper(f);
			helper.addFaultCause(e);
			f = (CDSInternalFault) helper.getFault();
			throw f;
		}
		dbBuilt = false;

	}

	private void buildDatabase() throws CDSInternalFault {
		if (!dbBuilt) {
			try {
				if (!this.db.tableExists(TABLE)) {
					String trust = "CREATE TABLE " + TABLE + " ("
							+ DELEGATION_ID
							+ " BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,"
							+ GRID_IDENTITY + " VARCHAR(255) NOT NULL,"
							+ POLICY_TYPE + " VARCHAR(255) NOT NULL," + STATUS
							+ " VARCHAR(50) NOT NULL," + EXPIRATION
							+ " BIGINT NOT NULL, INDEX document_index ("
							+ DELEGATION_ID + "));";
					db.update(trust);
				}

				dbBuilt = true;
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				CDSInternalFault f = new CDSInternalFault();
				f.setFaultString("Unexpected Database Error.");
				FaultHelper helper = new FaultHelper(f);
				helper.addFaultCause(e);
				f = (CDSInternalFault) helper.getFault();
				throw f;
			}
		}
	}

}
