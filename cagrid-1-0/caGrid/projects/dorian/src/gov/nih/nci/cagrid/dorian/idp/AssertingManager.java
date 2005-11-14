package gov.nih.nci.cagrid.gums.idp;

import gov.nih.nci.cagrid.gums.bean.GUMSInternalFault;
import gov.nih.nci.cagrid.gums.bean.Metadata;
import gov.nih.nci.cagrid.gums.ca.CertificateAuthority;
import gov.nih.nci.cagrid.gums.common.Database;
import gov.nih.nci.cagrid.gums.common.GUMSObject;
import gov.nih.nci.cagrid.gums.common.MetadataManager;
import gov.nih.nci.cagrid.gums.common.ca.CertUtil;
import gov.nih.nci.cagrid.gums.common.ca.KeyUtil;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.GregorianCalendar;

import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.globus.wsrf.utils.FaultHelper;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class AssertingManager extends GUMSObject {


	private final static String IDP_PRIVATE_KEY = "IdP Private Key";

	private final static String IDP_CERTIFICATE = "IdP Certificate";
	
	private final static String IDP_PRIVATE_KEY_DESCRIPTION = "Private key used by IdP to sign authentication assersions.";

	private final static String IDP_CERTIFICATE_DESCRIPTION = "Certificate corresponding to the private key used by IdP to sign authentication assersions.";

	private final static String CA_SUBJECT = "IdP Authentication Asserter";
	
	

	private MetadataManager mm;

	private CertificateAuthority ca;
	
	private IdPConfiguration conf;

	public AssertingManager(IdPConfiguration conf, CertificateAuthority ca, Database db)
			throws GUMSInternalFault {
		try {
			mm = new MetadataManager(db, "IDP_ASSERTER");
			this.ca = ca;
			this.conf = conf;

			if ((!mm.exists(IDP_PRIVATE_KEY)) || (!mm.exists(IDP_CERTIFICATE))) {
				if(conf.isAutoCreateAssertingCredentials()){
					createNewCredentials();
				}else{
					storeCredentials(conf.getAssertingCertificate(),conf.getAssertingKey(),conf.getKeyPassword());
				}
			}
		} catch (Exception e) {
			logError(e.getMessage(), e);
			GUMSInternalFault fault = new GUMSInternalFault();
			fault.setFaultString("Error initializing the IDP Asserting Manager.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (GUMSInternalFault) helper.getFault();
			throw fault;
		}
	}
	
	private void storeCredentials(X509Certificate cert, PrivateKey pkey, String keyPassword) throws Exception{
		Metadata key = new Metadata();
		key.setName(IDP_PRIVATE_KEY);
		key.setValue(KeyUtil.writePrivateKeyToString(pkey,keyPassword));
		key.setDescription(IDP_PRIVATE_KEY_DESCRIPTION);
		mm.insert(key);
		
		Metadata certificate = new Metadata();
		certificate.setName(IDP_CERTIFICATE);
		certificate.setValue(CertUtil.writeCertificateToString(cert));
		certificate.setDescription(IDP_CERTIFICATE_DESCRIPTION);
		mm.insert(certificate);		
	}
	
	
	private void createNewCredentials() throws Exception{
		// VALIDATE DN
		X509Certificate cacert = ca.getCACertificate();
		String caSubject = cacert.getSubjectDN().getName();
		int caindex = caSubject.lastIndexOf(",");
		String caPreSub = caSubject.substring(0, caindex);

		String subject = caPreSub + ",CN=" + CA_SUBJECT;
		KeyPair pair = KeyUtil.generateRSAKeyPair1024();
		PKCS10CertificationRequest req = CertUtil
				.generateCertficateRequest(subject, pair);
		GregorianCalendar cal = new GregorianCalendar();
		Date start = cal.getTime();

		X509Certificate cert = ca.requestCertificate(req, start, cacert
				.getNotAfter());
		storeCredentials(cert,pair.getPrivate(),conf.getKeyPassword());
	}
	
	public X509Certificate getIdPCertificate() throws GUMSInternalFault {
		try {
		 return null;
		} catch (Exception e) {
			logError(e.getMessage(), e);
			GUMSInternalFault fault = new GUMSInternalFault();
			fault.setFaultString("Error obtaining the IDP Property, "
					+ IDP_CERTIFICATE + ".");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (GUMSInternalFault) helper.getFault();
			throw fault;
		}
		
	}

	

}
