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

package gov.nih.nci.cagrid.gums.ifs;

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
public class IFSConfiguration implements AbstractMobiusConfiguration {

	public static final String RESOURCE = "IFSConfiguration";

	public static final String IDP_NAME_LENGTH = "idp-name-length";

	public static final String MIN_LENGTH = "min";

	public static final String MAX_LENGTH = "max";

	public static final String CREDENTIALS_VALID = "credentials-valid";

	public static final String CREDENTIALS_VALID_YEARS = "years";

	public static final String CREDENTIALS_VALID_MONTHS = "months";

	public static final String CREDENTIALS_VALID_DAYS = "days";

	private int minimumIdPNameLength;

	private int maximumIdPNameLength;

	private int credentialsValidYears;

	private int credentialsValidMonths;

	private int credentialsValidDays;

	public void setMaximumIdPNameLength(int maximumIdPNameLength) {
		this.maximumIdPNameLength = maximumIdPNameLength;
	}

	public void setMinimumIdPNameLength(int minimumIdPNameLength) {
		this.minimumIdPNameLength = minimumIdPNameLength;
	}
	
	

	public int getCredentialsValidDays() {
		return credentialsValidDays;
	}

	public int getCredentialsValidMonths() {
		return credentialsValidMonths;
	}

	public int getCredentialsValidYears() {
		return credentialsValidYears;
	}

	public void parse(MobiusResourceManager resourceManager, Element config)
			throws MobiusException {

		Element idpLength = config.getChild(IDP_NAME_LENGTH, config
				.getNamespace());
		if (idpLength == null) {
			throw new MobiusException(
					"Error configuring IFS, no IdP name length specified.");
		} else {
			String min = idpLength.getAttributeValue(MIN_LENGTH, idpLength
					.getNamespace());
			String max = idpLength.getAttributeValue(MAX_LENGTH, idpLength
					.getNamespace());
			if ((min == null) && (max == null)) {
				throw new MobiusException(
						"Error configuring IFS, no min or max IdP name length specified.");
			} else {
				try {
					this.minimumIdPNameLength = Integer.parseInt(min);
					this.maximumIdPNameLength = Integer.parseInt(max);
				} catch (Exception n) {
					throw new MobiusException(
							"Error configuring IFS, the min and max IdP name length must be an integer.");
				}

				if ((this.minimumIdPNameLength <= 0)
						|| (this.minimumIdPNameLength > 255)) {
					throw new MobiusException(
							"Error configuring IFS, the min IdP name length must be greater than 0 and less than 255.");
				}

				if ((this.maximumIdPNameLength <= 0)
						|| (this.maximumIdPNameLength > 255)) {
					throw new MobiusException(
							"Error configuring IFS, the max IdP name length must be greater than 0 and less than 255.");
				}
				if (maximumIdPNameLength < minimumIdPNameLength) {
					throw new MobiusException(
							"Error configuring IFS, the max IdP name length must be greater than or equal to the min username length.");
				}

			}
		}
		Element valid = config.getChild(CREDENTIALS_VALID, config
				.getNamespace());
		if (valid == null) {
			throw new MobiusException("Error configuring IFS, "
					+ CREDENTIALS_VALID
					+ " element specified in the configuration file.");
		} else {
			
			String sdays = valid.getAttributeValue(CREDENTIALS_VALID_DAYS);
			if (sdays == null) {
				throw new MobiusException(
						"Error configuring IFS, no "+CREDENTIALS_VALID_DAYS+" attribute specified for the element "
								+ CREDENTIALS_VALID
								+ " in the configuration file.");
			} else {
				try {
					this.credentialsValidDays= Integer.valueOf(sdays)
							.intValue();
				} catch (Exception n) {
					throw new MobiusException(
							"Error configuring IFS, the "+CREDENTIALS_VALID_DAYS+" attribute for the element "
								+ CREDENTIALS_VALID
								+ " must be an integer.");
				}
			}
			
			String smonths = valid.getAttributeValue(CREDENTIALS_VALID_MONTHS);
			if (smonths == null) {
				throw new MobiusException(
						"Error configuring IFS, no "+CREDENTIALS_VALID_MONTHS+" attribute specified for the element "
								+ CREDENTIALS_VALID
								+ " in the configuration file.");
			} else {
				try {
					this.credentialsValidMonths = Integer.valueOf(smonths)
							.intValue();
				} catch (Exception n) {
					throw new MobiusException(
							"Error configuring IFS, the "+CREDENTIALS_VALID_MONTHS+" attribute for the element "
								+ CREDENTIALS_VALID
								+ " must be an integer.");
				}
			}
			
			String syears = valid.getAttributeValue(CREDENTIALS_VALID_YEARS);
			if (syears == null) {
				throw new MobiusException(
						"Error configuring IFS, no "+CREDENTIALS_VALID_YEARS+" attribute specified for the element "
								+ CREDENTIALS_VALID
								+ " in the configuration file.");
			} else {
				try {
					this.credentialsValidYears = Integer.valueOf(syears)
							.intValue();
				} catch (Exception n) {
					throw new MobiusException(
							"Error configuring IFS, the "+CREDENTIALS_VALID_YEARS+" attribute for the element "
								+ CREDENTIALS_VALID
								+ " must be an integer.");
				}
			}
		}
	}

	public int getMaximumIdPNameLength() {
		return maximumIdPNameLength;
	}

	public int getMinimumIdPNameLength() {
		return minimumIdPNameLength;
	}

	public void setCredentialsValidDays(int credentialsValidDays) {
		this.credentialsValidDays = credentialsValidDays;
	}

	public void setCredentialsValidMonths(int credentialsValidMonths) {
		this.credentialsValidMonths = credentialsValidMonths;
	}

	public void setCredentialsValidYears(int credentialsValidYears) {
		this.credentialsValidYears = credentialsValidYears;
	}
	
	

}
