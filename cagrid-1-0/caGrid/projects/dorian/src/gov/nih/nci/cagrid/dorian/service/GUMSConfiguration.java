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

package gov.nih.nci.cagrid.gums.service;

import org.jdom.Element;
import org.projectmobius.common.AbstractMobiusConfiguration;
import org.projectmobius.common.MobiusException;
import org.projectmobius.common.MobiusResourceManager;
import org.projectmobius.db.ConnectionManager;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class GUMSConfiguration implements AbstractMobiusConfiguration {
	public static final String DATABASE = "database";
	public static final String GUMS_ID = "gums-internal-id";
	public static final String USERNAME_LENGTH = "username-length";
	public static final String PASSWORD_LENGTH = "password-length";
	public static final String MIN_LENGTH = "min";
	public static final String MAX_LENGTH = "max";
	public static final String DEFAULT_PROXY_TIME = "default-proxy-time";
	public static final String DEFAULT_PROXY_HOURS = "hours";
	public static final String EMAIL_ELEMENT = "outgoing-email";
	public static final String EMAIL_SERVER_ATT = "host";
	public static final String EMAIL_PORT_ATT = "port";
	public static final String EMAIL_PROTOCOL_ATT = "protocol";
	
	private ConnectionManager rootConnectionManager;
	private String gumsInternalId;
	private int defaultProxyHours = 12;
	private int minimumUsernameLength;
	private int maximumUsernameLength;
	private int minimumPasswordLength;
	private int maximumPasswordLength;
	private String outgoingEmailHost;
	private int outgoingEmailPort;
	private String outgoingEmailProtocol;

   
	public void parse(MobiusResourceManager resourceManager, Element config) throws MobiusException {		
		Element rootDatabaseConfig = config.getChild(DATABASE);
		if (rootDatabaseConfig != null) {
			this.rootConnectionManager = new ConnectionManager(rootDatabaseConfig);
		} else {
			throw new MobiusException("No database defined in the Janus Configuration.");
		}
		this.gumsInternalId = config.getChildText(GUMS_ID);
		if(gumsInternalId == null){
			throw new MobiusException("No internal id specified.");
		}
		Element ptime = config.getChild(DEFAULT_PROXY_TIME, config.getNamespace());
		if (ptime == null) {
			throw new MobiusException("Error configuring GUMS, no default proxy time specified.");
		} else {
			String t = ptime.getAttributeValue(DEFAULT_PROXY_HOURS, ptime.getNamespace());
			if (t == null){
				throw new MobiusException("Error configuring GUMS, no default proxy hours specified.");
			} else {
				try {
					this.defaultProxyHours = Integer.parseInt(t);
				} catch (Exception n) {
					throw new MobiusException(
						"Error configuring GUMS, the default proxy hours must be an integer.");
				}
			}
		}
		Element passwordLength = config.getChild(PASSWORD_LENGTH, config.getNamespace());
		if (passwordLength == null) {
			throw new MobiusException("Error configuring GUMS, no password length specified.");
		} else {
			String min = passwordLength.getAttributeValue(MIN_LENGTH, passwordLength.getNamespace());
			String max = passwordLength.getAttributeValue(MAX_LENGTH, passwordLength.getNamespace());
			if ((min == null) && (max == null)) {
				throw new MobiusException("Error configuring GUMS, no min or max password length specified.");
			} else {
				try {
					this.minimumPasswordLength = Integer.parseInt(min);
					this.maximumPasswordLength = Integer.parseInt(max);

				} catch (Exception n) {
					throw new MobiusException(
						"Error configuring GUMS, the min and max password length must be an integer.");
				}

				if ((this.minimumPasswordLength <= 0) || (this.minimumPasswordLength > 255)) {
					throw new MobiusException(
						"Error configuring GUMS, the min password length must be greater than 0 and less than 255.");
				}

				if ((this.maximumPasswordLength <= 0) || (this.maximumPasswordLength > 255)) {
					throw new MobiusException(
						"Error configuring GUMS, the max password length must be greater than 0 and less than 255.");
				}
				if (maximumPasswordLength < minimumPasswordLength) {
					throw new MobiusException(
						"Error configuring GUMS, the max username length must be greater than or equal to the min password length.");
				}

			}

		}

		Element usernameLength = config.getChild(USERNAME_LENGTH, config.getNamespace());
		if (usernameLength == null) {
			throw new MobiusException("Error configuring GUMS, no username length specified.");
		} else {
			String min = usernameLength.getAttributeValue(MIN_LENGTH, usernameLength.getNamespace());
			String max = usernameLength.getAttributeValue(MAX_LENGTH, usernameLength.getNamespace());
			if ((min == null) && (max == null)) {
				throw new MobiusException("Error configuring GUMS, no min or max username length specified.");
			} else {
				try {
					this.minimumUsernameLength = Integer.parseInt(min);
					this.maximumUsernameLength = Integer.parseInt(max);
				} catch (Exception n) {
					throw new MobiusException(
						"Error configuring GUMS, the min and max username length must be an integer.");
				}

				if ((this.minimumUsernameLength <= 0) || (this.minimumUsernameLength > 255)) {
					throw new MobiusException(
						"Error configuring GUMS, the min username length must be greater than 0 and less than 255.");
				}

				if ((this.maximumUsernameLength <= 0) || (this.maximumUsernameLength > 255)) {
					throw new MobiusException(
						"Error configuring GUMS, the max username length must be greater than 0 and less than 255.");
				}
				if (maximumUsernameLength < minimumUsernameLength) {
					throw new MobiusException(
						"Error configuring GUMS, the max username length must be greater than or equal to the min username length.");
				}

			}
		}
		Element email = config.getChild(EMAIL_ELEMENT, config.getNamespace());
		if (email == null) {
			throw new MobiusException("Error configuring GUMS, no outgoing email configuration specified.");
		} else {
			String server = email.getAttributeValue(EMAIL_SERVER_ATT, email.getNamespace());
			if (server == null) {
				throw new MobiusException("Error configuring GUMS, no outgoing email server specified.");
			} else {
				this.outgoingEmailHost = server;
			}

			String port = email.getAttributeValue(EMAIL_PORT_ATT, email.getNamespace());
			if (port == null) {
				throw new MobiusException("Error configuring GUMS, no outgoing email server port specified.");
			} else {
				try {
					this.outgoingEmailPort = Integer.valueOf(port).intValue();
				} catch (Exception ex) {
					throw new MobiusException(
						"Error configuring GUMS, the outgoing email server port specified was not an integer.");
				}
			}
			String protocol = email.getAttributeValue(EMAIL_PROTOCOL_ATT, email.getNamespace());
			if (protocol == null) {
				throw new MobiusException("Error configuring GUMS, no outgoing email protocol specified.");
			} else {
				this.outgoingEmailProtocol = protocol;
			}
		}
	}
	
	

	public String getGUMSInternalId() {
		return gumsInternalId;
	}






	/**
	 * @return Returns the rootConnectionManager.
	 */
	public ConnectionManager getConnectionManager() {
		return rootConnectionManager;
	}



	public int getDefaultProxyHours() {
		return defaultProxyHours;
	}



	public int getMaximumPasswordLength() {
		return maximumPasswordLength;
	}



	public int getMaximumUsernameLength() {
		return maximumUsernameLength;
	}



	public int getMinimumPasswordLength() {
		return minimumPasswordLength;
	}



	public int getMinimumUsernameLength() {
		return minimumUsernameLength;
	}



	public String getOutgoingEmailHost() {
		return outgoingEmailHost;
	}



	public int getOutgoingEmailPort() {
		return outgoingEmailPort;
	}



	public String getOutgoingEmailProtocol() {
		return outgoingEmailProtocol;
	}
}
