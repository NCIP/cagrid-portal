package gov.nih.nci.cagrid.introduce.test;


import gov.nih.nci.cagrid.introduce.IntroduceConstants;

import java.io.File;

public class TestCaseInfoForImportService extends TestCaseInfo {

	public String name = "IntroduceTestImportService";

	public String dir = "IntroduceTestImportService";

	public String packageName = "org.test.im";
	
	public String packageDir = "org" + File.separator + "test"+ File.separator + "im";

	public String namespaceDomain = "http://im.test.org/IntroduceTestImportService";

	public TestCaseInfoForImportService() {

	}

	public TestCaseInfoForImportService(String name, String dir, String packageName,
			String namespaceDomain) {
		this.name = name;
		this.dir = dir;
		this.packageName = packageName;
		this.namespaceDomain = namespaceDomain;
		

	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.cagrid.introduce.TestCaseInfoI#getDir()
	 */
	public String getDir() {
		return dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.cagrid.introduce.TestCaseInfoI#getName()
	 */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.cagrid.introduce.TestCaseInfoI#getNamespaceDomain()
	 */
	public String getNamespace() {
		return namespaceDomain;
	}

	public void setNamespaceDomain(String namespaceDomain) {
		this.namespaceDomain = namespaceDomain;
	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.cagrid.introduce.TestCaseInfoI#getPackageName()
	 */
	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getPackageDir() {
		return getPackageName().replace('.',File.separatorChar);
	}
	
	public String getResourceFrameworkType() {
		return IntroduceConstants.INTRODUCE_MAIN_RESOURCE + "," + IntroduceConstants.INTRODUCE_SINGLETON_RESOURCE + "," + IntroduceConstants.INTRODUCE_IDENTIFIABLE_RESOURCE;
	}

}
