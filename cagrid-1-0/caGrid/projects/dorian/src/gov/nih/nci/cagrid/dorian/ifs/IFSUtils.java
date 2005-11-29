package gov.nih.nci.cagrid.gums.ifs;

import gov.nih.nci.cagrid.gums.ifs.bean.ProxyValid;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class IFSUtils {
	public static Date getProxyValid(ProxyValid valid) {
		Calendar c = new GregorianCalendar();
		c.add(Calendar.HOUR_OF_DAY, valid.getHours());
		c.add(Calendar.MINUTE, valid.getMinutes());
		c.add(Calendar.SECOND, valid.getSeconds());
		return c.getTime();
	}
}
