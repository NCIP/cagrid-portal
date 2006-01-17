package gov.nih.nci.cagrid.introduce;

import java.io.File;

public class Constants {
	
	public final static String CACHE_POSTFIX = "_backup.zip";
	
	public final static String LAST_DIR_RESOURCE_FILE = "";
	
	public final static String getResourcePath(){
		String userHome = System.getProperty("user.home");
		File userHomeF = new File(userHome);
		File caGridCache = new File(userHomeF.getAbsolutePath()
				+ File.separator + ".cagrid");
		caGridCache.mkdir();
		File introduceCache = new File(caGridCache + File.separator
				+ "introduce");
		introduceCache.mkdir();
		return introduceCache.getAbsolutePath();
	}
}
