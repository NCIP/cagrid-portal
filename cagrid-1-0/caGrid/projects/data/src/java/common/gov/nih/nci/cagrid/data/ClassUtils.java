package gov.nih.nci.cagrid.data;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/** 
 *  ClassUtils
 *  Utilities to load and examine java classes
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Jun 19, 2006 
 * @version $Id$ 
 */
public class ClassUtils {
	
	/**
	 * Loads a class by looking through an array of jar files.
	 * Basically a convenience wrapper on URLClassLoader
	 * @param className
	 * 		The name of the class to load
	 * @param jarFiles
	 * 		An array of file names for jar files
	 * @return
	 * 		The class requested
	 * @throws MalformedURLException
	 * @throws ClassNotFoundException
	 */
	public static Class loadClass(String className, String[] jarFiles) 
		throws MalformedURLException, ClassNotFoundException {
		URL[] urls = new URL[jarFiles.length];
		for (int i = 0; i < jarFiles.length; i++) {
			urls[i] = new File(jarFiles[i]).toURL();
		}
		URLClassLoader loader = new URLClassLoader(
			urls, Thread.currentThread().getContextClassLoader());
		Class clazz = loader.loadClass(className);
		return clazz;
	}
}
