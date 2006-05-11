package gov.nih.nci.cagrid.data.ui.browser;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/** 
 *  JarFileFilter
 *  Filters for jar files
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created May 11, 2006 
 * @version $Id$ 
 */
public class JarFileFilter extends FileFilter implements java.io.FileFilter {

	public boolean accept(File f) {
		return f.isDirectory() || f.getName().endsWith(".jar");
	}


	public String getDescription() {
		return "JAR Files (*.jar)";
	}
}
