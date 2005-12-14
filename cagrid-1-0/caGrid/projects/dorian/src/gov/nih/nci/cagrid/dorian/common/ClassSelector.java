/*-----------------------------------------------------------------------------
 * Copyright (c) 2003-2004, The Ohio State University,
 * Department of Biomedical Informatics, Multiscale Computing Laboratory
 * All rights reserved.
 * 
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided
 * that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * 3  All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement: This product includes
 *    material developed by the Mobius Project (http://www.projectmobius.org/).
 * 
 * 4. Neither the name of the Ohio State University, Department of Biomedical
 *    Informatics, Multiscale Computing Laboratory nor the names of its
 *    contributors may be used to endorse or promote products derived from
 *    this software without specific prior written permission.
 * 
 * 5. Products derived from this Software may not be called "Mobius"
 *    nor may "Mobius" appear in their names without prior written
 *    permission of Ohio State University, Department of Biomedical
 *    Informatics, Multiscale Computing Laboratory
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *---------------------------------------------------------------------------*/
package gov.nih.nci.cagrid.gums.common;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.StringTokenizer;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;


/**
 * Dialog to allow users to select a factory class. Classes must be in the java
 * classpath.
 * 
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella</A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings</A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster</A>
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin </A>
 * @created Sep 20, 2004
 * @version $Id: ClassSelector.java,v 1.1 2005-12-14 16:12:12 langella Exp $
 */
public class ClassSelector {
	private List ignores;
	private List classes;


	/**
	 * Create a new Class Selector with default files excluded
	 * 
	 * @param name
	 *            The name of this dialog (eg. "Select A Factory")
	 * @param classType
	 *            The class to show subclasses of
	 */
	public ClassSelector(Class classType) {
		this(classType, getDefaultExcludes());
	}


	/**
	 * Create a new Class Selector with user defined excludes
	 * 
	 * @param name
	 *            The name of this dialog (eg. "Select A Factory")
	 * @param classType
	 *            The class type to get subclasses of
	 * @param excludes
	 *            A List of Strings. jar files containing these strings will not
	 *            be searched for classes
	 */
	public ClassSelector(Class classType, List excludes) {
		this.ignores = excludes;
		this.classes = this.getDerivedClasses(classType);
	}


	public List getClasses() {
		return classes;
	}


	private List getDerivedClasses(Class superClass) {
		List derivedClasses = new ArrayList();
		String cp = System.getProperty("java.class.path");
		String sep = ";";
		if (cp.indexOf(sep) == -1) {
			sep = ":";
		}

		StringTokenizer st = new StringTokenizer(cp, sep);
		while (st.hasMoreTokens()) {
			String tok = st.nextToken();

			if (tok.indexOf(".jar") >= 0) {
				// search for classes in the jar file
				try {
					if (!ignoreJar(tok)) {
						JarFile jfile = new JarFile(new File(tok));
						Enumeration e = jfile.entries();
						while (e.hasMoreElements()) {
							ZipEntry entry = (ZipEntry) e.nextElement();
							String entryname = entry.getName();
							if (entryname.endsWith(".class")) {
								String classname = entryname.substring(0, entryname.length() - 6);
								classname = classname.replace('/', '.');
								if (isSubclass(superClass, classname)) {
									derivedClasses.add(classname);
								}
							}
						}
					} else {
						// System.out.println("Ignoring jar " + tok);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				// directory
				derivedClasses.addAll(getSubclassesFromDirectory(new File(tok), superClass, ""));
			}
		}
		return derivedClasses;
	}


	private List getSubclassesFromDirectory(File directory, Class superClass, String path) {
		List subClasses = new ArrayList();
		String[] files = directory.list();
		for (int i = 0; i < files.length; i++) {
			// System.out.println(files[i]);
			// we are only interested in .class files
			if (files[i].endsWith(".class")) {
				// removes the .class extension
				String classname = files[i].substring(0, files[i].length() - 6);
				classname = path + "." + classname;
				// System.out.println(classname);
				if (isSubclass(superClass, classname)) {
					subClasses.add(classname);
				}
			} else {
				File f = new File(directory.getAbsolutePath() + File.separator + files[i]);
				if (f.isDirectory()) {
					String newPath = null;
					if (path.length() == 0) {
						newPath = files[i];
					} else {
						newPath = path + "." + files[i];
					}
					subClasses.addAll(getSubclassesFromDirectory(f, superClass, newPath));
				}
			}
		}
		return subClasses;
	}


	private boolean isSubclass(Class superClass, String candidate) {
		try {
			Class clazz = Class.forName(candidate);
			if (superClass.isAssignableFrom(clazz)) {
				if (superClass != clazz) {
					return true;
				}
			}
		} catch (java.lang.NoClassDefFoundError e) {
		} catch (NullPointerException cnfex) {
			// System.err.println(cnfex);
		} catch (Exception cnfex) {
			// System.err.println(cnfex);
		} catch (Throwable t) {

		}
		return false;
	}


	private boolean ignoreJar(String file) {
		for (int i = 0; i < ignores.size(); i++) {
			String ignore = (String) ignores.get(i);
			if (file.indexOf(ignore) != -1) {
				return true;
			}
		}
		return false;
	}


	/**
	 * Get the default exclude names
	 * 
	 * @return A List of Strings
	 */
	public static List getDefaultExcludes() {
		List excludes = new ArrayList();
		excludes.add("jdom");
		excludes.add("mysql-connector-java");
		excludes.add("junit");
		excludes.add("mobius-xquery-parser");
		excludes.add("mobius-castor");
		excludes.add("xmldb");
		excludes.add("xquark-");
		excludes.add("xml-apis");
		excludes.add("crimson");
		excludes.add("concurrent");
		excludes.add("jdbc2_0-stdext");
		excludes.add("jdepend");
		excludes.add("xmlunit");
		excludes.add("ojdbc14_g");
		excludes.add("icu4j");
		excludes.add("jena");
		excludes.add("commons-logging");
		return excludes;
	}
}
