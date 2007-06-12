package gov.nih.nci.cagrid.dorian.test;

import gov.nih.nci.cagrid.dorian.conf.AccountPolicies;
import gov.nih.nci.cagrid.dorian.conf.AccountPolicy;
import gov.nih.nci.cagrid.dorian.conf.CertificateAuthorityType;
import gov.nih.nci.cagrid.dorian.conf.DorianCAConfiguration;
import gov.nih.nci.cagrid.dorian.conf.IdentityAssignmentPolicy;
import gov.nih.nci.cagrid.dorian.ifs.bean.IFSUserPolicy;
import gov.nih.nci.cagrid.dorian.ifs.bean.TrustedIdP;
import gov.nih.nci.cagrid.dorian.service.Database;
import gov.nih.nci.cagrid.dorian.service.ca.CertificateAuthority;
import gov.nih.nci.cagrid.dorian.service.ca.DBCertificateAuthority;
import gov.nih.nci.cagrid.dorian.service.ca.EracomCertificateAuthority;
import gov.nih.nci.cagrid.dorian.service.ca.EracomWrappingCertificateAuthority;
import gov.nih.nci.cagrid.dorian.service.ifs.AutoApprovalAutoRenewalPolicy;
import gov.nih.nci.cagrid.dorian.service.ifs.AutoApprovalPolicy;
import gov.nih.nci.cagrid.dorian.service.ifs.ManualApprovalAutoRenewalPolicy;
import gov.nih.nci.cagrid.dorian.service.ifs.ManualApprovalPolicy;
import gov.nih.nci.cagrid.dorian.service.ifs.UserManager;
import gov.nih.nci.cagrid.opensaml.SAMLAssertion;
import gov.nih.nci.cagrid.opensaml.SAMLAttribute;
import gov.nih.nci.cagrid.opensaml.SAMLAttributeStatement;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;

import junit.framework.TestCase;

public class Utils {

	private static final String DB = "test_dorian";

	// public static String CA_SUBJECT_PREFIX =
	// "C=US,O=abc,OU=xyz,OU=caGrid,OU=Users";

	// public static String CA_SUBJECT_DN = "caGrid Dorian CA";

	public static String CA_SUBJECT_PREFIX = null;

	public static String CA_SUBJECT_DN = null;

	public static String CA_SUBJECT = null;

	private static Database db = null;

	public static Database getDB() throws Exception {
		if (db == null) {
			InputStream resource = TestCase.class
					.getResourceAsStream(Constants.DB_CONFIG);
			gov.nih.nci.cagrid.dorian.conf.Database conf = (gov.nih.nci.cagrid.dorian.conf.Database) gov.nih.nci.cagrid.common.Utils
					.deserializeObject(new InputStreamReader(resource),
							gov.nih.nci.cagrid.dorian.conf.Database.class);
			db = new Database(conf, DB);
			db.createDatabaseIfNeeded();
		}
		return db;
	}

	public static AccountPolicies getAccountPolicies() {
		AccountPolicies ap = new AccountPolicies();
		AccountPolicy[] policies = new AccountPolicy[4];
		policies[0] = new AccountPolicy();
		policies[0].setName(ManualApprovalAutoRenewalPolicy.class.getName());
		policies[0].setClassname(ManualApprovalAutoRenewalPolicy.class
				.getName());
		policies[1] = new AccountPolicy();
		policies[1].setName(AutoApprovalAutoRenewalPolicy.class.getName());
		policies[1].setClassname(AutoApprovalAutoRenewalPolicy.class.getName());
		policies[2] = new AccountPolicy();
		policies[2].setName(ManualApprovalPolicy.class.getName());
		policies[2].setClassname(ManualApprovalPolicy.class.getName());
		policies[3] = new AccountPolicy();
		policies[3].setName(AutoApprovalPolicy.class.getName());
		policies[3].setClassname(AutoApprovalPolicy.class.getName());
		ap.setAccountPolicy(policies);
		return ap;
	}

	public static String getDorianIdPUserId(IdentityAssignmentPolicy policy,
			String idpName, String caSubject, String uid) throws Exception {
		TrustedIdP idp = new TrustedIdP();
		idp.setId(1);
		idp.setName(idpName);
		return UserManager.getUserSubject(policy, caSubject, idp, uid);
	}

	public static String getDorianIdPUserId(IdentityAssignmentPolicy policy,
			String caSubject, String uid) throws Exception {
		return getDorianIdPUserId(policy, "Dorian IdP", caSubject, uid);
	}

	public static IFSUserPolicy[] getUserPolicies() {
		IFSUserPolicy[] policies = new IFSUserPolicy[4];
		policies[0] = new IFSUserPolicy(ManualApprovalAutoRenewalPolicy.class
				.getName(), "");
		policies[1] = new IFSUserPolicy(AutoApprovalAutoRenewalPolicy.class
				.getName(), "");
		policies[2] = new IFSUserPolicy(ManualApprovalPolicy.class.getName(),
				"");
		policies[3] = new IFSUserPolicy(AutoApprovalPolicy.class.getName(), "");
		return policies;
	}

	public static CertificateAuthority getCA() throws Exception {
		return getCA(getDB());
	}

	public static String getCASubject() throws Exception {
		if (CA_SUBJECT == null) {
			DorianCAConfiguration conf = getCertificateAuthorityConf();
			return getCASubject(conf);
		}
		return CA_SUBJECT;
	}

	public static String getCASubject(DorianCAConfiguration conf)
			throws Exception {
		if (CA_SUBJECT == null) {
			CA_SUBJECT = conf.getAutoCreate().getCASubject();
			int index = CA_SUBJECT.lastIndexOf(",");
			CA_SUBJECT_PREFIX = CA_SUBJECT.substring(0, index);
			index = CA_SUBJECT.indexOf("CN=");
			CA_SUBJECT_DN = CA_SUBJECT.substring(index + 3);
		}
		return CA_SUBJECT;
	}

	public static DorianCAConfiguration getCertificateAuthorityConf()
			throws Exception {
		InputStream resource = TestCase.class
				.getResourceAsStream(Constants.CA_CONFIG);
		DorianCAConfiguration conf = (DorianCAConfiguration) gov.nih.nci.cagrid.common.Utils
				.deserializeObject(new InputStreamReader(resource),
						DorianCAConfiguration.class);
		return conf;
	}

	public static CertificateAuthority getCA(Database cadb) throws Exception {
		DorianCAConfiguration conf = getCertificateAuthorityConf();
		getCASubject(conf);
		CertificateAuthority ca = null;

		if (conf.getCertificateAuthorityType().equals(
				CertificateAuthorityType.Eracom)) {
			ca = new EracomCertificateAuthority(conf);
		} else if (conf.getCertificateAuthorityType().equals(
				CertificateAuthorityType.EracomHybrid)) {
			ca = new EracomWrappingCertificateAuthority(db, conf);
		} else {
			ca = new DBCertificateAuthority(db, conf);
		}
		ca.clearCertificateAuthority();
		return ca;
	}

	public static String getAttribute(SAMLAssertion saml, String namespace,
			String name) {
		Iterator itr = saml.getStatements();
		while (itr.hasNext()) {
			Object o = itr.next();
			if (o instanceof SAMLAttributeStatement) {
				SAMLAttributeStatement att = (SAMLAttributeStatement) o;
				Iterator attItr = att.getAttributes();
				while (attItr.hasNext()) {
					SAMLAttribute a = (SAMLAttribute) attItr.next();
					if ((a.getNamespace().equals(namespace))
							&& (a.getName().equals(name))) {
						Iterator vals = a.getValues();
						while (vals.hasNext()) {
							String val = gov.nih.nci.cagrid.common.Utils
									.clean((String) vals.next());
							if (val != null) {
								return val;
							}
						}
					}
				}
			}
		}
		return null;
	}

}
