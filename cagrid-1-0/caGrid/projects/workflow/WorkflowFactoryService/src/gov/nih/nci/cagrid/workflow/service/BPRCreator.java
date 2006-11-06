package gov.nih.nci.cagrid.workflow.service;


import gov.nih.nci.cagrid.workflow.stubs.types.WSDLReferences;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;


public class BPRCreator {

	public static String makeBpr(String bpelFileName, String workflowName, WSDLReferences[] wsdlRefArray) throws Exception {
		String bprName = 
			System.getProperty("java.io.tmpdir") + File.separator + workflowName + ".bpr";
		String wsdlCatalogFileName = 
			System.getProperty("java.io.tmpdir") + File.separator + "wsdlCatalog.xml";
		String wsdlFileName = 
			System.getProperty("java.io.tmpdir") + File.separator + "SampleService1_flattened.wsdl";
		File bprFile = new File(bprName);
		File wsdlCatalog = new File(wsdlCatalogFileName);
		File wsdlFile = new File(wsdlFileName);
		bprFile.deleteOnExit();
		String bprFileName = bprFile.getAbsolutePath();
		String serviceName = workflowName + "Service";
		String pddFileName = createPDD(bpelFileName, workflowName, serviceName, wsdlRefArray);
		String[] fileList = {bpelFileName, pddFileName};
		createBPR(fileList, bprFileName);
		return bprFileName;
	}
	

	private static void createWSDLCatalog(String[] wsdlReferences) throws Exception {
		//Get all the WSDLs from the wsdl references
		for (int i=0;i<wsdlReferences.length;i++) {
			URL wsdlUrl = new URL(wsdlReferences[i]);
			BufferedReader in = new BufferedReader(
						new InputStreamReader(
						wsdlUrl.openStream()));

			String inputLine;

			while ((inputLine = in.readLine()) != null)
			    System.out.println(inputLine);

			in.close();
		    }
	}
	private static String createPDD(String bpelFileName, String workflowName, 
				String serviceName, WSDLReferences[] wsdlRefArray) throws Exception {
		return PDDGenerator.createPDD(workflowName, bpelFileName, serviceName, wsdlRefArray);
	}
	private static void createBPR(String[] fileList, String bprPath)
			throws IOException {
		FileOutputStream fos = new FileOutputStream(bprPath);
		Manifest manifest = new Manifest();
		JarOutputStream jos = new JarOutputStream(fos, manifest);
		try {
			for (int i = 0; i < fileList.length; i++) {
				System.out.println("file path:" + fileList[i]);
				jarFile(fileList[i], jos);
			}
		} catch (Exception e) {

		} finally {
			jos.flush();
			jos.close();
			fos.close();
		}
	}

	private static void jarFile(String filePath, JarOutputStream jos)
			throws IOException {
		FileInputStream fis = new FileInputStream(filePath);
		BufferedInputStream bis = new BufferedInputStream(fis);
		String fileNameEntry = filePath.substring(filePath.lastIndexOf(File.separatorChar) + 1, filePath.length()).trim();
		JarEntry fileEntry = new JarEntry(fileNameEntry);
		jos.putNextEntry(fileEntry);
		byte[] data = new byte[1024];
		int byteCount;
		while ((byteCount = bis.read(data, 0, 1024)) > -1) {
			jos.write(data, 0, byteCount);
		}

	}
}
