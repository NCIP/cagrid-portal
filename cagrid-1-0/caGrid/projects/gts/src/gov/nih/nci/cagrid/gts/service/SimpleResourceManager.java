package gov.nih.nci.cagrid.gts.service;

import org.projectmobius.common.MobiusConfigurator;
import org.projectmobius.common.MobiusResourceManager;
import java.io.InputStream;

public class SimpleResourceManager extends MobiusResourceManager{
    public SimpleResourceManager(String file) throws Exception{
            MobiusConfigurator.parseMobiusConfiguration(file,this);
    }
    
    public SimpleResourceManager(InputStream in) throws Exception{
            MobiusConfigurator.parseMobiusConfiguration(in,this);
    }

}