package gov.nih.nci.cagrid.introduce;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Archive {

	private static synchronized void getDirectoryListing(List names, File dir,
			String parentPath) {

		System.out.println("Looking at dir: " + dir.getAbsolutePath());
		System.out.println("Parent path is : " + parentPath);

		String[] children = dir.list();

		if (children == null) {
			// Either dir does not exist or is empty
		} else {
			for (int i = 0; i < children.length; i++) {
				File child = new File(dir.getAbsolutePath() + File.separator
						+ children[i]);
				System.out.println("check file: " + child.getAbsolutePath());
				if (child.isDirectory()) {
					if (parentPath.equals("")) {
						getDirectoryListing(names, child, child.getName());
					} else {
						getDirectoryListing(names, child, parentPath
								+ File.separator + child.getName());
					}

				} else {
					if (parentPath.equals("")) {
						System.out.println("Adding file: " + child.getName());
						names.add(child.getName());
					} else {
						System.out.println("Adding file: " + parentPath
								+ File.separator + child.getName());
						names
								.add(parentPath + File.separator
										+ child.getName());
					}

				}
			}
		}
	}

	public static synchronized void createArchive(String id, String serviceName,
			String baseDir) throws Exception {
		File dir = new File(baseDir);

		String userHome = System.getProperty("user.home");
		File userHomeF = new File(userHome);
		File caGridCache = new File(userHomeF.getAbsolutePath()
				+ File.separator + ".cagrid");
		caGridCache.mkdir();
		File introduceCache = new File(caGridCache + File.separator
				+ "introduce");
		introduceCache.mkdir();

		List filenames = new ArrayList();
		getDirectoryListing(filenames, dir, "");

		// Create a buffer for reading the files
		byte[] buf = new byte[1024];

		// Create the ZIP file
		String outFilename = introduceCache.getAbsolutePath() + File.separator
				+ serviceName + "_" + id + "_backup.zip";
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(
				outFilename));

		// Compress the files
		for (int i = 0; i < filenames.size(); i++) {
			FileInputStream in = new FileInputStream(dir.getAbsolutePath()
					+ File.separator + (String) filenames.get(i));

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

	}

	private static void unzip(String baseDir, ZipInputStream zin, String s)
			throws IOException {
		System.out.println("unzipping " + s);
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

	public static synchronized void restoreLatest(String serviceName,
			String baseDir) throws Exception {

		String userHome = System.getProperty("user.home");
		File userHomeF = new File(userHome);
		File caGridCache = new File(userHomeF.getAbsolutePath()
				+ File.separator + ".cagrid");
		caGridCache.mkdir();
		File introduceCache = new File(caGridCache + File.separator
				+ "introduce");
		introduceCache.mkdir();

		String[] cacheFiles = introduceCache.list();
		long lastTime = 0;
		for (int i = 0; i < cacheFiles.length; i++) {
			StringTokenizer strtok = new StringTokenizer(cacheFiles[i], "_",
					false);
			strtok.nextToken();
			String timeS = strtok.nextToken();
			long time = Long.parseLong(timeS);
			if (time > lastTime) {
				lastTime = time;
			}
		}

		File cachedFile = new File(introduceCache.getAbsolutePath()
				+ File.separator + serviceName + "_" + String.valueOf(lastTime)
				+ "_backup.zip");

		System.out.println("Restoring from file: "
				+ cachedFile.getAbsoluteFile());

		InputStream in = new BufferedInputStream(
				new FileInputStream(cachedFile));
		ZipInputStream zin = new ZipInputStream(in);
		ZipEntry e;

		while ((e = zin.getNextEntry()) != null) {
			unzip(baseDir, zin, e.getName());
		}
		zin.close();

	}

	public static void main(String[] args) {
		try {
			Archive.createArchive(String.valueOf(System.currentTimeMillis()),"HelloWorld", "c:\\HelloWorld");

			Thread.sleep(5000);

			Archive.restoreLatest("HelloWorld", "c:\\HelloWorld");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
