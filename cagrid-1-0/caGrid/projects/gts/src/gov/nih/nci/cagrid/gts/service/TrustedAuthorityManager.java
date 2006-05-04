package gov.nih.nci.cagrid.gts.service;

import gov.nih.nci.cagrid.gridca.common.CertUtil;
import gov.nih.nci.cagrid.gts.bean.Lifetime;
import gov.nih.nci.cagrid.gts.bean.Status;
import gov.nih.nci.cagrid.gts.bean.TrustedAuthority;
import gov.nih.nci.cagrid.gts.bean.TrustedAuthorityFilter;
import gov.nih.nci.cagrid.gts.common.Database;
import gov.nih.nci.cagrid.gts.stubs.GTSInternalFault;
import gov.nih.nci.cagrid.gts.stubs.IllegalTrustedAuthorityFault;
import gov.nih.nci.cagrid.gts.stubs.InvalidTrustedAuthorityFault;

import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
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

	private TrustLevelLookup lookup;


	public TrustedAuthorityManager(String gtsURI, TrustLevelLookup lookup, Database db) {
		logger = Logger.getLogger(this.getClass().getName());
		this.gtsURI = gtsURI;
		this.db = db;
		this.lookup = lookup;
	}


	public synchronized TrustedAuthority[] findTrustAuthorities(TrustedAuthorityFilter filter) throws GTSInternalFault {

		this.buildDatabase();
		Connection c = null;
		List authorities = new ArrayList();
		StringBuffer sql = new StringBuffer();
		try {
			c = db.getConnection();
			Statement s = c.createStatement();

			sql.append("select * from " + TRUSTED_AUTHORITIES_TABLE);
			if (filter != null) {
				boolean firstAppended = false;

				if (filter.getTrustedAuthorityName() != null) {
					sql = appendWhereOrAnd(firstAppended, sql);
					firstAppended = true;
					sql.append(" NAME = '" + filter.getTrustedAuthorityName() + "'");
				}

				if (filter.getCertificateDN() != null) {
					sql = appendWhereOrAnd(firstAppended, sql);
					firstAppended = true;
					sql.append(" CERTIFICATE_DN = '" + filter.getCertificateDN() + "'");
				}

				if (filter.getStatus() != null) {
					sql = appendWhereOrAnd(firstAppended, sql);
					firstAppended = true;
					sql.append(" STATUS='" + filter.getStatus() + "'");
				}

				if (filter.getTrustLevel() != null) {
					sql = appendWhereOrAnd(firstAppended, sql);
					firstAppended = true;
					sql.append(" TRUST_LEVEL='" + filter.getTrustLevel() + "'");
				}

				if (filter.getIsAuthority() != null) {
					sql = appendWhereOrAnd(firstAppended, sql);
					firstAppended = true;
					sql.append(" IS_AUTHORITY='" + filter.getIsAuthority().booleanValue() + "'");
				}

				if (filter.getAuthorityTrustService() != null) {
					sql = appendWhereOrAnd(firstAppended, sql);
					firstAppended = true;
					sql.append(" AUTHORITY_GTS = '" + filter.getAuthorityTrustService() + "'");
				}

				if (filter.getSourceTrustService() != null) {
					sql = appendWhereOrAnd(firstAppended, sql);
					firstAppended = true;
					sql.append(" SOURCE_GTS = '" + filter.getSourceTrustService() + "'");
				}

				if (filter.getLifetime() != null) {
					if (filter.getLifetime().equals(Lifetime.Valid)) {
						sql = appendWhereOrAnd(firstAppended, sql);
						firstAppended = true;
						Calendar cal = new GregorianCalendar();
						long time = cal.getTimeInMillis();
						sql.append(" (EXPIRES=0 OR EXPIRES>" + time + ")");
					} else if (filter.getLifetime().equals(Lifetime.Expired)) {
						sql = appendWhereOrAnd(firstAppended, sql);
						firstAppended = true;
						Calendar cal = new GregorianCalendar();
						long time = cal.getTimeInMillis();
						sql.append(" EXPIRES<" + time);
					}
				}

			}

			ResultSet rs = s.executeQuery(sql.toString());
			while (rs.next()) {
				TrustedAuthority ta = new TrustedAuthority();
				ta.setTrustedAuthorityName(rs.getString("NAME"));
				ta.setTrustLevel(rs.getString("TRUST_LEVEL"));
				ta.setStatus(Status.fromValue(rs.getString("STATUS")));
				ta.setIsAuthority(Boolean.valueOf(rs.getBoolean("IS_AUTHORITY")));
				ta.setAuthorityTrustService(rs.getString("AUTHORITY_GTS"));
				ta.setSourceTrustService(rs.getString("SOURCE_GTS"));
				ta.setExpires(rs.getLong("EXPIRES"));
				ta.setCertificate(new gov.nih.nci.cagrid.gts.bean.X509Certificate(rs.getString("CERTIFICATE")));
				String crl = rs.getString("CRL");
				if ((crl != null) && (crl.trim().length() > 0)) {
					ta.setCRL(new gov.nih.nci.cagrid.gts.bean.X509CRL(crl));
				}
				authorities.add(ta);
			}
			rs.close();
			s.close();

			TrustedAuthority[] list = new TrustedAuthority[authorities.size()];
			for (int i = 0; i < list.length; i++) {
				list[i] = (TrustedAuthority) authorities.get(i);
			}
			return list;

		} catch (Exception e) {
			this.logger.log(Level.SEVERE,
				"Unexpected database error incurred in finding trusted authorities, the following statement generated the error: \n"
					+ sql.toString() + "\n", e);
			GTSInternalFault fault = new GTSInternalFault();
			fault.setFaultString("Unexpected error occurred in finding Trusted Authorities");
			throw fault;
		} finally {
			db.releaseConnection(c);
		}
	}


	public synchronized void updateTrustedAuthority(TrustedAuthority ta) throws GTSInternalFault,
		IllegalTrustedAuthorityFault, InvalidTrustedAuthorityFault {
		updateTrustedAuthority(ta, true);
	}


	public synchronized void updateTrustedAuthority(TrustedAuthority ta, boolean internal) throws GTSInternalFault,
		IllegalTrustedAuthorityFault, InvalidTrustedAuthorityFault {

		TrustedAuthority curr = this.getTrustedAuthority(ta.getTrustedAuthorityName());
		StringBuffer sql = new StringBuffer();
		boolean needsUpdate = false;
		if (internal) {
			// TODO: ADD TEST FOR THIS
			if (!curr.getAuthorityTrustService().equals(gtsURI)) {
				IllegalTrustedAuthorityFault fault = new IllegalTrustedAuthorityFault();
				fault.setFaultString("The Trusted Authority cannot be updated, this GTS is not its authority!!!");
				throw fault;
			}

			if ((clean(ta.getAuthorityTrustService()) != null)
				&& (!ta.getAuthorityTrustService().equals(curr.getAuthorityTrustService()))) {
				IllegalTrustedAuthorityFault fault = new IllegalTrustedAuthorityFault();
				fault.setFaultString("The authority trust service for a Trusted Authority cannot be changed");
				throw fault;
			}

			if (ta.getCertificate() != null) {
				if ((clean(ta.getCertificate().getCertificateEncodedString()) != null)
					&& (!ta.getCertificate().equals(curr.getCertificate()))) {
					IllegalTrustedAuthorityFault fault = new IllegalTrustedAuthorityFault();
					fault.setFaultString("The certificate for a Trusted Authority cannot be changed");
					throw fault;
				}
			}

			if (!ta.getSourceTrustService().equals(curr.getSourceTrustService())) {
				IllegalTrustedAuthorityFault fault = new IllegalTrustedAuthorityFault();
				fault.setFaultString("The source trust service for a Trusted Authority cannot be changed");
				throw fault;
			}

			if (ta.getExpires() != curr.getExpires()) {
				IllegalTrustedAuthorityFault fault = new IllegalTrustedAuthorityFault();
				fault.setFaultString("The expiration for a Trusted Authority cannot be changed");
				throw fault;
			}
		} else {

			if ((curr.getIsAuthority().booleanValue()) && (!ta.getAuthorityTrustService().equals(gtsURI))) {
				IllegalTrustedAuthorityFault fault = new IllegalTrustedAuthorityFault();
				fault.setFaultString("The Trusted Authority " + ta.getTrustedAuthorityName()
					+ " cannot be updated, a conflict was detected, this gts (" + gtsURI
					+ ") was specified as its authority, however the URI of another GTS ( "
					+ ta.getAuthorityTrustService() + ") was specified.");
				throw fault;
			}

			if (!ta.getAuthorityTrustService().equals(curr.getAuthorityTrustService())) {
				buildUpdate(needsUpdate, sql, "AUTHORITY_GTS", ta.getAuthorityTrustService());
				needsUpdate = true;
			}

			if (ta.getCertificate() != null) {
				if ((clean(ta.getCertificate().getCertificateEncodedString()) != null)
					&& (!ta.getCertificate().equals(curr.getCertificate()))) {
					X509Certificate cert = checkAndExtractCertificate(ta);
					if ((!ta.getTrustedAuthorityName().equals(cert.getSubjectDN().toString()))) {
						IllegalTrustedAuthorityFault fault = new IllegalTrustedAuthorityFault();
						fault
							.setFaultString("The Trusted Authority Name must match the subject of the Trusted Authority's certificate");
						throw fault;
					}

					buildUpdate(needsUpdate, sql, "CERTIFICATE", ta.getCertificate().getCertificateEncodedString());
					needsUpdate = true;
				}
			}

			if (!ta.getSourceTrustService().equals(curr.getSourceTrustService())) {
				buildUpdate(needsUpdate, sql, "SOURCE_GTS", ta.getSourceTrustService());
				needsUpdate = true;
			}

			if (ta.getExpires() != curr.getExpires()) {
				buildUpdate(needsUpdate, sql, "EXPIRES", ta.getExpires());
				needsUpdate = true;
			}

		}

		if ((ta.getIsAuthority() != null) && (!ta.getIsAuthority().equals(curr.getIsAuthority()))) {
			IllegalTrustedAuthorityFault fault = new IllegalTrustedAuthorityFault();
			fault.setFaultString("The authority trust service for a Trusted Authority cannot be changed");
			throw fault;
		}

		if (ta.getCRL() != null) {
			if ((clean(ta.getCRL().getCrlEncodedString()) != null) && (!ta.getCRL().equals(curr.getCRL()))) {
				X509Certificate cert = checkAndExtractCertificate(ta);
				checkAndExtractCRL(ta, cert);
				buildUpdate(needsUpdate, sql, "CRL", ta.getCRL().getCrlEncodedString());
				needsUpdate = true;
			}
		}

		if ((ta.getStatus() != null) && (!ta.getStatus().equals(curr.getStatus()))) {
			buildUpdate(needsUpdate, sql, "STATUS", ta.getStatus().getValue());
			needsUpdate = true;
		}

		if ((ta.getTrustLevel() != null) && (!ta.getTrustLevel().equals(curr.getTrustLevel()))) {
			if (!lookup.doesTrustLevelExist(ta.getTrustLevel())) {
				IllegalTrustedAuthorityFault fault = new IllegalTrustedAuthorityFault();
				fault.setFaultString("The Trusted Authority " + ta.getTrustedAuthorityName()
					+ " could not be updated, the trust level " + ta.getTrustLevel() + " does not exist.");
				throw fault;
			}
			buildUpdate(needsUpdate, sql, "TRUST_LEVEL", ta.getTrustLevel());
			needsUpdate = true;
		}

		try {
			if (!ta.equals(curr)) {
				if (needsUpdate) {
					sql.append(" WHERE NAME='" + ta.getTrustedAuthorityName() + "'");
					db.update(sql.toString());
				}
			}
		} catch (Exception e) {
			this.logger.log(Level.SEVERE, "Unexpected database error incurred in updating "
				+ ta.getTrustedAuthorityName() + ", the following statement generated the error: \n" + sql.toString()
				+ "\n", e);
			GTSInternalFault fault = new GTSInternalFault();
			fault.setFaultString("Unexpected error occurred in updating " + ta.getTrustedAuthorityName() + ".");
			throw fault;
		}
	}


	private String clean(String s) {
		if ((s == null) || (s.trim().length() == 0)) {
			return null;
		} else {
			return s;
		}
	}


	private void buildUpdate(boolean needsUpdate, StringBuffer sql, String field, String value) {
		if (needsUpdate) {
			sql.append(",").append(field).append("='").append(value).append("'");
		} else {
			sql.append("UPDATE " + TRUSTED_AUTHORITIES_TABLE + " SET ");
			sql.append(field).append("='").append(value).append("'");
		}

	}


	private void buildUpdate(boolean needsUpdate, StringBuffer sql, String field, long value) {
		if (needsUpdate) {
			sql.append(",").append(field).append("=").append(value).append("");
		} else {
			sql.append("UPDATE " + TRUSTED_AUTHORITIES_TABLE + " SET ");
			sql.append(field).append("='").append(value).append("'");
		}

	}


	private StringBuffer appendWhereOrAnd(boolean firstAppended, StringBuffer sql) {
		if (firstAppended) {
			sql.append(" AND ");
		} else {
			sql.append(" WHERE");
		}
		return sql;
	}


	public synchronized TrustedAuthority getTrustedAuthority(String name) throws GTSInternalFault,
		InvalidTrustedAuthorityFault {
		String sql = "select * from " + TRUSTED_AUTHORITIES_TABLE + " where NAME='" + name + "'";
		Connection c = null;
		try {
			c = db.getConnection();
			Statement s = c.createStatement();
			ResultSet rs = s.executeQuery(sql);
			if (rs.next()) {
				TrustedAuthority ta = new TrustedAuthority();
				ta.setTrustedAuthorityName(rs.getString("NAME"));
				ta.setTrustLevel(rs.getString("TRUST_LEVEL"));
				ta.setStatus(Status.fromValue(rs.getString("STATUS")));
				ta.setIsAuthority(Boolean.valueOf(rs.getBoolean("IS_AUTHORITY")));
				ta.setAuthorityTrustService(rs.getString("AUTHORITY_GTS"));
				ta.setSourceTrustService(rs.getString("SOURCE_GTS"));
				ta.setExpires(rs.getLong("EXPIRES"));
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
				+ name + ", the following statement generated the error: \n" + sql + "\n", e);
			GTSInternalFault fault = new GTSInternalFault();
			fault.setFaultString("Unexpected error obtaining the TrustedAuthority " + name);
			throw fault;
		} finally {
			db.releaseConnection(c);
		}
		InvalidTrustedAuthorityFault fault = new InvalidTrustedAuthorityFault();
		fault.setFaultString("The TrustedAuthority " + name + " does not exist.");
		throw fault;
	}


	public synchronized boolean doesTrustedAuthorityExist(String name) throws GTSInternalFault {
		this.buildDatabase();
		String sql = "select count(*) from " + TRUSTED_AUTHORITIES_TABLE + " where NAME='" + name + "'";
		Connection c = null;
		boolean exists = false;
		try {
			c = db.getConnection();
			Statement s = c.createStatement();
			ResultSet rs = s.executeQuery(sql);
			if (rs.next()) {
				int count = rs.getInt(1);
				if (count > 0) {
					exists = true;
				}
			}
			rs.close();
			s.close();
		} catch (Exception e) {
			this.logger.log(Level.SEVERE, "Unexpected database error incurred in odetermining if the TrustedAuthority "
				+ name + " exists, the following statement generated the error: \n" + sql + "\n", e);
			GTSInternalFault fault = new GTSInternalFault();
			fault.setFaultString("Unexpected error in determining if the TrustedAuthority " + name + " exists.");
			throw fault;
		} finally {
			db.releaseConnection(c);
		}
		return exists;
	}


	public synchronized void removeTrustedAuthority(String name) throws GTSInternalFault, InvalidTrustedAuthorityFault {
		if (doesTrustedAuthorityExist(name)) {
			String sql = "delete FROM " + TRUSTED_AUTHORITIES_TABLE + " where NAME='" + name + "'";
			try {
				db.update(sql);
			} catch (Exception e) {
				this.logger.log(Level.SEVERE, "Unexpected database error incurred in removing the Trusted Authority, "
					+ name + ", the following statement generated the error: \n" + sql + "\n", e);
				GTSInternalFault fault = new GTSInternalFault();
				fault.setFaultString("Unexpected error removing the TrustedAuthority " + name);
				throw fault;
			}
		} else {
			InvalidTrustedAuthorityFault fault = new InvalidTrustedAuthorityFault();
			fault.setFaultString("The TrustedAuthority " + name + " does not exist.");
			throw fault;
		}
	}


	public synchronized void removeTrustedAuthoritiesByLevel(String level) throws GTSInternalFault {
		buildDatabase();
		String sql = "delete FROM " + TRUSTED_AUTHORITIES_TABLE + " where TRUST_LEVEL='" + level + "'";
		try {
			db.update(sql);
		} catch (Exception e) {
			this.logger.log(Level.SEVERE,
				"Unexpected database error incurred in removing the Trusted Authorities with the trust level, " + level
					+ ", the following statement generated the error: \n" + sql + "\n", e);
			GTSInternalFault fault = new GTSInternalFault();
			fault.setFaultString("Unexpected error removing the TrustedAuthorities with the trust level, " + level);
			throw fault;
		}
	}


	private synchronized void insertTrustedAuthority(TrustedAuthority ta, X509Certificate cert, X509CRL crl)
		throws GTSInternalFault {
		StringBuffer insert = new StringBuffer();
		try {

			insert.append("INSERT INTO " + TRUSTED_AUTHORITIES_TABLE + " SET NAME='" + ta.getTrustedAuthorityName()
				+ "',CERTIFICATE_DN='" + cert.getSubjectDN().toString() + "',TRUST_LEVEL='" + ta.getTrustLevel()
				+ "', STATUS='" + ta.getStatus().getValue() + "', IS_AUTHORITY='" + ta.getIsAuthority().booleanValue()
				+ "',AUTHORITY_GTS='" + ta.getAuthorityTrustService() + "',SOURCE_GTS='" + ta.getSourceTrustService()
				+ "', EXPIRES=" + ta.getExpires() + ", CERTIFICATE='"
				+ ta.getCertificate().getCertificateEncodedString() + "'");

			if (crl != null) {
				insert.append(",CRL='" + ta.getCRL().getCrlEncodedString() + "'");
			}
			db.update(insert.toString());
		} catch (Exception e) {
			this.logger.log(Level.SEVERE, "Unexpected database error incurred in adding the Trusted Authority, "
				+ ta.getTrustedAuthorityName() + ", the following statement generated the error: \n"
				+ insert.toString() + "\n", e);
			GTSInternalFault fault = new GTSInternalFault();
			fault.setFaultString("Unexpected error adding the Trusted Authority, " + ta.getTrustedAuthorityName()
				+ "!!!");
			throw fault;
		}
	}


	public synchronized TrustedAuthority addTrustedAuthority(TrustedAuthority ta) throws GTSInternalFault,
		IllegalTrustedAuthorityFault {
		return this.addTrustedAuthority(ta, true);
	}


	public synchronized TrustedAuthority addTrustedAuthority(TrustedAuthority ta, boolean internal)
		throws GTSInternalFault, IllegalTrustedAuthorityFault {
		this.buildDatabase();
		X509Certificate cert = checkAndExtractCertificate(ta);
		if ((ta.getTrustedAuthorityName() != null)
			&& (!ta.getTrustedAuthorityName().equals(cert.getSubjectDN().toString()))) {
			IllegalTrustedAuthorityFault fault = new IllegalTrustedAuthorityFault();
			fault
				.setFaultString("The Trusted Authority Name must match the subject of the Trusted Authority's certificate");
			throw fault;
		} else {
			ta.setTrustedAuthorityName(cert.getSubjectDN().toString());
		}

		if (this.doesTrustedAuthorityExist(ta.getTrustedAuthorityName())) {
			IllegalTrustedAuthorityFault fault = new IllegalTrustedAuthorityFault();
			fault.setFaultString("The Trusted Authority " + ta.getTrustedAuthorityName() + " already exists.");
			throw fault;
		}

		X509CRL crl = checkAndExtractCRL(ta, cert);

		if (ta.getTrustLevel() == null) {
			IllegalTrustedAuthorityFault fault = new IllegalTrustedAuthorityFault();
			fault.setFaultString("No trust level specified for the Trusted Authority!!!");
			throw fault;
		}

		if (!lookup.doesTrustLevelExist(ta.getTrustLevel())) {
			IllegalTrustedAuthorityFault fault = new IllegalTrustedAuthorityFault();
			fault.setFaultString("The Trusted Authority " + ta.getTrustedAuthorityName()
				+ " could not be added, the trust level " + ta.getTrustLevel() + " does not exist.");
			throw fault;
		}

		if (ta.getStatus() == null) {
			IllegalTrustedAuthorityFault fault = new IllegalTrustedAuthorityFault();
			fault.setFaultString("No status specified for the Trusted Authority!!!");
			throw fault;
		}
		if (internal) {
			if ((ta.getIsAuthority() != null) && (!ta.getIsAuthority().booleanValue())) {
				logger
					.log(
						Level.WARNING,
						"The Trusted Authority "
							+ ta.getTrustedAuthorityName()
							+ ", specified does not make the Trust Service the an authority, this will be ignored since adding a Trust Authority makes the Trust Service an authority.");
			}

			if ((ta.getAuthorityTrustService() != null) && (!ta.getAuthorityTrustService().equals(gtsURI))) {
				logger
					.log(
						Level.WARNING,
						"The Trusted Authority "
							+ ta.getTrustedAuthorityName()
							+ ", specified an authority Trust Service, this will be ignored since adding a Trust Authority makes the Trust Service an authority.");

			}

			if ((ta.getSourceTrustService() != null) && (!ta.getSourceTrustService().equals(gtsURI))) {
				logger
					.log(
						Level.WARNING,
						"The Trusted Authority "
							+ ta.getTrustedAuthorityName()
							+ ", specified a Source Trust Service, this will be ignored since adding a Trust Authority makes the Trust Service its source.");

			}

			if (ta.getExpires() != 0) {
				logger
					.log(
						Level.WARNING,
						"The Trusted Authority "
							+ ta.getTrustedAuthorityName()
							+ ", specified an expiration date, this will be ignored since this trust service is its authority.");

			}

			ta.setIsAuthority(Boolean.TRUE);
			ta.setAuthorityTrustService(gtsURI);
			ta.setSourceTrustService(gtsURI);
			ta.setExpires(0);
		} else {
			if ((ta.getIsAuthority() == null)) {
				IllegalTrustedAuthorityFault fault = new IllegalTrustedAuthorityFault();
				fault.setFaultString("The Trusted Authority " + ta.getTrustedAuthorityName()
					+ " cannot be added because it does not specify whether or not this GTS is the authority of it.");
				throw fault;
			}

			if (ta.getAuthorityTrustService() == null) {
				IllegalTrustedAuthorityFault fault = new IllegalTrustedAuthorityFault();
				fault.setFaultString("The Trusted Authority " + ta.getTrustedAuthorityName()
					+ " cannot be added because it does not specify an authority trust service.");
				throw fault;

			}

			if (ta.getSourceTrustService() == null) {
				IllegalTrustedAuthorityFault fault = new IllegalTrustedAuthorityFault();
				fault.setFaultString("The Trusted Authority " + ta.getTrustedAuthorityName()
					+ " cannot be added because it does not specify an source trust service.");
				throw fault;
			}

			if ((!ta.getIsAuthority().booleanValue()) && (ta.getExpires() <= 0)) {
				IllegalTrustedAuthorityFault fault = new IllegalTrustedAuthorityFault();
				fault.setFaultString("The Trusted Authority " + ta.getTrustedAuthorityName()
					+ " cannot be added because it does not specify an expiration.");
				throw fault;
			}

			if ((ta.getIsAuthority().booleanValue()) && (!ta.getAuthorityTrustService().equals(gtsURI))) {
				IllegalTrustedAuthorityFault fault = new IllegalTrustedAuthorityFault();
				fault.setFaultString("The Trusted Authority " + ta.getTrustedAuthorityName()
					+ " cannot be added, a conflict was detected, this gts (" + gtsURI
					+ ") was specified as its authority, however the URI of another GTS ( "
					+ ta.getAuthorityTrustService() + ") was specified.");
				throw fault;
			}

		}
		insertTrustedAuthority(ta, cert, crl);
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


	public synchronized void buildDatabase() throws GTSInternalFault {
		if (!dbBuilt) {
			db.createDatabaseIfNeeded();
			if (!this.db.tableExists(TRUSTED_AUTHORITIES_TABLE)) {
				String trust = "CREATE TABLE " + TRUSTED_AUTHORITIES_TABLE + " ("
					+ "NAME VARCHAR(255) NOT NULL PRIMARY KEY," + "CERTIFICATE_DN VARCHAR(255) NOT NULL,"
					+ "TRUST_LEVEL VARCHAR(255) NOT NULL," + "STATUS VARCHAR(50) NOT NULL,"
					+ "IS_AUTHORITY VARCHAR(5) NOT NULL," + "AUTHORITY_GTS VARCHAR(255) NOT NULL,"
					+ "SOURCE_GTS VARCHAR(255) NOT NULL," + "EXPIRES BIGINT NOT NULL," + "CERTIFICATE TEXT NOT NULL,"
					+ "CRL TEXT, INDEX document_index (NAME));";
				db.update(trust);
			}
			dbBuilt = true;
		}
	}


	public void destroy() throws GTSInternalFault {
		buildDatabase();
		db.update("DROP TABLE IF EXISTS " + TRUSTED_AUTHORITIES_TABLE);
		dbBuilt = false;
	}
}
