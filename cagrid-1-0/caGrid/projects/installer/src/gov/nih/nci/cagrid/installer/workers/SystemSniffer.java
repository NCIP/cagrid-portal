package gov.nih.nci.cagrid.installer.workers;

import gov.nih.nci.cagrid.installer.utils.Logutil;

import java.util.Properties;
import java.io.*;
import java.util.logging.Logger;
import java.util.logging.Level;

public class SystemSniffer {

	private Logger logger;
	
	public SystemSniffer(){
		logger = Logutil.getLogger();
	}
	
	public String getSystemPropertyValue(String property){
		Properties p = System.getProperties();
		return p.getProperty(property);
		
	}
	public boolean doesExist(String component){
		return true;
	}
	
	public boolean isFirstInstallation(){
		boolean isFirstInstallation = true;
		Properties p = System.getProperties();
		String userHome = p.getProperty("user.home");
		 try{
			 String fileName = userHome + File.separator+ "caGridInstallation.session";
			 System.out.println(fileName);
			 File f = new File(fileName);
			 if(f.exists()){
				 isFirstInstallation = false;
				 System.out.println(fileName);
			 }
		 }catch(Exception ex){
			 logger.log(Level.SEVERE,ex.getMessage());
		 }
		return isFirstInstallation;
	}
	
	public long getInstallationDate(){
		long date = 3;
		Properties p = System.getProperties();
		String userHome = p.getProperty("user.home");
		 try{
			 String fileName = userHome + File.pathSeparator+ "caGridInstallation.session";
			 File f = new File(fileName);
			 date = f.lastModified();
		 }catch(Exception ex){
			 logger.log(Level.SEVERE,ex.getMessage());
		 }
		return date;
	}
	
	private String getLastInstallationStatus(){
		return null;
	}
	
	public String checkSystem(){
		StringBuffer stbr = new StringBuffer();
		
		if(this.isFirstInstallation()){
			
			
			stbr.append("\t");
			stbr.append("No previous caGird installation has been found on this machine.");
			stbr.append("\n");
			stbr.append("\t");
			stbr.append("caGird pack will be downloaded and installed on this machine.");
			
			stbr.append("\n");
			
			String ant_home = System.getenv("ANT_HOME");
			if(ant_home!=null){
				
				stbr.append("\t");
				stbr.append("Ant installation has been found on this machine!");
				
				stbr.append("\n");
			}else{
				
				stbr.append("\t");
				stbr.append("Ant installation has not been found on this machine!");
				stbr.append("\n");
				
				stbr.append("\t");
				stbr.append("Ant will be downloaded and installed!");
			}
			
			stbr.append("\n");
			stbr.append("\n");
			stbr.append("\t");
			
			stbr.append("Please press next to continue!");
			
		}else{
			stbr.append("Already Installed!");
		}
		
		return stbr.toString();
		
	}
	
    
}
