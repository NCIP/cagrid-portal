package gov.nih.nci.cagrid.common;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;

/** 
 *  JarUtilities
 *  Utilities to manipulate jar files
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Oct 25, 2006 
 * @version $Id$ 
 */
public class JarUtilities {
	
	public static StringBuffer getFileContents(JarFile jar, String entryName) throws IOException {
		JarEntry entry = jar.getJarEntry(entryName);
		if (entry == null) {
			return null;
		}
		BufferedInputStream entryStream = new BufferedInputStream(jar.getInputStream(entry));
		StringBuffer buffer = Utils.inputStreamToStringBuffer(entryStream);
		return buffer;
	}
	
	
	public static byte[] removeJarEntry(JarFile jar, String entryName) throws IOException {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		JarOutputStream jarOut = new JarOutputStream(bytes);
		// copy all entries except the one with the matching name
		Set<String> copiedEntries = new HashSet<String>();
		Enumeration jarEntries = jar.entries();
		while (jarEntries.hasMoreElements()) {
			JarEntry entry = (JarEntry) jarEntries.nextElement();
			String name = entry.getName();
			if (!name.equals(entryName)) {
				if (!copiedEntries.contains(name)) {
					jarOut.putNextEntry(entry);
					InputStream entryStream = jar.getInputStream(entry);
					byte[] entryBytes = streamToBytes(entryStream);
					entryStream.close();
					jarOut.write(entryBytes);
					jarOut.closeEntry();
					copiedEntries.add(name);
				}
			}
		}
		jarOut.flush();
		jarOut.close();
		return bytes.toByteArray();
	}
    
    
    public static void jarDirectory(File dir, File jarFile) throws IOException {
        JarOutputStream jarOut = new JarOutputStream(new FileOutputStream(jarFile));
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
            JarEntry entry = new JarEntry(relativeFileName);
            jarOut.putNextEntry(entry);
            if (!fileToAdd.isDirectory()) {
                BufferedInputStream fileInput = new BufferedInputStream(new FileInputStream(fileToAdd));
                copyStreams(fileInput, jarOut);
                fileInput.close();
            }
        }
        jarOut.flush();
        jarOut.close();
    }
	
	
	private static byte[] streamToBytes(InputStream stream) throws IOException {
		byte[] bytes = new byte[0];
		byte[] buffer = new byte[8192];
		int len = -1;
		while ((len = stream.read(buffer)) != -1) {
			byte[] temp = new byte[bytes.length + len];
			System.arraycopy(bytes, 0, temp, 0, bytes.length);
			System.arraycopy(buffer, 0, temp, bytes.length, len);
			bytes = temp;
		}
		return bytes;
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
