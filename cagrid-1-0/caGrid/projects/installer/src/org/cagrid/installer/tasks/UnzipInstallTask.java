/**
 * 
 */
package org.cagrid.installer.tasks;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cagrid.installer.steps.Constants;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class UnzipInstallTask extends BasicTask {

	private static final int BUFFER_SIZE = 1024;

	private String tempFileNameProp;

	private String installDirPathProp;

	private String dirNameProp;

	private String homeProp;

	private static final Log logger = LogFactory.getLog(UnzipInstallTask.class);

	public UnzipInstallTask(String name, String description,
			String tempFileNameProp, String installDirPathProp,
			String dirNameProp, String homeProp) {
		super(name, description);
		this.tempFileNameProp = tempFileNameProp;
		this.installDirPathProp = installDirPathProp;
		this.dirNameProp = dirNameProp;
		this.homeProp = homeProp;
	}


	protected Object internalExecute(Map state) throws Exception {

		ZipFile zipFile = null;
		try {
			String path = state.get(Constants.TEMP_DIR_PATH) + "/"
					+ state.get(this.tempFileNameProp);

			logger.info("Trying to open ZipFile for " + path);
			zipFile = new ZipFile(new File(path));
		} catch (Exception ex) {
			throw new RuntimeException("Error instantiating zip file: "
					+ ex.getMessage(), ex);
		}
//		setStepCount(zipFile.size());

		File installDir = new File((String) state.get(this.installDirPathProp));
		File home = new File(installDir.getAbsolutePath() + "/"
				+ state.get(this.dirNameProp));
		state.put(this.homeProp, home.getAbsolutePath());

		home.delete();

		try {
			String path = state.get(Constants.TEMP_DIR_PATH) + "/"
					+ state.get(this.tempFileNameProp);

			logger.info("Trying to open ZipFile for " + path);
			zipFile = new ZipFile(new File(path));
		} catch (Exception ex) {
			throw new RuntimeException("Error instantiating zip file: "
					+ ex.getMessage(), ex);
		}

		String baseOut = installDir.getAbsolutePath() + "/";
		Enumeration entries = zipFile.entries();
		int subTaskNum = 0;
		while (entries.hasMoreElements()) {
			ZipEntry entry = (ZipEntry) entries.nextElement();
			if (entry.isDirectory()) {
				File dir = new File(baseOut + entry.getName());
				if (!dir.exists()) {
					dir.mkdirs();
				}

			} else {

				InputStream in = zipFile.getInputStream(entry);
				BufferedOutputStream out = new BufferedOutputStream(
						new FileOutputStream(baseOut + entry.getName()));
				byte[] buffer = new byte[BUFFER_SIZE];
				int len = -1;
				while ((len = in.read(buffer)) > 0) {
					out.write(buffer, 0, len);
					buffer = new byte[BUFFER_SIZE];
				}
				out.flush();
				out.close();
				in.close();
			}
//			setLastStep(getLastStep() + 1);
		}
		zipFile.close();

		return null;
	}

}
