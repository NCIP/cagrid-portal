/*-----------------------------------------------------------------------------
 * Copyright (c) 2003-2004, The Ohio State University,
 * Department of Biomedical Informatics, Multiscale Computing Laboratory
 * All rights reserved.
 * 
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided
 * that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * 3  All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement: This product includes
 *    material developed by the Mobius Project (http://www.projectmobius.org/).
 * 
 * 4. Neither the name of the Ohio State University, Department of Biomedical
 *    Informatics, Multiscale Computing Laboratory nor the names of its
 *    contributors may be used to endorse or promote products derived from
 *    this software without specific prior written permission.
 * 
 * 5. Products derived from this Software may not be called "Mobius"
 *    nor may "Mobius" appear in their names without prior written
 *    permission of Ohio State University, Department of Biomedical
 *    Informatics, Multiscale Computing Laboratory
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *---------------------------------------------------------------------------*/

package gov.nih.nci.cagrid.dorian.service.idp;

import gov.nih.nci.cagrid.gridca.common.CertUtil;
import gov.nih.nci.cagrid.gridca.common.KeyUtil;

import java.io.File;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

import org.jdom.Element;
import org.projectmobius.common.AbstractMobiusConfiguration;
import org.projectmobius.common.MobiusException;
import org.projectmobius.common.MobiusResourceManager;


/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class IdPConfiguration implements AbstractMobiusConfiguration {

	public static final String RESOURCE = "IdPConfiguration";

	public static final String UID_LENGTH = "uid-length";

	public static final String PASSWORD_LENGTH = "password-length";

	public static final String MIN_LENGTH = "min";

	public static final String MAX_LENGTH = "max";

	public static final String REGISTRATION_POLICY = "registration-policy";

	public static final String REGISTRATION_POLICY_CLASS = "class";

	public static final String ASSERTING_CREDENTIALS = "asserting-credentials";
	public static final String ASSERTING_CREDENTIALS_AUTO = "auto";
	public static final String ASSERTING_CREDENTIALS_RENEW = "renew";
	public static final String ASSERTING_CREDENTIALS_CERT = "cert";
	public static final String ASSERTING_CREDENTIALS_KEY = "key";
	public static final String ASSERTING_CREDENTIALS_PASS = "key-password";

	private int minimumUsernameLength;

	private int maximumUsernameLength;

	private int minimumPasswordLength;

	private int maximumPasswordLength;

	private IdPRegistrationPolicy registrationPolicy;

	private boolean autoCreateAssertingCredentials;

	private boolean autoRenewAssertingCredentials;

	private X509Certificate assertingCertificate;

	private PrivateKey assertingKey;

	private String keyPassword;


	public void parse(MobiusResourceManager resourceManager, Element config) throws MobiusException {
		Element creds = config.getChild(ASSERTING_CREDENTIALS, config.getNamespace());

		if (creds == null) {
			throw new MobiusException("Error configuring IdP, no asserting credentials specified.");
		} else {
			String autoS = creds.getAttributeValue(ASSERTING_CREDENTIALS_AUTO);
			keyPassword = creds.getAttributeValue(ASSERTING_CREDENTIALS_PASS);
			if (autoS == null) {
				throw new MobiusException(
					"Error configuring IdP, asserting credentials invalidly specified, please specify whether to user auto mode or not.");
			} else {
				this.autoCreateAssertingCredentials = Boolean.valueOf(autoS).booleanValue();
				String renewS = creds.getAttributeValue(ASSERTING_CREDENTIALS_RENEW);
				if (renewS == null) {
					this.autoRenewAssertingCredentials = false;
				} else {
					this.autoRenewAssertingCredentials = Boolean.valueOf(renewS).booleanValue();
				}
				if (!this.autoCreateAssertingCredentials) {
					String certLocation = creds.getAttributeValue(ASSERTING_CREDENTIALS_CERT);
					if (certLocation == null) {
						throw new MobiusException(
							"Error configuring IdP, no asserting certificate specified, an asserting certificate is required when the IdP is not configured to automatically generate credentials.");
					} else {
						try {
							this.assertingCertificate = CertUtil.loadCertificate(new File(certLocation));
						} catch (Exception e) {
							throw new MobiusException(
								"Error configuring IdP, an error occurred loading the certificate " + certLocation
									+ ": " + e.getMessage(), e);
						}
					}

					String keyLocation = creds.getAttributeValue(ASSERTING_CREDENTIALS_KEY);
					if (keyLocation == null) {
						throw new MobiusException(
							"Error configuring IdP, no asserting key specified, an asserting key is required when the IdP is not configured to automatically generate credentials.");
					} else {
						try {
							this.assertingKey = KeyUtil.loadPrivateKey(new File(keyLocation), keyPassword);
						} catch (Exception e) {
							throw new MobiusException("Error configuring IdP, an error occurred loading the key "
								+ keyLocation + ": " + e.getMessage(), e);
						}
					}

				}
			}

		}

		Element reg = config.getChild(REGISTRATION_POLICY, config.getNamespace());

		if (reg == null) {
			throw new MobiusException("Error configuring IdP, registration policy not specified.");
		} else {
			String cl = reg.getAttributeValue(REGISTRATION_POLICY_CLASS);
			try {
				registrationPolicy = (IdPRegistrationPolicy) Class.forName(cl).newInstance();
			} catch (Exception e) {
				throw new MobiusException("Error configuring IdP, invalid registration policy specified.");
			}
		}

		Element passwordLength = config.getChild(PASSWORD_LENGTH, config.getNamespace());
		if (passwordLength == null) {
			throw new MobiusException("Error configuring IdP, no password length specified.");
		} else {
			String min = passwordLength.getAttributeValue(MIN_LENGTH, passwordLength.getNamespace());
			String max = passwordLength.getAttributeValue(MAX_LENGTH, passwordLength.getNamespace());
			if ((min == null) && (max == null)) {
				throw new MobiusException("Error configuring IdP, no min or max password length specified.");
			} else {
				try {
					this.minimumPasswordLength = Integer.parseInt(min);
					this.maximumPasswordLength = Integer.parseInt(max);

				} catch (Exception n) {
					throw new MobiusException(
						"Error configuring IdP, the min and max password length must be an integer.");
				}

				if ((this.minimumPasswordLength <= 0) || (this.minimumPasswordLength > 255)) {
					throw new MobiusException(
						"Error configuring IdP, the min password length must be greater than 0 and less than 255.");
				}

				if ((this.maximumPasswordLength <= 0) || (this.maximumPasswordLength > 255)) {
					throw new MobiusException(
						"Error configuring IdP, the max password length must be greater than 0 and less than 255.");
				}
				if (maximumPasswordLength < minimumPasswordLength) {
					throw new MobiusException(
						"Error configuring IdP, the max username length must be greater than or equal to the min password length.");
				}

			}

		}

		Element usernameLength = config.getChild(UID_LENGTH, config.getNamespace());
		if (usernameLength == null) {
			throw new MobiusException("Error configuring IdP, no username length specified.");
		} else {
			String min = usernameLength.getAttributeValue(MIN_LENGTH, usernameLength.getNamespace());
			String max = usernameLength.getAttributeValue(MAX_LENGTH, usernameLength.getNamespace());
			if ((min == null) && (max == null)) {
				throw new MobiusException("Error configuring IdP, no min or max username length specified.");
			} else {
				try {
					this.minimumUsernameLength = Integer.parseInt(min);
					this.maximumUsernameLength = Integer.parseInt(max);
				} catch (Exception n) {
					throw new MobiusException(
						"Error configuring IdP, the min and max username length must be an integer.");
				}

				if ((this.minimumUsernameLength <= 0) || (this.minimumUsernameLength > 255)) {
					throw new MobiusException(
						"Error configuring IdP, the min username length must be greater than 0 and less than 255.");
				}

				if ((this.maximumUsernameLength <= 0) || (this.maximumUsernameLength > 255)) {
					throw new MobiusException(
						"Error configuring IdP, the max username length must be greater than 0 and less than 255.");
				}
				if (maximumUsernameLength < minimumUsernameLength) {
					throw new MobiusException(
						"Error configuring IdP, the max username length must be greater than or equal to the min username length.");
				}

			}
		}

	}


	public int getMaximumPasswordLength() {
		return maximumPasswordLength;
	}


	public int getMaximumUIDLength() {
		return maximumUsernameLength;
	}


	public int getMinimumPasswordLength() {
		return minimumPasswordLength;
	}


	public int getMinimumUIDLength() {
		return minimumUsernameLength;
	}


	public IdPRegistrationPolicy getRegistrationPolicy() {
		return registrationPolicy;
	}


	public X509Certificate getAssertingCertificate() {
		return assertingCertificate;
	}


	public PrivateKey getAssertingKey() {
		return assertingKey;
	}


	public boolean isAutoCreateAssertingCredentials() {
		return autoCreateAssertingCredentials;
	}


	public boolean isAutoRenewAssertingCredentials() {
		return autoRenewAssertingCredentials;
	}


	public String getKeyPassword() {
		return keyPassword;
	}


	public void setRegistrationPolicy(IdPRegistrationPolicy registrationPolicy) {
		this.registrationPolicy = registrationPolicy;
	}


	public void setAssertingCertificate(X509Certificate assertingCertificate) {
		this.assertingCertificate = assertingCertificate;
	}


	public void setAssertingKey(PrivateKey assertingKey) {
		this.assertingKey = assertingKey;
	}


	public void setAutoCreateAssertingCredentials(boolean autoCreateAssertingCredentials) {
		this.autoCreateAssertingCredentials = autoCreateAssertingCredentials;
	}


	public void setAutoRenewAssertingCredentials(boolean autoRenewAssertingCredentials) {
		this.autoRenewAssertingCredentials = autoRenewAssertingCredentials;
	}


	public void setKeyPassword(String keyPassword) {
		this.keyPassword = keyPassword;
	}

}
