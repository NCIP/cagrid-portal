package gov.nih.nci.cagrid.gts.service;

import gov.nih.nci.cagrid.gridca.common.CertUtil;
import gov.nih.nci.cagrid.gts.bean.Status;
import gov.nih.nci.cagrid.gts.bean.TrustLevel;
import gov.nih.nci.cagrid.gts.bean.TrustedAuthority;
import gov.nih.nci.cagrid.gts.common.Database;
import gov.nih.nci.cagrid.gts.stubs.GTSInternalFault;
import gov.nih.nci.cagrid.gts.stubs.IllegalTrustedAuthorityFault;

import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.Level;
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


	public TrustedAuthority getTrustedAuthority(long id) throws GTSInternalFault {
		String sql = "select * from " + TRUSTED_AUTHORITIES_TABLE + " where ID=" + id;
		Connection c = null;
		try {
			c = db.getConnection();
			Statement s = c.createStatement();
			ResultSet rs = s.executeQuery(sql);
			if (rs.next()) {
				TrustedAuthority ta = new TrustedAuthority();
				ta.setTrustedAuthorityId(rs.getLong("ID"));
				ta.setTrustedAuthorityName(rs.getString("NAME"));
				ta.setTrustLevel(TrustLevel.fromValue(rs.getString("TRUST_LEVEL")));
				ta.setStatus(Status.fromValue(rs.getString("STATUS")));
				ta.setIsAuthority(rs.getBoolean("IS_AUTHORITY"));
				ta.setAuthority(rs.getString("AUTHORITY"));
				ta.setCertificate(new gov.nih.nci.cagrid.gts.bean.X509Certificate(rs.getString("CERTIFICATE")));
				String crl = rs.getString("CRL");
				if ((crl != null) && (crl.trim().length() > 0)) {
					ta.setCRL(new gov.nih.nci.cagrid.gts.bean.X509CRL(crl));
				}
				return ta;
			}
			rs.close();
			s.close();
		} catch (Exception e) {
			this.logger.log(Level.SEVERE, "Unexpected database error incurred in obtaining the Trusted Authority, "
				+ id + ", the following statement generated the error: \n" + sql + "\n", e);
			GTSInternalFault fault = new GTSInternalFault();
			fault.setFaultString("Unexpected error obtaining the TrustedAuthority " + id);
			throw fault;
		} finally {
			db.releaseConnection(c);
		}
		return null;
	}


	public TrustedAuthority addTrustedAuthority(TrustedAuthority ta) throws GTSInternalFault,
		IllegalTrustedAuthorityFault {
		this.buildDatabase();
		X509Certificate cert = checkAndExtractCertificate(ta);
		X509CRL crl = checkAndExtractCRL(ta, cert);

		if (ta.getTrustedAuthorityName() == null) {
			ta.setTrustedAuthorityName(cert.getSubjectDN().toString());
			logger.log(Level.WARNING, "No name specified for the trusted authority, defaulting to the CA Subject, "
				+ ta.getTrustedAuthorityName());
		}

		if (ta.getTrustLevel() == null) {
			IllegalTrustedAuthorityFault fault = new IllegalTrustedAuthorityFault();
			fault.setFaultString("No trust level specified for the Trusted Authority!!!");
			throw fault;
		}

		//TODO: MAY NEED TO RESET STATUS BASED ON USER PERMISSION
		
		if (ta.getStatus() == null) {
			IllegalTrustedAuthorityFault fault = new IllegalTrustedAuthorityFault();
			fault.setFaultString("No status specified for the Trusted Authority!!!");
			throw fault;
		}

		if (!ta.isIsAuthority()) {
			logger
				.log(
					Level.WARNING,
					"The Trusted Authority "
						+ ta.getTrustedAuthorityName()
						+ ", specified not make the Trust Service the an authority, this will be ignored since adding a Trust Authority makes the Trust Service an authority.");
		}

		if (ta.getAuthority() != null) {
			logger
				.log(
					Level.WARNING,
					"The Trusted Authority "
						+ ta.getTrustedAuthorityName()
						+ ", specified an authority Trust Service, this will be ignored since adding a Trust Authority makes the Trust Service an authority.");

		}

		boolean isAuthority = true;
		StringBuffer insert = new StringBuffer();
		try {

			insert.append("INSERT INTO " + TRUSTED_AUTHORITIES_TABLE + " SET NAME='" + ta.getTrustedAuthorityName()
				+ "',TRUST_LEVEL='" + ta.getTrustLevel().getValue() + "', STATUS='" + ta.getStatus().getValue()
				+ "', IS_AUTHORITY='" + isAuthority + "',AUTHORITY='" + gtsURI  + "', CERTIFICATE='"
				+ ta.getCertificate().getCertificateEncodedString() + "'");

			if (crl != null) {
				insert.append(",CRL='" + ta.getCRL().getCrlEncodedString() + "'");
			}
			long id = db.insertGetId(insert.toString());
			ta.setTrustedAuthorityId(id);
			ta.setIsAuthority(true);
			ta.setAuthority(gtsURI);
		} catch (Exception e) {
			this.logger.log(Level.SEVERE, "Unexpected database error incurred in adding the Trusted Authority, "
				+ ta.getTrustedAuthorityName() + ", the following statement generated the error: \n"
				+ insert.toString() + "\n", e);
			GTSInternalFault fault = new GTSInternalFault();
			fault.setFaultString("Unexpected error adding the Trusted Authority, " + ta.getTrustedAuthorityName()
				+ "!!!");
			throw fault;
		}
		return ta;
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
				try {
					crl.verify(signer.getPublicKey());
				} catch (Exception e) {
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
