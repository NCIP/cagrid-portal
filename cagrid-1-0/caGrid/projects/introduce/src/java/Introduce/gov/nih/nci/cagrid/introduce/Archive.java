package gov.nih.nci.cagrid.introduce;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


public class Archive {

	public static synchronized void getDirectoryListing(List names, File dir, String parentPath) {

		System.out.println("Looking at dir: " + dir.getAbsolutePath());
		System.out.println("Parent path is : " + parentPath);
		// // This filter only returns directories
		// FilenameFilter fileFilter = new FilenameFilter() {
		// public boolean accept(File file, String string) {
		// if(file.getName().endsWith(".zip")){
		// return false;
		// }
		// return true;
		// }
		// };
		String[] children = dir.list();

		if (children == null) {
			// Either dir does not exist or is empty
		} else {
			for (int i = 0; i < children.length; i++) {
				File child = new File(dir.getAbsolutePath() + File.separator + children[i]);
				System.out.println("check file: " + child.getAbsolutePath());
				if (child.isDirectory()) {
					if (parentPath.equals("")) {
						getDirectoryListing(names, child, child.getName());
					} else {
						getDirectoryListing(names, child, parentPath + File.separator + child.getName());
					}

				} else {
					if (parentPath.equals("")) {
						System.out.println("Adding file: " + child.getName());
						names.add(child.getName());
					} else {
						System.out.println("Adding file: " + parentPath + File.separator + child.getName());
						names.add(parentPath + File.separator + child.getName());
					}

				}
			}
		}
	}


	public static synchronized void create(String baseDir) {
		File dir = new File(baseDir);

		List filenames = new ArrayList();
		getDirectoryListing(filenames, dir, "");

		// Create a buffer for reading the files
		byte[] buf = new byte[1024];

		try {
			// Create the ZIP file
			String outFilename = "c:\\outfile.zip";
			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(outFilename));

			// Compress the files
			for (int i = 0; i < filenames.size(); i++) {
				FileInputStream in = new FileInputStream(dir.getAbsolutePath() + File.separator
					+ (String) filenames.get(i));

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
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}


	public static synchronized void restoreFromLast(String baseDir) {

	}


	public static void main(String[] args) {
		Archive.create("c:\\HelloWorld");
	}

}
