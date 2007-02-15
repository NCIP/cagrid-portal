package gov.nih.nci.cagrid.introduce.axis;

import gov.nih.nci.cagrid.introduce.beans.ServiceDescription;
import gov.nih.nci.cagrid.introduce.beans.service.ServiceType;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.Properties;
import java.util.StringTokenizer;

import org.apache.axis.utils.XMLUtils;
import org.globus.wsrf.encoding.ObjectDeserializer;

public class FixSoapBindingStub {

	public String soapBindingFile;
	public String customSerializationNamespaces;

	public FixSoapBindingStub(String soapBindingFile,
			String customSerializationNamespaces) {
		this.soapBindingFile = soapBindingFile;
		this.customSerializationNamespaces = customSerializationNamespaces;
	}

	public void execute() {

		StringTokenizer strtok = new StringTokenizer(
				customSerializationNamespaces, " ", false);

		StringBuffer oldContent = null;
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(
					this.soapBindingFile)));
			StringBuffer sb = new StringBuffer();
			try {
				String s = null;
				while ((s = br.readLine()) != null) {
					sb.append(s + "\n");
				}
			} finally {
				br.close();
			}

			oldContent = sb;
		} catch (Exception e) {
			e.printStackTrace();
		}

		while (strtok.hasMoreElements()) {
			String namespace = strtok.nextToken();

			StringBuffer newFileContent = new StringBuffer();

			// find the method
			BufferedReader br = new BufferedReader(new StringReader(oldContent
					.toString()));

			try {
				String line = br.readLine();
				while (line != null) {
					if (line.indexOf(namespace) >= 0) {
						for (int i = 0; i < 6; i++) {
							br.readLine();
						}
					} else {
						newFileContent.append(line + "\n");
					}
					line = br.readLine();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			oldContent = newFileContent;

		}

		try {
			FileWriter fw = new FileWriter(new File(this.soapBindingFile));
			fw.write(oldContent.toString());
			fw.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public static void main(String[] args) {
		System.out.println(args[0]);
		ServiceDescription introService = null;
		try {
			InputStream inputStream = null;

			inputStream = new FileInputStream(args[0] + File.separator
					+ "introduce.xml");
			org.w3c.dom.Document doc = XMLUtils.newDocument(inputStream);
			introService = (ServiceDescription) ObjectDeserializer.toObject(doc
					.getDocumentElement(), ServiceDescription.class);
			inputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		Properties props = new Properties();
		try {
			props.load(new FileInputStream(new File(args[0] + File.separator
					+ "introduce.properties")));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		String excludeArgs = (String) props.get("introduce.ns.excludes");
		StringTokenizer strtok = new StringTokenizer(excludeArgs, " ", false);
		String excludes = "";
		while (strtok.hasMoreElements()) {
			String token = strtok.nextToken();
			if (!token.equals("-x")) {
				excludes += token;
				System.out.println("ADDING EXCLUDE TO FIX STUB: " + token);
				if (strtok.hasMoreElements()) {
					excludes += " ";
				}
			}
		}

		ServiceType[] services = introService.getServices().getService();
		String mainServiceName = services[0].getName();
		for (int i = 0; i < services.length; i++) {
			String stubFileName = args[0] + File.separator + "build"
					+ File.separator + "stubs-" + mainServiceName
					+ File.separator + "src" + File.separator
					+ services[i].getPackageName().replace(".", File.separator)
					+ File.separator + "stubs" + File.separator + "bindings"
					+ File.separator + services[i].getName()
					+ "PortTypeSOAPBindingStub.java";
			FixSoapBindingStub stubFixer = new FixSoapBindingStub(stubFileName,
					excludes);
			stubFixer.execute();
		}

	}
}
