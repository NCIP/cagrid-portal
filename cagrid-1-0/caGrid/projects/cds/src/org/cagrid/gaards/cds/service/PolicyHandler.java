package org.cagrid.gaards.cds.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cagrid.gaards.cds.conf.PolicyHandlerConfiguration;
import org.cagrid.gaards.cds.conf.Property;
import org.cagrid.gaards.cds.stubs.types.CDSInternalFault;
import org.cagrid.tools.database.Database;


public abstract class PolicyHandler {

	private Database db;
	private Log log;
	private Map<String, String> properties;


	public PolicyHandler(PolicyHandlerConfiguration conf, Database db) {
		this.log = LogFactory.getLog(this.getClass().getName());
		this.db = db;
		properties = new HashMap<String, String>();
		if (conf != null) {
			Property[] props = conf.getProperty();
			if (props != null) {
				for (int i = 0; i < props.length; i++) {
					properties.put(props[i].getName(), props[i].getValue());
				}
			}
		}
	}


	public String getPropertyValue(String name) {
		return this.properties.get(name);
	}


	public abstract void removeAllStoredPolicies() throws CDSInternalFault;


	protected Log getLog() {
		return log;
	}


	protected Database getDB() {
		return this.db;
	}

}
