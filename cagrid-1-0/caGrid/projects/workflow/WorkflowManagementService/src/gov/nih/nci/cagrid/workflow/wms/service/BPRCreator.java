package gov.nih.nci.cagrid.workflow.wms.service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;


public class BPRCreator {

	public static String makeBpr(String bpelFileName, String pddFileName, String workflowName) throws Exception {
		String bprName = System.getProperty("java.io.tmpdir")+ workflowName + ".bpr";
		File bprFile = new File(bprName);
		bprFile.deleteOnExit();
		String bprFileName = bprFile.getAbsolutePath();
		pddFileName = createPDD(bpelFileName, workflowName);
		String[] fileList = {bpelFileName, pddFileName};
		createBPR(fileList, bprFileName);
		return bprFileName;
	}
	
	public static String createPDD(String bpelFileName, String workflowName) throws Exception {
		return PDDGenerator.createPDD(workflowName, bpelFileName);
	}
	public static void createBPR(String[] fileList, String bprPath)
			throws IOException {
		FileOutputStream fos = new FileOutputStream(bprPath);
		Manifest manifest = new Manifest();
		JarOutputStream jos = new JarOutputStream(fos, manifest);
		try {
			for (int i = 0; i < fileList.length; i++) {
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
