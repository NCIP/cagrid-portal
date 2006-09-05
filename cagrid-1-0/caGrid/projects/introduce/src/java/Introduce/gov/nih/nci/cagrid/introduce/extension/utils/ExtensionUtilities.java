package gov.nih.nci.cagrid.introduce.extension.utils;

import gov.nih.nci.cagrid.common.Utils;

import java.io.File;
import java.io.FileWriter;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.swing.JFileChooser;

import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.projectmobius.common.XMLUtilities;

/** 
 *  ExtensionUtilities
 *  Some generic utilities to make extensions easier
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Jun 7, 2006 
 * @version $Id: ExtensionUtilities.java,v 1.4 2006-09-05 17:44:48 dervin Exp $ 
 */
public class ExtensionUtilities {
	public static final String CLASSPATHENTRY_ELEMENT = "classpathentry";


	/**
	 * Adds libraries to an eclipse .classpath file
	 * 
	 * @param classpathFile
	 * 		The .classpath file
	 * @param additionalLibs
	 * 		The libraries (jars) to add to the .classpath.  The jars will be added
	 * 		with paths <i>relative</i> to the .classpath file's location
	 * @throws Exception
	 */
	public static void syncEclipseClasspath(File classpathFile, File[] additionalLibs) throws Exception {
		Element classpathElement = XMLUtilities.fileNameToDocument(classpathFile.getAbsolutePath()).getRootElement();
		
		// get the list of additional libraries for the query processor
		Set libNames = new HashSet();
		for (int i = 0; i < additionalLibs.length; i++) {
			String relativeLibName = Utils.getRelativePath(classpathFile, additionalLibs[i]);
			relativeLibName = convertToUnixStylePath(relativeLibName);
			libNames.add(relativeLibName);
		}
		
		// find out which libs are NOT yet in the classpath
		Iterator classpathEntryIter = classpathElement.getChildren(
			CLASSPATHENTRY_ELEMENT, classpathElement.getNamespace()).iterator();
		while (classpathEntryIter.hasNext()) {
			Element entry = (Element) classpathEntryIter.next();
			if (entry.getAttributeValue("kind").equals("lib")) {
				libNames.remove(entry.getAttributeValue("path"));
			}
		}
		
		// anything left over now has to be added to the classpath
		Iterator additionalLibIter = libNames.iterator();
		while (additionalLibIter.hasNext()) {
			String libName = (String) additionalLibIter.next();
			Element entryElement = new Element(CLASSPATHENTRY_ELEMENT);
			entryElement.setAttribute("kind", "lib");
			entryElement.setAttribute("path", libName);
			classpathElement.addContent(entryElement);
		}
		
		// write the .classpath file back out to disk
		XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
		FileWriter writer = new FileWriter(classpathFile);
		outputter.output(classpathElement, writer);
		writer.flush();
		writer.close();
	}
	
	
	private static String convertToUnixStylePath(String pathname) {
		return pathname.replace('\\', '/');
	}
	
	
	public static void main(String[] args) {
		JFileChooser chooser = new JFileChooser();
		chooser.showOpenDialog(null);
		File source = chooser.getSelectedFile();
		chooser.showOpenDialog(null);
		File destination = chooser.getSelectedFile();
		System.out.println("Path");
		System.out.println("\tFrom: " + source.getAbsolutePath());
		System.out.println("\tTo:   " + destination.getAbsolutePath());
		try {
			String relative = Utils.getRelativePath(source, destination);
			System.out.println("\n\tRel: " + relative);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
