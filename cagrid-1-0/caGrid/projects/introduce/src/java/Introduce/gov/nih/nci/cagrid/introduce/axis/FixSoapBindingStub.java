package gov.nih.nci.cagrid.introduce.axis;

import gov.nih.nci.cagrid.common.XMLUtilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;


public class FixSoapBindingStub {

	public String soapBindingFile;
	public String customSerializationNamespaces;


	public FixSoapBindingStub(String soapBindingFile, String customSerializationNamespaces) {
		this.soapBindingFile = soapBindingFile;
		this.customSerializationNamespaces = customSerializationNamespaces;
	}


	public void execute() {
		if (customSerializationNamespaces == null) {
		    System.out.println("there are no custom serialized namespaces");
			return;
		}

		StringTokenizer strtok = new StringTokenizer(customSerializationNamespaces, " ", false);

		StringBuffer oldContent = null;
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(soapBindingFile)));
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
			System.out.println("scanning for references to objects from the namespace: " + namespace);

			StringBuffer newFileContent = new StringBuffer();

			// find the method
			BufferedReader br = new BufferedReader(new StringReader(oldContent.toString()));

			try {
				String line = br.readLine();
				while (line != null) {
					if (line.indexOf(namespace) >= 0) {
						for (int i = 0; i < 6; i++) {
							br.readLine();
						}
						System.out.println("removed reference");
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
			FileWriter fw = new FileWriter(new File(soapBindingFile));
			fw.write(oldContent.toString());
			fw.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}


	public static void main(String[] args) {
		Document doc = null;
		try {
			doc = XMLUtilities.fileNameToDocument(args[0] + File.separator + "introduce.xml");
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		Properties props = new Properties();
		String excludeArgs = null;
		try {
			props.load(new FileInputStream(new File(args[0] + File.separator + "introduce.properties")));
			excludeArgs = (String) props.get("introduce.soap.binding.excludes");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		List services = doc.getRootElement().getChild("Services",Namespace.getNamespace("gme://gov.nih.nci.cagrid.introduce/1/Services")).getChildren("Service", Namespace.getNamespace("gme://gov.nih.nci.cagrid.introduce/1/Services"));
		String mainServiceName = ((Element)services.get(0)).getAttributeValue("name");
		for (int i = 0; i < services.size(); i++) {
		    System.out.println("looking to fix soap binding for service " + ((Element)services.get(i)).getAttributeValue("name"));
			String stubFileName = args[0] + File.separator + "build" + File.separator + "stubs-" + mainServiceName
				+ File.separator + "src" + File.separator + ((Element)services.get(i)).getAttributeValue("packageName").replace(".", File.separator)
				+ File.separator + "stubs" + File.separator + "bindings" + File.separator + ((Element)services.get(i)).getAttributeValue("name")
				+ "PortTypeSOAPBindingStub.java";
			FixSoapBindingStub stubFixer = new FixSoapBindingStub(stubFileName, excludeArgs);
			stubFixer.execute();
		}

	}
}
