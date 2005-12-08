package gov.nih.nci.cagrid.introduce;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.apache.ws.jaxme.js.JavaMethod;
import org.jdom.DataConversionException;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 * SyncGWSDL TODO:DOCUMENT ME
 * 
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @created Jun 14, 2005
 * @version $Id: mobiusEclipseCodeTemplates.xml,v 1.2 2005/04/19 14:58:02 oster
 *          Exp $
 */
public class SyncWSDL {

	public static final String XMLSCHEMA_NAMESPACE = "http://www.w3.org/2001/XMLSchema";

	public static final String WSDL_NAMESPACE = "http://schemas.xmlsoap.org/wsdl/";

	public static final String XMLSCHEMA_NAMESPACE_PREFIX = "xsd";

	public static final String WSDL_NAMESPACE_PREFIX = "wsdl";

	public static final String WSDL_FAULT_NAMESPACE_PREFIX = "wsbfw";

	public static final String FAULT_NAMESPACE_PREFIX = "wsbf";

	Document wsdl;

	Properties deploymentProperties;

	Element schema;

	Element definitions;

	Element portType;

	Element types;

	File baseDir;

	int namespaceCount;

	public SyncWSDL(File baseDir, Properties deploymentProperties) {
		this.baseDir = baseDir;
		this.deploymentProperties = deploymentProperties;
	}

	private Element createInputMessage(Element method) {
		Element inputMessage = new Element("message", Namespace
				.getNamespace(SyncWSDL.WSDL_NAMESPACE));
		inputMessage.setAttribute("name", method.getAttributeValue("name")
				+ "InputMessage");
		Element part = new Element("part", Namespace
				.getNamespace(SyncWSDL.WSDL_NAMESPACE));
		part.setAttribute("name", "parameters");
		part.setAttribute("element", "tns:" + method.getAttributeValue("name"));
		inputMessage.addContent(part);
		return inputMessage;
	}

	private Element createOutputMessage(Element method) {
		Element outputMessage = new Element("message", Namespace
				.getNamespace(SyncWSDL.WSDL_NAMESPACE));
		outputMessage.setAttribute("name", method.getAttributeValue("name")
				+ "OutputMessage");
		Element part = new Element("part", Namespace
				.getNamespace(SyncWSDL.WSDL_NAMESPACE));
		part.setAttribute("name", "parameters");
		part.setAttribute("element", "tns:" + method.getAttributeValue("name")
				+ "Response");
		outputMessage.addContent(part);
		return outputMessage;
	}

	private Element createFaultMessage(Element fault) {
		Element faultMessage = new Element("message", Namespace
				.getNamespace(SyncWSDL.WSDL_NAMESPACE));
		faultMessage.setAttribute("name", fault.getAttributeValue("name")
				+ "FaultMessage");
		Element part = new Element("part", Namespace
				.getNamespace(SyncWSDL.WSDL_NAMESPACE));
		part.setAttribute("name", "parameters");
		part.setAttribute("element", "tns:" + fault.getAttributeValue("name"));
		faultMessage.addContent(part);
		return faultMessage;
	}

	private Element createInputType(Element method) {
		Element inputType = new Element("element", Namespace.getNamespace(SyncWSDL.XMLSCHEMA_NAMESPACE));
		Element cType = new Element("complexType", Namespace.getNamespace(SyncWSDL.XMLSCHEMA_NAMESPACE));
		inputType.setAttribute("name", method.getAttributeValue("name"));

		if (method.getChild("inputs", method.getNamespace()) != null) {

			List params = method.getChild("inputs", method.getNamespace())
					.getChildren();
			for (int i = 0; i < params.size(); i++) {
				Element param = (Element) params.get(i);
				Element sequence = new Element("sequence", Namespace.getNamespace(SyncWSDL.XMLSCHEMA_NAMESPACE));
				Element element = new Element("element", Namespace.getNamespace(SyncWSDL.XMLSCHEMA_NAMESPACE));
				element.setAttribute("name", param.getAttributeValue("name"));
				Namespace thisNamespace = null;
				// get the right namespace prefix for the type.....
				List namespaces = definitions.getAdditionalNamespaces();
				for (int nameIndex = 0; nameIndex < namespaces.size(); nameIndex++) {
					Namespace tempNS = (Namespace) namespaces.get(nameIndex);
					if (tempNS.getURI().equals(
							param.getAttributeValue("namespace"))) {
						thisNamespace = tempNS;
					}
				}

				Namespace ns = null;
				if (thisNamespace == null) {
					// need to add this import and namespace to the
					// list.....
					ns = Namespace.getNamespace("ns"
							+ String.valueOf(this.namespaceCount++), param
							.getAttributeValue("namespace"));
					this.definitions.addNamespaceDeclaration(ns);
					Element importEl = new Element("import", this.definitions
							.getNamespace());
					if (param.getAttribute("location") != null) {
						importEl.setAttribute("location", param
								.getAttributeValue("location"));
					}
					importEl.setAttribute("namespace", param
							.getAttributeValue("namespace"));
					this.definitions.addContent(0, importEl);
				}

				try {
					if ((param.getAttributeValue("maxOccurs") != null)
							&& (param.getAttributeValue("maxOccurs").equals(
									"unbounded") || param.getAttribute(
									"maxOccurs").getIntValue() > 1)) {
						// need to create an element array wrapper for gwsdl to
						// accept an array as input to a client
						element.setAttribute("minOccurs", "1");
						element.setAttribute("maxOccurs", "1");
						Element acType = new Element(
								"complexType",
								Namespace.getNamespace(SyncWSDL.XMLSCHEMA_NAMESPACE));
						Element acContent = new Element(
								"complexContent",
								Namespace.getNamespace(SyncWSDL.XMLSCHEMA_NAMESPACE));
						Element aRestriction = new Element(
								"restriction",
								Namespace.getNamespace(SyncWSDL.XMLSCHEMA_NAMESPACE));
						aRestriction.setAttribute("base", "soapenc:Array");
						Element aAttribute = new Element(
								"attribute",
								Namespace.getNamespace(SyncWSDL.XMLSCHEMA_NAMESPACE));
						aAttribute.setAttribute("ref", "soapenc:arrayType");
						if (thisNamespace == null) {
							aAttribute
									.setAttribute(
											"arrayType",
											ns.getPrefix()
													+ ":"
													+ param
															.getAttributeValue("type")
													+ "[]",
											definitions
													.getNamespace(SyncWSDL.WSDL_NAMESPACE_PREFIX));
						} else {
							aAttribute
									.setAttribute(
											"arrayType",
											thisNamespace.getPrefix()
													+ ":"
													+ param
															.getAttributeValue("type")
													+ "[]",
											definitions
													.getNamespace(SyncWSDL.WSDL_NAMESPACE_PREFIX));
						}
						aRestriction.setContent(aAttribute);
						acContent.setContent(aRestriction);
						acType.setContent(acContent);
						element.setContent(acType);
					} else {
						if (thisNamespace == null) {
							element.setAttribute("type", ns.getPrefix() + ":"
									+ param.getAttributeValue("type"));
						} else {
							element.setAttribute("type", thisNamespace
									.getPrefix()
									+ ":" + param.getAttributeValue("type"));
						}
						if (param.getAttributeValue("minOccurs") != null) {
							element.setAttribute("minOccurs", param
									.getAttributeValue("minOccurs"));
						}
						if (param.getAttributeValue("maxOccurs") != null) {
							element.setAttribute("maxOccurs", param
									.getAttributeValue("maxOccurs"));
						}
					}
				} catch (DataConversionException e) {
					e.printStackTrace();
				}
				sequence.addContent(element);
				cType.addContent(sequence);
			}
		}
		inputType.addContent(cType);
		return inputType;
	}

	private Element createOutputType(Element method) {
		Element outputType = new Element("element", Namespace.getNamespace(SyncWSDL.XMLSCHEMA_NAMESPACE));
		outputType.setAttribute("name", method.getAttributeValue("name")
				+ "Response");
		Element cType = new Element("complexType", Namespace.getNamespace(SyncWSDL.XMLSCHEMA_NAMESPACE));
		Element sequence = new Element("sequence", Namespace.getNamespace(SyncWSDL.XMLSCHEMA_NAMESPACE));
		Element output = method.getChild("output", method.getNamespace());

		// if this methods return has a namespace and type
		if (!output.getAttributeValue("className").equals("void")) {
			Element element = new Element("element", Namespace.getNamespace(SyncWSDL.XMLSCHEMA_NAMESPACE));
			element.setAttribute("name", "value");
			Namespace thisNamespace = null;
			// get the right namespace prefix for the type.....
			List namespaces = definitions.getAdditionalNamespaces();
			for (int nameIndex = 0; nameIndex < namespaces.size(); nameIndex++) {
				Namespace tempNS = (Namespace) namespaces.get(nameIndex);
				if (tempNS.getURI().equals(
						output.getAttributeValue("namespace"))) {
					thisNamespace = tempNS;
				}
			}
			if (thisNamespace == null) {
				// need to add this import and namespace to the list.....
				Namespace ns = Namespace.getNamespace("ns"
						+ String.valueOf(this.namespaceCount++), output
						.getAttributeValue("namespace"));
				this.definitions.addNamespaceDeclaration(ns);
				Element importEl = new Element("import", this.definitions
						.getNamespace());
				if (output.getAttribute("location") != null) {
					importEl.setAttribute("location", output
							.getAttributeValue("location"));
				}
				importEl.setAttribute("namespace", output
						.getAttributeValue("namespace"));
				this.definitions.addContent(0, importEl);
				element.setAttribute("type", ns.getPrefix() + ":"
						+ output.getAttributeValue("type"));
			} else {
				element.setAttribute("type", thisNamespace.getPrefix() + ":"
						+ output.getAttributeValue("type"));
			}
			if (output.getAttributeValue("minOccurs") != null) {
				element.setAttribute("minOccurs", output
						.getAttributeValue("minOccurs"));
			}
			if (output.getAttributeValue("maxOccurs") != null) {
				element.setAttribute("maxOccurs", output
						.getAttributeValue("maxOccurs"));
			}

			sequence.addContent(element);
		}

		cType.addContent(sequence);
		outputType.addContent(cType);
		return outputType;
	}

	private Element createFaultType(Element fault) {
		Element faultType = new Element("element", definitions
				.getNamespace(SyncWSDL.XMLSCHEMA_NAMESPACE_PREFIX));
		Element cType = new Element("complexType", definitions
				.getNamespace(SyncWSDL.XMLSCHEMA_NAMESPACE_PREFIX));
		Element cContent = new Element("complexContext", definitions
				.getNamespace(SyncWSDL.XMLSCHEMA_NAMESPACE_PREFIX));
		Element extension = new Element("extension", definitions
				.getNamespace(SyncWSDL.XMLSCHEMA_NAMESPACE_PREFIX));

		// set the name of the fault
		faultType.setAttribute("name", fault.getAttributeValue("name"));
		// set the base fault type...
		extension.setAttribute("base", SyncWSDL.FAULT_NAMESPACE_PREFIX
				+ ":BaseFaultType");

		cContent.addContent(extension);
		cType.addContent(cContent);
		faultType.addContent(cType);
		return faultType;
	}

	private Element createOperation(Element method) {
		Element operation = new Element("operation", this.definitions
				.getNamespace());
		operation.setAttribute("name", method.getAttributeValue("name"));
		Element input = new Element("input", this.definitions.getNamespace());
		input.setAttribute("message", "tns:" + method.getAttributeValue("name")
				+ "InputMessage");
		Element output = new Element("output", this.definitions.getNamespace());
		output.setAttribute("message", "tns:"
				+ method.getAttributeValue("name") + "OutputMessage");
		operation.addContent(input);
		operation.addContent(output);

		// process the faults for this method...
		Element exceptionsEl = method.getChild("exceptions", method
				.getNamespace());
		if (exceptionsEl != null) {
			List children = exceptionsEl.getChildren();
			for (int i = 0; i < children.size(); i++) {
				Element fault = (Element) children.get(i);
				Element faultEl = new Element("fault", this.definitions
						.getNamespace());
				faultEl.setAttribute("message", "tns:"
						+ fault.getAttributeValue("name") + "FaultMessage");
				faultEl.setAttribute("name", fault.getAttributeValue("name"));
				operation.addContent(faultEl);
			}
		}

		return operation;
	}

	private void addMethods(List additions) {
		for (int i = 0; i < additions.size(); i++) {
			Element method = (Element) additions.get(i);
			this.addMethod(method);
		}
	}

	private void addMethod(Element method) {
		// process the faults for this method...
		Element exceptionsEl = method.getChild("exceptions", method
				.getNamespace());
		if (exceptionsEl != null) {
			List children = exceptionsEl.getChildren();
			for (int i = 0; i < children.size(); i++) {
				Element child = (Element) children.get(i);
				// first add the type to the schema
				this.schema.addContent(createFaultType(child));
				int typesIndex = this.definitions.indexOf(types);
				this.definitions.addContent(typesIndex + 1,
						createFaultMessage(child));
			}
		}

		Element inputType = this.createInputType(method);
		Element outputType = this.createOutputType(method);
		Element inputMessage = this.createInputMessage(method);
		Element outputMessage = this.createOutputMessage(method);
		Element operation = this.createOperation(method);
		int typesIndex = this.definitions.indexOf(types);
		this.schema.addContent(inputType);
		this.schema.addContent(outputType);
		this.definitions.addContent(typesIndex + 1, inputMessage);
		this.definitions.addContent(typesIndex + 2, outputMessage);
		this.portType.addContent(0, operation);
	}

	public void sync(List additions, List removals) {

		SAXBuilder builder = new SAXBuilder(false);
		try {
			wsdl = builder.build(new File(baseDir.getAbsolutePath()
					+ File.separator
					+ "schema"
					+ File.separator
					+ this.deploymentProperties
							.get("introduce.skeleton.service.name")
					+ File.separator
					+ this.deploymentProperties
							.get("introduce.skeleton.service.name") + ".wsdl"));
			this.definitions = wsdl.getRootElement();
			this.types = this.definitions.getChild("types", Namespace
					.getNamespace(SyncWSDL.WSDL_NAMESPACE));
			this.schema = this.types.getChild("schema", Namespace
					.getNamespace(SyncWSDL.XMLSCHEMA_NAMESPACE));
			this.portType = this.definitions.getChild("portType", Namespace
					.getNamespace(SyncWSDL.WSDL_NAMESPACE));

			this.namespaceCount = definitions.getAdditionalNamespaces().size() + 1;

		} catch (JDOMException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		this.removeMethods(removals);
		this.addMethods(additions);

		try {
			FileWriter fw = new FileWriter(new File(baseDir.getAbsolutePath()
					+ File.separator
					+ "schema"
					+ File.separator
					+ this.deploymentProperties
							.get("introduce.skeleton.service.name")
					+ File.separator
					+ this.deploymentProperties
							.get("introduce.skeleton.service.name") + ".wsdl"));
			XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
			out.output(this.wsdl, fw);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void removeMethods(List removals) {
		for (int i = 0; i < removals.size(); i++) {
			JavaMethod method = (JavaMethod) removals.get(i);
			this.removeMethod(method);
		}
	}

	private void removeMethod(JavaMethod method) {
		// remove operation
		List operations = this.portType.getChildren("operation", Namespace
				.getNamespace(SyncWSDL.WSDL_NAMESPACE));
		this.portType.getChildren();
		for (int i = 0; i < operations.size(); i++) {
			Element operation = (Element) operations.get(i);
			if (operation.getAttributeValue("name").equals(method.getName())) {
				this.portType.removeContent(operation);
				break;
			}
		}

		// remove input message
		List messages = this.definitions.getChildren("message", Namespace
				.getNamespace(SyncWSDL.WSDL_NAMESPACE));
		for (int i = 0; i < messages.size(); i++) {
			Element message = (Element) messages.get(i);
			if (message.getAttributeValue("name").equals(
					method.getName() + "InputMessage")) {
				this.definitions.removeContent(message);
			}
		}

		// remove output message
		messages = this.definitions.getChildren("message", Namespace
				.getNamespace(SyncWSDL.WSDL_NAMESPACE));
		for (int i = 0; i < messages.size(); i++) {
			Element message = (Element) messages.get(i);
			if (new String(message.getAttributeValue("name")).equals(method
					.getName()
					+ "OutputMessage")) {
				this.definitions.removeContent(message);
			}
		}

		// remove input type
		List types = this.schema.getChildren("element", Namespace
				.getNamespace(SyncWSDL.XMLSCHEMA_NAMESPACE));
		for (int i = 0; i < types.size(); i++) {
			Element type = (Element) types.get(i);
			if (type.getAttributeValue("name").equals(method.getName())) {
				this.schema.removeContent(type);
			}
		}

		// remove output type
		types = this.schema.getChildren("element", Namespace
				.getNamespace(SyncWSDL.XMLSCHEMA_NAMESPACE));
		for (int i = 0; i < types.size(); i++) {
			Element type = (Element) types.get(i);
			if (new String(type.getAttributeValue("name")).equals(method
					.getName()
					+ "Response")) {
				this.schema.removeContent(type);
			}
		}
	}

}
