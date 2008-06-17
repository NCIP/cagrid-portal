package org.cagrid.gaards.dorian.federation;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.cagrid.gaards.dorian.federation.ProxyLifetime;


public class FederationUtils {
	public static Date getProxyValid(ProxyLifetime valid) {
		Calendar c = new GregorianCalendar();
		c.add(Calendar.HOUR_OF_DAY, valid.getHours());
		c.add(Calendar.MINUTE, valid.getMinutes());
		c.add(Calendar.SECOND, valid.getSeconds());
		return c.getTime();
	}


	public static Date getMaxProxyLifetime(IdentityFederationProperties conf) {
		Calendar c = new GregorianCalendar();
		c.add(Calendar.HOUR_OF_DAY, conf.getMaxProxyLifetime().getHours());
		c.add(Calendar.MINUTE, conf.getMaxProxyLifetime().getMinutes());
		c.add(Calendar.SECOND, conf.getMaxProxyLifetime().getSeconds());
		return c.getTime();
	}


	public static long getTimeInSeconds(ProxyLifetime lifetime) {
		long seconds = lifetime.getSeconds();
		seconds = seconds + (lifetime.getMinutes() * 60);
		seconds = seconds + (lifetime.getHours() * 60 * 60);
		return seconds;
	}
}
