/*
 * Created on Jun 10, 2006
 */
package gov.nih.nci.cagrid.introduce.util;

public class OSUtils
{
	public static boolean isWindows()
	{
		return System.getProperty("os.name").toLowerCase().indexOf("win") != -1;
	}
}
