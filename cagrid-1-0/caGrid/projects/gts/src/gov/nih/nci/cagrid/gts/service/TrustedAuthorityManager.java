package gov.nih.nci.cagrid.gts.service;

import gov.nih.nci.cagrid.gridca.common.CertUtil;
import gov.nih.nci.cagrid.gts.bean.Lifetime;
import gov.nih.nci.cagrid.gts.bean.Status;
import gov.nih.nci.cagrid.gts.bean.TrustedAuthority;
import gov.nih.nci.cagrid.gts.bean.TrustedAuthorityFilter;
import gov.nih.nci.cagrid.gts.common.Database;
import gov.nih.nci.cagrid.gts.service.db.DBManager;
import gov.nih.nci.cagrid.gts.service.db.TrustedAuthorityTable;
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: TrustedAuthorityManager.java,v 1.1 2006/03/08 19:48:46 langella
 *          Exp $
 */
public class TrustedAuthorityManager {

	private Log log;

	private boolean dbBuilt = false;

	private Database db;

	private String gtsURI;

	private TrustLevelLookup lookup;

	private DBManager dbManager;


	public TrustedAuthorityManager(String gtsURI, TrustLevelLookup lookup, DBManager dbManager) {
		log = LogFactory.getLog(this.getClass().getName());
		this.gtsURI = gtsURI;
		this.dbManager = dbManager;
		this.db = dbManager.getDatabase();
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

			sql.append("select * from " + TrustedAuthorityTable.TABLE_NAME);
			if (filter != null) {
				boolean firstAppended = false;

				if (filter.getName() != null) {
					sql = appendWhereOrAnd(firstAppended, sql);
					firstAppended = true;
					sql.append(" " + TrustedAuthorityTable.NAME + " = '" + filter.getName() + "'");
				}

				if (filter.getCertificateDN() != null) {
					sql = appendWhereOrAnd(firstAppended, sql);
					firstAppended = true;
					sql.append(" " + TrustedAuthorityTable.CERTIFICATE_DN + " = '" + filter.getCertificateDN() + "'");
				}

				if (filter.getStatus() != null) {
					sql = appendWhereOrAnd(firstAppended, sql);
					firstAppended = true;
					sql.append(" " + TrustedAuthorityTable.STATUS + "='" + filter.getStatus() + "'");
				}

				if (filter.getTrustLevel() != null) {
					sql = appendWhereOrAnd(firstAppended, sql);
					firstAppended = true;
					sql.append(" " + TrustedAuthorityTable.TRUST_LEVEL + "='" + filter.getTrustLevel() + "'");
				}

				if (filter.getIsAuthority() != null) {
					sql = appendWhereOrAnd(firstAppended, sql);
					firstAppended = true;
					sql.append(" " + TrustedAuthorityTable.IS_AUTHORITY + "='" + filter.getIsAuthority().booleanValue()
						+ "'");
				}

				if (filter.getAuthorityGTS() != null) {
					sql = appendWhereOrAnd(firstAppended, sql);
					firstAppended = true;
					sql.append(" " + TrustedAuthorityTable.AUTHORITY_GTS + " = '" + filter.getAuthorityGTS() + "'");
				}

				if (filter.getSourceGTS() != null) {
					sql = appendWhereOrAnd(firstAppended, sql);
					firstAppended = true;
					sql.append(" " + TrustedAuthorityTable.SOURCE_GTS + " = '" + filter.getSourceGTS() + "'");
				}

				if (filter.getLifetime() != null) {
					if (filter.getLifetime().equals(Lifetime.Valid)) {
						sql = appendWhereOrAnd(firstAppended, sql);
						firstAppended = true;
						Calendar cal = new GregorianCalendar();
						long time = cal.getTimeInMillis();
						sql.append(" (" + TrustedAuthorityTable.EXPIRES + "=0 OR " + TrustedAuthorityTable.EXPIRES
							+ ">" + time + ")");
					} else if (filter.getLifetime().equals(Lifetime.Expired)) {
						sql = appendWhereOrAnd(firstAppended, sql);
						firstAppended = true;
						Calendar cal = new GregorianCalendar();
						long time = cal.getTimeInMillis();
						sql.append(" (" + TrustedAuthorityTable.EXPIRES + "<>0 AND " + TrustedAuthorityTable.EXPIRES
							+ "<" + time + ")");
					}
				}

			}

			ResultSet rs = s.executeQuery(sql.toString());
			while (rs.next()) {
				TrustedAuthority ta = new TrustedAuthority();
				ta.setName(rs.getString(TrustedAuthorityTable.NAME));
				ta.setTrustLevel(rs.getString(TrustedAuthorityTable.TRUST_LEVEL));
				ta.setStatus(Status.fromValue(rs.getString(TrustedAuthorityTable.STATUS)));
				ta.setIsAuthority(Boolean.valueOf(rs.getBoolean(TrustedAuthorityTable.IS_AUTHORITY)));
				ta.setAuthorityGTS(rs.getString(TrustedAuthorityTable.AUTHORITY_GTS));
				ta.setSourceGTS(rs.getString(TrustedAuthorityTable.SOURCE_GTS));
				ta.setExpires(rs.getLong(TrustedAuthorityTable.EXPIRES));
				ta.setLastUpdated(rs.getLong(TrustedAuthorityTable.LAST_UPDATED));
				ta.setCertificate(new gov.nih.nci.cagrid.gts.bean.X509Certificate(rs
					.getString(TrustedAuthorityTable.CERTIFICATE)));
				String crl = rs.getString(TrustedAuthorityTable.CRL);
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
			this.log.error(
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

		TrustedAuthority curr = this.getTrustedAuthority(ta.getName());
		StringBuffer sql = new StringBuffer();
		boolean needsUpdate = false;
		if (internal) {
			if (!curr.getAuthorityGTS().equals(gtsURI)) {
				IllegalTrustedAuthorityFault fault = new IllegalTrustedAuthorityFault();
				fault.setFaultString("The Trusted Authority cannot be updated, the GTS (" + gtsURI
					+ ") is not its authority!!!");
				throw fault;
			}

			if ((clean(ta.getAuthorityGTS()) != null) && (!ta.getAuthorityGTS().equals(curr.getAuthorityGTS()))) {
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

			if ((clean(ta.getSourceGTS()) != null) && (!ta.getSourceGTS().equals(curr.getSourceGTS()))) {
				IllegalTrustedAuthorityFault fault = new IllegalTrustedAuthorityFault();
				fault.setFaultString("The source trust service for a Trusted Authority cannot be changed");
				throw fault;
			}

		} else {

			if ((curr.getIsAuthority().booleanValue()) && (!ta.getAuthorityGTS().equals(gtsURI))) {
				IllegalTrustedAuthorityFault fault = new IllegalTrustedAuthorityFault();
				fault.setFaultString("The Trusted Authority " + ta.getName()
					+ " cannot be updated, a conflict was detected, this gts (" + gtsURI
					+ ") was specified as its authority, however the URI of another GTS ( " + ta.getAuthorityGTS()
					+ ") was specified.");
				throw fault;
			}

			if (!ta.getAuthorityGTS().equals(curr.getAuthorityGTS())) {
				buildUpdate(needsUpdate, sql, TrustedAuthorityTable.AUTHORITY_GTS, ta.getAuthorityGTS());
				needsUpdate = true;
			}

			if (ta.getCertificate() != null) {
				if ((clean(ta.getCertificate().getCertificateEncodedString()) != null)
					&& (!ta.getCertificate().equals(curr.getCertificate()))) {
					X509Certificate cert = checkAndExtractCertificate(ta);
					if ((!ta.getName().equals(cert.getSubjectDN().toString()))) {
						IllegalTrustedAuthorityFault fault = new IllegalTrustedAuthorityFault();
						fault
							.setFaultString("The Trusted Authority Name must match the subject of the Trusted Authority's certificate");
						throw fault;
					}

					buildUpdate(needsUpdate, sql, TrustedAuthorityTable.CERTIFICATE, ta.getCertificate()
						.getCertificateEncodedString());
					needsUpdate = true;
				}
			}

			if (!ta.getSourceGTS().equals(curr.getSourceGTS())) {
				buildUpdate(needsUpdate, sql, TrustedAuthorityTable.SOURCE_GTS, ta.getSourceGTS());
				needsUpdate = true;
			}

			if (ta.getExpires() != curr.getExpires()) {
				buildUpdate(needsUpdate, sql, TrustedAuthorityTable.EXPIRES, ta.getExpires());
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
				buildUpdate(needsUpdate, sql, TrustedAuthorityTable.CRL, ta.getCRL().getCrlEncodedString());
				needsUpdate = true;
			}
		}

		if ((ta.getStatus() != null) && (!ta.getStatus().equals(curr.getStatus()))) {
			buildUpdate(needsUpdate, sql, TrustedAuthorityTable.STATUS, ta.getStatus().getValue());
			needsUpdate = true;
		}

		if ((ta.getTrustLevel() != null) && (!ta.getTrustLevel().equals(curr.getTrustLevel()))) {
			if (!lookup.doesTrustLevelExist(ta.getTrustLevel())) {
				IllegalTrustedAuthorityFault fault = new IllegalTrustedAuthorityFault();
				fault.setFaultString("The Trusted Authority " + ta.getName()
					+ " could not be updated, the trust level " + ta.getTrustLevel() + " does not exist.");
				throw fault;
			}
			buildUpdate(needsUpdate, sql, TrustedAuthorityTable.TRUST_LEVEL, ta.getTrustLevel());
			needsUpdate = true;
		}

		try {
			if (!ta.equals(curr)) {
				if (needsUpdate) {
					Calendar c = new GregorianCalendar();
					ta.setLastUpdated(c.getTimeInMillis());
					buildUpdate(needsUpdate, sql, TrustedAuthorityTable.LAST_UPDATED, ta.getLastUpdated());
					sql.append(" WHERE " + TrustedAuthorityTable.NAME + "='" + ta.getName() + "'");
					db.update(sql.toString());
				}
			}
		} catch (Exception e) {
			this.log.error("Unexpected database error incurred in updating " + ta.getName()
				+ ", the following statement generated the error: \n" + sql.toString() + "\n", e);
			GTSInternalFault fault = new GTSInternalFault();
			fault.setFaultString("Unexpected error occurred in updating " + ta.getName() + ".");
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
			sql.append("UPDATE " + TrustedAuthorityTable.TABLE_NAME + " SET ");
			sql.append(field).append("='").append(value).append("'");
		}

	}


	private void buildUpdate(boolean needsUpdate, StringBuffer sql, String field, long value) {
		if (needsUpdate) {
			sql.append(",").append(field).append("=").append(value).append("");
		} else {
			sql.append("UPDATE " + TrustedAuthorityTable.TABLE_NAME + " SET ");
			sql.append(field).append("=").append(value);
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
		String sql = "select * from " + TrustedAuthorityTable.TABLE_NAME + " where " + TrustedAuthorityTable.NAME
			+ "='" + name + "'";
		Connection c = null;
		try {
			c = db.getConnection();
			Statement s = c.createStatement();
			ResultSet rs = s.executeQuery(sql);
			if (rs.next()) {
				TrustedAuthority ta = new TrustedAuthority();
				ta.setName(rs.getString(TrustedAuthorityTable.NAME));
				ta.setTrustLevel(rs.getString(TrustedAuthorityTable.TRUST_LEVEL));
				ta.setStatus(Status.fromValue(rs.getString(TrustedAuthorityTable.STATUS)));
				ta.setIsAuthority(Boolean.valueOf(rs.getBoolean(TrustedAuthorityTable.IS_AUTHORITY)));
				ta.setAuthorityGTS(rs.getString(TrustedAuthorityTable.AUTHORITY_GTS));
				ta.setSourceGTS(rs.getString(TrustedAuthorityTable.SOURCE_GTS));
				ta.setExpires(rs.getLong(TrustedAuthorityTable.EXPIRES));
				ta.setLastUpdated(rs.getLong(TrustedAuthorityTable.LAST_UPDATED));
				ta.setCertificate(new gov.nih.nci.cagrid.gts.bean.X509Certificate(rs
					.getString(TrustedAuthorityTable.CERTIFICATE)));
				String crl = rs.getString(TrustedAuthorityTable.CRL);
				if ((crl != null) && (crl.trim().length() > 0)) {
					ta.setCRL(new gov.nih.nci.cagrid.gts.bean.X509CRL(crl));
				}
				return ta;
			}
			rs.close();
			s.close();
		} catch (Exception e) {
			this.log.error("Unexpected database error incurred in obtaining the Trusted Authority, " + name
				+ ", the following statement generated the error: \n" + sql + "\n", e);
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
		String sql = "select count(*) from " + TrustedAuthorityTable.TABLE_NAME + " where "
			+ TrustedAuthorityTable.NAME + "='" + name + "'";
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
			this.log.error("Unexpected database error incurred in odetermining if the TrustedAuthority " + name
				+ " exists, the following statement generated the error: \n" + sql + "\n", e);
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
			String sql = "delete FROM " + TrustedAuthorityTable.TABLE_NAME + " where " + TrustedAuthorityTable.NAME
				+ "='" + name + "'";
			try {
				db.update(sql);
			} catch (Exception e) {
				this.log.error("Unexpected database error incurred in removing the Trusted Authority, " + name
					+ ", the following statement generated the error: \n" + sql + "\n", e);
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
		String sql = "delete FROM " + TrustedAuthorityTable.TABLE_NAME + " where " + TrustedAuthorityTable.TRUST_LEVEL
			+ "='" + level + "'";
		try {
			db.update(sql);
		} catch (Exception e) {
			this.log.error(
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
			Calendar c = new GregorianCalendar();
			ta.setLastUpdated(c.getTimeInMillis());
			insert.append("INSERT INTO " + TrustedAuthorityTable.TABLE_NAME + " SET " + TrustedAuthorityTable.NAME
				+ "='" + ta.getName() + "'," + TrustedAuthorityTable.CERTIFICATE_DN + "='"
				+ cert.getSubjectDN().toString() + "'," + TrustedAuthorityTable.TRUST_LEVEL + "='" + ta.getTrustLevel()
				+ "', " + TrustedAuthorityTable.STATUS + "='" + ta.getStatus().getValue() + "', "
				+ TrustedAuthorityTable.IS_AUTHORITY + "='" + ta.getIsAuthority().booleanValue() + "',"
				+ TrustedAuthorityTable.AUTHORITY_GTS + "='" + ta.getAuthorityGTS() + "',"
				+ TrustedAuthorityTable.SOURCE_GTS + "='" + ta.getSourceGTS() + "', " + TrustedAuthorityTable.EXPIRES
				+ "=" + ta.getExpires() + ", " + TrustedAuthorityTable.LAST_UPDATED + "=" + ta.getLastUpdated() + ", "
				+ TrustedAuthorityTable.CERTIFICATE + "='" + ta.getCertificate().getCertificateEncodedString() + "'");

			if (crl != null) {
				insert.append("," + TrustedAuthorityTable.CRL + "='" + ta.getCRL().getCrlEncodedString() + "'");
			}
			db.update(insert.toString());
		} catch (Exception e) {
			this.log.error("Unexpected database error incurred in adding the Trusted Authority, " + ta.getName()
				+ ", the following statement generated the error: \n" + insert.toString() + "\n", e);
			GTSInternalFault fault = new GTSInternalFault();
			fault.setFaultString("Unexpected error adding the Trusted Authority, " + ta.getName() + "!!!");
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
		if ((ta.getName() != null) && (!ta.getName().equals(cert.getSubjectDN().toString()))) {
			IllegalTrustedAuthorityFault fault = new IllegalTrustedAuthorityFault();
			fault
				.setFaultString("The Trusted Authority Name must match the subject of the Trusted Authority's certificate");
			throw fault;
		} else {
			ta.setName(cert.getSubjectDN().toString());
		}

		if (this.doesTrustedAuthorityExist(ta.getName())) {
			IllegalTrustedAuthorityFault fault = new IllegalTrustedAuthorityFault();
			fault.setFaultString("The Trusted Authority " + ta.getName() + " already exists.");
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
			fault.setFaultString("The Trusted Authority " + ta.getName() + " could not be added, the trust level "
				+ ta.getTrustLevel() + " does not exist.");
			throw fault;
		}

		if (ta.getStatus() == null) {
			IllegalTrustedAuthorityFault fault = new IllegalTrustedAuthorityFault();
			fault.setFaultString("No status specified for the Trusted Authority!!!");
			throw fault;
		}
		if (internal) {
			ta.setIsAuthority(Boolean.TRUE);
			ta.setAuthorityGTS(gtsURI);
			ta.setSourceGTS(gtsURI);
			ta.setExpires(0);
		} else {
			if ((ta.getIsAuthority() == null)) {
				IllegalTrustedAuthorityFault fault = new IllegalTrustedAuthorityFault();
				fault.setFaultString("The Trusted Authority " + ta.getName()
					+ " cannot be added because it does not specify whether or not this GTS is the authority of it.");
				throw fault;
			}

			if (ta.getAuthorityGTS() == null) {
				IllegalTrustedAuthorityFault fault = new IllegalTrustedAuthorityFault();
				fault.setFaultString("The Trusted Authority " + ta.getName()
					+ " cannot be added because it does not specify an authority trust service.");
				throw fault;

			}

			if (ta.getSourceGTS() == null) {
				IllegalTrustedAuthorityFault fault = new IllegalTrustedAuthorityFault();
				fault.setFaultString("The Trusted Authority " + ta.getName()
					+ " cannot be added because it does not specify an source trust service.");
				throw fault;
			}

			if ((!ta.getIsAuthority().booleanValue()) && (ta.getExpires() <= 0)) {
				IllegalTrustedAuthorityFault fault = new IllegalTrustedAuthorityFault();
				fault.setFaultString("The Trusted Authority " + ta.getName()
					+ " cannot be added because it does not specify an expiration.");
				throw fault;
			}

			if ((ta.getIsAuthority().booleanValue()) && (!ta.getAuthorityGTS().equals(gtsURI))) {
				IllegalTrustedAuthorityFault fault = new IllegalTrustedAuthorityFault();
				fault.setFaultString("The Trusted Authority " + ta.getName()
					+ " cannot be added, a conflict was detected, this gts (" + gtsURI
					+ ") was specified as its authority, however the URI of another GTS ( " + ta.getAuthorityGTS()
					+ ") was specified.");
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
			try {
				db.createDatabase();
				if (!this.db.tableExists(TrustedAuthorityTable.TABLE_NAME)) {
					String sql = dbManager.getTrustedAuthorityTable().getCreateTableSQL();
					db.update(sql);
				}
				dbBuilt = true;
			} catch (Exception e) {
				this.log.error("Unexpected error in creating the database.", e);
				GTSInternalFault fault = new GTSInternalFault();
				fault.setFaultString("Unexpected error in creating the database.");
				throw fault;
			}
		}
	}


	public synchronized void clearDatabase() throws GTSInternalFault {
		try {
			buildDatabase();
			db.update("delete FROM " + TrustedAuthorityTable.TABLE_NAME);
		} catch (Exception e) {
			this.log.error("Unexpected error in removing the database.", e);
			GTSInternalFault fault = new GTSInternalFault();
			fault.setFaultString("Unexpected error in removing the database.");
			throw fault;
		}
	}
}
