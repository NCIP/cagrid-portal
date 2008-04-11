package gov.nih.nci.cagrid.dorian.service.ca;

import gov.nih.nci.cagrid.dorian.common.Lifetime;
import gov.nih.nci.cagrid.dorian.stubs.types.DorianInternalFault;

public class EracomCertificateAuthorityProperties extends
		CertificateAuthorityProperties {

	private int slot;

	public EracomCertificateAuthorityProperties(
			String certificateAuthorityPassword, int issuedCertificateKeySize,
			int slot) throws DorianInternalFault {
		this(certificateAuthorityPassword, null, issuedCertificateKeySize,
				false, null, false, null, slot);
	}

	public EracomCertificateAuthorityProperties(
			String certificateAuthorityPassword, String policyOID,
			int issuedCertificateKeySize, int slot) throws DorianInternalFault {
		this(certificateAuthorityPassword, policyOID, issuedCertificateKeySize,
				false, null, false, null, slot);
	}

	public EracomCertificateAuthorityProperties(
			String certificateAuthorityPassword, String policyOID,
			int issuedCertificateKeySize, boolean autoCreate,
			CertificateAuthorityCreationPolicy creationPolicy,
			boolean autoRenew, Lifetime renewalLifetime, int slot)
			throws DorianInternalFault {
		super(certificateAuthorityPassword, policyOID,
				issuedCertificateKeySize, autoCreate, creationPolicy,
				autoRenew, renewalLifetime);
		this.slot = slot;
	}

	public int getSlot() {
		return slot;
	}

}
