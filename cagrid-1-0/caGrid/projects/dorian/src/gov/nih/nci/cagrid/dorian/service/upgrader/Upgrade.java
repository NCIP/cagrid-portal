package gov.nih.nci.cagrid.dorian.service.upgrader;

import gov.nih.nci.cagrid.dorian.conf.DorianConfiguration;


public interface Upgrade {
	public void upgrade(DorianConfiguration conf, boolean trialRun) throws Exception;


	public float getStartingVersion();


	public float getUpgradedVersion();
}
