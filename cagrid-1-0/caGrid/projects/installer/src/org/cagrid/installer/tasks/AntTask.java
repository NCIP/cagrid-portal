/**
 * 
 */
package org.cagrid.installer.tasks;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.cagrid.installer.steps.Constants;
import org.cagrid.installer.util.AntUtils;
import org.cagrid.installer.util.PropertyUtils;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class AntTask implements Task {
	
	private String name;
	private String description;
	private String target;
	
	public AntTask(String name, String description, String target){
		this.name = name;
		this.description = description;
		this.target = target;
	}

	/* (non-Javadoc)
	 * @see org.cagrid.installer.steps.Task#execute()
	 */
	public Object execute(Map state) throws Exception {
		String buildFilePath = PropertyUtils.getRequiredProperty(state, Constants.BUILD_FILE_PATH);
		String tempDirPath = PropertyUtils.getRequiredProperty(state, Constants.TEMP_DIR_PATH);
		
		try {
            File tempDir = new File(tempDirPath);
            tempDir.mkdirs();
            File propsFile = new File(tempDir.getAbsolutePath() + "/" + Math.random() + ".properties");
            Properties props = new Properties();
            props.putAll(state);
            props.store(new FileOutputStream(propsFile), "Temporary Properties File");

            File buildFile = new File(buildFilePath);
            if(!buildFile.exists()){
            	throw new RuntimeException("Build file doesn't exist: " + buildFilePath);
            }
            File baseDir = buildFile.getParentFile();
            Map<String, String> env = new HashMap<String, String>(System.getenv());
            String[] envp = new String[env.size()];
            int i = 0;
            for (String key : env.keySet()) {
                envp[i++] = key + "=" + env.get(key);
            }
            AntUtils.runAnt(baseDir, buildFilePath, this.target, new Properties(), envp, propsFile.getAbsolutePath());
            propsFile.delete();
        } catch (Exception ex) {
            throw new RuntimeException("Error encountered: " + ex.getMessage(), ex);
        }
		return null;
	}

	

	/* (non-Javadoc)
	 * @see org.cagrid.installer.steps.Task#getDescription()
	 */
	public String getDescription() {
		return description;
	}

	/* (non-Javadoc)
	 * @see org.cagrid.installer.steps.Task#getName()
	 */
	public String getName() {
		return name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static void main(String[] args) throws Exception {
		String buildFilePath = "/Users/joshua/dev3/cagrid_head/caGrid/projects/installer/deployer/tomcat-tools.xml";
		String targetName = "unzip-globus";
		String tempDirPath = "temp";
		Map<String,String> state = new HashMap<String,String>();
		state.put(Constants.BUILD_FILE_PATH, buildFilePath);
		state.put(Constants.TOMCAT_ZIP_PATH, "/Users/joshua/dev3/cagrid_head/caGrid/projects/installer/deployer/software/jakarta-tomcat-5.0.28.zip");
		state.put(Constants.GLOBUS_ZIP_PATH, "/Users/joshua/dev3/cagrid_head/caGrid/projects/installer/deployer/software/ws-core-4.0.3-bin.zip");
		state.put(Constants.TEMP_DIR_PATH, tempDirPath);
		AntTask task = new AntTask("test", "test", targetName);
		task.execute(state);
	}
}
