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

package gov.nih.nci.cagrid.dorian.ca;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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
public class DorianCertificateAuthorityConf implements AbstractMobiusConfiguration {
	public static final String RESOURCE = "DorianCertificateAuthorityConf";
	private static final String CA_PASSWORD = "ca-password";
	private static final String CA_PASSWORD_ATT = "value";
	private static final String AUTO_RENEWAL = "auto-renewal";
	private static final String AUTO_RENEWAL_ENABLED = "enabled";
	private static final String AUTO_RENEWAL_YEARS = "years";
	private static final String AUTO_RENEWAL_MONTHS = "months";
	private static final String AUTO_RENEWAL_DAYS = "days";
	private static final String AUTO_RENEWAL_HOURS = "hours";
	private static final String AUTO_RENEWAL_MINUTES = "minutes";
	private static final String AUTO_RENEWAL_SECONDS = "seconds";

	private String caPassword;
	private boolean autoRenewal;
	private int autoRenewalYears;
	private int autoRenewalMonths;
	private int autoRenewalDays;
	private int autoRenewalHours;
	private int autoRenewalMinutes;
	private int autoRenewalSeconds;
	
   
	public void parse(MobiusResourceManager resourceManager, Element config) throws MobiusException {		
		Element pass = config.getChild(CA_PASSWORD,config.getNamespace());
		if(pass==null){
			throw new MobiusException("Error configuring the Dorian Certificate Authority, no "+CA_PASSWORD+" specified.");
			
		}else{
			caPassword=pass.getAttributeValue(CA_PASSWORD_ATT);
			if(caPassword==null){
				throw new MobiusException("Error configuring the Dorian Certificate Authority, no "+CA_PASSWORD+" specified, the attribute "+CA_PASSWORD_ATT+" is missing.");
			}
		}
		
		Element auto = config.getChild(AUTO_RENEWAL,config.getNamespace());
		if(auto == null){
			autoRenewal = false;
		}else{
			String s = auto.getAttributeValue(AUTO_RENEWAL_ENABLED);
			if((s!=null )&&(s.equalsIgnoreCase("true"))){
				this.autoRenewalYears = getAutoRenewalInteger(auto,AUTO_RENEWAL_YEARS);
				this.autoRenewalMonths = getAutoRenewalInteger(auto,AUTO_RENEWAL_MONTHS);
				this.autoRenewalDays = getAutoRenewalInteger(auto,AUTO_RENEWAL_DAYS);
				this.autoRenewalHours = getAutoRenewalInteger(auto,AUTO_RENEWAL_HOURS);
				this.autoRenewalMinutes = getAutoRenewalInteger(auto,AUTO_RENEWAL_MINUTES);
				this.autoRenewalSeconds = getAutoRenewalInteger(auto,AUTO_RENEWAL_SECONDS);
			}
		}
		
	}
	
	private int getAutoRenewalInteger(Element e, String att) throws MobiusException{
		String s = e.getAttributeValue(att);
		if(s==null){
			throw new MobiusException("The "+AUTO_RENEWAL+" attribute, "+att+" must be specified.");
		}else{
			try{
				return Integer.valueOf(s).intValue();
			}catch (Exception ex) {
				throw new MobiusException("The "+AUTO_RENEWAL+" attribute, "+att+" must be an integer.");
			}
		}
	}


	public boolean isAutoRenewal() {
		return autoRenewal;
	}


	public void setAutoRenewal(boolean autoRenewal) {
		this.autoRenewal = autoRenewal;
	}


	public int getAutoRenewalDays() {
		return autoRenewalDays;
	}


	public void setAutoRenewalDays(int autoRenewalDays) {
		this.autoRenewalDays = autoRenewalDays;
	}


	public int getAutoRenewalHours() {
		return autoRenewalHours;
	}


	public void setAutoRenewalHours(int autoRenewalHours) {
		this.autoRenewalHours = autoRenewalHours;
	}


	public int getAutoRenewalMinutes() {
		return autoRenewalMinutes;
	}


	public void setAutoRenewalMinutes(int autoRenewalMinutes) {
		this.autoRenewalMinutes = autoRenewalMinutes;
	}


	public int getAutoRenewalMonths() {
		return autoRenewalMonths;
	}


	public void setAutoRenewalMonths(int autoRenewalMonths) {
		this.autoRenewalMonths = autoRenewalMonths;
	}


	public int getAutoRenewalSeconds() {
		return autoRenewalSeconds;
	}


	public void setAutoRenewalSeconds(int autoRenewalSeconds) {
		this.autoRenewalSeconds = autoRenewalSeconds;
	}


	public int getAutoRenewalYears() {
		return autoRenewalYears;
	}


	public void setAutoRenewalYears(int autoRenewalYears) {
		this.autoRenewalYears = autoRenewalYears;
	}


	public String getCaPassword() {
		return caPassword;
	}


	public void setCaPassword(String caPassword) {
		this.caPassword = caPassword;
	}
	
	public Date getRenewalDate() {
		GregorianCalendar cal = new GregorianCalendar();
		cal.add(Calendar.YEAR,getAutoRenewalYears());
		cal.add(Calendar.MONTH, this.getAutoRenewalMonths());
		cal.add(Calendar.DAY_OF_MONTH, this.getAutoRenewalDays());
		cal.add(Calendar.HOUR_OF_DAY, this.getAutoRenewalHours());
		cal.add(Calendar.MINUTE, this.getAutoRenewalMinutes());
		cal.add(Calendar.SECOND, this.getAutoRenewalSeconds());
		return cal.getTime();
	}

}
