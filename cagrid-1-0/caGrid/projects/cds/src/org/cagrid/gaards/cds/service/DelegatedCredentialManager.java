package org.cagrid.gaards.cds.service;

import org.cagrid.gaards.cds.common.DelegationPolicy;
import org.cagrid.tools.database.Database;
import org.cagrid.tools.database.DatabaseException;


public class DelegatedCredentialManager {

	private final static String TABLE = "delegated_credentials";
	private final static String DELEGATION_ID = "DELEGATION_ID";
	private final static String GRID_IDENTITY = "GRID_IDENTITY";
	private final static String STATUS = "STATUS";
	private final static String POLICY_TYPE = "POLICY_TYPE";
	private final static String EXPIRATION = "EXPIRATION";

	private Database db;
	private boolean dbBuilt = false;


	public DelegatedCredentialManager(Database db) {
		this.db = db;
	}


	public void delegateCredential(String callerGridIdentity, DelegationPolicy policy) {

	}


	public void clearDatabase() throws DatabaseException {
		buildDatabase();
		db.update("DROP TABLE IF EXISTS " + TABLE);
		dbBuilt = false;
	}


	private void buildDatabase() throws DatabaseException {
		if (!dbBuilt) {
			if (!this.db.tableExists(TABLE)) {
				String trust = "CREATE TABLE " + TABLE + " (" + DELEGATION_ID
					+ " INT NOT NULL AUTO_INCREMENT PRIMARY KEY," + GRID_IDENTITY + " VARCHAR(255) NOT NULL,"
					+ POLICY_TYPE + " VARCHAR(255) NOT NULL," + STATUS + " VARCHAR(50) NOT NULL," + EXPIRATION
					+ " BIGINT NOT NULL" + "INDEX document_index (" + DELEGATION_ID + "));";
				db.update(trust);
			}
			dbBuilt = true;
		}
	}

}
