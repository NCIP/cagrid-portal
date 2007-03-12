package org.cagrid.grape;

import gov.nih.nci.cagrid.common.Utils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JMenu;

import org.cagrid.grape.model.Application;
import org.cagrid.grape.model.Configuration;
import org.cagrid.grape.model.ConfigurationDescriptor;
import org.cagrid.grape.model.ConfigurationDescriptors;
import org.cagrid.grape.model.ConfigurationGroup;
import org.cagrid.grape.model.ConfigurationGroups;

public class ConfigurationManager {

	
	private static final String GRAPE_USER_HOME = Utils.getCaGridUserHome().getAbsolutePath()+File.separator+"grape";
	
	private JMenu menu = null;

	private Map confsByName = null;
	private Map objectsByName = null;
	private boolean hasItemsInMenu;

	public ConfigurationManager(Configuration configuration) throws Exception{
		confsByName = new HashMap();
		objectsByName = new HashMap();
		menu = new javax.swing.JMenu();
		menu.setText("Configuration");
		menu.setMnemonic(java.awt.event.KeyEvent.VK_F);
		hasItemsInMenu = false;

		if (configuration != null) {
			File f = new File(GRAPE_USER_HOME);
			f.mkdirs();
			this.processConfigurationGroups(configuration.getConfigurationGroups(), this.menu);
			this.processConfigurationDescriptors(configuration
					.getConfigurationDescriptors(), this.menu);

		}
	}
	
	private void processConfigurationGroups(ConfigurationGroups list,
			JMenu m) throws Exception{
		if (list != null) {
			ConfigurationGroup[] group = list.getConfigurationGroup();
			if (group != null) {
				for (int i = 0; i < group.length; i++) {
					this.processConfigurationGroup(group[i], m);
				}

			}
		}
	}
	

	private void processConfigurationDescriptors(ConfigurationDescriptors list,
			JMenu m) throws Exception{
		if (list != null) {
			ConfigurationDescriptor[] des = list.getConfigurationDescriptor();
			if (des != null) {
				for (int i = 0; i < des.length; i++) {
					this.processConfigurationDescriptor(des[i], m);
				}

			}
		}
	}
	
	private void processConfigurationGroup(ConfigurationGroup des,
			JMenu m) throws Exception{

	}

	private void processConfigurationDescriptor(final ConfigurationDescriptor des,
			JMenu m) throws Exception{
		if(confsByName.containsKey(des.getSystemName())){
			throw new Exception("Error configuring the application, more than one configuration was specified with the system name "+des.getSystemName()+"!!!");
		}else{
			Object obj = null;
			File conf = new File(GRAPE_USER_HOME+File.separator+des.getSystemName()+"-conf.xml");
			if(!conf.exists()){
				File template = new File(des.getDefaultFile());
				if(!template.exists()){
					throw new Exception("Error configuring the application,the default file specified for the configuration "+des.getSystemName()+" does not exist!!!\n"+template.getAbsolutePath()+" not found!!!");
				}else{
					obj = Utils.deserializeDocument(template.getAbsolutePath(), Class.forName(des.getModelClassname()));
				}
			}else{
				obj = Utils.deserializeDocument(conf.getAbsolutePath(), Class.forName(des.getModelClassname()));
			}
			
			//TODO Add menu item to the menu
			
			confsByName.put(des.getSystemName(), des);
			objectsByName.put(des.getSystemName(), obj);
		}
		
		
	}

	public JMenu getConfigurationMenu() {
		if (hasItemsInMenu) {
			return menu;
		}else{
			return null;
		}
	}

}
