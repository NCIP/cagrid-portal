package gov.nih.nci.cagrid.dorian.service.ifs;

import gov.nih.nci.cagrid.dorian.ifs.bean.ProxyLifetime;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class IFSUtils {
	public static Date getProxyValid(ProxyLifetime valid) {
		Calendar c = new GregorianCalendar();
		c.add(Calendar.HOUR_OF_DAY, valid.getHours());
		c.add(Calendar.MINUTE, valid.getMinutes());
		c.add(Calendar.SECOND, valid.getSeconds());
		return c.getTime();
	}
	
	public static long getTimeInSeconds(ProxyLifetime lifetime){
		long seconds=lifetime.getSeconds();
		seconds = seconds + (lifetime.getMinutes()*60);
		seconds = seconds + (lifetime.getHours()*60*60);
		return seconds;
	}
}
