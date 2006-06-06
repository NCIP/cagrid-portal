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

package gov.nih.nci.cagrid.dorian.service.ifs;

import gov.nih.nci.cagrid.dorian.ifs.bean.IFSUser;
import gov.nih.nci.cagrid.dorian.ifs.bean.IFSUserPolicy;
import gov.nih.nci.cagrid.dorian.ifs.bean.TrustedIdP;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

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

	public static final String CREDENTIALS_VALID_HOURS = "hours";

	public static final String CREDENTIALS_VALID_MINUTES = "minutes";

	public static final String CREDENTIALS_VALID_SECONDS = "seconds";

	public static final String MAX_PROXY_LIFETIME = "max-proxy-lifetime";

	public static final String MAX_PROXY_LIFETIME_HOURS = "hours";

	public static final String MAX_PROXY_LIFETIME_MINUTES = "minutes";

	public static final String MAX_PROXY_LIFETIME_SECONDS = "seconds";

	public static final String POLICIES = "policies";

	public static final String POLICY = "policy";

	public static final String POLICY_NAME = "name";

	public static final String POLICY_CLASS = "class";

	public static final String GTS_SERVICES = "gts-services";

	public static final String GTS_SERVICE = "gts-service";

	private int minimumIdPNameLength;

	private int maximumIdPNameLength;

	private int credentialsValidYears;

	private int credentialsValidMonths;

	private int credentialsValidDays;

	private int credentialsValidHours;

	private int credentialsValidMinutes;

	private int credentialsValidSeconds;

	private int maxProxyLifetimeHours;

	private int maxProxyLifetimeMinutes;

	private int maxProxyLifetimeSeconds;

	private TrustedIdP initalTrustedIdP;

	private IFSUser initialUser;

	private IFSUserPolicy[] userPolicies;

	private List gtsServices;


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


	public int getCredentialsValidHours() {
		return credentialsValidHours;
	}


	public void setCredentialsValidHours(int credentialsValidHours) {
		this.credentialsValidHours = credentialsValidHours;
	}


	public int getCredentialsValidMinutes() {
		return credentialsValidMinutes;
	}


	public void setCredentialsValidMinutes(int credentialsValidMinutes) {
		this.credentialsValidMinutes = credentialsValidMinutes;
	}


	public int getCredentialsValidSeconds() {
		return credentialsValidSeconds;
	}


	public void setCredentialsValidSeconds(int credentialsValidSeconds) {
		this.credentialsValidSeconds = credentialsValidSeconds;
	}


	public void parse(MobiusResourceManager resourceManager, Element config) throws MobiusException {
		Element idpLength = config.getChild(IDP_NAME_LENGTH, config.getNamespace());
		if (idpLength == null) {
			throw new MobiusException("Error configuring IFS, no IdP name length specified.");
		} else {
			String min = idpLength.getAttributeValue(MIN_LENGTH, idpLength.getNamespace());
			String max = idpLength.getAttributeValue(MAX_LENGTH, idpLength.getNamespace());
			if ((min == null) && (max == null)) {
				throw new MobiusException("Error configuring IFS, no min or max IdP name length specified.");
			} else {
				try {
					this.minimumIdPNameLength = Integer.parseInt(min);
					this.maximumIdPNameLength = Integer.parseInt(max);
				} catch (Exception n) {
					throw new MobiusException(
						"Error configuring IFS, the min and max IdP name length must be an integer.");
				}

				if ((this.minimumIdPNameLength <= 0) || (this.minimumIdPNameLength > 255)) {
					throw new MobiusException(
						"Error configuring IFS, the min IdP name length must be greater than 0 and less than 255.");
				}

				if ((this.maximumIdPNameLength <= 0) || (this.maximumIdPNameLength > 255)) {
					throw new MobiusException(
						"Error configuring IFS, the max IdP name length must be greater than 0 and less than 255.");
				}
				if (maximumIdPNameLength < minimumIdPNameLength) {
					throw new MobiusException(
						"Error configuring IFS, the max IdP name length must be greater than or equal to the min username length.");
				}

			}

		}

		Element valid = config.getChild(CREDENTIALS_VALID, config.getNamespace());
		if (valid == null) {
			throw new MobiusException("Error configuring IFS, " + CREDENTIALS_VALID
				+ " element specified in the configuration file.");
		} else {

			String sdays = valid.getAttributeValue(CREDENTIALS_VALID_DAYS);
			if (sdays == null) {
				throw new MobiusException("Error configuring IFS, no " + CREDENTIALS_VALID_DAYS
					+ " attribute specified for the element " + CREDENTIALS_VALID + " in the configuration file.");
			} else {
				try {
					this.credentialsValidDays = Integer.valueOf(sdays).intValue();
				} catch (Exception n) {
					throw new MobiusException("Error configuring IFS, the " + CREDENTIALS_VALID_DAYS
						+ " attribute for the element " + CREDENTIALS_VALID + " must be an integer.");
				}
			}

			String smonths = valid.getAttributeValue(CREDENTIALS_VALID_MONTHS);
			if (smonths == null) {
				throw new MobiusException("Error configuring IFS, no " + CREDENTIALS_VALID_MONTHS
					+ " attribute specified for the element " + CREDENTIALS_VALID + " in the configuration file.");
			} else {
				try {
					this.credentialsValidMonths = Integer.valueOf(smonths).intValue();
				} catch (Exception n) {
					throw new MobiusException("Error configuring IFS, the " + CREDENTIALS_VALID_MONTHS
						+ " attribute for the element " + CREDENTIALS_VALID + " must be an integer.");
				}
			}

			String syears = valid.getAttributeValue(CREDENTIALS_VALID_YEARS);
			if (syears == null) {
				throw new MobiusException("Error configuring IFS, no " + CREDENTIALS_VALID_YEARS
					+ " attribute specified for the element " + CREDENTIALS_VALID + " in the configuration file.");
			} else {
				try {
					this.credentialsValidYears = Integer.valueOf(syears).intValue();
				} catch (Exception n) {
					throw new MobiusException("Error configuring IFS, the " + CREDENTIALS_VALID_YEARS
						+ " attribute for the element " + CREDENTIALS_VALID + " must be an integer.");
				}
			}

			String shours = valid.getAttributeValue(CREDENTIALS_VALID_HOURS);
			if (shours == null) {
				throw new MobiusException("Error configuring IFS, no " + CREDENTIALS_VALID_HOURS
					+ " attribute specified for the element " + CREDENTIALS_VALID + " in the configuration file.");
			} else {
				try {
					this.credentialsValidHours = Integer.valueOf(shours).intValue();
				} catch (Exception n) {
					throw new MobiusException("Error configuring IFS, the " + CREDENTIALS_VALID_HOURS
						+ " attribute for the element " + CREDENTIALS_VALID + " must be an integer.");
				}
			}

			String sminutes = valid.getAttributeValue(CREDENTIALS_VALID_MINUTES);
			if (sminutes == null) {
				throw new MobiusException("Error configuring IFS, no " + CREDENTIALS_VALID_MINUTES
					+ " attribute specified for the element " + CREDENTIALS_VALID + " in the configuration file.");
			} else {
				try {
					this.credentialsValidMinutes = Integer.valueOf(sminutes).intValue();
				} catch (Exception n) {
					throw new MobiusException("Error configuring IFS, the " + CREDENTIALS_VALID_MINUTES
						+ " attribute for the element " + CREDENTIALS_VALID + " must be an integer.");
				}
			}

			String sseconds = valid.getAttributeValue(CREDENTIALS_VALID_SECONDS);
			if (sseconds == null) {
				throw new MobiusException("Error configuring IFS, no " + CREDENTIALS_VALID_SECONDS
					+ " attribute specified for the element " + CREDENTIALS_VALID + " in the configuration file.");
			} else {
				try {
					this.credentialsValidSeconds = Integer.valueOf(sseconds).intValue();
				} catch (Exception n) {
					throw new MobiusException("Error configuring IFS, the " + CREDENTIALS_VALID_SECONDS
						+ " attribute for the element " + CREDENTIALS_VALID + " must be an integer.");
				}
			}

		}

		Element proxy = config.getChild(MAX_PROXY_LIFETIME, config.getNamespace());
		if (proxy == null) {
			throw new MobiusException("Error configuring IFS, " + MAX_PROXY_LIFETIME
				+ " element specified in the configuration file.");
		} else {

			String shours = proxy.getAttributeValue(MAX_PROXY_LIFETIME_HOURS);
			if (shours == null) {
				throw new MobiusException("Error configuring IFS, no " + MAX_PROXY_LIFETIME_HOURS
					+ " attribute specified for the element " + MAX_PROXY_LIFETIME + " in the configuration file.");
			} else {
				try {
					this.maxProxyLifetimeHours = Integer.valueOf(shours).intValue();
				} catch (Exception n) {
					throw new MobiusException("Error configuring IFS, the " + MAX_PROXY_LIFETIME_HOURS
						+ " attribute for the element " + MAX_PROXY_LIFETIME + " must be an integer.");
				}
			}

			String sminutes = proxy.getAttributeValue(MAX_PROXY_LIFETIME_MINUTES);
			if (sminutes == null) {
				throw new MobiusException("Error configuring IFS, no " + MAX_PROXY_LIFETIME_MINUTES
					+ " attribute specified for the element " + MAX_PROXY_LIFETIME + " in the configuration file.");
			} else {
				try {
					this.maxProxyLifetimeMinutes = Integer.valueOf(sminutes).intValue();
				} catch (Exception n) {
					throw new MobiusException("Error configuring IFS, the " + MAX_PROXY_LIFETIME_MINUTES
						+ " attribute for the element " + MAX_PROXY_LIFETIME + " must be an integer.");
				}
			}

			String sseconds = proxy.getAttributeValue(MAX_PROXY_LIFETIME_SECONDS);
			if (sseconds == null) {
				throw new MobiusException("Error configuring IFS, no " + MAX_PROXY_LIFETIME_SECONDS
					+ " attribute specified for the element " + MAX_PROXY_LIFETIME + " in the configuration file.");
			} else {
				try {
					this.maxProxyLifetimeSeconds = Integer.valueOf(sseconds).intValue();
				} catch (Exception n) {
					throw new MobiusException("Error configuring IFS, the " + MAX_PROXY_LIFETIME_SECONDS
						+ " attribute for the element " + MAX_PROXY_LIFETIME + " must be an integer.");
				}
			}
			Element policyElement = config.getChild(POLICIES, config.getNamespace());
			if (policyElement == null) {
				throw new MobiusException("Error configuring IFS, no " + POLICIES + " element specified.");
			} else {
				List policies = policyElement.getChildren(POLICY, policyElement.getNamespace());
				if (policies.size() == 0) {
					throw new MobiusException("Error configuring IFS, at least one " + POLICY
						+ " element must be specified.");
				} else {
					this.userPolicies = new IFSUserPolicy[policies.size()];
					for (int i = 0; i < policies.size(); i++) {
						Element pol = (Element) policies.get(i);
						String name = pol.getAttributeValue(POLICY_NAME);
						String className = pol.getAttributeValue(POLICY_CLASS);
						if (name == null) {
							throw new MobiusException("Error configuring IFS, no " + POLICY_NAME
								+ " attribute specified for the element " + POLICY + " in the configuration file.");
						}

						if (className == null) {
							throw new MobiusException("Error configuring IFS, no " + POLICY_CLASS
								+ " attribute specified for the element " + POLICY + " in the configuration file.");
						}

						try {
							Class c = Class.forName(className);
							if (!UserPolicy.class.isAssignableFrom(c)) {
								throw new MobiusException("Error configuring IFS, Invalid policy class (" + className
									+ ") specified.");
							}

						} catch (ClassNotFoundException e) {
							throw new MobiusException("Error configuring IFS, Invalid policy class (" + className
								+ ") specified.");
						}

						userPolicies[i] = new IFSUserPolicy();
						userPolicies[i].setName(name);
						userPolicies[i].setClassName(className);
					}
				}
			}

		}

		gtsServices = new ArrayList();
		Element gtsList = config.getChild(GTS_SERVICES, config.getNamespace());
		if (gtsList != null) {
			List gtsL = gtsList.getChildren(GTS_SERVICE, gtsList.getNamespace());
			for (int i = 0; i < gtsL.size(); i++) {
				Element gts = (Element) gtsL.get(i);
				gtsServices.add(gts.getText());
			}
		}
	}


	public IFSUserPolicy[] getUserPolicies() {
		return userPolicies;
	}


	public Date getMaxProxyLifetime() {
		Calendar c = new GregorianCalendar();
		c.add(Calendar.HOUR_OF_DAY, getMaxProxyLifetimeHours());
		c.add(Calendar.MINUTE, getMaxProxyLifetimeMinutes());
		c.add(Calendar.SECOND, getMaxProxyLifetimeSeconds());
		return c.getTime();
	}


	public Date getCredentialsValid() {
		Calendar c = new GregorianCalendar();
		c.add(Calendar.YEAR, getCredentialsValidYears());
		c.add(Calendar.MONTH, getCredentialsValidMonths());
		c.add(Calendar.DAY_OF_MONTH, getCredentialsValidDays());
		c.add(Calendar.HOUR_OF_DAY, getCredentialsValidHours());
		c.add(Calendar.MINUTE, getCredentialsValidMinutes());
		c.add(Calendar.SECOND, getCredentialsValidSeconds());
		return c.getTime();
	}


	public int getMaxProxyLifetimeHours() {
		return maxProxyLifetimeHours;
	}


	public void setMaxProxyLifetimeHours(int maxProxyValidHours) {
		this.maxProxyLifetimeHours = maxProxyValidHours;
	}


	public int getMaxProxyLifetimeMinutes() {
		return maxProxyLifetimeMinutes;
	}


	public void setMaxProxyLifetimeMinutes(int maxProxyValidMinutes) {
		this.maxProxyLifetimeMinutes = maxProxyValidMinutes;
	}


	public int getMaxProxyLifetimeSeconds() {
		return maxProxyLifetimeSeconds;
	}


	public void setMaxProxyLifetimeSeconds(int maxProxyValidSeconds) {
		this.maxProxyLifetimeSeconds = maxProxyValidSeconds;
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


	public TrustedIdP getInitalTrustedIdP() {
		return initalTrustedIdP;
	}


	public void setInitalTrustedIdP(TrustedIdP initalTrustedIdP) {
		this.initalTrustedIdP = initalTrustedIdP;
	}


	public IFSUser getInitialUser() {
		return initialUser;
	}


	public void setInitialUser(IFSUser initialUser) {
		this.initialUser = initialUser;
	}


	public void setUserPolicies(IFSUserPolicy[] userPolicies) {
		this.userPolicies = userPolicies;
	}


	public List getGTSServices() {
		return gtsServices;
	}


	public void setGTSServices(List gtsServices) {
		this.gtsServices = gtsServices;
	}

}
