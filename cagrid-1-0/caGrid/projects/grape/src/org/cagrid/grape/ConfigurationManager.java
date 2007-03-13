package org.cagrid.grape;

import gov.nih.nci.cagrid.common.Utils;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JMenuItem;
import javax.xml.namespace.QName;

import org.apache.log4j.Logger;
import org.cagrid.grape.model.Configuration;
import org.cagrid.grape.model.ConfigurationDescriptor;
import org.cagrid.grape.model.ConfigurationDescriptors;
import org.cagrid.grape.model.ConfigurationGroup;
import org.cagrid.grape.model.ConfigurationGroups;

public class ConfigurationManager {

	private static final String GRAPE_USER_HOME = Utils.getCaGridUserHome()
			.getAbsolutePath()
			+ File.separator + "grape";

	private Map confsByName = null;

	private Map objectsByName = null;

	private Logger log;

	private ApplicationContext context;

	public ConfigurationManager(ApplicationContext context,
			Configuration configuration) throws Exception {
		confsByName = new HashMap();
		objectsByName = new HashMap();
		this.context = context;
		log = Logger.getLogger(this.getClass().getName());
		if (configuration != null) {
			File f = new File(GRAPE_USER_HOME);
			f.mkdirs();
			this.processConfigurationGroups(configuration
					.getConfigurationGroups());
			this.processConfigurationDescriptors(configuration
					.getConfigurationDescriptors());

		}
	}

	private void processConfigurationGroups(ConfigurationGroups list)
			throws Exception {
		if (list != null) {
			ConfigurationGroup[] group = list.getConfigurationGroup();
			if (group != null) {
				for (int i = 0; i < group.length; i++) {
					this.processConfigurationGroup(group[i]);
				}

			}
		}
	}

	private void processConfigurationDescriptors(ConfigurationDescriptors list)
			throws Exception {
		if (list != null) {
			ConfigurationDescriptor[] des = list.getConfigurationDescriptor();
			if (des != null) {
				for (int i = 0; i < des.length; i++) {
					this.processConfigurationDescriptor(des[i]);
				}

			}
		}
	}

	private void processConfigurationGroup(ConfigurationGroup des)
			throws Exception {
		if (des != null) {
			processConfigurationDescriptors(des.getConfigurationDescriptors());
		}

	}

	private void processConfigurationDescriptor(
			final ConfigurationDescriptor des) throws Exception {
		if (confsByName.containsKey(des.getSystemName())) {
			throw new Exception(
					"Error configuring the application, more than one configuration was specified with the system name "
							+ des.getSystemName() + "!!!");
		} else {
			Object obj = null;
			File conf = new File(GRAPE_USER_HOME + File.separator
					+ des.getSystemName() + "-conf.xml");
			if (!conf.exists()) {
				File template = new File(des.getDefaultFile());
				if (!template.exists()) {
					throw new Exception(
							"Error configuring the application,the default file specified for the configuration "
									+ des.getSystemName()
									+ " does not exist!!!\n"
									+ template.getAbsolutePath()
									+ " not found!!!");
				} else {
					obj = Utils.deserializeDocument(template.getAbsolutePath(),
							Class.forName(des.getModelClassname()));
					log.info("Loading configuration for "
							+ des.getDisplayName() + " from "
							+ template.getAbsolutePath());
				}
			} else {
				obj = Utils.deserializeDocument(conf.getAbsolutePath(), Class
						.forName(des.getModelClassname()));
				log.info("Loading configuration for "
						+ des.getDisplayName() + " from "
						+ conf.getAbsolutePath());
			}

			// TODO: Move this elsewhere
			if (des.getUIClassname() != null) {
				Class[] types = new Class[3];
				types[0] = ApplicationContext.class;
				types[1] = String.class;
				types[2] = Class.forName(des.getModelClassname());

				Constructor c = Class.forName(des.getUIClassname())
						.getConstructor(types);
				Object[] args = new Object[3];
				args[0] = context;
				args[1] = des.getSystemName();
				args[2] = obj;
				final ConfigurationComponent config = (ConfigurationComponent) c
						.newInstance(args);
				JMenuItem item = new javax.swing.JMenuItem();
				item.setText(des.getDisplayName());
				item.setMnemonic(java.awt.event.KeyEvent.VK_Q);
				item.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent e) {
						context.addApplicationComponent(config);
					}
				});
			}
			confsByName.put(des.getSystemName(), des);
			objectsByName.put(des.getSystemName(), obj);
		}

	}

	public ConfigurationDescriptor getConfigurationDescriptor(String systemName)
			throws Exception {
		if (confsByName.containsKey(systemName)) {
			return (ConfigurationDescriptor) confsByName.get(systemName);
		} else {
			throw new Exception("The configuration " + systemName
					+ " does not exist!!!");
		}
	}
	
	public Object getConfigurationObject(String systemName) throws Exception {
		if (objectsByName.containsKey(systemName)) {
			return objectsByName.get(systemName);
		} else {
			throw new Exception("The configuration " + systemName
					+ " does not exist!!!");
		}
	}

	public void save(String systemName, Object obj) throws Exception {
		try {
			ConfigurationDescriptor des = getConfigurationDescriptor(systemName);
			File conf = new File(GRAPE_USER_HOME + File.separator
					+ des.getSystemName() + "-conf.xml");
			QName ns = new QName(des.getQname().getNamespace(), des.getQname()
					.getName());
			Utils.serializeDocument(conf.getAbsolutePath(), obj, ns);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new Exception("Error saving the configuration " + systemName
					+ ":\n" + e.getMessage());
		}
	}


}
