package gov.nih.nci.cagrid.dorian.test;

import gov.nih.nci.cagrid.dorian.ca.CertificateAuthority;
import gov.nih.nci.cagrid.dorian.ca.DorianCertificateAuthority;
import gov.nih.nci.cagrid.dorian.ca.DorianCertificateAuthorityConf;
import gov.nih.nci.cagrid.dorian.common.Database;
import gov.nih.nci.cagrid.dorian.common.ca.CertUtil;
import gov.nih.nci.cagrid.dorian.common.ca.KeyUtil;
import gov.nih.nci.cagrid.dorian.ifs.AutoApprovalAutoRenewalPolicy;
import gov.nih.nci.cagrid.dorian.ifs.AutoApprovalPolicy;
import gov.nih.nci.cagrid.dorian.ifs.ManualApprovalAutoRenewalPolicy;
import gov.nih.nci.cagrid.dorian.ifs.ManualApprovalPolicy;
import gov.nih.nci.cagrid.dorian.ifs.bean.IFSUserPolicy;

import java.io.File;
import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.bouncycastle.asn1.x509.X509Name;
import org.jdom.Document;
import org.projectmobius.common.XMLUtilities;
import org.projectmobius.db.ConnectionManager;

public class Utils {
	
	private static final String DB = "TEST_DORIAN";

	public static String DB_CONFIG = "resources" + File.separator
			+ "general-test" + File.separator + "db-config.xml";
	
	
	public static String CA_SUBJECT_PREFIX =  "O=Ohio State University,OU=BMI,OU=TEST";
	public static String CA_SUBJECT_DN= "Temp Certificate Authority";
	
	public static Database getDB() throws Exception{
		Document doc = XMLUtilities.fileNameToDocument(DB_CONFIG);
		ConnectionManager cm = new ConnectionManager(doc.getRootElement());
		Database db = new Database(cm, DB);
		db.destroyDatabase();
		db.createDatabaseIfNeeded();	
		return db;
	}
	
	public static IFSUserPolicy[] getUserPolicies(){
		IFSUserPolicy[] policies = new IFSUserPolicy[4];
		policies[0] = new IFSUserPolicy(ManualApprovalAutoRenewalPolicy.class.getName(),"");
		policies[1] = new IFSUserPolicy(AutoApprovalAutoRenewalPolicy.class.getName(),"");
		policies[2] = new IFSUserPolicy(ManualApprovalPolicy.class.getName(),"");
		policies[3] = new IFSUserPolicy(AutoApprovalPolicy.class.getName(),"");
	    return policies;
	}
	
	
	
	public static CertificateAuthority getCA() throws Exception{	
		return getCA(getDB());
	}

	
	public static String getCASubject(){
		return CA_SUBJECT_PREFIX+",CN="+CA_SUBJECT_DN;
	}
	
public static CertificateAuthority getCA(Database db) throws Exception{	
		DorianCertificateAuthorityConf conf = new DorianCertificateAuthorityConf();
		conf.setCaPassword("password");
		conf.setAutoRenewal(false);
		DorianCertificateAuthority ca = new DorianCertificateAuthority(db, conf);
		KeyPair rootPair = KeyUtil.generateRSAKeyPair1024();
		
		String rootSub = getCASubject();
		X509Name rootSubject = new X509Name(rootSub);
		GregorianCalendar cal = new GregorianCalendar();
		Date start = cal.getTime();
		cal.add(Calendar.YEAR, 1);
		Date end = cal.getTime();
		X509Certificate root = CertUtil.generateCACertificate(rootSubject,
				start, end, rootPair);
		ca.setCACredentials(root, rootPair.getPrivate());
		return ca;
		
	}

}
