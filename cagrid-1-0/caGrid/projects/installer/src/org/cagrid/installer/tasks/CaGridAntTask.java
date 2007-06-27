/**
 * 
 */
package org.cagrid.installer.tasks;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

import org.cagrid.installer.steps.Constants;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class CaGridAntTask extends BasicTask {

	private String targetName;
	
	/**
	 * @param name
	 * @param description
	 */
	public CaGridAntTask(String name, String description, String targetName) {
		super(name, description);
		this.targetName = targetName;
	}

	/* (non-Javadoc)
	 * @see org.cagrid.installer.tasks.BasicTask#internalExecute(java.util.Map)
	 */
	@Override
	protected Object internalExecute(Map state) throws Exception {
		Map env = new HashMap();
		env.put("GLOBUS_LOCATION", state.get(Constants.GLOBUS_HOME));
		env.put("CATALINA_HOME", state.get(Constants.TOMCAT_HOME));
		Properties sysProps = new Properties();
		for(Iterator i = state.entrySet().iterator(); i.hasNext();){
			Entry entry = (Entry)i.next();
			if(entry.getKey() instanceof String && entry.getValue() instanceof String){
				sysProps.setProperty((String)entry.getKey(), (String)entry.getValue());
			}
		}
		Map m = new HashMap(state);
		m.put(Constants.BUILD_FILE_PATH, getBuildFilePath(state));
		return runAntTask(m, this.targetName, env, sysProps);
	}
	
	protected Object runAntTask(Map state, String target, Map env, Properties sysProps) throws Exception{
		
		new AntTask("", "", target, env, sysProps).execute(state);
		
		return null;
	}
	
	protected String getBuildFilePath(Map state){
		return (String)state.get(Constants.BUILD_FILE_PATH);
	}

}
