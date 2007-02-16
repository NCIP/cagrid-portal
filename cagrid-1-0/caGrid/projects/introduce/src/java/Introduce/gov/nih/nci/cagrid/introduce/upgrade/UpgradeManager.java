package gov.nih.nci.cagrid.introduce.upgrade;

import java.lang.reflect.Constructor;

import org.apache.axis.description.ServiceDesc;

import gov.nih.nci.cagrid.introduce.beans.ServiceDescription;
import gov.nih.nci.cagrid.introduce.common.CommonTools;


public class UpgradeManager {
	private ServiceDescription service;
	
	public UpgradeManager(ServiceDescription service){
		this.service = service;
	}
	

	private static String getUpgradeVersion(String oldVersion) {
		if (oldVersion.equals("1.0")) {
			return "1.1";
		}

		return null;
	}


	private static String getUpgradeClass(String version) {
		if (version.equals("1.1")) {
			return "gov.nih.nci.cagrid.introduce.upgrade.introduce.Introduce_1_0__1_1_Upgrader";
		}
		return null;
	}

	
	public boolean canBeUpgraded(String version){
		if(getUpgradeVersion(version)!=null && getUpgradeClass(getUpgradeVersion(version))!=null){
			return true;
		} else {
		return false;	
		}
	}
	
	public void upgrade() throws Exception {
		System.out.println("Trying to upgrade the service");
		
		//now upgrade the rest
		String version = CommonTools.getIntroduceVersion();
		if (version != null) {

			String vers = service.getIntroduceVersion();

			while (canBeUpgraded(vers)) {
				String newVersion = getUpgradeVersion(vers);
				if (newVersion == null) {
					System.out.println("The service"
						+ " is upgradeable however no upgrade version from the version " + vers
						+ " could be found.");
					break;
				}

				String className = getUpgradeClass(newVersion);
				if (className == null) {
					System.out.println("The service"
						+ " is upgradeable however no upgrade class from the version " + vers
						+ " could be found.");
					break;
				}
				
				System.out.println("Upgrading the service from version " + vers
					+ " to " + newVersion + ".............");
				Class clazz = Class.forName(className);
				Constructor con = clazz.getConstructor(new Class[]{ServiceDescription.class});
				UpgraderI upgrader = (UpgraderI) con.newInstance(new Object []{service});
				upgrader.execute();

			
                System.out.println("COMPLETED Upgrading the service from version " + vers
                    + " to " + newVersion + ".............");
            	
                vers = newVersion;
			}
		} else {
			System.err.println("ERROR: The service"
				+ " is not upgradable because it's version cannot be determined or is corupt");
		}
	}

}
