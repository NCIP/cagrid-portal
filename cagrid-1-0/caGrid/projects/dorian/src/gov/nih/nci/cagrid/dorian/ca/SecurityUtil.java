package gov.nih.nci.cagrid.gums.ca;

import java.security.Security;
/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Langella </A>
 * @version $Id: SecurityUtil.java,v 1.1 2005-09-27 18:31:18 langella Exp $
 */
public class SecurityUtil {
	private static boolean isInit = false;

	public static void init(){
		if(!isInit){
			Security
			.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
			isInit = true;
		}
	}
}
