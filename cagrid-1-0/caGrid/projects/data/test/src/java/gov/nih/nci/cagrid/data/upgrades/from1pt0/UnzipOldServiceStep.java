package gov.nih.nci.cagrid.data.upgrades.from1pt0;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.atomicobject.haste.framework.Step;

/** 
 *  UnzipOldServiceStep
 *  Step to unzip the old service
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>  * 
 * @created Feb 20, 2007 
 * @version $Id: UnzipOldServiceStep.java,v 1.1 2007-02-20 21:03:17 dervin Exp $ 
 */
public class UnzipOldServiceStep extends Step {
	private String testDir;
	
	public UnzipOldServiceStep(String testDir) {
		super();
		this.testDir = testDir;
	}
	

	public void runStep() throws Throwable {
		FileInputStream zipFileInput = new FileInputStream(testDir + File.separator + "resources" + File.separator + "CaGridTutorialService1.0.zip");
		ZipInputStream zipInput = new ZipInputStream(zipFileInput);
		ZipEntry entry = null;
		while ((entry = zipInput.getNextEntry()) != null) {
			String name = entry.getName();
			File outFile = new File(testDir + File.separator + name);
			if (entry.isDirectory()) {
				outFile.mkdirs();
			} else {
				outFile.createNewFile();
				BufferedOutputStream fileOut = new BufferedOutputStream(
					new FileOutputStream(testDir + File.separator + name));
				copyStreams(zipInput, fileOut);
				fileOut.flush();
				fileOut.close();
			}
		}
		zipInput.close();
	}
	
	
	private void copyStreams(ZipInputStream input, BufferedOutputStream output) throws IOException {
		byte[] temp = new byte[8192];
		int read = -1;
		while ((read = input.read(temp)) != -1) {
			output.write(temp, 0, read);
		}
	}
}
