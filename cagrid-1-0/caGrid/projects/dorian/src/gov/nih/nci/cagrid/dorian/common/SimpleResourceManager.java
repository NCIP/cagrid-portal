package gov.nih.nci.cagrid.dorian.common;

import org.projectmobius.common.MobiusConfigurator;
import org.projectmobius.common.MobiusResourceManager;

public class SimpleResourceManager extends MobiusResourceManager{
	public SimpleResourceManager(String file) throws Exception{
			MobiusConfigurator.parseMobiusConfiguration(file,this);
	}

}
