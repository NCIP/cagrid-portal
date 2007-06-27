/**
 * 
 */
package org.cagrid.installer.tasks;

import java.io.File;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cagrid.installer.util.PropertyUtils;
import org.pietschy.wizard.InvalidStateException;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class FindDirectoryNameTask extends BasicTask {
	

	private String parentDirPathProperty;
	private String pattern;
	private String propertyName;
	
	private static final Log logger = LogFactory.getLog(FindDirectoryNameTask.class);
	
	public FindDirectoryNameTask(String name, String description, String parentDirPathProperty, String pattern, String propertyName){
		super(name, description);
		this.parentDirPathProperty = parentDirPathProperty;
		this.pattern = pattern;
		this.propertyName = propertyName;
	}
	

	protected Object internalExecute(Map state) throws Exception {
		
		String parentDirPath = PropertyUtils.getRequiredProperty(state, this.parentDirPathProperty);
		File dir = new File(parentDirPath);
		if(!dir.exists()){
			throw new InvalidStateException(parentDirPath + " does not exist");
		}
		String dirPath = null;
		for(File f : dir.listFiles()){
			if(f.isDirectory() && this.pattern.matches(f.getName())){
				dirPath = f.getAbsolutePath();
				break;
			}
		}
		if(dirPath == null){
			if(dirPath == null){
				throw new InvalidStateException("Couldn't locate " + this.pattern + " directory in " + parentDirPath + " for " + this.propertyName);
			}
		}
		logger.debug("Setting " + this.propertyName + " to " + dirPath);
		state.put(this.propertyName, dirPath);
		
		return null;
	}


}
