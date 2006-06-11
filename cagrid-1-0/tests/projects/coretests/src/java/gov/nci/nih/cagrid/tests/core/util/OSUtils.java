/*
 * Created on Jun 10, 2006
 */
package gov.nci.nih.cagrid.tests.core.util;

public class OSUtils
{
	public static boolean isWindows()
	{
		return System.getProperty("os.name").toLowerCase().indexOf("win") != -1;
	}
}
