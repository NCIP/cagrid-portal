package gov.nih.nci.cagrid.gums.ifs;

import gov.nih.nci.cagrid.gums.bean.GUMSInternalFault;
import gov.nih.nci.cagrid.gums.ca.CertificateAuthority;
import gov.nih.nci.cagrid.gums.common.Database;
import gov.nih.nci.cagrid.gums.common.GUMSObject;
import gov.nih.nci.cagrid.gums.common.ca.CertUtil;
import gov.nih.nci.cagrid.gums.common.ca.KeyUtil;
import gov.nih.nci.cagrid.gums.ifs.bean.CredentialsFault;
import gov.nih.nci.cagrid.gums.ifs.bean.IFSUser;

import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.globus.wsrf.utils.FaultHelper;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class UserManager extends GUMSObject {

	private static final String USERS_TABLE = "IFS_USERS";

	private Database db;

	private boolean dbBuilt = false;

	private CredentialsManager credentialsManager;
	
	private IFSConfiguration conf;
	
	public UserManager(Database db,IFSConfiguration conf) {
		this.db = db;
		this.credentialsManager = new CredentialsManager(db);
		this.conf = conf;
	}

	public CredentialsManager getCredentialsManager() {
		return credentialsManager;
	}

	public synchronized boolean determineIfUserExists(long idpId, String uid)
			throws GUMSInternalFault {
		buildDatabase();
		Connection c = null;
		boolean exists = false;
		try {
			c = db.getConnectionManager().getConnection();
			Statement s = c.createStatement();
			ResultSet rs = s.executeQuery("select count(*) from " + USERS_TABLE
					+ " WHERE IDP_ID=" + idpId + " AND UID='" + uid + "'");
			if (rs.next()) {
				int count = rs.getInt(1);
				if (count > 0) {
					exists = true;
				}
			}
			rs.close();
			s.close();

		} catch (Exception e) {
			GUMSInternalFault fault = new GUMSInternalFault();
			fault.setFaultString("Unexpected Database Error");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (GUMSInternalFault) helper.getFault();
			throw fault;
		} finally {
			db.getConnectionManager().releaseConnection(c);
		}
		return exists;
	}
	
	private String getCredentialsManagerUID(long idpId, String uid){
		return "[IdPId="+idpId+", UID="+uid+"]";
	}
	
	
	
	public synchronized X509Certificate createUserCredentials(long idpId, String uid)
			throws GUMSInternalFault, CredentialsFault {
		try {
			CertificateAuthority ca = IFSManager.getInstance()
					.getCertificateAuthority();
			String caSubject = ca.getCACertificate().getSubjectDN().getName();
			int caindex = caSubject.lastIndexOf(",");
			String caPreSub = caSubject.substring(0, caindex);
			String sub = caPreSub + ",OU=IdP [" + idpId + "],CN=" + uid;
			Calendar c = new GregorianCalendar();
			Date start = c.getTime();
			c.roll(Calendar.YEAR,conf.getCredentialsValidYears());
			c.roll(Calendar.MONTH,conf.getCredentialsValidMonths());
			c.roll(Calendar.DAY_OF_MONTH,conf.getCredentialsValidDays());
			Date end = c.getTime();
			if(end.after(ca.getCACertificate().getNotAfter())){
				end = ca.getCACertificate().getNotAfter();
			}
			
			KeyPair pair = KeyUtil.generateRSAKeyPair1024();
			
			PKCS10CertificationRequest req = CertUtil.generateCertficateRequest(
					sub, pair);
			X509Certificate cert = ca.requestCertificate(req,start,end);
			this.credentialsManager.addCredentials(getCredentialsManagerUID(idpId,uid),null,cert,pair.getPrivate());
			return cert;
		} catch (Exception e) {
			logError(e.getMessage(), e);
			CredentialsFault fault = new CredentialsFault();
			fault.setFaultString("Error creating credentials.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (CredentialsFault) helper.getFault();
			throw fault;
		}
	}

	public synchronized IFSUser addUser(IFSUser user) throws GUMSInternalFault {
		this.buildDatabase();
		if (determineIfUserExists(user.getIdPId(), user.getUID())) {
			try {
				// Write method for creating and setting a users credentials

			} catch (Exception e) {
				try {
					this.removeUser(user.getIdPId(), user.getUID());
				} catch (Exception ex) {
					logError(ex.getMessage(), ex);
				}
				logError(e.getMessage(), e);
				GUMSInternalFault fault = new GUMSInternalFault();
				fault
						.setFaultString("Error adding the user "
								+ user.getUID()
								+ " to the IFS, an unexpected database error occurred.");
				FaultHelper helper = new FaultHelper(fault);
				helper.addFaultCause(e);
				fault = (GUMSInternalFault) helper.getFault();
				throw fault;
			}

		} else {
			/*
			 * InvalidTrustedIdPFault fault = new InvalidTrustedIdPFault();
			 * fault.setFaultString("Cannot add the Trusted IdP, " +
			 * idp.getName() + " because it already exists."); throw fault;
			 */
		}
		return user;
	}

	public synchronized void removeUser(long idpId, String uid)
			throws GUMSInternalFault {
		this.buildDatabase();
		db.update("delete from " + USERS_TABLE + " WHERE IDP_ID=" + idpId
				+ " AND UID='" + uid + "'");
	}

	private void buildDatabase() throws GUMSInternalFault {
		if (!dbBuilt) {
			if (!this.db.tableExists(USERS_TABLE)) {
				String users = "CREATE TABLE " + USERS_TABLE + " ("
						+ "IDP_ID INT NOT NULL," + "UID VARCHAR(255) NOT NULL,"
						+ "GID VARCHAR(255) NOT NULL,"
						+ "STATUS VARCHAR(10) NOT NULL,"
						+ "ADMIN VARCHAR(5) NOT NULL, "
						+ "EMAIL VARCHAR(255) NOT NULL, "
						+ "INDEX document_index (UID));";
				db.update(users);
			}
			this.dbBuilt = true;
		}
	}

}