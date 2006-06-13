/*
 * Created on Jun 8, 2006
 */
package gov.nci.nih.cagrid.tests.core.steps;

import gov.nci.nih.cagrid.tests.core.compare.BeanComparator;
import gov.nci.nih.cagrid.tests.core.util.FileUtils;
import gov.nci.nih.cagrid.tests.core.util.IntroduceServiceInfo;
import gov.nci.nih.cagrid.tests.core.util.ReflectionUtils;
import gov.nih.nci.cagrid.common.Utils;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.globus.gsi.GlobusCredential;

import com.atomicobject.haste.framework.Step;

public class InvokeServiceStep
	extends Step
{
	private File serviceDir;
	private String className;
	private String methodName;

	private Object[] params;
	private File methodDir;
	
	private File resultsFile;
	private String url;
	private GlobusCredential proxy;
	
	private Class cl;
	
	public InvokeServiceStep(
		File serviceDir, File testDir, File methodDir, String methodName, String url
	) 
		throws Exception 
	{
		super();
		
		this.serviceDir = serviceDir;
		this.methodName = methodName;
		this.url = url;
		
		// set className
		File serviceXmlDescriptor = new File(testDir, IntroduceServiceInfo.INTRODUCE_SERVICEXML_FILENAME);
		IntroduceServiceInfo serviceInfo = new IntroduceServiceInfo(serviceXmlDescriptor);
		String serviceName = serviceInfo.getServiceName();
		String packageName = serviceInfo.getPackageName();
		this.className = packageName + ".client." + serviceName + "Client";
		
		// set cl, params, and resultsFile
		//File methodDir = new File(testDir + "test" + File.separator + "resources" + File.separator + methodName);
		//this.cl = loadClass();
		//params = parseParams(methodDir);
		this.params = null;
		this.methodDir = methodDir;
		this.resultsFile = getResultsFile(methodDir);
		
		// TODO set proxy
		this.proxy = null;
	}

	public InvokeServiceStep(
		File serviceDir, String className, String methodName, Object[] params, File resultsFile, 
		String url, GlobusCredential proxy
	) {
		super();
		
		this.serviceDir = serviceDir;
		this.className = className;
		this.methodName = methodName;
		this.params = params;
		this.methodDir = null;
		this.url = url;
		this.proxy = proxy;
		this.resultsFile = resultsFile;
	}
	
	public void runStep() 
		throws Throwable
	{
		if (this.cl == null) cl = loadClass();
		if (params == null) params = parseParams(methodDir);
		
		// find method
		Method[] methods = ReflectionUtils.getMethodsByName(cl, methodName);
		assertTrue(methods.length > 0);
		Method m = methods[0];
		
		// create client
		Object obj = null;
		if (proxy != null) {
			Constructor cstor = cl.getConstructor(new Class[] { String.class, GlobusCredential.class});
			obj = cstor.newInstance(new Object[] { url, proxy });
		} else {
			Constructor cstor = cl.getConstructor(new Class[] { String.class });
			obj = cstor.newInstance(new Object[] { url });
		}
		
		// invoke client
		Object result = m.invoke(obj, params);
		
		// compare to results file
		validateResults(result);
	}
	
	private void validateResults(Object result) 
		throws Exception
	{
		// simple results
		if (resultsFile.getName().endsWith(".txt")) {
			assertEquals(FileUtils.readText(resultsFile), String.valueOf(result));
		}
		// complex results
		else if (resultsFile.getName().endsWith(".xml")) {
			BeanComparator bc = new BeanComparator(this);
			bc.assertEquals(
				Utils.deserializeDocument(resultsFile.toString(), result.getClass()),
				result
			);
		}
		// anything else
		else {
			throw new IllegalArgumentException("results file " + resultsFile + " not a valid file type");
		}
	}

	private Class loadClass() 
		throws MalformedURLException, ClassNotFoundException
	{
		// create class loader and load class
		File[] jars = getServiceJars();
		URL[] urls = new URL[jars.length];
		for (int i = 0; i < jars.length; i++) {
			urls[i] = jars[i].toURL();
		}
		ClassLoader cloader = new URLClassLoader(urls);
		return cloader.loadClass(className);
	}
	
	private Object[] parseParams(File dir) 
		throws Exception
	{
		// get method
		Method[] methods = ReflectionUtils.getMethodsByName(cl, methodName);
		assertTrue(methods.length > 0);
		Method m = methods[0];
		
		// get param types and files
		Class[] paramTypes = m.getParameterTypes();
		File[] files = getParamFiles(dir);
		ArrayList<Object> params = new ArrayList<Object>(files.length);
		
		for (int i = 0; i < paramTypes.length; i++) {
			File file = files[i];
			Class paramType = paramTypes[i];

			Object obj = null;
			// simply type (*.txt)
			if (file.getName().endsWith(".txt")) {
				String s = FileUtils.readText(file);
				Constructor cstor = paramType.getConstructor(new Class[] { String.class });
				cstor.setAccessible(true);
				obj = cstor.newInstance(new Object[] { s });
			}
			// complext type (*.xml)
			else if (file.getName().endsWith(".xml")) {
				obj = Utils.deserializeDocument(file.toString(), paramType);
			}
			// don't understand anything else
			else {
				throw new IllegalArgumentException("param file " + file + " not .txt or .xml");
			}
			params.add(obj);
		}
		
		return params.toArray();
	}

	private File[] getParamFiles(File dir)
	{
		// get files
		File[] files = dir.listFiles(new FileFilter() {
			public boolean accept(File file) {
				return ! file.getName().startsWith("out.") && file.isFile();
			}
		});
		
		// sort files
		ArrayList<File> fileList = new ArrayList<File>(files.length);
		for (File file : files) fileList.add(file);
		Collections.sort(fileList, new Comparator<File>() {
			public int compare(File f1, File f2) {
				return parseParamPos(f1) - parseParamPos(f2);
			}
		});
		
		return fileList.toArray(new File[0]);
	}
	
	public static int parseParamPos(File file)
	{
		String name = file.getName();
		int index = name.indexOf('_');
		return Integer.parseInt(name.substring(0, index));
	}
	
	public static String parseParamName(File file)
	{
		String name = file.getName();
		int index = name.indexOf('_');
		return name.substring(index+1);
	}
	
	private File getResultsFile(File methodDir)
	{
		String[] fileNames = methodDir.list(new FilenameFilter() {
			public boolean accept(File dir, String fileName) {
				return fileName.startsWith("out.") && (fileName.endsWith(".txt") || fileName.endsWith(".xml"));
			}
		});
		if (fileNames.length == 0) throw new IllegalArgumentException("missing out file in " + methodDir);
		return new File(methodDir, fileNames[0]);
	}

	private File[] getServiceJars()
	{
		ArrayList<File> jars = new ArrayList<File>();
		
		addJars(new File(serviceDir, "lib"), jars);
		addJars(new File(serviceDir, "ext"), jars);
		addJars(new File(serviceDir, "build"), jars);
		
		return jars.toArray(new File[0]);
	}
	
	private void addJars(File file, ArrayList<File> jars)
	{
		if (file.getName().endsWith(".jar")) {
			jars.add(file);
		} else if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (File nextFile : files) addJars(nextFile, jars);
		}
	}
}
