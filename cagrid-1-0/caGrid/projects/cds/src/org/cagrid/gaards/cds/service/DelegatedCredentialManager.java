package org.cagrid.gaards.cds.service;

import gov.nih.nci.cagrid.common.FaultHelper;

import java.security.KeyPair;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cagrid.gaards.cds.common.DelegationPolicy;
import org.cagrid.gaards.cds.stubs.types.CDSInternalFault;
import org.cagrid.gaards.cds.stubs.types.InvalidPolicyFault;
import org.cagrid.tools.database.Database;

public class DelegatedCredentialManager {

	private final static String TABLE = "delegated_credentials";
	private final static String DELEGATION_ID = "DELEGATION_ID";
	private final static String GRID_IDENTITY = "GRID_IDENTITY";
	private final static String STATUS = "STATUS";
	private final static String POLICY_TYPE = "POLICY_TYPE";
	private final static String EXPIRATION = "EXPIRATION";

	private Database db;
	private boolean dbBuilt = false;
	private List<PolicyHandler> handlers;
	private Log log;
	private KeyManager keyManager;
	private ProxyPolicy proxyPolicy;

	public DelegatedCredentialManager(Database db, PropertyManager properties,
			KeyManager keyManager, List<PolicyHandler> policyHandlers,
			ProxyPolicy proxyPolicy) throws CDSInternalFault {
		this.db = db;
		this.log = LogFactory.getLog(this.getClass().getName());
		this.handlers = policyHandlers;
		this.proxyPolicy = proxyPolicy;
		String currentKeyManager = properties.getKeyManager();
		if ((currentKeyManager != null)
				&& (!currentKeyManager.equals(keyManager.getClass().getName()))) {
			CDSInternalFault f = new CDSInternalFault();
			f.setFaultString("The key manager cannot be changed from "
					+ currentKeyManager + " to "
					+ keyManager.getClass().getName() + ".");
			throw f;
		}
		this.keyManager = keyManager;
		if(currentKeyManager==null){
			properties.setKeyManager(this.keyManager.getClass().getName());
		}
	}

	public void delegateCredential(String callerGridIdentity,
			DelegationPolicy policy) throws CDSInternalFault,
			InvalidPolicyFault {
		this.buildDatabase();
		PolicyHandler handler = null;
		boolean handlerFound = false;
		for (int i = 0; i < handlers.size(); i++) {
			if (handlers.get(i).isSupported(policy)) {
				if (!handlerFound) {
					handler = handlers.get(i);
					handlerFound = true;
				} else {
					InvalidPolicyFault f = new InvalidPolicyFault();
					f
							.setFaultString("Multiple handlers found for handling the policy, "
									+ policy.getClass().getName()
									+ ", cannot decide which handler to employ.");
					throw f;
				}
			}
		}
		if (!handlerFound) {
			InvalidPolicyFault f = new InvalidPolicyFault();
			f.setFaultString("The policy " + policy.getClass().getName()
					+ " is not a supported policy.");
			throw f;
		}

		if (!this.proxyPolicy.isKeySizeSupported(policy.getKeyLength())) {
			InvalidPolicyFault f = new InvalidPolicyFault();
			f.setFaultString("Invalid key length specified.");
			throw f;
		}

		Connection c = null;
		long delegationId = -1;
		try {
			c = this.db.getConnection();

			PreparedStatement s = c.prepareStatement("INSERT INTO " + TABLE
					+ " SET " + GRID_IDENTITY + "= ?, " + POLICY_TYPE + "= ?, "
					+ STATUS + "= ?");
			s.setString(1, callerGridIdentity);
			s.setString(2, policy.getClass().getName());
			s.setString(3, "");
			s.execute();
			s.close();
			delegationId = db.getLastAutoId(c);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			try {
				this.delete(delegationId);
			} catch (Exception ex) {
				log.error(ex.getMessage(), ex);
			}
			CDSInternalFault f = new CDSInternalFault();
			f.setFaultString("Unexpected Database Error.");
			FaultHelper helper = new FaultHelper(f);
			helper.addFaultCause(e);
			f = (CDSInternalFault) helper.getFault();
			throw f;
		} finally {
			db.releaseConnection(c);
		}

		if (delegationId == -1) {
			try {
				this.delete(delegationId);
			} catch (Exception ex) {
				log.error(ex.getMessage(), ex);
			}
			CDSInternalFault f = new CDSInternalFault();
			f
					.setFaultString("An unexpected error occurred in delegating the credential, a delegation id could not be assigned.");
			throw f;
		}

		// Create and Store Key Pair.
		KeyPair keys = this.keyManager.createAndStoreKeyPair(String
				.valueOf(delegationId), policy.getKeyLength());

		// Store Policy

	}

	public void delete(long delegationId) throws CDSInternalFault {
		buildDatabase();
		Connection c = null;
		try {
			c = db.getConnection();
			PreparedStatement s = c.prepareStatement("DELETE FROM " + TABLE
					+ "  WHERE " + DELEGATION_ID + "= ?");
			s.setLong(1, delegationId);
			s.execute();
			s.close();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			CDSInternalFault f = new CDSInternalFault();
			f.setFaultString("Unexpected Database Error.");
			FaultHelper helper = new FaultHelper(f);
			helper.addFaultCause(e);
			f = (CDSInternalFault) helper.getFault();
			throw f;
		} finally {
			db.releaseConnection(c);
		}
	}

	public void clearDatabase() throws CDSInternalFault {
		buildDatabase();
		try {
			for (int i = 0; i < handlers.size(); i++) {
				this.handlers.get(i).removeAllStoredPolicies();
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
							+ " BIGINT, INDEX document_index (" + DELEGATION_ID
							+ "));";
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
