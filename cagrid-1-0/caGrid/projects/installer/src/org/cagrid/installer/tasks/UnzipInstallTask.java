/**
 * 
 */
package org.cagrid.installer.tasks;

import java.io.File;
import java.net.URL;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cagrid.installer.steps.Constants;
import org.cagrid.installer.util.Utils;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class UnzipInstallTask implements Task {

	private String name;

	private String description;

	private String downloadUrlProp;

	private String tempFileNameProp;

	private String installDirPathProp;

	private String dirNameProp;

	private String homeProp;

	private static final Log logger = LogFactory.getLog(UnzipInstallTask.class);

	public UnzipInstallTask(String name, String description,
			String downloadUrlProp, String tempFileNameProp,
			String installDirPathProp, String dirNameProp, String homeProp) {
		this.name = name;
		this.description = description;
		this.downloadUrlProp = downloadUrlProp;
		this.tempFileNameProp = tempFileNameProp;
		this.installDirPathProp = installDirPathProp;
		this.dirNameProp = dirNameProp;
		this.homeProp = homeProp;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cagrid.installer.tasks.Task#execute(java.util.Map)
	 */
	public Object execute(Map state) throws Exception {

		URL fromUrl = new URL((String) state.get(this.downloadUrlProp));
		String tempDir = (String) state.get(Constants.TEMP_DIR_PATH);
		File toFile = new File(tempDir + "/"
				+ (String) state.get(this.tempFileNameProp));

		logger.info("Downloading " + fromUrl.toString() + " to "
				+ toFile.getAbsolutePath());
		Utils.downloadFile(fromUrl, toFile);

		File installDir = new File((String) state.get(this.installDirPathProp));
		File home = new File(installDir.getAbsolutePath() + "/"
				+ state.get(this.dirNameProp));
		state.put(this.homeProp, home.getAbsolutePath());

		logger.info("Deleting '" + home.getAbsolutePath());
		home.delete();

		logger.info("Unzipping " + toFile.getAbsolutePath() + " to "
				+ installDir.getAbsolutePath());
		Utils.unzipFile(toFile, installDir);

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cagrid.installer.tasks.Task#getDescription()
	 */
	public String getDescription() {
		return this.description;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cagrid.installer.tasks.Task#getName()
	 */
	public String getName() {
		return this.name;
	}

}
