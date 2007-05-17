/**
 * 
 */
package org.cagrid.installer.steps;

import gov.nih.nci.cagrid.common.wizard.steps.BaseBusyStep;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.cagrid.installer.util.AntUtils;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class RunAntStep extends BaseBusyStep {

	private static final String BUILD_FILE_NAME = "installer.ant.buildFile";

	private static final String TARGET_NAME = "installer.ant.target";

	private Map<String,String> properties;
	
	private String tempDirName = "temp";
	private String defaultBuildFileName;
	private String defaultTargetName;
	
	public String getDefaultTargetName() {
		return defaultTargetName;
	}

	public void setDefaultTargetName(String defaultTargetName) {
		this.defaultTargetName = defaultTargetName;
	}

	public String getDefaultBuildFileName() {
		return defaultBuildFileName;
	}

	public void setDefaultBuildFileName(String defaultBuildFileName) {
		this.defaultBuildFileName = defaultBuildFileName;
	}

	/**
	 * @param description
	 */
	public RunAntStep(Map<String,String> properties, String description) {
		super(description);

		this.properties = properties;
		
	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.cagrid.common.wizard.steps.BaseBusyStep#doWork()
	 */
	@Override
	protected void doWork() {
		
		try{
			File tempDir = new File(getTempDirName());
			tempDir.mkdirs();
			File propsFile = new File(tempDir.getAbsolutePath() + "/" + System.currentTimeMillis() + ".properties");
			Properties props = new Properties();
			props.putAll(this.properties);
			props.store(new FileOutputStream(propsFile), "Temporary Properties File");
			
			String buildFileName = props.getProperty(BUILD_FILE_NAME, getDefaultBuildFileName());
			String targetName = props.getProperty(TARGET_NAME, getDefaultTargetName());
			File baseDir = new File(buildFileName).getParentFile();
			Map<String,String> env = new HashMap<String,String>(System.getenv());
			String[] envp = new String[env.size()];
			int i = 0;
			for(String key : env.keySet()){
				envp[i++] = key + "=" + env.get(key);
			}
			AntUtils.runAnt(baseDir, buildFileName, targetName, new Properties(), envp, propsFile.getAbsolutePath());
			tempDir.delete();
		}catch(Exception ex){
			throw new RuntimeException("Error encountered: " + ex.getMessage(), ex);
		}
	}

	public String getTempDirName() {
		return tempDirName;
	}

	public void setTempDirName(String tempDirName) {
		this.tempDirName = tempDirName;
	}

}
