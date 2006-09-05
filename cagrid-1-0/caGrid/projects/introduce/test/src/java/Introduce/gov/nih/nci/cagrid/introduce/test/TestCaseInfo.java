package gov.nih.nci.cagrid.introduce.test;

import java.io.File;

public abstract class TestCaseInfo {
	public static final String GOLD_SCHEMA_DIR= "test" + File.separator + "resources" + File.separator + "schema";

	public abstract String getDir();

	public abstract String getName();

	public abstract String getNamespace();

	public abstract String getPackageName();

	public abstract String getPackageDir();
	
	public abstract String getResourceFrameworkType();

}