/**
 * 
 */
package org.cagrid.installer.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class Utils {

	/**
	 * 
	 */
	public Utils() {
		// TODO Auto-generated constructor stub
	}

	public static void downloadFile(URL fromUrl, File toFile) throws Exception {

		BufferedOutputStream out = new BufferedOutputStream(
				new FileOutputStream(toFile));
		InputStream in = fromUrl.openStream();
		byte[] buffer = new byte[1024];
		int len = -1;
		while ((len = in.read(buffer)) > 0) {
			out.write(buffer, 0, len);
		}
		out.flush();
		out.close();
		in.close();
	}

	public static void main(String[] args) throws Exception {

		URL url = new URL(
				"file:/Users/joshua/downloads/apache-ant-1.6.5-bin.zip");
		File file = new File("/Users/joshua/temp/ant.zip");
		downloadFile(url, file);

	}

	public static void unzipFile(File toFile, File toDir) throws Exception {
		String baseOut = toDir.getAbsolutePath() + "/";
		ZipFile zipFile = new ZipFile(toFile);
		Enumeration entries = zipFile.entries();
		while (entries.hasMoreElements()) {
			ZipEntry entry = (ZipEntry) entries.nextElement();
			if (entry.isDirectory()) {
				File dir = new File(baseOut + entry.getName());
				if (!dir.exists()) {
					dir.mkdirs();
				}
				continue;
			}

			InputStream in = zipFile.getInputStream(entry);
			BufferedOutputStream out = new BufferedOutputStream(
					new FileOutputStream(baseOut + entry.getName()));
			byte[] buffer = new byte[1024];
			int len = -1;
			while ((len = in.read(buffer)) > 0) {
				out.write(buffer, 0, len);
				buffer = new byte[1024];
			}
			out.flush();
			out.close();
			in.close();
		}
		zipFile.close();
	}

	public static void addToClassPath(String s) throws IOException {
		File f = new File(s);
		addToClassPath(f);
	}

	public static void addToClassPath(File f) throws IOException {
		addToClassPath(f.toURL());
	}

	public static void addToClassPath(URL u) throws IOException {

		URLClassLoader sysloader = (URLClassLoader) ClassLoader
				.getSystemClassLoader();
		Class sysclass = URLClassLoader.class;

		try {
			Method method = sysclass.getDeclaredMethod("addURL",
					new Class[] { URL.class });
			method.setAccessible(true);
			method.invoke(sysloader, new Object[] { u });
		} catch (Throwable t) {
			t.printStackTrace();
			throw new IOException(
					"Error, could not add URL to system classloader");
		}

	}
}
