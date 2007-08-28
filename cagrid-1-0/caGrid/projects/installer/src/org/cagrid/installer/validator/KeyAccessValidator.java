/**
 * 
 */
package org.cagrid.installer.validator;

import java.io.File;
import java.io.FileInputStream;
import java.security.Security;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.globus.gsi.OpenSSLKey;
import org.globus.gsi.bc.BouncyCastleOpenSSLKey;
import org.pietschy.wizard.InvalidStateException;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class KeyAccessValidator implements Validator {

	static {
		Security
				.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
	}

	private static final Log logger = LogFactory
			.getLog(KeyAccessValidator.class);

	private String keyPathProp;

	private String keyPasswordProp;

	private String message;

	public KeyAccessValidator(String keyPathProp, String keyPasswordProp,
			String message) {
		this.keyPathProp = keyPathProp;
		this.keyPasswordProp = keyPasswordProp;
		this.message = message;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cagrid.installer.validator.Validator#validate(java.util.Map)
	 */
	public void validate(Map state) throws InvalidStateException {
		try {
			if (!state.containsKey(this.keyPathProp)) {
				throw new Exception("No value for '" + this.keyPathProp
						+ "' found in state.");
			}
			File keyPath = new File((String) state.get(this.keyPathProp));
			if (!keyPath.exists()) {
				throw new Exception("Key file '" + keyPath.getAbsolutePath()
						+ "' could not be found.");
			}
			String keyPwd = null;
			if (this.keyPasswordProp != null) {
				if (!state.containsKey(this.keyPasswordProp)) {
					throw new Exception("No value for '" + this.keyPasswordProp
							+ "' found in state.");
				}
				keyPwd = (String) state.get(this.keyPasswordProp);
			}
			try {
				OpenSSLKey key = new BouncyCastleOpenSSLKey(
						new FileInputStream(keyPath));
				if (key.isEncrypted()) {
					key.decrypt(keyPwd);
				}
				key.getPrivateKey();
			} catch (Exception ex) {
				throw new Exception("Error reading key file: "
						+ ex.getMessage(), ex);
			}
		} catch (Exception ex) {
			logger.error(this.message + " - " + ex.getMessage(), ex);
			throw new InvalidStateException(this.message, ex);
		}
	}

}
