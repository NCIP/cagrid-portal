package gov.nih.nci.cagrid.dorian.service;

import gov.nih.nci.cagrid.dorian.bean.Metadata;
import gov.nih.nci.cagrid.dorian.conf.CertificateAuthorityType;
import gov.nih.nci.cagrid.dorian.stubs.types.DorianInternalFault;


/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class PropertyManager {

	private static String TABLE = "properties";
	private static String VERSION_PROPERTY = "version";
	private static String CA_TYPE_PROPERTY = "certificate authority";
	public static float CURRENT_VERSION = 1.1F;
	private MetadataManager manager;
	private Metadata version;
	private Metadata certificateAuthorityType;


	public PropertyManager(Database db) throws DorianInternalFault {
		this.manager = new MetadataManager(db, TABLE);
		version = manager.get(VERSION_PROPERTY);
		if (version == null) {
			version = new Metadata();
			version.setName(VERSION_PROPERTY);
			version.setDescription("The software version of this Dorian.");
		}
		certificateAuthorityType = manager.get(CA_TYPE_PROPERTY);
	}


	public CertificateAuthorityType getCertificateAuthorityType() {
		if (this.certificateAuthorityType == null) {
			return null;
		} else {
			return CertificateAuthorityType.fromValue(certificateAuthorityType.getValue());
		}
	}


	public void setCertificateAuthorityType(CertificateAuthorityType ca) throws DorianInternalFault {
		this.certificateAuthorityType = new Metadata();
		this.certificateAuthorityType.setName(CA_TYPE_PROPERTY);
		this.certificateAuthorityType.setDescription("The certificate authority type used by this Dorian.");
		this.certificateAuthorityType.setValue(ca.getValue());
		this.manager.update(this.certificateAuthorityType);
	}


	public void setCurrentVersion() throws DorianInternalFault {
		this.setVersion(CURRENT_VERSION);
	}


	public void setVersion(float version) throws DorianInternalFault {
		this.version.setValue(String.valueOf(version));
		this.manager.update(this.version);
	}


	public float getVersion() {
		String s = this.version.getValue();
		if (s == null) {
			return 1.0F;
		} else {
			return Float.valueOf(s).floatValue();
		}
	}

}
