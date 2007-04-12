package gov.nih.nci.cagrid.dorian.common;

import gov.nih.nci.cagrid.dorian.conf.CredentialLifetime;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class Utils {

	public static Date getExpiredDate(CredentialLifetime lifetime) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.add(Calendar.YEAR, lifetime.getYears());
		cal.add(Calendar.MONTH, lifetime.getMonths());
		cal.add(Calendar.DAY_OF_MONTH, lifetime.getDays());
		cal.add(Calendar.HOUR_OF_DAY, lifetime.getHours());
		cal.add(Calendar.MINUTE, lifetime.getMinutes());
		cal.add(Calendar.SECOND, lifetime.getSeconds());
		return cal.getTime();
	}

}
