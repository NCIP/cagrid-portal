/**
 * 
 */
package org.cagrid.installer.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
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

		BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(toFile));
		InputStream in = fromUrl.openStream();
		int b = -1;
		while((b = in.read()) != -1){
			out.write(b);
		}
		out.flush();
		out.close();
	}
	
	public static void main(String[] args) throws Exception {
		
		URL url = new URL("file:/Users/joshua/downloads/apache-ant-1.6.5-bin.zip");
		File file = new File("/Users/joshua/temp/ant.zip");
		downloadFile(url, file);
		
	}

	public static void unzipFile(File toFile, File toDir) throws Exception {
		String baseOut = toDir.getAbsolutePath() + "/";
		ZipFile zipFile = new ZipFile(toFile);
		Enumeration entries = zipFile.entries();
		while(entries.hasMoreElements()){
			ZipEntry entry = (ZipEntry) entries.nextElement();
			if(entry.isDirectory()){
				File dir = new File(baseOut + entry.getName());
				if(!dir.exists()){
					dir.mkdirs();
				}
				continue;
			}
			
			InputStream in = zipFile.getInputStream(entry);
			BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(baseOut + entry.getName()));
			byte[] buffer = new byte[1024];
			int len;
			while((len = in.read(buffer)) >= 0){
				out.write(buffer);
			}
			out.flush();
			out.close();
			in.close();
		}
		zipFile.close();
	}

}
