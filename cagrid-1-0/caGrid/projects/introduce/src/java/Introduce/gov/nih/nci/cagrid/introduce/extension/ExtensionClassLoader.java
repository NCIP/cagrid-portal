package gov.nih.nci.cagrid.introduce.extension;

import java.io.File;
import java.io.FileFilter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;


public class ExtensionClassLoader {
	private File root;
	private Map loadedClasses;


	public ExtensionClassLoader(File rootDir) {
		if (rootDir == null)
			throw new IllegalArgumentException("Null root directory");
		root = rootDir;
		loadedClasses = new HashMap();
	}


	public Class loadClass(String name) throws ClassNotFoundException {
		Class c = (Class) loadedClasses.get(name);
		if (c == null) {
			// Create a new class loader with the directory
			ClassLoader cl = null;
			try {
				File libDir = new File(root.getAbsolutePath() + File.separator + "lib" + File.separator);
				File[] theirjars = libDir.listFiles(new FileFilter() {
					public boolean accept(File pathname) {
						return pathname.getName().toLowerCase().endsWith(".jar");
					}
				});
				
				if (theirjars != null) {
					URL[] urls = new URL[theirjars.length];
					for (int i = 0; i < theirjars.length; i++) {
						System.out.println("Using jar: " + theirjars[i].getAbsolutePath());
						urls[i] = theirjars[i].toURL();
					}
					cl = new URLClassLoader(urls, this.getClass().getClassLoader());
				} else {
					cl = this.getClass().getClassLoader();
				}				
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// Load in the class
			System.out.println("Loading in extension class: " + name);
			c = cl.loadClass(name);
			loadedClasses.put(name, c);
		}
		return c;
	}
}
