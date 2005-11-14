package gov.nih.nci.cagrid.gums.test;

import gov.nih.nci.cagrid.gums.ca.CertificateAuthority;
import gov.nih.nci.cagrid.gums.ca.GUMSCertificateAuthority;
import gov.nih.nci.cagrid.gums.ca.GUMSCertificateAuthorityConf;
import gov.nih.nci.cagrid.gums.common.Database;
import gov.nih.nci.cagrid.gums.common.ca.CertUtil;
import gov.nih.nci.cagrid.gums.common.ca.KeyUtil;

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

public class TestUtils {
	
	private static final String DB = "TEST_GUMS";

	public static String DB_CONFIG = "resources" + File.separator
			+ "general-test" + File.separator + "db-config.xml";
	
	
	
	
	public static Database getDB() throws Exception{
		Document doc = XMLUtilities.fileNameToDocument(DB_CONFIG);
		ConnectionManager cm = new ConnectionManager(doc.getRootElement());
		Database db = new Database(cm, DB);
		db.destroyDatabase();
		db.createDatabaseIfNeeded();	
		return db;
	}
	
	
	
	public static CertificateAuthority getCA() throws Exception{	
		return getCA(getDB());
	}

public static CertificateAuthority getCA(Database db) throws Exception{	
		GUMSCertificateAuthorityConf conf = new GUMSCertificateAuthorityConf();
		conf.setCaPassword("password");
		conf.setAutoRenewal(false);
		GUMSCertificateAuthority ca = new GUMSCertificateAuthority(db, conf);
		KeyPair rootPair = KeyUtil.generateRSAKeyPair1024();
		
		String rootSub = "O=Ohio State University,OU=BMI,OU=TEST,CN=Temp Certificate Authority";
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
