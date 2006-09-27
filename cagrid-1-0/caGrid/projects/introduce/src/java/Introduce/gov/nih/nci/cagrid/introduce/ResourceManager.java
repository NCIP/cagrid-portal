package gov.nih.nci.cagrid.introduce;

import gov.nih.nci.cagrid.common.Utils;

import java.awt.Component;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import org.jdom.Document;
import org.projectmobius.common.XMLUtilities;


/**
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 */
public class ResourceManager {
	public static final int MAX_ARCHIVE = 5;

	public final static String PORTAL_CONFIG_FILE = "introduce-portal-conf.xml";

	public final static String SERVICE_URL_FILE = "service_urls.properties";

	public final static String CONFIG_PROPERTIES_FILE = "configuration.properties";

	public final static String CACHE_POSTFIX = "_backup.zip";

	public final static String STATE_FILE = "introduce.state.resources";

	public final static String LAST_DIRECTORY = "introduce.lastdir";

	public final static String LAST_DEPLOYMENT = "introduce.lastdeployment";

	public final static String LAST_FILE = "introduce.lastfile";


	public static String getResourcePath() {
		File caGridCache = Utils.getCaGridUserHome();
		File introduceCache = new File(caGridCache + File.separator + "introduce");
		introduceCache.mkdir();
		return introduceCache.getAbsolutePath();
	}


	public static String getServiceURLProperty(String key) throws Exception {
		Properties serviceProps = new Properties();
		if (!new File(getResourcePath() + File.separator + SERVICE_URL_FILE).exists()) {
			serviceProps.store(new FileOutputStream(new File(getResourcePath() + File.separator + SERVICE_URL_FILE)),
				"Introduce Global Service URLs");
		} else {
			serviceProps.load(new FileInputStream(new File(getResourcePath() + File.separator + SERVICE_URL_FILE)));
		}
		return (String) serviceProps.get(key);
	}


	public static Enumeration getServiceURLKeys() throws Exception {
		Properties serviceProps = new Properties();
		if (!new File(getResourcePath() + File.separator + SERVICE_URL_FILE).exists()) {
			serviceProps.store(new FileOutputStream(new File(getResourcePath() + File.separator + SERVICE_URL_FILE)),
				"Introduce Global Service URLs");
		} else {
			serviceProps.load(new FileInputStream(new File(getResourcePath() + File.separator + SERVICE_URL_FILE)));
		}
		return serviceProps.keys();
	}


	public static void setServiceURLProperty(String key, String value) throws Exception {
		Properties serviceProps = new Properties();
		serviceProps.load(new FileInputStream(new File(getResourcePath() + File.separator + SERVICE_URL_FILE)));
		serviceProps.put(key, value);
		serviceProps.store(new FileOutputStream(new File(getResourcePath() + File.separator + SERVICE_URL_FILE)),
			"Introduce Global Service URLs");
	}


	public static String getConfigurationProperty(String key) throws Exception {
		Properties serviceProps = new Properties();
		if (!new File(getResourcePath() + File.separator + CONFIG_PROPERTIES_FILE).exists()) {
			serviceProps.store(new FileOutputStream(new File(getResourcePath() + File.separator
				+ CONFIG_PROPERTIES_FILE)), "Introduce Global Configuration Properties");
		} else {
			serviceProps
				.load(new FileInputStream(new File(getResourcePath() + File.separator + CONFIG_PROPERTIES_FILE)));
		}
		return (String) serviceProps.get(key);
	}


	public static Enumeration getConfigurationPropertyKeys() throws Exception {
		Properties serviceProps = new Properties();
		if (!new File(getResourcePath() + File.separator + CONFIG_PROPERTIES_FILE).exists()) {
			serviceProps.store(new FileOutputStream(new File(getResourcePath() + File.separator
				+ CONFIG_PROPERTIES_FILE)), "Introduce Global Configuration Properties");
		} else {
			serviceProps
				.load(new FileInputStream(new File(getResourcePath() + File.separator + CONFIG_PROPERTIES_FILE)));
		}
		return serviceProps.keys();
	}


	public static void setConfigurationProperty(String key, String value) throws Exception {
		Properties serviceProps = new Properties();
		serviceProps.load(new FileInputStream(new File(getResourcePath() + File.separator + CONFIG_PROPERTIES_FILE)));
		serviceProps.put(key, value);
		serviceProps.store(new FileOutputStream(new File(getResourcePath() + File.separator + CONFIG_PROPERTIES_FILE)),
			"Introduce Global Configuration Properties");
	}


	public static String getPortalConfigFileLocation() {
		if (!new File(getResourcePath() + File.separator + PORTAL_CONFIG_FILE).exists()) {
			// need to copy over the example configuration file to the users
			// space
			try {
				Utils.copyFile(new File("conf" + File.separator + "introduce" + File.separator
					+ "introduce-portal-conf.xml.example"), new File(getResourcePath() + File.separator
					+ PORTAL_CONFIG_FILE));
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
		return new File(getResourcePath() + File.separator + PORTAL_CONFIG_FILE).getAbsolutePath();
	}


	public static void setConfigFile(Document doc) throws Exception {
		FileWriter fw = new FileWriter(getPortalConfigFileLocation());
		fw.write(XMLUtilities.formatXML(XMLUtilities.documentToString(doc)));
		fw.close();

	}


	public static String getStateProperty(String key) throws Exception {
		File lastDir = new File(getResourcePath() + File.separator + STATE_FILE);
		Properties properties = new Properties();
		if (!lastDir.exists()) {
			lastDir.createNewFile();
		}
		properties.load(new FileInputStream(lastDir));
		return properties.getProperty(key);
	}


	public static void setStateProperty(String key, String value) throws Exception {
		if (key != null) {
			File lastDir = new File(getResourcePath() + File.separator + STATE_FILE);
			if (!lastDir.exists()) {
				lastDir.createNewFile();
			}
			Properties properties = new Properties();
			properties.load(new FileInputStream(lastDir));
			properties.setProperty(key, value);
			properties.store(new FileOutputStream(lastDir), "");
		}
	}


	private static synchronized void getDirectoryListing(List names, File dir, String parentPath) {
		String[] children = dir.list();
		if (children != null) {
			for (int i = 0; i < children.length; i++) {
				File child = new File(dir.getAbsolutePath() + File.separator + children[i]);
				if (child.isDirectory()) {
					if (parentPath.equals("")) {
						getDirectoryListing(names, child, child.getName());
					} else {
						getDirectoryListing(names, child, parentPath + File.separator + child.getName());
					}
				} else {
					if (parentPath.equals("")) {
						names.add(child.getName());
					} else {
						names.add(parentPath + File.separator + child.getName());
					}
				}
			}
		}
	}


	public static synchronized void purgeArchives(String serviceName) throws Exception {
		String introduceCache = getResourcePath();

		final String finalServiceName = serviceName;
		FilenameFilter f = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.indexOf(finalServiceName) != -1;
			}
		};

		File introduceCacheFile = new File(introduceCache);
		String[] cacheFiles = introduceCacheFile.list(f);
		List cacheFilesList = Arrays.asList(cacheFiles);
		Collections.sort(cacheFilesList, String.CASE_INSENSITIVE_ORDER);
		Collections.reverse(cacheFilesList);

		for (int i = 0; i < cacheFilesList.size(); i++) {
			System.out.println("Removing file from cache: " + i + "  " + introduceCache + File.separator
				+ cacheFilesList.get(i));
			File cacheFile = new File(introduceCache + File.separator + cacheFilesList.get(i));
			cacheFile.delete();
		}
	}


	public static synchronized void createArchive(String id, String serviceName, String baseDir) throws Exception {
		File dir = new File(baseDir);

		String introduceCache = getResourcePath();

		List filenames = new ArrayList();
		getDirectoryListing(filenames, dir, "");

		// Create a buffer for reading the files
		byte[] buf = new byte[1024];

		// Create the ZIP file
		String outFilename = introduceCache + File.separator + serviceName + "_" + id + CACHE_POSTFIX;
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(outFilename));

		// Compress the files
		for (int i = 0; i < filenames.size(); i++) {
			FileInputStream in = new FileInputStream(dir.getAbsolutePath() + File.separator + (String) filenames.get(i));

			// Add ZIP entry to output stream.
			out.putNextEntry(new ZipEntry((String) filenames.get(i)));

			// Transfer bytes from the file to the ZIP file
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}

			// Complete the entry
			out.closeEntry();
			in.close();
		}

		// Complete the ZIP file
		out.flush();
		out.close();

		// cleanup if there are more that MAX_ARCHIVE files in the backup area
		cleanup(serviceName);
	}


	private static void cleanup(String serviceName) {
		String introduceCache = getResourcePath();

		final String finalServiceName = serviceName;
		FilenameFilter f = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.indexOf(finalServiceName) != -1;
			}
		};

		File introduceCacheFile = new File(introduceCache);
		String[] cacheFiles = introduceCacheFile.list(f);
		List cacheFilesList = Arrays.asList(cacheFiles);
		Collections.sort(cacheFilesList, String.CASE_INSENSITIVE_ORDER);
		Collections.reverse(cacheFilesList);

		if (cacheFilesList.size() > MAX_ARCHIVE) {
			for (int i = MAX_ARCHIVE; i < cacheFilesList.size(); i++) {
				System.out.println("Removing file from cache: " + i + "  " + introduceCache + File.separator
					+ cacheFilesList.get(i));
				File cacheFile = new File(introduceCache + File.separator + cacheFilesList.get(i));
				cacheFile.delete();
			}
		}
	}


	private static void unzip(String baseDir, ZipInputStream zin, String s) throws IOException {
		File file = new File(new File(baseDir).getAbsolutePath() + File.separator + s);
		file.getParentFile().mkdirs();
		FileOutputStream out = new FileOutputStream(file);
		byte[] b = new byte[512];
		int len = 0;
		while ((len = zin.read(b)) != -1) {
			out.write(b, 0, len);
		}
		out.close();
	}


	public static synchronized void restoreLatest(String currentId, String serviceName, String baseDir)
		throws Exception {

		File introduceCache = new File(getResourcePath());
		introduceCache.mkdir();

		final String finalServiceName = serviceName;
		FilenameFilter f = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.indexOf(finalServiceName) != -1;
			}
		};

		String[] cacheFiles = introduceCache.list(f);
		long thisTime = Long.parseLong(currentId);
		long lastTime = 0;
		for (int i = 0; i < cacheFiles.length; i++) {
			StringTokenizer strtok = new StringTokenizer(cacheFiles[i], "_", false);
			strtok.nextToken();
			String timeS = strtok.nextToken();
			long time = Long.parseLong(timeS);
			if (time > lastTime && time < thisTime) {
				lastTime = time;
			}
		}

		File cachedFile = new File(introduceCache.getAbsolutePath() + File.separator + serviceName + "_"
			+ String.valueOf(lastTime) + CACHE_POSTFIX);

		InputStream in = new BufferedInputStream(new FileInputStream(cachedFile));
		ZipInputStream zin = new ZipInputStream(in);
		ZipEntry e;
		while ((e = zin.getNextEntry()) != null) {
			if (e.isDirectory()) {
				new File(new File(baseDir).getAbsolutePath() + File.separator + e.getName()).mkdirs();
			} else {
				unzip(baseDir, zin, e.getName());
			}
		}
		zin.close();
	}


	public static void main(String[] args) {
		try {
			ResourceManager.createArchive(String.valueOf(System.currentTimeMillis()), "HelloWorld", "c:\\HelloWorld");

			Thread.sleep(5000);

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public static String promptDir(Component comp, String defaultLocation) throws Exception {
		JFileChooser chooser = null;
		if (defaultLocation != null && defaultLocation.length() > 0 && new File(defaultLocation).exists()) {
			chooser = new JFileChooser(new File(defaultLocation));
		} else if (getStateProperty(LAST_DIRECTORY) != null) {
			chooser = new JFileChooser(new File(getStateProperty(LAST_DIRECTORY)));
		} else {
			chooser = new JFileChooser();
		}
		chooser.setApproveButtonText("Open");
		chooser.setDialogTitle("Select Directory");
		chooser.setDialogType(JFileChooser.OPEN_DIALOG);
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setMultiSelectionEnabled(false);

		int returnVal = chooser.showOpenDialog(comp);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			setStateProperty(ResourceManager.LAST_DIRECTORY, chooser.getSelectedFile().getAbsolutePath());
			return chooser.getSelectedFile().getAbsolutePath();
		}
		return null;
	}


	public static String promptFile(Component comp, String defaultLocation, FileFilter filter) throws Exception {
		String[] files = internalPromptFiles(comp, defaultLocation, filter, false, "Select File");
		if (files != null) {
			return files[0];
		}
		return null;
	}


	public static String[] promptMultiFiles(Component comp, String defaultLocation, FileFilter filter) throws Exception {
		String[] files = internalPromptFiles(comp, defaultLocation, filter, true, "Select File(s)");
		return files;
	}


	private static String[] internalPromptFiles(Component invoker, String defaultLocation, FileFilter filter,
		boolean multiSelect, String title) throws Exception {
		String[] fileNames = null;
		JFileChooser chooser = null;
		if (defaultLocation != null && defaultLocation.length() != 0 && new File(defaultLocation).exists()) {
			chooser = new JFileChooser(new File(defaultLocation));
		} else if (getStateProperty(LAST_FILE) != null) {
			chooser = new JFileChooser(new File(getStateProperty(LAST_FILE)));
		} else {
			chooser = new JFileChooser();
		}
		chooser.setApproveButtonText("Open");
		chooser.setApproveButtonToolTipText("Open");
		chooser.setMultiSelectionEnabled(multiSelect);
		chooser.setDialogTitle(title);
		chooser.setFileFilter(filter);
		chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

		int choice = chooser.showOpenDialog(invoker);
		if (choice == JFileChooser.APPROVE_OPTION) {
			File[] files = null;
			if (multiSelect) {
				files = chooser.getSelectedFiles();
			} else {
				files = new File[]{chooser.getSelectedFile()};
			}
			setStateProperty(ResourceManager.LAST_FILE, files[0].getAbsolutePath());
			fileNames = new String[files.length];
			for (int i = 0; i < fileNames.length; i++) {
				fileNames[i] = files[i].getAbsolutePath();
			}
		}
		return fileNames;
	}
}
