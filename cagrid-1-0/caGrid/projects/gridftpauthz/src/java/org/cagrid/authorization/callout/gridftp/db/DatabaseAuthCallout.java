package org.cagrid.authorization.callout.gridftp.db;
import java.net.MalformedURLException;
import java.util.logging.Level;

import org.cagrid.authorization.callout.gridftp.AbstractAuthCallout;
import org.cagrid.authorization.callout.gridftp.GridFTPTuple;
import org.cagrid.authorization.callout.gridftp.GridFTPOperation.Operation;



public class DatabaseAuthCallout extends AbstractAuthCallout {

	@Override
	public boolean authorizeOperation(String identity, Operation operation, String target) {

		boolean authorized = false;
		
		DBUtil util;
		try {
			String DB_NAME = "gridftp_authorization";
			String connectionString = "jdbc:hsqldb:hsql://localhost/" + DB_NAME;
			String dbuser = "sa";
			String password = "ch3ck1t@uth";

			util = new DBUtil(connectionString, dbuser, password);
			
			GridFTPTuple tuple = new GridFTPTuple(identity, operation, target);
			authorized = util.tupleExists(tuple);
		} catch (DatabaseException e) {
			String msg = "Could not check if tuple exists";
			_logger.log(Level.SEVERE, msg, e);
		} catch (MalformedURLException e) {
			String msg = "Bad URL: " + target;
			_logger.log(Level.SEVERE, msg, e);
		}

		return authorized;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DatabaseAuthCallout callout = new DatabaseAuthCallout();
		callout.authorize("/O=cagrid.org/OU=training/OU=caBIG User Group/OU=IdP [1]/CN=gridftp", "test2", "test3");

	}

}
