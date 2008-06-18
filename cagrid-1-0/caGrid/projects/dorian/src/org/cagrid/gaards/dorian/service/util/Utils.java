package org.cagrid.gaards.dorian.service.util;

import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.cagrid.gaards.dorian.common.Lifetime;

public class Utils {

	public static Date getExpiredDate(Lifetime lifetime) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.add(Calendar.YEAR, lifetime.getYears());
		cal.add(Calendar.MONTH, lifetime.getMonths());
		cal.add(Calendar.DAY_OF_MONTH, lifetime.getDays());
		cal.add(Calendar.HOUR_OF_DAY, lifetime.getHours());
		cal.add(Calendar.MINUTE, lifetime.getMinutes());
		cal.add(Calendar.SECOND, lifetime.getSeconds());
		return cal.getTime();
	}

	public static String getHostCertificateSubjectPrefix(X509Certificate cacert) {
		String caSubject = cacert.getSubjectDN().getName();
		int caindex = caSubject.lastIndexOf(",");
		String caPreSub = caSubject.substring(0, caindex);
		return caPreSub + ",OU=Services,CN=host/";
	}

	public static String getHostCertificateSubject(X509Certificate cacert,
			String host) {
		return getHostCertificateSubjectPrefix(cacert) + host;
	}

}
