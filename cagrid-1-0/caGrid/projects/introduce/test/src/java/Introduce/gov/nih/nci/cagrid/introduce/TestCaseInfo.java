package gov.nih.nci.cagrid.introduce;

import java.io.File;

public class TestCaseInfo {
	public static final String GOLD_SCHEMA_DIR= "test" + File.separator + "resources" + File.separator + "schema";

	public String name = "IntroduceTestService";

	public String dir = "IntroduceTest";

	public String packageName = "org.test";
	
	public String packageDir = "org" + File.separator + "test";

	public String namespaceDomain = "test.org";

	public TestCaseInfo() {

	}

	public TestCaseInfo(String name, String dir, String packageName,
			String namespaceDomain) {
		this.name = name;
		this.dir = dir;
		this.packageName = packageName;
		this.namespaceDomain = namespaceDomain;

	}

	public String getDir() {
		return dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNamespaceDomain() {
		return namespaceDomain;
	}

	public void setNamespaceDomain(String namespaceDomain) {
		this.namespaceDomain = namespaceDomain;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getPackageDir() {
		return packageDir;
	}

	public void setPackageDir(String packageDir) {
		this.packageDir = packageDir;
	}

}
