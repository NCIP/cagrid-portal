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
public class IdPProperties extends GUMSObject {

	private final static String MIN_PASSWORD_LENGTH = "Minimum Password Length";

	private final static String MIN_PASSWORD_LENGTH_DESCRIPTION = "The minimum length for a user\\'s password.  This is enforced at registration and when changing passwords.";

	public final static int DEFAULT_MIN_PASSWORD_LENGTH = 6;

	private final static String MAX_PASSWORD_LENGTH = "Maximum Password Length";

	private final static String MAX_PASSWORD_LENGTH_DESCRIPTION = "The maximum length for a user\\'s password.  This is enforced at registration and when changing passwords.";

	public final static int DEFAULT_MAX_PASSWORD_LENGTH = 10;

	private final static String MIN_UID_LENGTH = "Minimum UID Length";

	private final static String MIN_UID_LENGTH_DESCRIPTION = "The minimum length for a user\\'s UID.  This is enforced at registration and when changing passwords.";

	public final static int DEFAULT_MIN_UID_LENGTH = 4;

	private final static String MAX_UID_LENGTH = "Maximum UID Length";

	private final static String MAX_UID_LENGTH_DESCRIPTION = "The maximum length for a user\\'s UID.  This is enforced at registration and when changing passwords.";

	public final static int DEFAULT_MAX_UID_LENGTH = 10;

	private final static String REGISTRATION_POLICY = "Registration Policy";

	private final static String REGISTRATION_POLICY_DESCRIPTION = "The registration to apply to users when they register.";

	private final static String DEFAULT_REGISTRATION_POLICY = ManualRegistrationPolicy.class
			.getName();

	private final static String IDP_PRIVATE_KEY = "IdP Private Key";

	private final static String IDP_CERTIFICATE = "IdP Certificate";

	private final static String IDP_PRIVATE_KEY_DESCRIPTION = "Private key used by IdP to sign authentication assersions.";

	private final static String IDP_CERTIFICATE_DESCRIPTION = "Certificate corresponding to the private key used by IdP to sign authentication assersions.";

	private final static String CA_SUBJECT = "IdP Authentication Asserter";
	
	private final static String KEY_ENCRYPTOR = "key4idp";

	private MetadataManager mm;

	private CertificateAuthority ca;

	public IdPProperties(CertificateAuthority ca, Database db)
			throws GUMSInternalFault {
		try {
			mm = new MetadataManager(db, "IDP_PROPERTIES");
			this.ca = ca;

			if (!mm.exists(MIN_UID_LENGTH)) {
				Metadata minUID = new Metadata();
				minUID.setName(MIN_UID_LENGTH);
				minUID.setValue(String.valueOf(DEFAULT_MIN_UID_LENGTH));
				minUID.setDescription(MIN_UID_LENGTH_DESCRIPTION);
				mm.insert(minUID);
			}
			if (!mm.exists(MAX_UID_LENGTH)) {
				Metadata maxUID = new Metadata();
				maxUID.setName(MAX_UID_LENGTH);
				maxUID.setValue(String.valueOf(DEFAULT_MAX_UID_LENGTH));
				maxUID.setDescription(MAX_UID_LENGTH_DESCRIPTION);
				mm.insert(maxUID);
			}

			if (!mm.exists(MIN_PASSWORD_LENGTH)) {
				Metadata minPass = new Metadata();
				minPass.setName(MIN_PASSWORD_LENGTH);
				minPass.setValue(String.valueOf(DEFAULT_MIN_PASSWORD_LENGTH));
				minPass.setDescription(MIN_PASSWORD_LENGTH_DESCRIPTION);
				mm.insert(minPass);
			}
			if (!mm.exists(MAX_PASSWORD_LENGTH)) {
				Metadata maxPass = new Metadata();
				maxPass.setName(MAX_PASSWORD_LENGTH);
				maxPass.setValue(String.valueOf(DEFAULT_MAX_PASSWORD_LENGTH));
				maxPass.setDescription(MAX_PASSWORD_LENGTH_DESCRIPTION);
				mm.insert(maxPass);
			}

			if (!mm.exists(REGISTRATION_POLICY)) {
				Metadata regPolicy = new Metadata();
				regPolicy.setName(REGISTRATION_POLICY);
				regPolicy.setValue(DEFAULT_REGISTRATION_POLICY);
				regPolicy.setDescription(REGISTRATION_POLICY_DESCRIPTION);
				mm.insert(regPolicy);
			}

			if ((!mm.exists(IDP_PRIVATE_KEY)) || (!mm.exists(IDP_CERTIFICATE))) {

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

				Metadata key = new Metadata();
				key.setName(IDP_PRIVATE_KEY);
				key.setValue(KeyUtil.writePrivateKeyToString(pair.getPrivate(),KEY_ENCRYPTOR));
				key.setDescription(IDP_PRIVATE_KEY_DESCRIPTION);
				mm.insert(key);
				
				Metadata certificate = new Metadata();
				certificate.setName(IDP_CERTIFICATE);
				certificate.setValue(CertUtil.writeCertificateToString(cert));
				certificate.setDescription(IDP_CERTIFICATE_DESCRIPTION);
				mm.insert(certificate);
			}
		} catch (Exception e) {
			logError(e.getMessage(), e);
			GUMSInternalFault fault = new GUMSInternalFault();
			fault.setFaultString("Error initializing the IDP properties.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (GUMSInternalFault) helper.getFault();
			throw fault;
		}
	}

	public int getMinimumPasswordLength() throws GUMSInternalFault {
		try {
			return Integer.valueOf(mm.get(MIN_PASSWORD_LENGTH).getValue())
					.intValue();
		} catch (Exception e) {
			logError(e.getMessage(), e);
			GUMSInternalFault fault = new GUMSInternalFault();
			fault.setFaultString("Error obtaining the IDP Property, "
					+ MIN_PASSWORD_LENGTH + ".");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (GUMSInternalFault) helper.getFault();
			throw fault;
		}
	}

	public int getMaximumPasswordLength() throws GUMSInternalFault {
		try {
			return Integer.valueOf(mm.get(MAX_PASSWORD_LENGTH).getValue())
					.intValue();
		} catch (Exception e) {
			logError(e.getMessage(), e);
			GUMSInternalFault fault = new GUMSInternalFault();
			fault.setFaultString("Error obtaining the IDP Property, "
					+ MAX_PASSWORD_LENGTH + ".");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (GUMSInternalFault) helper.getFault();
			throw fault;
		}
	}

	public int getMinimumUIDLength() throws GUMSInternalFault {
		try {
			return Integer.valueOf(mm.get(MIN_UID_LENGTH).getValue())
					.intValue();
		} catch (Exception e) {
			logError(e.getMessage(), e);
			GUMSInternalFault fault = new GUMSInternalFault();
			fault.setFaultString("Error obtaining the IDP Property, "
					+ MIN_UID_LENGTH + ".");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (GUMSInternalFault) helper.getFault();
			throw fault;
		}
	}

	public int getMaximumUIDLength() throws GUMSInternalFault {
		try {
			return Integer.valueOf(mm.get(MAX_UID_LENGTH).getValue())
					.intValue();
		} catch (Exception e) {
			logError(e.getMessage(), e);
			GUMSInternalFault fault = new GUMSInternalFault();
			fault.setFaultString("Error obtaining the IDP Property, "
					+ MAX_UID_LENGTH + ".");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (GUMSInternalFault) helper.getFault();
			throw fault;
		}
	}

	public IdPRegistrationPolicy getRegistrationPolicy()
			throws GUMSInternalFault {
		try {
			Metadata regPolicy = mm.get(REGISTRATION_POLICY);
			IdPRegistrationPolicy policy = (IdPRegistrationPolicy) Class
					.forName(regPolicy.getValue()).newInstance();
			return policy;
		} catch (Exception e) {
			logError(e.getMessage(), e);
			GUMSInternalFault fault = new GUMSInternalFault();
			fault
					.setFaultString("Error obtaining the IDP Registration Policy.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (GUMSInternalFault) helper.getFault();
			throw fault;
		}
	}

	public void setRegistrationPolicy(IdPRegistrationPolicy policy)
			throws GUMSInternalFault {
		try {
			Metadata regPolicy = new Metadata();
			regPolicy.setName(REGISTRATION_POLICY);
			regPolicy.setValue(policy.getClass().getName());
			regPolicy.setDescription(REGISTRATION_POLICY_DESCRIPTION);
			mm.update(regPolicy);

		} catch (Exception e) {
			logError(e.getMessage(), e);
			GUMSInternalFault fault = new GUMSInternalFault();
			fault.setFaultString("Error setting the IDP Registration Policy.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (GUMSInternalFault) helper.getFault();
			throw fault;
		}
	}

}
