package gov.nih.nci.cagrid.gums.service;

import gov.nih.nci.cagrid.gums.common.GUMSInternalException;

import org.projectmobius.common.MobiusConfigurator;
import org.projectmobius.common.MobiusResourceManager;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @created Oct 14, 2004
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class GUMSManager extends MobiusResourceManager{
	
	private Database db;
	private RequiredAttributesManager userAttributeManager;
	
	public static final String GUMS_CONFIGURATION_RESOURCE="GUMSConfiguration";
	public static final String REQUIRED_USER_ATTRIBUTES = "REQUIRED_USER_ATTRIBUTES";
	
	public GUMSManager(String configFile) throws GUMSInternalException{
		try{
		MobiusConfigurator.parseMobiusConfiguration(configFile,this);
		}catch(Exception e){
			e.printStackTrace();
			throw new GUMSInternalException("An unexpected error occurred in configuring the service.");
		}	
		this.db = new Database(getJanusConfiguration().getConnectionManager(),getJanusConfiguration().getGUMSInternalId());
		this.db.createDatabaseIfNeeded();
		this.userAttributeManager = new RequiredAttributesManager(this.db,REQUIRED_USER_ATTRIBUTES);
	}
	
	public GUMSConfiguration getJanusConfiguration(){
		return (GUMSConfiguration)this.getResource(GUMS_CONFIGURATION_RESOURCE);
	}

	public Database getDatabase() {
		return this.db;
	}

	public RequiredAttributesManager getUserAttributeManager() {
		return userAttributeManager;
	}

	
	
}
