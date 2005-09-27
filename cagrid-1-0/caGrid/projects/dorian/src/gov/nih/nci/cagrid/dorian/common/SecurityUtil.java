
package gov.nih.nci.cagrid.gums.common;

import java.io.File;
import java.io.FileInputStream;

import org.globus.gsi.GlobusCredential;
import org.globus.util.ConfigUtil;


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
