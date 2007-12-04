package gov.nih.nci.cagrid.sdkquery4.processor;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.introduce.common.FileFilters;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.util.ClassPath;
import org.apache.bcel.util.Repository;
import org.apache.bcel.util.SyntheticRepository;
import org.apache.log4j.Logger;

/** 
 *  InheritanceManager
 *  Discovers inheritance relationships between classes
 * 
 * @author David Ervin
 * 
 * @created Dec 3, 2007 2:13:21 PM
 * @version $Id: InheritanceManager.java,v 1.1 2007-12-04 21:54:52 dervin Exp $ 
 */
public class InheritanceManager {
    private static Logger LOG = Logger.getLogger(InheritanceManager.class);
    
    private Repository repository = null;
    private List<File> pathElements;
    
    // TODO: unless I'm doing something more complicated, I could just store the names
    private Map<String, List<JavaClass>> packageToClass;
    private Map<JavaClass, List<JavaClass>> classToSubclasses;
    
    /**
     * Creates an InheritanceManager which uses libraries found on the
     * current java class path
     * @return
     *      An InheritanceManager instance
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static InheritanceManager getManager(boolean includeJavaLibs) throws IOException, ClassNotFoundException {
        String classPath = System.getProperty("java.class.path");
        StringTokenizer tokenizer = new StringTokenizer(classPath, File.pathSeparator);
        List<File> pathElements = new LinkedList<File>();
        while (tokenizer.hasMoreElements()) {
            pathElements.add(new File(tokenizer.nextToken()));
        }
        return getManager(pathElements, includeJavaLibs);
    }
    
    
    /**
     * Creates an InheritanceManager which can navigate classes in a single library
     * @param pathElement
     *      The library to introspect
     * @param includeJavaLibs
     *      If true, libraries from java.home/lib will be included
     * @return
     *      An InheritanceManager instance
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static InheritanceManager getManager(File pathElement, boolean includeJavaLibs) throws IOException, ClassNotFoundException {
        List<File> pathElements = new ArrayList<File>();
        pathElements.add(pathElement);
        return getManager(pathElements, includeJavaLibs);
    }
    
    
    /**
     * Creates an InheritanceManager which can navigate classes in a list
     * of libraries (directories, jars, and / or zips)
     * @param pathElements
     *      The list of path elements to explore
     * @param includeJavaLibs
     *      If true, libraries from java.home/lib will be included
     * @return
     *      An InheritanceManager instance
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static InheritanceManager getManager(List<File> pathElements, boolean includeJavaLibs) throws IOException, ClassNotFoundException {
        if (includeJavaLibs) {
            pathElements.addAll(getJavaLibs());
        }
        return new InheritanceManager(pathElements);
    }

    
    private InheritanceManager(List<File> pathElements) throws IOException, ClassNotFoundException {
        this.pathElements = pathElements;
        this.packageToClass = new HashMap<String, List<JavaClass>>();
        this.classToSubclasses = new HashMap<JavaClass, List<JavaClass>>();
        StringBuilder path = new StringBuilder();
        Iterator<File> pathIter = pathElements.iterator();
        while (pathIter.hasNext()) {
            File element = pathIter.next();
            path.append(element.getAbsolutePath());
            if (pathIter.hasNext()) {
                path.append(File.pathSeparator);
            }
        }
        ClassPath classpath = new ClassPath(path.toString());
        repository = SyntheticRepository.getInstance(classpath);
        initializeTree();
    }
    
    
    private void initializeTree() throws IOException, ClassNotFoundException {
        LOG.debug("Initializing inheritance tree");
        FileFilter classFilter = new FileFilter() {
            public boolean accept(File path) {
                return path.getName().endsWith(".class");
            }
        };
        // compile a list of all class names
        // use a set so as not to duplicate class names
        Set<String> classNames = new HashSet<String>();
        for (File pathElement : pathElements) {
            LOG.debug("Finding classes in " + pathElement.getAbsolutePath());
            String fileName = pathElement.getName().toLowerCase();
            if (pathElement.isDirectory()) {
                List<File> files = Utils.recursiveListFiles(pathElement, classFilter);
                for (File f : files) {
                    String fullName = f.getAbsolutePath();
                    String subName = fullName.substring(pathElement.getName().length());
                    String className = subName.replace(File.separatorChar, '.');
                    if (className.startsWith(".")) {
                        className = className.substring(1);
                    }
                    classNames.add(className);
                }
            } else if (fileName.endsWith(".jar") || fileName.endsWith(".zip")) {
                ZipFile zip = new ZipFile(pathElement);
                Enumeration entries = zip.entries();
                while (entries.hasMoreElements()) {
                    ZipEntry entry = (ZipEntry) entries.nextElement();
                    String entryName = entry.getName();
                    if (entryName.toLowerCase().endsWith(".class")) {
                        String className = entryName.substring(0, entryName.length() - 6);
                        className = className.replace('/', '.');
                        classNames.add(className);
                    }
                }
                zip.close();
            }
        }
        
        LOG.debug("Mapping classes to packages");
        List<JavaClass> tmpAllClasses = new ArrayList<JavaClass>(classNames.size());
        // walk classes, map classes into packages
        for (String className : classNames) {
            JavaClass clazz = this.repository.loadClass(className);
            String packageName = clazz.getPackageName();
            List<JavaClass> classesInPackage = packageToClass.get(packageName);
            if (classesInPackage == null) {
                classesInPackage = new LinkedList<JavaClass>();
                packageToClass.put(packageName, classesInPackage);
            }
            classesInPackage.add(clazz);
            tmpAllClasses.add(clazz);
        }
        
        LOG.debug("Mapping out subclasses");
        // now figure out subclasses
        for (JavaClass clazz : tmpAllClasses) {
            // find all super classes of the given class
            List<JavaClass> supers = new LinkedList<JavaClass>();
            JavaClass c = clazz;
            while (c != null) {
                c = c.getSuperClass();
                if (c != null) {
                    supers.add(c);
                }
            }
            // the given class is a subclass of every superclass
            for (JavaClass sc : supers) {
                List<JavaClass> subs = classToSubclasses.get(sc);
                if (subs == null) {
                    subs = new LinkedList<JavaClass>();
                    classToSubclasses.put(sc, subs);
                }
                subs.add(clazz);
            }
        }
    }
    
    
    public List<JavaClass> getSubclasses(String className) throws ClassNotFoundException {
        // figure out package and class name
        int nameIndex = className.lastIndexOf('.');
        String packageName = "";
        if (nameIndex != -1) {
            packageName = className.substring(0, nameIndex);
        }
        JavaClass classInstance = null;
        List<JavaClass> classes = packageToClass.get(packageName);
        for (JavaClass c : classes) {
            if (c.getClassName().equals(className)) {
                classInstance = c;
                break;
            }
        }
        if (classInstance == null) {
            throw new ClassNotFoundException(
                "Class " + className + " was not found in this inheritance tree");
        }
        List<JavaClass> subs = classToSubclasses.get(classInstance);
        if (subs != null) {
            List<JavaClass> returnMe = new ArrayList<JavaClass>(subs.size());
            for (JavaClass c : subs) {
                returnMe.add(c);
            }
            return returnMe;
        }
        return null;
    }
    
    
    private static List<File> getJavaLibs() {
        String javaHome = System.getProperty("java.home");
        File javaLib = new File(javaHome, "lib");
        File[] libs = javaLib.listFiles(new FileFilters.JarFileFilter());
        List<File> justTheLibs = new ArrayList<File>();
        for (File f : libs) {
            if (f.isFile()) {
                justTheLibs.add(f);
            }
        }
        return justTheLibs;
    }
}
