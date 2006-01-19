package gov.nih.nci.cagrid.introduce;

import java.awt.Component;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.swing.JFileChooser;


public class ResourceManager {
	public static final int MAX_ARCHIVE = 5;

	public final static String CACHE_POSTFIX = "_backup.zip";

	public final static String RESOURCE_FILE = "introduce.resources";

	public final static String LAST_DIRECTORY = "introduce.lastdir";


	public static String getResourcePath() {
		String userHome = System.getProperty("user.home");
		File userHomeF = new File(userHome);
		File caGridCache = new File(userHomeF.getAbsolutePath() + File.separator + ".cagrid");
		caGridCache.mkdir();
		File introduceCache = new File(caGridCache + File.separator + "introduce");
		introduceCache.mkdir();
		return introduceCache.getAbsolutePath();
	}


	public static String getProperty(String key) throws Exception {
		File lastDir = new File(getResourcePath() + File.separator + RESOURCE_FILE);
		Properties properties = new Properties();
		if (!lastDir.exists()) {
			lastDir.createNewFile();
		}
		properties.load(new FileInputStream(lastDir));
		return properties.getProperty(key);
	}


	public static void setProperty(String key, String value) throws Exception {
		if (key != null) {
			File lastDir = new File(getResourcePath() + File.separator + RESOURCE_FILE);
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

		if (children == null) {
			// Either dir does not exist or is empty
		} else {
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
		cleanup(id, serviceName);

	}


	private static void cleanup(String currentId, String serviceName) {
		String introduceCache = getResourcePath();

		final String finalServiceName = serviceName;
		FilenameFilter f = new FilenameFilter() {

			public boolean accept(File dir, String name) {
				if (name.indexOf(finalServiceName) >= 0) {
					return true;
				}
				return false;
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

		String userHome = System.getProperty("user.home");
		File userHomeF = new File(userHome);
		File caGridCache = new File(userHomeF.getAbsolutePath() + File.separator + ".cagrid");
		caGridCache.mkdir();
		File introduceCache = new File(caGridCache + File.separator + "introduce");
		introduceCache.mkdir();

		final String finalServiceName = serviceName;
		FilenameFilter f = new FilenameFilter() {

			public boolean accept(File dir, String name) {
				if (name.indexOf(finalServiceName) >= 0) {
					return true;
				}
				return false;
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
			unzip(baseDir, zin, e.getName());
		}
		zin.close();

	}


	public static void main(String[] args) {
		try {
			ResourceManager.createArchive(String.valueOf(System.currentTimeMillis()), "HelloWorld", "c:\\HelloWorld");

			Thread.sleep(5000);

			// Archive.restoreLatest("HelloWorld", "c:\\HelloWorld");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public static String promptDir(Component comp) throws Exception {
		JFileChooser chooser = null;
		if (getProperty(LAST_DIRECTORY) != null) {
			chooser = new JFileChooser(new File(getProperty(LAST_DIRECTORY)));
		} else {
			chooser = new JFileChooser();
		}
		chooser.setDialogTitle("Select Attribute File");
		chooser.setDialogType(JFileChooser.OPEN_DIALOG);
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setMultiSelectionEnabled(false);
		int returnVal = chooser.showOpenDialog(comp);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			
			return chooser.getSelectedFile().getAbsolutePath();
		} else {
			return "";
		}
	}

}
