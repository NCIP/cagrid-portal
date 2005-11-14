package gov.nih.nci.cagrid.gums.idp;

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
public class IdPManager{
	
	private Database db;
	private static IdPManager instance;
	private IdentityProvider idp;
	private CertificateAuthority ca;
	private IdPConfiguration configuration;
	private AssertingManager assertingManager;
	
	
	
	private boolean configured = false;
	
	
	
	private IdPManager() throws GUMSInternalFault{
		
	}
	

	
	public static IdPManager getInstance() throws GUMSInternalFault{
		if(instance == null){
			instance = new IdPManager();
		}
		return instance;
	}
	
	public void configure(Database db, IdPConfiguration config, CertificateAuthority ca){
		this.db = db;
		this.ca = ca;
		this.configuration = config;
		configured = true;
	}
	

	public CertificateAuthority getCertificateAuthority() {
		return ca;
	}


	public Database getDatabase() {
		return this.db;
	}
	
	public AssertingManager getAssertingManager() throws GUMSInternalFault{
		if(idp == null){
			if(this.configured){
				
			this.assertingManager = new AssertingManager(configuration,getCertificateAuthority(),db);
			}else{
				GUMSInternalFault fault = new GUMSInternalFault();
				fault.setFaultString("Cannot create IdP, the IdPManager hase not been configured.");
				throw fault;
			}
		}
		return assertingManager;
	}


	public IdentityProvider getIdentityProvider() throws GUMSInternalFault{
		if(idp == null){
			if(this.configured){
				
			idp = new IdentityProvider(configuration,db, getAssertingManager());
			}else{
				GUMSInternalFault fault = new GUMSInternalFault();
				fault.setFaultString("Cannot create IdP, the IdPManager hase not been configured.");
				throw fault;
			}
		}
		return idp;
	}

	
	
}
