package gov.nih.nci.cagrid.dorian.service;

import gov.nih.nci.cagrid.dorian.service.ca.CertificateAuthority;
import gov.nih.nci.cagrid.dorian.service.idp.IdentityProviderProperties;
import gov.nih.nci.cagrid.dorian.service.ifs.IdentityFederationProperties;

import org.cagrid.tools.database.Database;

public class DorianProperties {
	private IdentityProviderProperties identityProviderProperties;
	private IdentityFederationProperties identityFederationProperties;
	private CertificateAuthority certificateAuthority;
	private Database database;

	public DorianProperties(Database db,
			IdentityProviderProperties identityProviderProperties,
			IdentityFederationProperties identityFederationProperties,
			CertificateAuthority certificateAuthority) {
		this.database = db;
		this.identityFederationProperties = identityFederationProperties;
		this.identityProviderProperties = identityProviderProperties;
		this.certificateAuthority = certificateAuthority;

	}

	public IdentityProviderProperties getIdentityProviderProperties() {
		return identityProviderProperties;
	}

	public IdentityFederationProperties getIdentityFederationProperties() {
		return identityFederationProperties;
	}

	public CertificateAuthority getCertificateAuthority() {
		return certificateAuthority;
	}

	public Database getDatabase() {
		return database;
	}

}
