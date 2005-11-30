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

	public static final String EMAIL_ELEMENT = "outgoing-email";

	public static final String EMAIL_SERVER_ATT = "host";

	public static final String EMAIL_PORT_ATT = "port";

	public static final String EMAIL_PROTOCOL_ATT = "protocol";

	private ConnectionManager rootConnectionManager;

	private String gumsInternalId;

	private String outgoingEmailHost;

	private int outgoingEmailPort;

	private String outgoingEmailProtocol;

	public void parse(MobiusResourceManager resourceManager, Element config)
			throws MobiusException {
		Element rootDatabaseConfig = config.getChild(DATABASE);
		if (rootDatabaseConfig != null) {
			this.rootConnectionManager = new ConnectionManager(
					rootDatabaseConfig);
		} else {
			throw new MobiusException(
					"No database defined in the Janus Configuration.");
		}
		this.gumsInternalId = config.getChildText(GUMS_ID);
		if (gumsInternalId == null) {
			throw new MobiusException("No internal id specified.");
		}

		Element email = config.getChild(EMAIL_ELEMENT, config.getNamespace());
		if (email == null) {
			throw new MobiusException(
					"Error configuring GUMS, no outgoing email configuration specified.");
		} else {
			String server = email.getAttributeValue(EMAIL_SERVER_ATT, email
					.getNamespace());
			if (server == null) {
				throw new MobiusException(
						"Error configuring GUMS, no outgoing email server specified.");
			} else {
				this.outgoingEmailHost = server;
			}

			String port = email.getAttributeValue(EMAIL_PORT_ATT, email
					.getNamespace());
			if (port == null) {
				throw new MobiusException(
						"Error configuring GUMS, no outgoing email server port specified.");
			} else {
				try {
					this.outgoingEmailPort = Integer.valueOf(port).intValue();
				} catch (Exception ex) {
					throw new MobiusException(
							"Error configuring GUMS, the outgoing email server port specified was not an integer.");
				}
			}
			String protocol = email.getAttributeValue(EMAIL_PROTOCOL_ATT, email
					.getNamespace());
			if (protocol == null) {
				throw new MobiusException(
						"Error configuring GUMS, no outgoing email protocol specified.");
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
