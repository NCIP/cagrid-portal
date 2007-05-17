/**
 * 
 */
package org.cagrid.installer.path;

import org.pietschy.wizard.models.BranchingPath;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class SystemCheckPath extends BranchingPath implements InitializingBean {

	private String gridEnvPropertiesFileName = System.getProperty("user.home") + "/.gridenv.properties";
	
	/**
	 * 
	 */
	public SystemCheckPath() {
		// TODO Auto-generated constructor stub
	}

	public void afterPropertiesSet() throws Exception {
		//Check for the existence of
		
	}

}
