package gov.nih.nci.cagrid.dorian.service.upgrader;

import org.cagrid.tools.database.Database;

import gov.nih.nci.cagrid.dorian.conf.DorianConfiguration;
import gov.nih.nci.cagrid.dorian.service.PropertyManager;

public class Upgrade1_1To1_2 implements Upgrade {

	public float getStartingVersion() {
		return 1.1F;
	}

	public float getUpgradedVersion() {
		return 1.2F;
	}

	public void upgrade(DorianConfiguration conf, boolean trialRun)
			throws Exception {
		Database db = new Database(conf.getDatabaseConfiguration(), conf
				.getDorianInternalId());
		db.createDatabaseIfNeeded();
		PropertyManager pm = new PropertyManager(db);
		if (PropertyManager.CURRENT_VERSION == PropertyManager.DORIAN_VERSION_1_2) {
			if (pm.getVersion() == PropertyManager.DORIAN_VERSION_1_1) {
				if (!trialRun) {
					pm.setVersion(PropertyManager.DORIAN_VERSION_1_2);
				}
			}
		}
	}
}
