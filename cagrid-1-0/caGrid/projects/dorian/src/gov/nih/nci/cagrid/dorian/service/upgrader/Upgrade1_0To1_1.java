package gov.nih.nci.cagrid.dorian.service.upgrader;

import gov.nih.nci.cagrid.common.FaultHelper;
import gov.nih.nci.cagrid.dorian.conf.DorianConfiguration;
import gov.nih.nci.cagrid.dorian.service.Database;
import gov.nih.nci.cagrid.dorian.service.PropertyManager;
import gov.nih.nci.cagrid.dorian.service.ifs.Group;
import gov.nih.nci.cagrid.dorian.service.ifs.GroupManager;
import gov.nih.nci.cagrid.dorian.service.ifs.IFS;
import gov.nih.nci.cagrid.dorian.stubs.types.DorianInternalFault;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class Upgrade1_0To1_1 implements Upgrade {

	private Log log;


	public Upgrade1_0To1_1() {
		log = LogFactory.getLog(this.getClass().getName());
	}


	public float getStartingVersion() {
		return 1.0F;
	}


	public float getUpgradedVersion() {
		return 1.1F;
	}


	public void upgrade(DorianConfiguration conf, boolean trialRun) throws Exception {
		Database db = new Database(conf.getDatabase(), conf.getDorianInternalId());
		db.createDatabaseIfNeeded();
		PropertyManager properties = new PropertyManager(db);
		GroupManager groupManager = new GroupManager(db);
		Group administrators = null;
		if (!groupManager.groupExists(IFS.ADMINISTRATORS)) {
			log.info("Adding group " + IFS.ADMINISTRATORS + ".");
			if (!trialRun) {
				groupManager.addGroup(IFS.ADMINISTRATORS);
			}
			administrators = groupManager.getGroup(IFS.ADMINISTRATORS);
		} else {
			administrators = groupManager.getGroup(IFS.ADMINISTRATORS);
		}

		Connection c = null;
		try {
			// First get all the users who's accounts are disabled.
			c = db.getConnection();
			Statement s = c.createStatement();
			String sql = "select GID from ifs_users where ROLE='Administrator'";
			ResultSet rs = s.executeQuery(sql.toString());
			while (rs.next()) {
				String gid = rs.getString(1);
				log.info("Adding user " + gid + " to the " + IFS.ADMINISTRATORS + " group.");
				if (!trialRun) {
					administrators.addMember(gid);
				}
			}
			rs.close();
			s.close();
			log.info("Removing ROLE column from ifs_users table.");
			if (!trialRun) {
				Statement s2 = c.createStatement();
				s2.executeUpdate("ALTER TABLE ifs_users DROP ROLE");
				s2.close();
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			DorianInternalFault fault = new DorianInternalFault();
			fault.setFaultString("Unexpected Error, could not obtain a list of users");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianInternalFault) helper.getFault();
			throw fault;
		} finally {
			db.releaseConnection(c);
		}
		if (!trialRun) {
			properties.setVersion(getUpgradedVersion());
		}
	}

}
