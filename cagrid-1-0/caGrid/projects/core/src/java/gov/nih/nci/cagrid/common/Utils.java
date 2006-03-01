package gov.nih.nci.cagrid.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.namespace.QName;

import org.apache.axis.AxisFault;
import org.apache.axis.utils.XMLUtils;
import org.globus.wsrf.encoding.ObjectDeserializer;
import org.globus.wsrf.encoding.ObjectSerializer;


public class Utils {

	public static String getExceptionMessage(Exception e) {
		String mess = e.getMessage();
		if (e instanceof AxisFault) {
			mess = ((AxisFault) e).getFaultString();
		}
		return mess;
	}


	public static Object deserializeDocument(String fileName, Class objectType) throws Exception {
		InputStream inputStream = null;

		try {
			inputStream = new FileInputStream(fileName);
			org.w3c.dom.Document doc = XMLUtils.newDocument(inputStream);

			return ObjectDeserializer.toObject(doc.getDocumentElement(), objectType);
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
				}
			}
		}
	}


	public static void copyFile(File in, File out) throws Exception {
		FileInputStream fis = new FileInputStream(in);
		FileOutputStream fos = new FileOutputStream(out);
		byte[] buf = new byte[1024];
		int i = 0;
		while ((i = fis.read(buf)) != -1) {
			fos.write(buf, 0, i);
		}
		fis.close();
		fos.close();
	}


	public static boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					System.err.println("could not remove directory: " + dir.getAbsolutePath());
					return false;
				}
			}
		}
		return dir.delete();
	}


	public static StringBuffer fileToStringBuffer(File file) throws Exception {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		StringBuffer sb = new StringBuffer();
		try {
			String s = null;
			while ((s = br.readLine()) != null) {
				sb.append(s + "\n");
			}
		} catch (Exception e) {
			br.close();
			throw new Exception("Error reading the buffer: " + e.getMessage());
		}

		br.close();

		return sb;
	}


	public static void serializeDocument(String fileName, Object object, QName qname) throws Exception {
		FileWriter fw = null;

		try {
			fw = new FileWriter(fileName);
			ObjectSerializer.serialize(fw, object, qname);
		} finally {
			if (fw != null) {
				try {
					fw.close();
				} catch (IOException e) {
				}
			}
		}

	}


	public static void stringBufferToFile(StringBuffer string, String fileName) throws Exception {
		FileWriter fw = new FileWriter(new File(fileName));
		fw.write(string.toString());
		fw.close();
	}

}
