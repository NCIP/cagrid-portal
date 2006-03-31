package gov.nih.nci.cagrid.introduce.extension;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;


public class ExtensionClassLoader extends ClassLoader {
	private File root;


	public ExtensionClassLoader(File rootDir) {
		if (rootDir == null)
			throw new IllegalArgumentException("Null root directory");
		root = rootDir;
	}


	protected Class loadClass(String name, boolean resolve) throws ClassNotFoundException {

		// Since all support classes of loaded class use same class loader
		// must check subclass cache of classes for things like Object
		Class c = findLoadedClass(name);
		if (c == null) {
			try {
				c = findSystemClass(name);
			} catch (Exception e) {
				// Ignore these
			}
		}

		// Class loaded yet?
		if (c == null) {
			// Create a new class loader with the directory
			ClassLoader cl = null;
			try {
				cl = new URLClassLoader(new URL[]{new URL(root.getAbsolutePath() + "/")});
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
