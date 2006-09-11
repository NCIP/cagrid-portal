/* 
 * Copyright 2005 Paul Hinds
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.tp23.antinstaller;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;

import org.apache.tools.ant.BuildListener;
import org.apache.tools.ant.taskdefs.Execute;
import org.tp23.antinstaller.page.Page;
import org.tp23.antinstaller.renderer.AntOutputRenderer;
import org.tp23.antinstaller.renderer.MessageRenderer;
import org.tp23.antinstaller.runtime.Logger;
import org.tp23.antinstaller.runtime.Runner;
import org.tp23.antinstaller.runtime.exe.AntLauncherFilter;
/**
 *
 * <p>A single InstallerContext is created by the ExecInstall class and
 * exist for the duration of the Install screens and the runing of
 * the Ant Script. </p>
 * @author Paul Hinds
 * @version $Id: InstallerContext.java,v 1.2 2006-09-11 02:17:21 kumarvi Exp $
 */
public class InstallerContext {

	/**
	 * This is the prefix for environment variables, unlike Ant this is fixed to
	 * the common prefix of "env".  If you dont like this complain to the bug reports
	 * on sourceforge
	 */
	public static final String ENV_PREFIX = "env.";
	/**
	 * This is the prefix for Java system property variables.
	 * This is fixed to "java."
	 */
	public static final String JAVA_PREFIX = "java.";
	
	/**
	 * Added by kumarvi
	 */
	/**
	 * This is the prefix for Custom property variables.
	 * This is fixed to "custom."
	 */
	public static final String CUSTOM_PREFIX = "custom.";
	
	public static final String GRID_ENV_PREFIX ="gridenv.";
	
	public static final String DOES_NOT_EXIST = "doesnotexist";
	
	public static final String GRID_ENV_PROPERTIES_FILE_NAME=".gridenv.properties";
	
	
	
	
	/**
	 * End of addition by kumarvi
	 */
	

	private Logger logger = null;
	private Installer installer = null;
	private MessageRenderer messageRenderer = null;
    private AntOutputRenderer antOutputRenderer = null;
    private Runner runner = null;
    private Page currentPage = null;
    private java.io.File fileRoot = null;
    private BuildListener buildListener = null;
    private AntLauncherFilter antRunner = null;
    private String uIOverride = null;
    
    // called after the Ant part has been run
    private boolean installedSucceded = false;
     
	public InstallerContext() {
	}

	public void setInstallSucceded(boolean installedSucceded){
		this.installedSucceded=installedSucceded;
	}
	public boolean isInstallSucceded(){
		return installedSucceded;
	}
	
	public void log(String message){
		if(logger!=null)logger.log(message);
	}
	public void log(Throwable message){
		if(logger!=null)logger.log(message);
	}

	/**
	 * Check to see if the system is windoze to be able to return the correct prompted
	 * directories.  This method should be IsNotWindows since it assumes anything
	 * that is not windows is Unix
	 * @return boolean true if not windows in the os.name System Property
	 */
	public static boolean isUnix(){
		return System.getProperty("os.name").toLowerCase().indexOf("windows") == -1;
	}

	/**
	 * Use the standard Ant way to load the environment variables, this is not all inclusive
	 * (but will be come Java 1.5 I imagine)
	 * @throws IOException
	 * @return Properties
	 */
	public static Properties getEnvironment(){
		Properties props = new Properties();
		try {
			Vector osEnv = Execute.getProcEnvironment();
			for (Enumeration e = osEnv.elements(); e.hasMoreElements(); ) {
				String entry = (String) e.nextElement();
				int pos = entry.indexOf('=');
				if (pos != -1) {
					props.put(ENV_PREFIX + entry.substring(0, pos),
							  entry.substring(pos + 1));
				}
			}
		}
		catch (Exception ex) {
			// swallow exceptions so this can be loaded statically
			// bit of a bugger if you need the environment on Mac OS 9 but not all apps
			// do so we don't want to die inother situations
			System.out.println("Can't load environment:"+ex.getClass()+","+ex.getMessage());
		}
		Properties javaSysProps = System.getProperties();
		Iterator iter = javaSysProps.keySet().iterator();
		while (iter.hasNext()) {
			Object key = (Object)iter.next();
			props.put(JAVA_PREFIX+key.toString(),javaSysProps.get(key));
		}
		return props;
	}
	
	/**
	 * Added by kumarvi for custom properties
	 * @return
	 */
	public  static Properties getCustomPropertiesXX(){
		return new Properties();
	}
	
	
	public  static Properties getCustomProperties(){
		System.out.println("Inside the method now !!");
		Properties prosTobeReturned = new Properties();
		Properties props = new Properties();
		
		File file = getLatestInstallDir();
		File resourcesFolder = new File(file,"resources");
		
		
		System.out.println("File Path"+resourcesFolder.getAbsolutePath());
		if(!resourcesFolder.exists()){
			return prosTobeReturned;
		}
		FileFilter ff = new PropertyFileFilter();
		File[] files = resourcesFolder.listFiles(ff);
		System.out.println("Size of property files:"+files.length);
		if((files.length<1)){
			//System.out.println("Returning without loading the property !");
			return prosTobeReturned;
		}
		File propertyFile = files[0];
		System.out.println("property file:"+propertyFile.getAbsolutePath());
		
		String propertyFileName = propertyFile.getName();
		
		/**
		 * Now check if this file exist in the user.home directory
		 */
		
		String userHome = System.getProperty("user.home");
		
		File fileInUserHome = new File(userHome,propertyFileName);
		boolean fileInUserHomeExist = fileInUserHome.exists();
		
		//System.out.println("Property file exist ?"+fileInUserHomeExist);
		/**
		 * Now let us see which file should we use to load the properties
		 */
		if(fileInUserHomeExist){
			try{
			FileInputStream fis_UserHomePropFile = new FileInputStream(fileInUserHome);
			FileInputStream fis_resourcePropFile = new FileInputStream(propertyFile);
			Properties p1 = new Properties();
			Properties p2 = new Properties();
			p1.load(fis_UserHomePropFile);
			//System.out.println("p1 Size:"+p1.keySet().size());
			//System.out.println("Frpm p1:");
			p1.list(System.out);
			p2.load(fis_resourcePropFile);
			//System.out.println("p2 Size:"+p2.keySet().size());
			//System.out.println("Frpm p2:");
			p2.list(System.out);
			
			if(p1.keySet().equals(p2.keySet())){
				//Nothing changed so get the prop from user dir
				//System.out.println("loading from user home");
				fis_UserHomePropFile = new FileInputStream(fileInUserHome);
				props.load(fis_UserHomePropFile);
			}else{
				//System.out.println("loading from resource home");
				fis_resourcePropFile = new FileInputStream(propertyFile);
				props.load(fis_resourcePropFile);
				//System.out.println("Right after lolad Size of key set:"+props.keySet().size());
			}
			
			}catch(Exception ex){
				ex.printStackTrace();
			}
			
		}else{
			try{
				//System.out.println("Should be called when file does not exist in user  home");
				FileInputStream fis_resourcePropFile = new FileInputStream(propertyFile);
				props.load(fis_resourcePropFile);
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
		
		Iterator iter = props.keySet().iterator();
		//System.out.println("Size of key set:"+props.keySet().size());
		while (iter.hasNext()) {
			Object key = (Object)iter.next();
			
			prosTobeReturned.put(CUSTOM_PREFIX+key.toString(),props.get(key));
			//System.out.println("Let us see:"+props.get(key));
		}
		
		
		
		return prosTobeReturned;
		
	}
	
	private static File getLatestInstallDir(){
		String tempDirName = System.getProperty("java.io.tmpdir");
		
		System.out.println("Using temp dir:"+tempDirName);
		File tempDir = new File (tempDirName);
		FileFilter antFilter = new AntInstallerFileFilter();
		File[] antinstalls = tempDir.listFiles(antFilter);
		long base = 1;
		File latestFile = null;
		for(int i=0;i<antinstalls.length;i++){
			File f = antinstalls[i];
			long lastmodified = f.lastModified();
			if(lastmodified>base){
				base = lastmodified;
				latestFile = f;
			}
			
		}
		
		return latestFile;
	}
	public static String getCustomPropertyFileName(){
		String fileName = DOES_NOT_EXIST;
		File file = getLatestInstallDir();
		File resourcesFolder = new File(file,"resources");
		
		System.out.println("File Path"+resourcesFolder.getAbsolutePath());
		FileFilter ff = new PropertyFileFilter();
		File[] files = resourcesFolder.listFiles(ff);
		if(files.length>0){
		   File  f = files[0];
		   fileName = f.getName();
		}
		
		return fileName;
	}
	
	public static Properties getGridEnviornementProperties(){
		Properties props = new Properties();
		/**
		 * First check if the file exist in user home
		 * If the file exist in user home then load the property file
		 * from this file and return.
		 * 
		 * If this file does not exist in user home then load
		 * the properties from resource folder. (gridenv.hiddenproperties)
		 */
		
			String userHome = System.getProperty("user.home");
		
			File fileInUserHome = new File(userHome,GRID_ENV_PROPERTIES_FILE_NAME);
			boolean fileInUserHomeExist = fileInUserHome.exists();
			if(fileInUserHomeExist){
				props= loadGridEnvProperties(fileInUserHome);
			}else{
				props = initGridEnvProperties();
			}
			
		return props;
	}
	
	private static Properties loadGridEnvProperties(File propFile){
		Properties props = new Properties();
		Properties propsToBeReturned = new Properties();
		try{
			FileInputStream fis = new FileInputStream(propFile);
			props.load(fis);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		Iterator iter = props.keySet().iterator();
		//System.out.println("Size of key set:"+props.keySet().size());
		while (iter.hasNext()) {
			Object key = (Object)iter.next();
			
			propsToBeReturned.put(GRID_ENV_PREFIX+key.toString(),props.get(key));
			//System.out.println("Let us see:"+props.get(key));
		}
		return propsToBeReturned;
	}
	
	private static Properties initGridEnvProperties(){
		/**
		 * Init the properties from this class only.
		 */
		Properties props = new Properties();
		
		String catalina_home = System.getenv("CATALINA_HOME");
		String globus_location = System.getenv("GLOBUS_LOCATION");
		
		if(catalina_home==null){
			props.put(GRID_ENV_PREFIX+"tomcat.exist", "false");
		}else{
			props.put(GRID_ENV_PREFIX+"tomcat.exist","true");
			props.put(GRID_ENV_PREFIX+"CATALINA_HOME", catalina_home);
		}
		
		if(globus_location==null){
			props.put(GRID_ENV_PREFIX+"globus.exist", "false");
		}else{
			props.put(GRID_ENV_PREFIX+"globus.exist","true");
			props.put(GRID_ENV_PREFIX+"GLOBUS_LOCATION",globus_location);
		}
		
		props.put(GRID_ENV_PREFIX+"mysql.exist", "");
		props.put(GRID_ENV_PREFIX+"mysql.user.name", "root");
		props.put(GRID_ENV_PREFIX+"mysql.user.password","");
		props.put(GRID_ENV_PREFIX+"mysql.host", "localhost");
		props.put(GRID_ENV_PREFIX+"mysql.home","");
		
		
		
		return props;
	}
	

	// Bean methods
	public Installer getInstaller() {
		return installer;
	}

	public String getMinJavaVersion() {
		return installer.getMinJavaVersion();
	}

	public MessageRenderer getMessageRenderer() {
		return messageRenderer;
	}

	public void setMessageRenderer(MessageRenderer messageRenderer) {
		this.messageRenderer = messageRenderer;
		this.messageRenderer.setInstallerContext(this);
	}
	
    public AntOutputRenderer getAntOutputRenderer() {
		return antOutputRenderer;
    }
    
    public void setAntOutputRenderer(AntOutputRenderer antOutputRenderer) {
		this.antOutputRenderer = antOutputRenderer;
    }
    
    public Page getCurrentPage() {
		return currentPage;
    }
    
    public void setCurrentPage(Page currentPage) {
		this.currentPage = currentPage;
    }

	public File getFileRoot() {
		return fileRoot;
	}

	public void setFileRoot(File fileRoot) {
		this.fileRoot = fileRoot;
	}

	public org.apache.tools.ant.BuildListener getBuildListener() {
		return buildListener;
	}

	public void setBuildListener(org.apache.tools.ant.BuildListener buildListener) {
		this.buildListener = buildListener;
	}

	public AntLauncherFilter getAntRunner() {
		return antRunner;
	}

	public void setAntRunner(AntLauncherFilter antRunner) {
		this.antRunner = antRunner;
	}

	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	public Runner getRunner() {
		return runner;
	}

	public void setRunner(Runner runner) {
		this.runner = runner;
	}

	public void setInstaller(Installer installer) {
		this.installer = installer;
	}

	public String getUIOverride() {
		return uIOverride;
	}

	public void setUIOverride(String override) {
		uIOverride = override;
	}
	
	
}



