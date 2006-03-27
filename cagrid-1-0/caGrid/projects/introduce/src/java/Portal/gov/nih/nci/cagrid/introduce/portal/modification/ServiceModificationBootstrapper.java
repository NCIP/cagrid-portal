package gov.nih.nci.cagrid.introduce.portal.modification;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.projectmobius.portal.GridPortalComponent;
import org.projectmobius.portal.PortalResourceManager;

import gov.nih.nci.cagrid.common.portal.PortalUtils;
import gov.nih.nci.cagrid.introduce.ResourceManager;
import gov.nih.nci.cagrid.introduce.portal.types.data.DataServiceModifier;

/** 
 *  ServiceModificationBootstrapper
 *  Allows user to select a grid service to modify and launches
 *  the appropriate viewer for making modifications
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 *  
 * @created Mar 27, 2006 
 * @version $Id$ 
 */
public class ServiceModificationBootstrapper extends GridPortalComponent {

	public ServiceModificationBootstrapper() {
		super();
		Thread chooserThread = createChooserThread();
		chooserThread.start();
	}
	
	
	private Thread createChooserThread() {
		Thread th = new Thread() {
			public void run() {
				try {
					String dirName = ResourceManager.promptDir(ServiceModificationBootstrapper.this, null);
					// load up properties
					Properties props = loadProperties(dirName);
					if (props.containsKey("introduce.data.service") && Boolean.valueOf(props.getProperty("introduce.data.service")).booleanValue()) {
						// it's a data service
						PortalResourceManager.getInstance().getGridPortal().addGridPortalComponent(new DataServiceModifier(new File(dirName)));
						dispose();
					} else {
						PortalResourceManager.getInstance().getGridPortal().addGridPortalComponent(new ModificationViewer(new File(dirName)));
						dispose();
					}
				} catch (Exception ex) {
					ex.printStackTrace();
					PortalUtils.showErrorMessage("Error choosing service", ex);
				}
			}
		};
		return th;
	}
	
	
	private Properties loadProperties(String dir) throws Exception {
		Properties serviceProps = new Properties();
		InputStream propsStream = new FileInputStream(new File(dir + File.separator + "introduce.properties"));
		serviceProps.load(propsStream);
		propsStream.close();
		return serviceProps;
	}
}
