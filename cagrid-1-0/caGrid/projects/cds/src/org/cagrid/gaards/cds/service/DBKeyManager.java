package org.cagrid.gaards.cds.service;

import gov.nih.nci.cagrid.common.FaultHelper;
import gov.nih.nci.cagrid.gridca.common.KeyUtil;

import java.io.ByteArrayInputStream;
import java.security.PrivateKey;

import org.cagrid.gaards.cds.conf.KeyManagerDescription;
import org.cagrid.gaards.cds.stubs.types.CDSInternalFault;
import org.cagrid.tools.database.Database;

public class DBKeyManager extends BaseDBKeyManager {

	private static final String KEY_ENCRYPTION_PASSWORD_PROPERTY = "key-encryption-password";

	private String password;

	public DBKeyManager(KeyManagerDescription conf, Database db)
			throws CDSInternalFault {
		super(conf, db);
		password = getPropertyValue(KEY_ENCRYPTION_PASSWORD_PROPERTY);
		if (password == null) {
			CDSInternalFault f = new CDSInternalFault();
			f.setFaultString("Error configuring the " + getClass().getName()
					+ " key manager, no key encryption password property ("
					+ KEY_ENCRYPTION_PASSWORD_PROPERTY + ") was specified.");
			throw f;
		}
	}

	public PrivateKey unwrapPrivateKey(WrappedKey wrappedKey)
			throws CDSInternalFault {
		try {
			return KeyUtil.loadPrivateKey(new ByteArrayInputStream(wrappedKey
					.getWrappedKeyData()), password);
		} catch (Exception e) {
			getLog().error(e.getMessage(), e);
			CDSInternalFault f = new CDSInternalFault();
			f.setFaultString("Unexpected error unwrapping key.");
			FaultHelper helper = new FaultHelper(f);
			helper.addFaultCause(e);
			f = (CDSInternalFault) helper.getFault();
			throw f;
		}
	}

	public WrappedKey wrapPrivateKey(PrivateKey key) throws CDSInternalFault {
		try {
			WrappedKey wk = new WrappedKey(KeyUtil.writePrivateKey(key,
					password).getBytes(), null);
			return wk;
		} catch (Exception e) {
			getLog().error(e.getMessage(), e);
			CDSInternalFault f = new CDSInternalFault();
			f.setFaultString("Unexpected error wrapping key.");
			FaultHelper helper = new FaultHelper(f);
			helper.addFaultCause(e);
			f = (CDSInternalFault) helper.getFault();
			throw f;
		}
	}

}
