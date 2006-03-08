package gov.nih.nci.cagrid.gts.service;

import gov.nih.nci.cagrid.gridca.common.CertUtil;
import gov.nih.nci.cagrid.gts.bean.TrustedAuthority;
import gov.nih.nci.cagrid.gts.common.Database;
import gov.nih.nci.cagrid.gts.stubs.GTSInternalFault;
import gov.nih.nci.cagrid.gts.stubs.IllegalTrustedAuthorityFault;

import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.logging.Logger;


/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: TrustedAuthorityManager.java,v 1.1 2006/03/08 19:48:46 langella
 *          Exp $
 */
public class TrustedAuthorityManager {
	private static final String TRUSTED_AUTHORITIES_TABLE = "TRUSTED_AUTHORITIES";
	private Logger logger;
	private boolean dbBuilt = false;
	private Database db;
	private String gtsURI;


	public TrustedAuthorityManager(String gtsURI, Database db) {
		logger = Logger.getLogger(this.getClass().getName());
		this.gtsURI = gtsURI;
		this.db = db;
	}


	public void addTrustedAuthority(TrustedAuthority ta) throws GTSInternalFault, IllegalTrustedAuthorityFault {
		this.buildDatabase();
		X509Certificate cert = checkAndExtractCertificate(ta);
		checkAndExtractCRL(ta,cert);

	}


	private X509Certificate checkAndExtractCertificate(TrustedAuthority ta) throws IllegalTrustedAuthorityFault {
		if (ta.getCertificate() == null) {
			IllegalTrustedAuthorityFault fault = new IllegalTrustedAuthorityFault();
			fault.setFaultString("No certificate specified!!!");
			throw fault;
		}

		if (ta.getCertificate().getCertificateEncodedString() == null) {
			IllegalTrustedAuthorityFault fault = new IllegalTrustedAuthorityFault();
			fault.setFaultString("No certificate specified!!!");
			throw fault;
		}

		try {
			return CertUtil.loadCertificate(ta.getCertificate().getCertificateEncodedString());
		} catch (Exception ex) {
			IllegalTrustedAuthorityFault fault = new IllegalTrustedAuthorityFault();
			fault.setFaultString("Invalid certificate Provided!!!");
			throw fault;
		}
	}


	private X509CRL checkAndExtractCRL(TrustedAuthority ta, X509Certificate signer) throws IllegalTrustedAuthorityFault {
		X509CRL crl = null;
		if (ta.getCRL() != null) {

			if (ta.getCRL().getCrlEncodedString() != null) {
				try {
					crl = CertUtil.loadCRL(ta.getCRL().getCrlEncodedString());
				} catch (Exception ex) {
					IllegalTrustedAuthorityFault fault = new IllegalTrustedAuthorityFault();
					fault.setFaultString("Invalid CRL provided!!!");
					throw fault;
				}
				try{
					crl.verify(signer.getPublicKey());
				}catch(Exception e){
					IllegalTrustedAuthorityFault fault = new IllegalTrustedAuthorityFault();
					fault.setFaultString("The CRL provided is not signed by the Trusted Authority!!!");
					throw fault;
				}

			}
		}

		return crl;
	}


	public void buildDatabase() throws GTSInternalFault {
		if (!dbBuilt) {
			if (!this.db.tableExists(TRUSTED_AUTHORITIES_TABLE)) {
				String trust = "CREATE TABLE " + TRUSTED_AUTHORITIES_TABLE + " ("
					+ "ID INT NOT NULL AUTO_INCREMENT PRIMARY KEY," + "NAME VARCHAR(255) NOT NULL,"
					+ "TRUST_LEVEL VARCHAR(255) NOT NULL," + "STATUS VARCHAR(50) NOT NULL,"
					+ "IS_AUTHORITY VARCHAR(5) NOT NULL," + "AUTHORITY VARCHAR(255) NOT NULL,"
					+ "CERTIFICATE TEXT NOT NULL," + "CRL TEXT, INDEX document_index (NAME));";
				db.update(trust);
			}
			dbBuilt = true;
		}
	}


	public void destroy() throws GTSInternalFault {
		db.update("DROP TABLE IF EXISTS " + TRUSTED_AUTHORITIES_TABLE);
		dbBuilt = false;
	}
}
