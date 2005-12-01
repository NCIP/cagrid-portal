package gov.nih.nci.cagrid.gums.service;

import gov.nih.nci.cagrid.gums.bean.GUMSInternalFault;
import gov.nih.nci.cagrid.gums.ca.CertificateAuthority;
import gov.nih.nci.cagrid.gums.ca.GUMSCertificateAuthority;
import gov.nih.nci.cagrid.gums.ca.GUMSCertificateAuthorityConf;
import gov.nih.nci.cagrid.gums.common.Database;
import gov.nih.nci.cagrid.gums.common.ca.CertUtil;
import gov.nih.nci.cagrid.gums.idp.IdPConfiguration;
import gov.nih.nci.cagrid.gums.idp.IdentityProvider;
import gov.nih.nci.cagrid.gums.idp.bean.BasicAuthCredential;
import gov.nih.nci.cagrid.gums.idp.bean.IdPUser;
import gov.nih.nci.cagrid.gums.ifs.AutoApprovalAutoRenewalPolicy;
import gov.nih.nci.cagrid.gums.ifs.IFS;
import gov.nih.nci.cagrid.gums.ifs.IFSConfiguration;
import gov.nih.nci.cagrid.gums.ifs.bean.IFSUser;
import gov.nih.nci.cagrid.gums.ifs.bean.IFSUserRole;
import gov.nih.nci.cagrid.gums.ifs.bean.IFSUserStatus;
import gov.nih.nci.cagrid.gums.ifs.bean.SAMLAuthenticationMethod;
import gov.nih.nci.cagrid.gums.ifs.bean.TrustedIdP;

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
public class GUMSManager extends MobiusResourceManager {

	private Database db;

	public static String GUMS_CONFIGURATION_FILE = "etc/gums-conf.xml";

	public static final String GUMS_CONFIGURATION_RESOURCE = "GUMSConfiguration";

	public static final String IDP_ADMIN_USER_ID = "gums";

	public static final String IDP_ADMIN_PASSWORD = "password";

	public static String SERVICE_ID = "localhost";

	private static GUMSManager instance;

	private CertificateAuthority ca;

	private IdentityProvider identityProvider;

	private IFS ifs;

	private IFSConfiguration ifsConfiguration;

	private GUMSManager() throws GUMSInternalFault {
		try {
			MobiusConfigurator.parseMobiusConfiguration(
					GUMS_CONFIGURATION_FILE, this);

			IdentityProvider.ADMIN_USER_ID = IDP_ADMIN_USER_ID;
			IdentityProvider.ADMIN_PASSWORD = IDP_ADMIN_PASSWORD;

			this.db = new Database(getGUMSConfiguration()
					.getConnectionManager(), getGUMSConfiguration()
					.getGUMSInternalId());
			this.db.createDatabaseIfNeeded();
			GUMSCertificateAuthorityConf caconf = (GUMSCertificateAuthorityConf) getResource(GUMSCertificateAuthorityConf.RESOURCE);
			this.ca = new GUMSCertificateAuthority(db, caconf);

			IdPConfiguration idpConf = (IdPConfiguration) getResource(IdPConfiguration.RESOURCE);
			this.identityProvider = new IdentityProvider(idpConf, db, ca);

			TrustedIdP idp = new TrustedIdP();
			idp.setName(SERVICE_ID);
			SAMLAuthenticationMethod[] methods = new SAMLAuthenticationMethod[1];
			methods[0] = SAMLAuthenticationMethod
					.fromString("urn:oasis:names:tc:SAML:1.0:am:password");
			idp.setAuthenticationMethod(methods);
			idp.setPolicyClass(AutoApprovalAutoRenewalPolicy.class.getName());
			idp.setIdPCertificate(CertUtil
					.writeCertificateToString(this.identityProvider
							.getIdPCertificate()));
			
			BasicAuthCredential cred = new BasicAuthCredential();
			cred.setUserId(IDP_ADMIN_USER_ID);
			cred.setPassword(IDP_ADMIN_PASSWORD);
			IdPUser idpUser = this.identityProvider.getUser(cred,IDP_ADMIN_USER_ID);		
			IFSUser usr = new IFSUser();
			usr.setUID(idpUser.getUserId());
			usr.setEmail(idpUser.getEmail());
			usr.setUserStatus(IFSUserStatus.Active);
			usr.setUserRole(IFSUserRole.Administrator);
			
			ifsConfiguration = (IFSConfiguration) getResource(IFSConfiguration.RESOURCE);
			ifsConfiguration.setInitalTrustedIdP(idp);
			ifsConfiguration.setInitialUser(usr);
			this.ifs = new IFS(ifsConfiguration, db, ca);

		} catch (Exception e) {
			GUMSInternalFault fault = new GUMSInternalFault();
			fault
					.setFaultString("An unexpected error occurred in configuring the service.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (GUMSInternalFault) helper.getFault();
			throw fault;
		}
	}

	public GUMSConfiguration getGUMSConfiguration() {
		return (GUMSConfiguration) this
				.getResource(GUMS_CONFIGURATION_RESOURCE);
	}

	public static GUMSManager getInstance() throws GUMSInternalFault {
		if (instance == null) {
			instance = new GUMSManager();
		}
		return instance;
	}

	public Database getDatabase() {
		return this.db;
	}

	public IdentityProvider getIdentityProvider() {
		return identityProvider;
	}

	public IFS getIFS() {
		return ifs;
	}

	public IFSConfiguration getIFSConfiguration() {
		return ifsConfiguration;
	}
	
	
	

}
