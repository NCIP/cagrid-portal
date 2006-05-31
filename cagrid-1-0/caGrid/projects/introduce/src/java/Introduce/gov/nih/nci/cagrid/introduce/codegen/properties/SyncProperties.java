package gov.nih.nci.cagrid.introduce.codegen.properties;

import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.beans.property.ServicePropertiesProperty;
import gov.nih.nci.cagrid.introduce.codegen.common.SyncTool;
import gov.nih.nci.cagrid.introduce.codegen.common.SynchronizationException;
import gov.nih.nci.cagrid.introduce.info.ServiceInformation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Create INTRODUCE_SERVICE_PROPERTIES file
 * 
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @created Jun 8, 2005
 * @version $Id: mobiusEclipseCodeTemplates.xml,v 1.2 2005/04/19 14:58:02 oster
 *          Exp $
 */
public class SyncProperties extends SyncTool {

	public SyncProperties(File baseDirectory, ServiceInformation info) {
		super(baseDirectory, info);
	}

	public void sync() throws SynchronizationException {

		
		Properties serviceProps = new Properties();
		//	load up the service properties
		if(getServiceInformation().getServiceProperties()!=null&&getServiceInformation().getServiceProperties().getProperty()!=null){
			for(int i =0;i < getServiceInformation().getServiceProperties().getProperty().length; i++){
				ServicePropertiesProperty prop = getServiceInformation().getServiceProperties().getProperty(i);
				if(prop.getValue()==null){
					serviceProps.put(prop.getKey(),"");
				} else {
					serviceProps.put(prop.getKey(),prop.getValue());
				}
			}
		}
		
		//write the service propertis out
		try {
			serviceProps.store(new FileOutputStream(new File(getBaseDirectory().getAbsolutePath()
				+ File.separator + IntroduceConstants.INTRODUCE_SERVICE_PROPERTIES)), "service deployment properties");
		} catch (Exception ex) {
			throw new SynchronizationException(ex.getMessage(),ex);
		}

	}

}
