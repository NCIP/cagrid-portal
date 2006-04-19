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
				File libDir = new File(root.getAbsolutePath() + File.separator + "lib");
				File[] theirjars = libDir.listFiles(new FileFilter() {
					public boolean accept(File pathname) {
						return pathname.getName().toLowerCase().endsWith(".jar");
					}
				});

				if (theirjars != null) {

					java.util.Properties p = System.getProperties();
					String globusLocation = p.getProperty("GLOBUS_LOCATION");
					String introduceLocation = p.getProperty("INTRODUCE_LOCATION");

					// give them introduce build
					File introduceBuildLibDir = new File(introduceLocation + File.separator + "build" + File.separator
						+ "jars");
					File[] introduceBuildLibDirjars = introduceBuildLibDir.listFiles(new FileFilter() {
						public boolean accept(File pathname) {
							return pathname.getName().toLowerCase().endsWith(".jar");
						}
					});

					// give them introduce lib
					File introduceLibDir = new File(introduceLocation + File.separator + "lib");
					File[] introduceLibDirjars = introduceLibDir.listFiles(new FileFilter() {
						public boolean accept(File pathname) {
							return pathname.getName().toLowerCase().endsWith(".jar");
						}
					});

					// give them introduce ext lib
					File introduceExtLibDir = new File(introduceLocation + File.separator + "ext" + File.separator
						+ "lib");
					File[] introduceExtLibDirjars = introduceExtLibDir.listFiles(new FileFilter() {
						public boolean accept(File pathname) {
							return pathname.getName().toLowerCase().endsWith(".jar");
						}
					});

					// give them globus
					File globDir = new File(globusLocation + File.separator + "lib");
					File[] globDirjars = globDir.listFiles(new FileFilter() {
						public boolean accept(File pathname) {
							return pathname.getName().toLowerCase().endsWith(".jar");
						}
					});

					File[] urls = new File[theirjars.length + globDirjars.length + introduceBuildLibDirjars.length
						+ introduceLibDirjars.length + introduceExtLibDirjars.length];
					System.arraycopy(theirjars, 0, urls, 0, theirjars.length);
					System.arraycopy(introduceBuildLibDirjars, 0, urls, theirjars.length,
						introduceBuildLibDirjars.length);
					System.arraycopy(introduceLibDirjars, 0, urls, theirjars.length + introduceBuildLibDirjars.length,
						introduceLibDirjars.length);
					System.arraycopy(introduceExtLibDirjars, 0, urls, theirjars.length
						+ +introduceBuildLibDirjars.length + introduceLibDirjars.length, introduceExtLibDirjars.length);
					System.arraycopy(globDirjars, 0, urls, theirjars.length + +introduceBuildLibDirjars.length
						+ introduceLibDirjars.length + introduceExtLibDirjars.length, globDirjars.length);
					
					URL[] urlArr = new URL[urls.length];
					for(int i = 0; i < urlArr.length; i++){
						urlArr[i] = urls[i].toURL();
					}

					cl = new URLClassLoader(urlArr);
				} else {
					cl = this.getClass().getClassLoader();
				}
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// Load in the class
			c = cl.loadClass(name);
			loadedClasses.put(name, c);
		}
		return c;
	}
}
