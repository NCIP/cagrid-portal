package org.cagrid.gaards.dorian.service.upgrader;


import org.cagrid.gaards.dorian.ca.DBCertificateAuthority;
import org.cagrid.gaards.dorian.ca.EracomCertificateAuthority;
import org.cagrid.gaards.dorian.ca.EracomWrappingCertificateAuthority;
import org.cagrid.gaards.dorian.service.PropertyManager;
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

			String caType = pm.getCertificateAuthorityType();
			String newCAType = null;
			if (caType.equals("DBCA")) {
				newCAType = DBCertificateAuthority.class.getName();
			} else if (caType.equals("Eracom")) {
				newCAType = EracomWrappingCertificateAuthority.class.getName();
			} else if (caType.equals("EracomHybrid")) {
				newCAType = EracomCertificateAuthority.class.getName();
			}
			if (newCAType != null) {
				if (!trialRun) {
					pm.setCertificateAuthorityType(newCAType);
				}
			} else {
				throw new Exception("Could not determine how to upgrade the Certificate Authority Type "+caType+".");
			}
		} else {
			if (!trialRun) {
				throw new Exception("Failed to run upgrader "
						+ getClass().getName()
						+ " the version of Dorian you are running is not "
						+ PropertyManager.DORIAN_VERSION_1_2 + ".");
			}
		}

		// TODO: Add code for moving the certificate authority property forward.
	}
}
