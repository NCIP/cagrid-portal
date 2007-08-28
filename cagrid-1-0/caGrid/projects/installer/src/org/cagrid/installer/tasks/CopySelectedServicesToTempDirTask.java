/**
 * 
 */
package org.cagrid.installer.tasks;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.cagrid.installer.model.CaGridInstallerModel;
import org.cagrid.installer.steps.Constants;
import org.cagrid.installer.util.InstallerUtils;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class CopySelectedServicesToTempDirTask extends CaGridInstallerAntTask {

	/**
	 * @param name
	 * @param description
	 * @param targetName
	 */
	public CopySelectedServicesToTempDirTask(String name, String description) {
		super(name, description, "copy-selected-services");
	}

	protected Object runAntTask(CaGridInstallerModel model, String target, Map<String,String> env,
			Properties sysProps) throws Exception {
		List<String> selectedServices = new ArrayList<String>();
		
		if(model.isTrue(Constants.INSTALL_DORIAN)){
			selectedServices.add("dorian");
		}
		if(model.isTrue(Constants.INSTALL_GTS)){
			selectedServices.add("gts");
		}
		if(model.isTrue(Constants.INSTALL_AUTHN_SVC)){
			selectedServices.add("authentication-service");
		}
		if(model.isTrue(Constants.INSTALL_GRID_GROUPER)){
			selectedServices.add("gridgrouper");
		}
		if(model.isTrue(Constants.INSTALL_INDEX_SVC)){
			selectedServices.add("index");
		}
		if(model.isTrue(Constants.INSTALL_GME)){
			selectedServices.add("gme");
		}
		if(model.isTrue(Constants.INSTALL_EVS)){
			selectedServices.add("evs");
		}
		if(model.isTrue(Constants.INSTALL_FQP)){
			selectedServices.add("fqp");
		}
		if(model.isTrue(Constants.INSTALL_CADSR)){
			selectedServices.add("cadsr");
		}
		if(model.isTrue(Constants.INSTALL_WORKFLOW)){
			selectedServices.add("workflow");
		}
		if(model.isTrue(Constants.INSTALL_SYNC_GTS)){
			selectedServices.add("syncgts");
		}
		
		if(model.isTrue(Constants.INSTALL_PORTAL)){
			selectedServices.add("portal");
		}
		
		StringBuilder sb = new StringBuilder();
		for(Iterator i = selectedServices.iterator(); i.hasNext();){
			sb.append((String)i.next());
			if(i.hasNext()){
				sb.append(",");
			}
		}
		sysProps.setProperty("selected.services", sb.toString());

		new AntTask("", "", target, env, sysProps).execute(model);

		if(model.isTrue(Constants.INSTALL_BROWSER)){
			new AntTask("", "", "copy-browser", env, sysProps).execute(model);
		}
		
		return null;
	}

}
