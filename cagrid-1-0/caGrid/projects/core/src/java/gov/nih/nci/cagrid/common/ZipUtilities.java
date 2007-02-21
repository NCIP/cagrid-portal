package gov.nih.nci.cagrid.common;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/** 
 *  ZipUtilities
 *  Utilities to maniuplate zip files
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>  * 
 * @created Feb 21, 2007 
 * @version $Id: ZipUtilities.java,v 1.3 2007-02-21 19:34:25 dervin Exp $ 
 */
public class ZipUtilities {
	
	/**
	 * Unzips a zip compressed file to a specific directory
	 * 
	 * @param zip
	 * 		The zip compressed file
	 * @param location
	 * 		The location to unzip into
	 * @throws IOException
	 */
	public static void unzip(File zip, File location) throws IOException {
		FileInputStream zipFileInput = new FileInputStream(zip.getAbsoluteFile());
		ZipInputStream zipInput = new ZipInputStream(zipFileInput);
		ZipEntry entry = null;
		String baseDir = null;
		if (location == null) {
			baseDir = zip.getParentFile().getAbsolutePath();
		} else {
			baseDir = location.getAbsolutePath();
		}
		while ((entry = zipInput.getNextEntry()) != null) {
			String name = entry.getName();
			File outFile = new File(baseDir + File.separator + name);
			if (entry.isDirectory()) {
				outFile.mkdirs();
			} else {
				if (!outFile.getParentFile().exists()) {
					outFile.getParentFile().mkdirs();
				}
				outFile.createNewFile();
				BufferedOutputStream fileOut = new BufferedOutputStream(
					new FileOutputStream(outFile));
				copyStreams(zipInput, fileOut);
				fileOut.flush();
				fileOut.close();
			}
		}
		zipInput.close();
	}
	

	/**
	 * Unzips a zip compressed file in the directory it resides in
	 * 
	 * @param zip
	 * 		The zip compressed file
	 * @throws IOException
	 */
	public static void unzipInPlace(File zip) throws IOException {
		unzip(zip, null);
	}
	
	
	/**
	 * Applies zip compression to a directory and all its contents
	 * 
	 * @param dir
	 * 		The directory to compress
	 * @param zipFile
	 * 		The file to create the zip archive in
	 * @throws IOException
	 */
	public static void zipDirectory(File dir, File zipFile) throws IOException {
		ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFile));
		List files = Utils.recursiveListFiles(dir, new FileFilter() {
			public boolean accept(File name) {
				return true;
			}
		});
		int baseDirNameLength = dir.getAbsolutePath().length();
		Iterator fileIter = files.iterator();
		while (fileIter.hasNext()) {
			File fileToAdd = (File) fileIter.next();
			String relativeFileName = fileToAdd.getAbsolutePath().substring(baseDirNameLength + 1);
			ZipEntry entry = new ZipEntry(relativeFileName);
			zipOut.putNextEntry(entry);
			if (!fileToAdd.isDirectory()) {
				BufferedInputStream fileInput = new BufferedInputStream(new FileInputStream(fileToAdd));
				copyStreams(fileInput, zipOut);
				fileInput.close();
			}
		}
		zipOut.flush();
		zipOut.close();
	}
	
	
	/**
	 * Extracts the contents of a zip file entry to a byte array
	 * 
	 * @param zipFile
	 * 		The zip file
	 * @param entryName
	 * 		The name of the entry to extract
	 * @return
	 * 		A byte array containing the (uncompressed) contents of the entry
	 * @throws IOException
	 */
	public static byte[] extractEntryContents(File zipFile, String entryName) throws IOException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		ZipFile zip = new ZipFile(zipFile);
		ZipEntry entry = zip.getEntry(entryName);
		InputStream stream = zip.getInputStream(entry);
		copyStreams(stream, output);
		stream.close();
		output.flush();
		output.close();
		return output.toByteArray();
	}
	
	
	/**
	 * Copies the contents of an input stream into an output stream
	 * 
	 * @param input
	 * @param output
	 * @throws IOException
	 */
	private static void copyStreams(InputStream input, OutputStream output) throws IOException {
		byte[] temp = new byte[8192];
		int read = -1;
		while ((read = input.read(temp)) != -1) {
			output.write(temp, 0, read);
		}
	}
}
