package gov.nih.nci.cagrid.installer.workers;

import java.io.File;
import java.net.URL;


import org.apache.tools.ant.Project;

import org.apache.tools.ant.Target;
import org.apache.tools.ant.taskdefs.Get;

public class NetworkResourceGetter extends Get {

	public NetworkResourceGetter() {
		
	  
	  
		//project = new Project();
		this.setProject(new Project());
		this.getProject().init();
		//project.init();
		//taskType = "get";
		this.setTaskName("get");
		this.setTaskType("get");
		this.setOwningTarget(new Target());
		//taskName = "get";
		//target = new Target();
		
		
		
	}

	public void getMySql(String destDir) {
		try {
			NetworkResourceGetter nrg = new NetworkResourceGetter();

			String destFolderName = destDir + File.separator + "downloads";
			File destFolder = new File(destFolderName);
			destFolder.mkdir();

			String destFileName = destFolder.getAbsolutePath() + File.separator
					+ "mysql-noinstall-5.0.21-win32.zip";

			// File dest = new
			// File("C:\\test\\mysql-noinstall-5.0.21-win32.zip");
			File dest = new File(destFileName);
			URL url = new URL(
					"http://dev.mysql.com/get/Downloads/MySQL-5.0/mysql-noinstall-5.0.21-win32.zip/from/http://mirror.services.wisc.edu/mysql/");
			nrg.setDest(dest);
			nrg.setSrc(url);
			nrg.setVerbose(true);
			nrg.execute();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void getAnt(String destDir) {
		try {
			NetworkResourceGetter nrg = new NetworkResourceGetter();
			String destFolderName = destDir + File.separator + "downloads";
			File destFolder = new File(destFolderName);
			destFolder.mkdir();

			String destFileName = destFolder.getAbsolutePath() + File.separator
					+ "apache-ant-1.6.5-bin.zip";

			// File dest = new
			// File("C:\\test\\mysql-noinstall-5.0.21-win32.zip");
			File dest = new File(destFileName);
			URL url = new URL(
					"http://apache.secsup.org/dist/ant/binaries/apache-ant-1.6.5-bin.zip");
			nrg.setDest(dest);
			nrg.setSrc(url);
			nrg.setVerbose(true);
			nrg.execute();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void getWs_Core(String destDir) {
		try {
			NetworkResourceGetter nrg = new NetworkResourceGetter();
			String destFolderName = destDir + File.separator + "downloads";
			File destFolder = new File(destFolderName);
			destFolder.mkdir();

			String destFileName = destFolder.getAbsolutePath() + File.separator
					+ "ws-core-4.0.0-bin.zip";

			// File dest = new
			// File("C:\\test\\mysql-noinstall-5.0.21-win32.zip");
			File dest = new File(destFileName);
			URL url = new URL(
					"ftp://ftp.globus.org/pub/gt4/4.0/4.0.0/ws-core/bin//ws-core-4.0.0-bin.zip");
			nrg.setDest(dest);
			nrg.setSrc(url);
			nrg.setVerbose(true);
			nrg.execute();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void getTomcat(String destDir) {
		try {
			NetworkResourceGetter nrg = new NetworkResourceGetter();
			String destFolderName = destDir + File.separator + "downloads";
			File destFolder = new File(destFolderName);
			destFolder.mkdir();

			String destFileName = destFolder.getAbsolutePath() + File.separator
					+ "apache-tomcat-5.5.17.zip";

			// File dest = new
			// File("C:\\test\\mysql-noinstall-5.0.21-win32.zip");
			File dest = new File(destFileName);
			URL url = new URL(
					"http://apache.hoxt.com/tomcat/tomcat-5/v5.5.17/bin/apache-tomcat-5.5.17.zip");
			nrg.setDest(dest);
			nrg.setSrc(url);
			nrg.setVerbose(true);
			nrg.execute();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void downLoadFile(String destDir, String url, String fileName)
			throws Exception {

		File destFile = new File(destDir + File.separator + fileName);

		URL uri = new URL(url);
		this.setDest(destFile);
		this.setSrc(uri);
		this.execute();

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		NetworkResourceGetter nrg = new NetworkResourceGetter();
		try {
			nrg.downLoadFile("C:\\cagrid_temp",
					"http://156.40.129.72:8080/cagrid/cagrid-1-0.zip",
					"cagrid-1-0.zip");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// nrg.getTomcat();
	}

}
