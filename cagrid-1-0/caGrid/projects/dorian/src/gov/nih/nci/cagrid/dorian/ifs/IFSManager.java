package gov.nih.nci.cagrid.gums.ifs;

import gov.nih.nci.cagrid.gums.bean.GUMSInternalFault;
import gov.nih.nci.cagrid.gums.ca.CertificateAuthority;
import gov.nih.nci.cagrid.gums.common.Database;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class IFSManager{
	
	private Database db;
	private static IFSManager instance;
	private IFSConfiguration configuration;
	private boolean configured = false;
	private CertificateAuthority ca;
	private UserManager userManager;
	private TrustManager trustManager;
	
	
	
	private IFSManager() throws GUMSInternalFault{
		
	}
	
	public static IFSManager getInstance() throws GUMSInternalFault{
		if(instance == null){
			instance = new IFSManager();
		}
		return instance;
	}
	
	public void configure(Database db, IFSConfiguration config, CertificateAuthority ca){
		this.db = db;
		this.configuration = config;
		this.ca = ca;
		this.userManager = new UserManager(db,configuration,this.ca);
		this.trustManager = new TrustManager(this.configuration,db);
		configured = true;
	}

	public CertificateAuthority getCertificateAuthority() throws GUMSInternalFault{
		if(this.configured){
			return ca;
		}else{
			GUMSInternalFault fault = new GUMSInternalFault();
			fault.setFaultString("Error, IFSManager has not been configured.");
			throw fault;
		}
		
	}

	public IFSConfiguration getConfiguration()  throws GUMSInternalFault{
		if(this.configured){
			return configuration;
		}else{
			GUMSInternalFault fault = new GUMSInternalFault();
			fault.setFaultString("Error, IFSManager has not been configured.");
			throw fault;
		}
		
	}



	public UserManager getUserManager() throws GUMSInternalFault{
		if(this.configured){
			return userManager;
		}else{
			GUMSInternalFault fault = new GUMSInternalFault();
			fault.setFaultString("Error, IFSManager has not been configured.");
			throw fault;
		}
		
	}
	
	

	public TrustManager getTrustManager() throws GUMSInternalFault{
		if(this.configured){
			return trustManager;
		}else{
			GUMSInternalFault fault = new GUMSInternalFault();
			fault.setFaultString("Error, IFSManager has not been configured.");
			throw fault;
		}	
	}

	public Database getDatabase() throws GUMSInternalFault{
		if(this.configured){
			return db;
		}else{
			GUMSInternalFault fault = new GUMSInternalFault();
			fault.setFaultString("Error, IFSManager has not been configured.");
			throw fault;
		}
		
	}
	
	
	
	
	

		
}
