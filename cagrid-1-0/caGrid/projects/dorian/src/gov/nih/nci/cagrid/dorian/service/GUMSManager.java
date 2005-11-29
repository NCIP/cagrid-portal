package gov.nih.nci.cagrid.gums.service;

import gov.nih.nci.cagrid.gums.bean.GUMSInternalFault;
import gov.nih.nci.cagrid.gums.ca.CertificateAuthority;
import gov.nih.nci.cagrid.gums.ca.GUMSCertificateAuthority;
import gov.nih.nci.cagrid.gums.ca.GUMSCertificateAuthorityConf;
import gov.nih.nci.cagrid.gums.common.Database;
import gov.nih.nci.cagrid.gums.idp.IdPConfiguration;
import gov.nih.nci.cagrid.gums.idp.IdPManager;
import gov.nih.nci.cagrid.gums.idp.IdentityProvider;
import gov.nih.nci.cagrid.gums.ifs.CredentialsManager;
import gov.nih.nci.cagrid.gums.ifs.bean.AttributeDescriptor;

import org.globus.wsrf.utils.FaultHelper;
import org.projectmobius.common.MobiusConfigurator;
import org.projectmobius.common.MobiusResourceManager;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class GUMSManager extends MobiusResourceManager{
	
	private Database db;
	private RequiredAttributesManager userAttributeManager;
	public static String GUMS_CONFIGURATION_FILE="etc/gums-conf.xml";
	public static final String GUMS_CONFIGURATION_RESOURCE="GUMSConfiguration";
	public static final String REQUIRED_USER_ATTRIBUTES = "REQUIRED_USER_ATTRIBUTES";
	private static GUMSManager instance;
	private CredentialsManager credentialsManager;
	private CertificateAuthority ca;
	private IdPManager idp;
	
	private GUMSManager() throws GUMSInternalFault{
		try{
		MobiusConfigurator.parseMobiusConfiguration(GUMS_CONFIGURATION_FILE,this);
		}catch(Exception e){
			GUMSInternalFault fault = new GUMSInternalFault();
			fault.setFaultString("An unexpected error occurred in configuring the service.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (GUMSInternalFault)helper.getFault();
			throw fault;
		}	
		this.db = new Database(getGUMSConfiguration().getConnectionManager(),getGUMSConfiguration().getGUMSInternalId());
		this.db.createDatabaseIfNeeded();
		GUMSCertificateAuthorityConf caconf = (GUMSCertificateAuthorityConf)getResource(GUMSCertificateAuthorityConf.RESOURCE);
		this.ca = new GUMSCertificateAuthority(db,caconf);
		
		this.idp = IdPManager.getInstance();
		this.idp.configure(db,(IdPConfiguration)getResource(IdPConfiguration.RESOURCE),ca);
		
		this.userAttributeManager = new RequiredAttributesManager(this.db,REQUIRED_USER_ATTRIBUTES);
		AttributeDescriptor des = new AttributeDescriptor();
		des.setNamespace("cagrid.nci.nih.gov/1/person");
		des.setName("person");
		this.userAttributeManager.insertRequiredAttribute(des);
		
		this.credentialsManager = new CredentialsManager(db);
		
		
	}
	
	public GUMSConfiguration getGUMSConfiguration(){
		return (GUMSConfiguration)this.getResource(GUMS_CONFIGURATION_RESOURCE);
	}
	
	public static GUMSManager getInstance() throws GUMSInternalFault{
		if(instance == null){
			instance = new GUMSManager();
		}
		return instance;
	}
	
	

	public Database getDatabase() {
		return this.db;
	}

	public RequiredAttributesManager getUserAttributeManager() {
		return userAttributeManager;
	}

	public CredentialsManager getCredentialsManager() {
		return credentialsManager;
	}

	public IdentityProvider getIdentityProvider() throws GUMSInternalFault{
		return idp.getIdentityProvider();
	}

	
	
}
