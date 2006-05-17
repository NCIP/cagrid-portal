package gov.nih.nci.cagrid.introduce;

import java.io.File;

public class TestCaseInfo2 extends TestCaseInfo {
	public static final String GOLD_SCHEMA_DIR= "test" + File.separator + "resources" + File.separator + "schema";

	public String name = "IntroduceTestServiceInnerService";

	public String dir = "IntroduceTest";

	public String packageName = "org.test2";
	
	public String packageDir = "org" + File.separator + "test2";

	public String namespaceDomain = "test2.org/IntroduceTestInnerService";

	public TestCaseInfo2() {

	}

	public TestCaseInfo2(String name, String dir, String packageName,
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
		return getPackageName().replace(".",File.separator);
	}
	
	public String getResourceFrameworkType() {
		return IntroduceConstants.INTRODUCE_BASE_RESOURCE;
	}

}
