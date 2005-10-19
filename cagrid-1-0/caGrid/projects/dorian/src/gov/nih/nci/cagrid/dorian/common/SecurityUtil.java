
package gov.nih.nci.cagrid.gums.common;

import java.io.File;
import java.io.FileInputStream;

import org.globus.gsi.GlobusCredential;
import org.globus.util.ConfigUtil;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class SecurityUtil {
	
	public static GlobusCredential getDefaultProxy() throws Exception{
		final File f = new File(ConfigUtil.discoverProxyLocation());
		if (!f.exists()) {
			throw new Exception("No Proxy found in the default location:\n "+ConfigUtil.discoverProxyLocation());
		}else{
			return new GlobusCredential(new FileInputStream(f));
		}
	}
	
	public static GlobusCredential getProxy(File f) throws Exception{
		if (!f.exists()) {
			throw new Exception("No Proxy found in the default location:\n "+ConfigUtil.discoverProxyLocation());
		}else{
			return new GlobusCredential(new FileInputStream(f));
		}
	}
}
