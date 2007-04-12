package gov.nih.nci.cagrid.dorian.test;

import gov.nih.nci.cagrid.dorian.ca.CertificateAuthority;
import gov.nih.nci.cagrid.dorian.ca.DorianCertificateAuthority;
import gov.nih.nci.cagrid.dorian.common.Database;
import gov.nih.nci.cagrid.dorian.conf.AccountPolicies;
import gov.nih.nci.cagrid.dorian.conf.AccountPolicy;
import gov.nih.nci.cagrid.dorian.conf.DorianCAConfiguration;
import gov.nih.nci.cagrid.dorian.ifs.bean.IFSUserPolicy;
import gov.nih.nci.cagrid.dorian.service.ifs.AutoApprovalAutoRenewalPolicy;
import gov.nih.nci.cagrid.dorian.service.ifs.AutoApprovalPolicy;
import gov.nih.nci.cagrid.dorian.service.ifs.ManualApprovalAutoRenewalPolicy;
import gov.nih.nci.cagrid.dorian.service.ifs.ManualApprovalPolicy;
import gov.nih.nci.cagrid.gridca.common.CertUtil;
import gov.nih.nci.cagrid.gridca.common.KeyUtil;
import gov.nih.nci.cagrid.opensaml.SAMLAssertion;
import gov.nih.nci.cagrid.opensaml.SAMLAttribute;
import gov.nih.nci.cagrid.opensaml.SAMLAttributeStatement;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;

import junit.framework.TestCase;

import org.bouncycastle.asn1.x509.X509Name;


public class Utils {

	private static final String DB = "test_dorian";

	public static String CA_SUBJECT_PREFIX = "O=Ohio State University,OU=BMI,OU=TEST";

	public static String CA_SUBJECT_DN = "Temp Certificate Authority";

	private static Database db = null;


	public static Database getDB() throws Exception {
		if (db == null) {
			InputStream resource = TestCase.class.getResourceAsStream(Constants.DB_CONFIG);
			gov.nih.nci.cagrid.dorian.conf.Database conf = (gov.nih.nci.cagrid.dorian.conf.Database) gov.nih.nci.cagrid.common.Utils
				.deserializeObject(new InputStreamReader(resource), gov.nih.nci.cagrid.dorian.conf.Database.class);
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
		policies[0].setClassname(ManualApprovalAutoRenewalPolicy.class.getName());
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


	public static IFSUserPolicy[] getUserPolicies() {
		IFSUserPolicy[] policies = new IFSUserPolicy[4];
		policies[0] = new IFSUserPolicy(ManualApprovalAutoRenewalPolicy.class.getName(), "");
		policies[1] = new IFSUserPolicy(AutoApprovalAutoRenewalPolicy.class.getName(), "");
		policies[2] = new IFSUserPolicy(ManualApprovalPolicy.class.getName(), "");
		policies[3] = new IFSUserPolicy(AutoApprovalPolicy.class.getName(), "");
		return policies;
	}


	public static CertificateAuthority getCA() throws Exception {
		return getCA(getDB());
	}


	public static String getCASubject() {
		return CA_SUBJECT_PREFIX + ",CN=" + CA_SUBJECT_DN;
	}


	public static CertificateAuthority getCA(Database cadb) throws Exception {
		DorianCAConfiguration conf = new DorianCAConfiguration();
		conf.setCertificateAuthorityPassword("password");
		DorianCertificateAuthority ca = new DorianCertificateAuthority(cadb, conf);
		ca.clearDatabase();
		KeyPair rootPair = KeyUtil.generateRSAKeyPair1024();

		String rootSub = getCASubject();
		X509Name rootSubject = new X509Name(rootSub);
		GregorianCalendar cal = new GregorianCalendar();
		Date start = cal.getTime();
		cal.add(Calendar.YEAR, 1);
		Date end = cal.getTime();
		X509Certificate root = CertUtil.generateCACertificate(rootSubject, start, end, rootPair);
		ca.setCACredentials(root, rootPair.getPrivate());
		return ca;

	}


	public static String getAttribute(SAMLAssertion saml, String namespace, String name) {
		Iterator itr = saml.getStatements();
		while (itr.hasNext()) {
			Object o = itr.next();
			if (o instanceof SAMLAttributeStatement) {
				SAMLAttributeStatement att = (SAMLAttributeStatement) o;
				Iterator attItr = att.getAttributes();
				while (attItr.hasNext()) {
					SAMLAttribute a = (SAMLAttribute) attItr.next();
					if ((a.getNamespace().equals(namespace)) && (a.getName().equals(name))) {
						Iterator vals = a.getValues();
						while (vals.hasNext()) {
							String val = gov.nih.nci.cagrid.common.Utils.clean((String) vals.next());
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
