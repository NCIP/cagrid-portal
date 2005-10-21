package gov.nih.nci.cagrid.gums.idp;

import org.globus.wsrf.utils.FaultHelper;

import gov.nih.nci.cagrid.gums.bean.GUMSInternalFault;
import gov.nih.nci.cagrid.gums.bean.Metadata;
import gov.nih.nci.cagrid.gums.common.Database;
import gov.nih.nci.cagrid.gums.common.GUMSObject;
import gov.nih.nci.cagrid.gums.common.MetadataManager;

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

	private MetadataManager mm;

	public IdPProperties(Database db) throws GUMSInternalFault {
		try {
			mm = new MetadataManager(db, "IDP_PROPERTIES");

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
				Metadata maxPass = new Metadata();
				maxPass.setName(REGISTRATION_POLICY);
				maxPass.setValue(DEFAULT_REGISTRATION_POLICY);
				maxPass.setDescription(REGISTRATION_POLICY_DESCRIPTION);
				mm.insert(maxPass);
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

}
