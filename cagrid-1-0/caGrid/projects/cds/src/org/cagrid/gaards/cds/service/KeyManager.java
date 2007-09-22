package org.cagrid.gaards.cds.service;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cagrid.gaards.cds.conf.KeyManagerDescription;
import org.cagrid.gaards.cds.conf.Property;
import org.cagrid.gaards.cds.stubs.types.CDSInternalFault;
import org.cagrid.gaards.cds.stubs.types.DelegationFault;
import org.cagrid.tools.database.Database;

public abstract class KeyManager {

	private Database db;
	private Log log;
	private Map<String, String> properties;

	public KeyManager(KeyManagerDescription conf, Database db) {
		this.log = LogFactory.getLog(this.getClass().getName());
		this.db = db;
		properties = new HashMap<String, String>();
		if (conf != null) {
			Property[] props = conf.getProperty();
			if (props != null) {
				for (int i = 0; i < props.length; i++) {
					properties.put(props[i].getName(), props[i].getValue());
				}
			}
		}
	}

	public abstract KeyPair createAndStoreKeyPair(String alias, int keyLength)
			throws CDSInternalFault;

	public abstract PublicKey getPublicKey(String alias)
			throws CDSInternalFault;

	public abstract PrivateKey getPrivateKey(String alias)
			throws CDSInternalFault;

	public abstract X509Certificate getCertificate(String alias)
			throws CDSInternalFault;

	public abstract boolean exists(String alias) throws CDSInternalFault;

	public abstract void storeCertificate(String alias, X509Certificate cert)
			throws CDSInternalFault, DelegationFault;

	public abstract void delete(String alias) throws CDSInternalFault;

	public abstract void deleteAll() throws CDSInternalFault;

	public String getPropertyValue(String name) {
		return this.properties.get(name);
	}

	protected Log getLog() {
		return log;
	}

	protected Database getDB() {
		return this.db;
	}

}
