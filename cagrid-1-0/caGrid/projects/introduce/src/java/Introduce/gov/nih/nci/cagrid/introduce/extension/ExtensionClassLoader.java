package gov.nih.nci.cagrid.introduce.extension;

import java.io.File;
import java.io.FileFilter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;


public class ExtensionClassLoader {
	private File root;


	public ExtensionClassLoader(File rootDir) {
		if (rootDir == null)
			throw new IllegalArgumentException("Null root directory");
		root = rootDir;
	}


	protected Class loadClass(String name) throws ClassNotFoundException {
		Class c = null;
		// Class loaded yet?
		if (c == null) {
			// Create a new class loader with the directory
			ClassLoader cl = null;
			try {
				File libDir = new File(root.getAbsolutePath() + "/lib/");
				File[] theirjars = libDir.listFiles(new FileFilter() {
					public boolean accept(File pathname) {
						if (pathname.getName().endsWith(".jar")) {
							return true;
						}
						return false;
					}

				});

				URL[] urls = new URL[theirjars.length];
				for (int i = 0; i < theirjars.length; i++) {
					urls[i] = new URL("file:/" + theirjars[i].getAbsolutePath());
				}

				cl = new URLClassLoader(urls,this.getClass().getClassLoader());
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// Load in the class
			Class cls = cl.loadClass(name);
			return cls;

		} else {
			return c;
		}

	}
}
