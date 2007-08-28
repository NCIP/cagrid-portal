/**
 * 
 */
package org.cagrid.installer.tasks;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tools.tar.TarEntry;
import org.apache.tools.tar.TarInputStream;
import org.cagrid.installer.model.CaGridInstallerModel;
import org.cagrid.installer.steps.Constants;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class UnTarInstallTask extends BasicTask {

	private String tempFileNameProp;

	private String installDirPathProp;

	private String dirNameProp;

	private String homeProp;

	private static final Log logger = LogFactory.getLog(UnTarInstallTask.class);

	public UnTarInstallTask(String name, String description,
			String tempFileNameProp, String installDirPathProp,
			String dirNameProp, String homeProp) {
		super(name, description);
		this.tempFileNameProp = tempFileNameProp;
		this.installDirPathProp = installDirPathProp;
		this.dirNameProp = dirNameProp;
		this.homeProp = homeProp;
	}

	protected Object internalExecute(CaGridInstallerModel model) throws Exception {

		
		File installDir = new File(model.getProperty(this.installDirPathProp));
		File home = new File(installDir.getAbsolutePath() + "/"
				+ model.getProperty(this.dirNameProp));
		model.setProperty(this.homeProp, home.getAbsolutePath());

		home.delete();

		try {
			String path = model.getProperty(Constants.TEMP_DIR_PATH) + "/"
					+ model.getProperty(this.tempFileNameProp);

			logger.info("Trying to extract Tar File for " + path);

			unTarFiles(path, installDir);

		} catch (Exception ex) {
			throw new RuntimeException("Error instantiating zip file: "
					+ ex.getMessage(), ex);
		}

		return null;
	}

	private void unTarFiles(String tarFileName, File dest) throws IOException {
		// assuming the file you pass in is not a dir
		dest.mkdir();
		// create tar input stream from a .tar.gz file
		TarInputStream tin = new TarInputStream(new GZIPInputStream(
				new FileInputStream(new File(tarFileName))));

		// get the first entry in the archive
		TarEntry tarEntry = tin.getNextEntry();
		while (tarEntry != null) {
			// create a file with the same name as the tarEntry
			File destPath = new File(dest.toString() + File.separatorChar
					+ tarEntry.getName());
			if (tarEntry.isDirectory()) {
				destPath.mkdir();
			} else {
				FileOutputStream fout = new FileOutputStream(destPath);
				tin.copyEntryContents(fout);
				fout.close();
			}
			tarEntry = tin.getNextEntry();
		}
		tin.close();
	}

}
