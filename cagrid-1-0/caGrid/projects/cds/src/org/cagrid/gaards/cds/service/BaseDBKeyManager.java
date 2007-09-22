package org.cagrid.gaards.cds.service;

import gov.nih.nci.cagrid.common.FaultHelper;
import gov.nih.nci.cagrid.gridca.common.CertUtil;
import gov.nih.nci.cagrid.gridca.common.KeyUtil;
import gov.nih.nci.cagrid.gridca.common.SecurityUtil;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.cagrid.gaards.cds.conf.KeyManagerDescription;
import org.cagrid.gaards.cds.stubs.types.CDSInternalFault;
import org.cagrid.gaards.cds.stubs.types.DelegationFault;
import org.cagrid.tools.database.Database;

public abstract class BaseDBKeyManager extends KeyManager {

	private final static String PROVIDER = "BC";
	private final static String TABLE = "key_manager";
	private final static String ALIAS = "ALIAS";
	private final static String PUBLIC_KEY = "PUBLIC_KEY";
	private final static String CERTIFICATE = "CERTIFICATE";
	private final static String PRIVATE_KEY = "PRIVATE_KEY";
	private final static String IV = "IV";

	private boolean dbBuilt = false;

	public BaseDBKeyManager(KeyManagerDescription conf, Database db) {
		super(conf, db);
		SecurityUtil.init();
	}

	public boolean exists(String alias) throws CDSInternalFault {
		try {
			return getDB().exists(TABLE, ALIAS, alias);
		} catch (Exception e) {
			getLog().error(e.getMessage(), e);
			CDSInternalFault f = new CDSInternalFault();
			f.setFaultString("Unexpected Database Error.");
			FaultHelper helper = new FaultHelper(f);
			helper.addFaultCause(e);
			f = (CDSInternalFault) helper.getFault();
			throw f;
		}
	}

	public abstract WrappedKey wrapPrivateKey(PrivateKey key)
			throws CDSInternalFault;

	public abstract PrivateKey unwrapPrivateKey(WrappedKey wrappedKey)
			throws CDSInternalFault;

	public KeyPair createAndStoreKeyPair(String alias, int keyLength)
			throws CDSInternalFault {
		try {
			KeyPair pair = KeyUtil.generateRSAKeyPair(PROVIDER, keyLength);
			String publicKey = KeyUtil.writePublicKey(pair.getPublic());
			WrappedKey privateKey = wrapPrivateKey(pair.getPrivate());
			insertKeypair(alias, publicKey, privateKey);
			return pair;
		} catch (CDSInternalFault f) {
			throw f;
		} catch (Exception e) {
			getLog().error(e.getMessage(), e);
			CDSInternalFault f = new CDSInternalFault();
			f.setFaultString("Unexpected Database Error.");
			FaultHelper helper = new FaultHelper(f);
			helper.addFaultCause(e);
			f = (CDSInternalFault) helper.getFault();
			throw f;
		}

	}

	public X509Certificate getCertificate(String alias) throws CDSInternalFault {
		this.buildDatabase();
		X509Certificate cert = null;
		if (exists(alias)) {

			Connection c = null;
			try {
				c = getDB().getConnection();
				PreparedStatement s = c.prepareStatement("select "
						+ CERTIFICATE + " from " + TABLE + " WHERE " + ALIAS
						+ "= ?");

				s.setString(1, alias);
				ResultSet rs = s.executeQuery();
				if (rs.next()) {
					cert = CertUtil.loadCertificate(rs.getString(CERTIFICATE));
				}
				rs.close();
				s.close();
			} catch (Exception e) {
				getLog().error(e.getMessage(), e);
				CDSInternalFault f = new CDSInternalFault();
				f.setFaultString("Unexpected Database Error.");
				FaultHelper helper = new FaultHelper(f);
				helper.addFaultCause(e);
				f = (CDSInternalFault) helper.getFault();
				throw f;
			} finally {
				getDB().releaseConnection(c);
			}
		}
		return cert;

	}

	public PublicKey getPublicKey(String alias) throws CDSInternalFault {
		this.buildDatabase();
		PublicKey key = null;
		if (exists(alias)) {

			Connection c = null;
			try {
				c = getDB().getConnection();
				PreparedStatement s = c.prepareStatement("select " + PUBLIC_KEY
						+ " from " + TABLE + " WHERE " + ALIAS + "= ?");

				s.setString(1, alias);
				ResultSet rs = s.executeQuery();
				if (rs.next()) {
					key = KeyUtil.loadPublicKey(rs.getString(PUBLIC_KEY));
				}
				rs.close();
				s.close();
			} catch (Exception e) {
				getLog().error(e.getMessage(), e);
				CDSInternalFault f = new CDSInternalFault();
				f.setFaultString("Unexpected Database Error.");
				FaultHelper helper = new FaultHelper(f);
				helper.addFaultCause(e);
				f = (CDSInternalFault) helper.getFault();
				throw f;
			} finally {
				getDB().releaseConnection(c);
			}
		}
		return key;
	}

	public PrivateKey getPrivateKey(String alias) throws CDSInternalFault {
		this.buildDatabase();
		PrivateKey key = null;
		if (exists(alias)) {

			Connection c = null;
			try {
				c = getDB().getConnection();
				PreparedStatement s = c.prepareStatement("select "
						+ PRIVATE_KEY + "," + IV + " from " + TABLE + " WHERE "
						+ ALIAS + "= ?");

				s.setString(1, alias);
				ResultSet rs = s.executeQuery();
				if (rs.next()) {
					byte[] keyData = rs.getBytes(PRIVATE_KEY);
					byte[] ivData = rs.getBytes(IV);
					key = unwrapPrivateKey(new WrappedKey(keyData, ivData));
				}
				rs.close();
				s.close();
			} catch (Exception e) {
				getLog().error(e.getMessage(), e);
				CDSInternalFault f = new CDSInternalFault();
				f.setFaultString("Unexpected Database Error.");
				FaultHelper helper = new FaultHelper(f);
				helper.addFaultCause(e);
				f = (CDSInternalFault) helper.getFault();
				throw f;
			} finally {
				getDB().releaseConnection(c);
			}
		}
		return key;

	}

	public void storeCertificate(String alias, X509Certificate cert)
			throws CDSInternalFault, DelegationFault {
		this.buildDatabase();
		if (exists(alias)) {
			if (!cert.getPublicKey().equals(getPublicKey(alias))) {
				DelegationFault f = new DelegationFault();
				f
						.setFaultString("The certificate provides is not bound to the public key generated.");
				throw f;
			}
			Connection c = null;
			try {
				c = getDB().getConnection();
				PreparedStatement s = c.prepareStatement("UPDATE " + TABLE
						+ " SET " + CERTIFICATE + "= ? WHERE " + ALIAS + "= ?");

				s.setString(1, CertUtil.writeCertificate(cert));
				s.setString(2, alias);
				s.execute();
				s.close();
			} catch (Exception e) {
				getLog().error(e.getMessage(), e);
				CDSInternalFault f = new CDSInternalFault();
				f.setFaultString("Unexpected Database Error.");
				FaultHelper helper = new FaultHelper(f);
				helper.addFaultCause(e);
				f = (CDSInternalFault) helper.getFault();
				throw f;
			} finally {
				getDB().releaseConnection(c);
			}
		} else {
			CDSInternalFault f = new CDSInternalFault();
			f
					.setFaultString("Cannot insert certificate, no key pair exists for the record ("
							+ alias + ").");
			throw f;

		}

	}

	private void insertKeypair(String alias, String publicKey,
			WrappedKey privateKey) throws CDSInternalFault {
		this.buildDatabase();
		if (!exists(alias)) {
			Connection c = null;
			try {
				c = getDB().getConnection();
				PreparedStatement s = c.prepareStatement("INSERT INTO " + TABLE
						+ " SET " + ALIAS + "= ?, " + PUBLIC_KEY + "= ?, "
						+ PRIVATE_KEY + "= ?, " + IV + "= ?, " + CERTIFICATE
						+ "= ?");
				s.setString(1, alias);
				s.setString(2, publicKey);
				s.setBytes(3, privateKey.getWrappedKeyData());
				s.setBytes(4, privateKey.getIV());
				s.setString(5, "");
				s.execute();
				s.close();
			} catch (Exception e) {
				getLog().error(e.getMessage(), e);
				CDSInternalFault f = new CDSInternalFault();
				f.setFaultString("Unexpected Database Error.");
				FaultHelper helper = new FaultHelper(f);
				helper.addFaultCause(e);
				f = (CDSInternalFault) helper.getFault();
				throw f;
			} finally {
				getDB().releaseConnection(c);
			}
		} else {
			CDSInternalFault f = new CDSInternalFault();
			f
					.setFaultString("Cannot insert key pair, a key pair already exists for the record ("
							+ alias + ").");
			throw f;

		}

	}

	public void delete(String alias) throws CDSInternalFault {
		buildDatabase();
		Connection c = null;
		try {
			c = getDB().getConnection();
			PreparedStatement s = c.prepareStatement("DELETE FROM " + TABLE
					+ "  WHERE " + ALIAS + "= ?");
			s.setString(1, alias);
			s.execute();
			s.close();
		} catch (Exception e) {
			getLog().error(e.getMessage(), e);
			CDSInternalFault f = new CDSInternalFault();
			f.setFaultString("Unexpected Database Error.");
			FaultHelper helper = new FaultHelper(f);
			helper.addFaultCause(e);
			f = (CDSInternalFault) helper.getFault();
			throw f;
		} finally {
			getDB().releaseConnection(c);
		}
	}

	public void deleteAll() throws CDSInternalFault {
		buildDatabase();
		try {
			getDB().update("DELETE FROM " + TABLE);
			dbBuilt = false;
		} catch (Exception e) {
			getLog().error(e.getMessage(), e);
			CDSInternalFault f = new CDSInternalFault();
			f.setFaultString("Unexpected Database Error.");
			FaultHelper helper = new FaultHelper(f);
			helper.addFaultCause(e);
			f = (CDSInternalFault) helper.getFault();
			throw f;
		}
	}

	private void buildDatabase() throws CDSInternalFault {
		if (!dbBuilt) {
			try {
				if (!this.getDB().tableExists(TABLE)) {
					String trust = "CREATE TABLE " + TABLE + " (" + ALIAS
							+ " VARCHAR(255) NOT NULL PRIMARY KEY,"
							+ PUBLIC_KEY + " TEXT NOT NULL," + PRIVATE_KEY
							+ " BLOB NOT NULL," + IV + " BLOB," + CERTIFICATE
							+ " TEXT, INDEX document_index (" + ALIAS
							+ "));";
					getDB().update(trust);
				}
			} catch (Exception e) {
				getLog().error(e.getMessage(), e);
				CDSInternalFault f = new CDSInternalFault();
				f.setFaultString("Unexpected Database Error.");
				FaultHelper helper = new FaultHelper(f);
				helper.addFaultCause(e);
				f = (CDSInternalFault) helper.getFault();
				throw f;
			}

			dbBuilt = true;
		}
	}
}
