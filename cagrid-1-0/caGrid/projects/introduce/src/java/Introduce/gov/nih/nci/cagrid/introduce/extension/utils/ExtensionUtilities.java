package gov.nih.nci.cagrid.introduce.extension.utils;

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
 * @version $Id: ExtensionUtilities.java,v 1.1 2006-06-08 20:07:46 oster Exp $ 
 */
public class ExtensionUtilities {

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
			String relativeLibName = getRelativePath(classpathFile, additionalLibs[i]);
			libNames.add(relativeLibName);
		}
		
		// find out which libs are NOT yet in the classpath
		Iterator classpathEntryIter = classpathElement.getChildren("classpathentry").iterator();
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
			Element entryElement = new Element("classpathentry");
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
	
	
	/**
	 * Gets a relative path from the source file to the destination
	 * @param source
	 * 		The source file or location
	 * @param destination
	 * 		The file to target with the relative path
	 * @return
	 * 		The relative path from the source file's directory to the destination file
	 */
	public static String getRelativePath(File source, File destination) throws Exception {
		String sourceDir = null;
		String destDir = null;
		if (source.isDirectory()) {
			sourceDir = source.getCanonicalPath();
		} else {
			sourceDir = source.getParentFile().getCanonicalPath();
		}
		if (destination.isDirectory()) {
			destDir = destination.getCanonicalPath();
		} else {
			destDir = destination.getParentFile().getCanonicalPath();
		}
		
		// find the overlap in the source and dest paths
		String overlap = findOverlap(sourceDir, destDir);
		if (overlap.endsWith(File.separator)) {
			overlap = overlap.substring(0, overlap.length() - File.separator.length() - 1);
		}
		int overlapDirs = countChars(overlap, File.separatorChar);
		if (overlapDirs == 0) {
			// no overlap at all, return full path of destination file
			return destination.getCanonicalPath();
		}
		// difference is the number of path elements to back up before moving down the tree
		int parentDirsNeeded = countChars(sourceDir, File.separatorChar) - overlapDirs;
		// difference is the number of path elements above the file to keep
		int parentDirsKept = countChars(destDir, File.separatorChar) - overlapDirs;
		
		// build the path
		StringBuffer relPath = new StringBuffer();
		for (int i = 0; i < parentDirsNeeded; i++) {
			relPath.append("..").append(File.separatorChar);
		}
		List parentPaths = new LinkedList();
		File parentDir = new File(destDir);
		for (int i = 0; i < parentDirsKept; i++) {
			parentPaths.add(parentDir.getName());
			parentDir = parentDir.getParentFile();
		}
		Collections.reverse(parentPaths);
		for (Iterator i = parentPaths.iterator(); i.hasNext();) {
			relPath.append(i.next()).append(File.separatorChar);
		}
		if (!destination.isDirectory()) {
			relPath.append(destination.getName());
		}
		return relPath.toString();
	}
	
	
	private static String findOverlap(String s1, String s2) {
		// TODO: More efficient would be some kind of binary search, divide and conquer
		StringBuffer overlap = new StringBuffer();
		int count = Math.min(s1.length(), s2.length());
		for (int i = 0; i < count; i++) {
			char c1 = s1.charAt(i);
			char c2 = s2.charAt(i);
			if (c1 == c2) {
				overlap.append(c1);
			} else {
				break;
			}
		}
		return overlap.toString();
	}
	
	
	private static int countChars(String s, char c) {
		int count = 0;
		int index = -1;
		while ((index = s.indexOf(c, index + 1)) != -1) {
			count++;
		}
		return count;
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
			String relative = getRelativePath(source, destination);
			System.out.println("\n\tRel: " + relative);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
