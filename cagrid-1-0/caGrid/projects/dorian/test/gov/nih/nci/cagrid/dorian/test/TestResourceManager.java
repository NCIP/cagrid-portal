package gov.nih.nci.cagrid.gums.test;

import org.projectmobius.common.MobiusConfigurator;
import org.projectmobius.common.MobiusResourceManager;

public class TestResourceManager extends MobiusResourceManager{
	public TestResourceManager(String file) throws Exception{
			MobiusConfigurator.parseMobiusConfiguration(file,this);
	}

}
