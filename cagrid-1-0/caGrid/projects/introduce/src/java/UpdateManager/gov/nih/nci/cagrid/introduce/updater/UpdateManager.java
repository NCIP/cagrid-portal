package gov.nih.nci.cagrid.introduce.updater;

import gov.nih.nci.cagrid.introduce.beans.software.ExtensionType;
import gov.nih.nci.cagrid.introduce.beans.software.IntroduceType;
import gov.nih.nci.cagrid.introduce.beans.software.SoftwareType;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.axis.utils.XMLUtils;
import org.apache.bcel.verifier.exc.StaticCodeConstraintException;
import org.globus.wsrf.encoding.DeserializationException;
import org.globus.wsrf.encoding.ObjectDeserializer;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class UpdateManager {

	private SoftwareType software = null;

	UpdateManager(SoftwareType software) {
		this.software = software;
	}

	public void execute() {
		IntroduceType[] introduceTypes = software.getIntroduce();

		ExtensionType[] extensionTypes = software.getExtension();

		
		if (introduceTypes != null) {
			for (int i = 0; i < introduceTypes.length; i++) {
				IntroduceType update = introduceTypes[i];
				//if it is an introduce update i need to delete all files and 
				//and directories before unzipping
				File baseDir = new File(".");
				File[] files = baseDir.listFiles();
				System.out.println("Removing old version of Introduce.");
				for(int fileI = 0; fileI < files.length; fileI++){
					File f = files[fileI];
					if(f.isDirectory() && !f.getName().equals("updates")){
						deleteDir(f);
					} else {
						f.delete();
					}
				}
				
				System.out.println("Installing new version of Introduce.");
				File updateFile = new File("." + File.separator
						+ "updates" + File.separator + "introduce"
						+ update.getVersion() + ".zip");
				try {
					unzipUpdate(updateFile);
					updateFile.delete();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		if (extensionTypes != null) {
			for (int i = 0; i < extensionTypes.length; i++) {
				ExtensionType update = extensionTypes[i];
				File updateFile = new File("." + File.separator
						+ "updates" + File.separator + update.getName()
						+ update.getVersion() + ".zip");
				try {
					unzipUpdate(updateFile);
					updateFile.delete();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}
	
	public static boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					System.err.println("could not remove directory: " + dir.getAbsolutePath());
					return false;
				}
			}
		}
		return dir.delete();
	}

	private void unzipUpdate(File cachedFile) throws IOException {

		InputStream in = new BufferedInputStream(
				new FileInputStream(cachedFile));
		ZipInputStream zin = new ZipInputStream(in);
		ZipEntry e;
		while ((e = zin.getNextEntry()) != null) {
			if (e.isDirectory()) {
				new File(e.getName()).mkdirs();
			} else {
				unzip(".", zin, e.getName());
			}
		}
		zin.close();
	}

	private static void unzip(String baseDir, ZipInputStream zin, String s)
			throws IOException {
		File file = new File(new File(baseDir).getAbsolutePath()
				+ File.separator + s);
		file.getParentFile().mkdirs();
		FileOutputStream out = new FileOutputStream(file);
		byte[] b = new byte[512];
		int len = 0;
		while ((len = zin.read(b)) != -1) {
			out.write(b, 0, len);
		}
		out.close();
	}
	
	

	public static void main(String[] args) {
		File updateFile = new File("updates" + File.separator + "software.xml");
		if(!updateFile.exists()){
			System.out.println("No updates to process");
			System.exit(0);
		}
		
		org.w3c.dom.Document doc = null;
		try {
			doc = XMLUtils.newDocument(new InputSource(new FileInputStream(updateFile
					)));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SoftwareType software = null;
		try {
			software = (SoftwareType) ObjectDeserializer.toObject(doc
					.getDocumentElement(), SoftwareType.class);
		} catch (DeserializationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		UpdateManager manager = new UpdateManager(software);
		manager.execute();
		updateFile.delete();
		System.exit(1);
	}

}
