package gov.nih.nci.cagrid.workflow.management.service;

import org.activebpel.rt.base64.Base64;
import org.xmlsoap.schemas.ws._2003._03.business_process.TProcess;
import java.io.*;
import java.util.jar.*;

import javax.xml.namespace.QName;

import gov.nih.nci.cagrid.common.Utils;

public class BPRCreator {

	private static int iBaseFolderLength = 0;

	public static void makeBPR(TProcess bpel, String bprPath) throws Exception {
		JarFile bpr = null;
		QName qname = new QName("http://AnnualDemo");
		Utils.serializeDocument("c:\\test\\AnnualDemoParallel.bpel", bpel, qname);
		String[] fileList = {"c:\\test\\AnnualDemoParallel.bpel", "c:\\test\\AnnualDemoPDD.pdd"};
		createBPR(fileList, bprPath);
	}

	public static void createBPR(String[] fileList, String bprPath)
			throws IOException {
		FileOutputStream fos = new FileOutputStream(bprPath);
		Manifest manifest = new Manifest();
		JarOutputStream jos = new JarOutputStream(fos, manifest);
		try {
			for (int i = 0; i < fileList.length; i++) {
				System.out.println(fileList[i]);
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
		String fileNameEntry = filePath.substring(iBaseFolderLength).replace(
				File.separatorChar, '/');
		JarEntry fileEntry = new JarEntry(fileNameEntry);
		jos.putNextEntry(fileEntry);
		System.out.println(fileEntry);
		byte[] data = new byte[1024];
		int byteCount;
		while ((byteCount = bis.read(data, 0, 1024)) > -1) {
			jos.write(data, 0, byteCount);
		}

	}

	public static String getBase64EncodedBpr(String pathToBpr) {
		return Base64.encodeFromFile(pathToBpr);
	}

	public static void main(String args[]) {
		try {
			String[] fileList = { "C:\\test\\AnnualDemoPDD.pdd",
					"C:\\test\\AnnualDemoParallel.bpel" };
			BPRCreator.createBPR(fileList, "c:\\test\\test.bpr");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
