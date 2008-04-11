package gov.nih.nci.cagrid.dorian.service.upgrader;

import gov.nih.nci.cagrid.dorian.service.PropertyManager;

import org.cagrid.tools.database.Database;

public class Upgrade1_2To1_3 extends Upgrade {

	public String getStartingVersion() {
		return PropertyManager.DORIAN_VERSION_1_2;
	}

	public String getUpgradedVersion() {
		return PropertyManager.DORIAN_VERSION_1_3;
	}

	public void upgrade(boolean trialRun) throws Exception {
		Database db = getBeanUtils().getDatabase();
		db.createDatabaseIfNeeded();
		PropertyManager pm = new PropertyManager(db);
		if (pm.getVersion().equals(PropertyManager.DORIAN_VERSION_1_2)) {
			if (!trialRun) {
				pm.setVersion(PropertyManager.DORIAN_VERSION_1_3);
			}
		} else {
			if (!trialRun) {
				throw new Exception("Failed to run upgrader "
						+ getClass().getName()
						+ " the version of Dorian you are running is not "
						+ PropertyManager.DORIAN_VERSION_1_2 + ".");
			} 
		}
		
		//TODO: Add code for moving the certificate authority property forward.
	}
}
