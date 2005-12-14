package gov.nih.nci.cagrid.introduce;

import gov.nih.nci.cagrid.introduce.beans.method.MethodType;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeExceptions;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeExceptionsException;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeInputsInput;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeOutput;

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

	private Element createInputMessage(MethodType method) {
		Element inputMessage = new Element("message", Namespace
				.getNamespace(SyncWSDL.WSDL_NAMESPACE));
		inputMessage.setAttribute("name", method.getName()
				+ "InputMessage");
		Element part = new Element("part", Namespace
				.getNamespace(SyncWSDL.WSDL_NAMESPACE));
		part.setAttribute("name", "parameters");
		part.setAttribute("element", "tns:" + method.getName());
		inputMessage.addContent(part);
		return inputMessage;
	}

	private Element createOutputMessage(MethodType method) {
		Element outputMessage = new Element("message", Namespace
				.getNamespace(SyncWSDL.WSDL_NAMESPACE));
		outputMessage.setAttribute("name", method.getName()
				+ "OutputMessage");
		Element part = new Element("part", Namespace
				.getNamespace(SyncWSDL.WSDL_NAMESPACE));
		part.setAttribute("name", "parameters");
		part.setAttribute("element", "tns:" + method.getName()
				+ "Response");
		outputMessage.addContent(part);
		return outputMessage;
	}

	private Element createFaultMessage(MethodTypeExceptionsException fault) {
		Element faultMessage = new Element("message", Namespace
				.getNamespace(SyncWSDL.WSDL_NAMESPACE));
		faultMessage.setAttribute("name", fault.getName()
				+ "FaultMessage");
		Element part = new Element("part", Namespace
				.getNamespace(SyncWSDL.WSDL_NAMESPACE));
		part.setAttribute("name", "parameters");
		part.setAttribute("element", "tns:" + fault.getName());
		faultMessage.addContent(part);
		return faultMessage;
	}

	private Element createInputType(MethodType method) {
		Element inputType = new Element("element", Namespace.getNamespace(SyncWSDL.XMLSCHEMA_NAMESPACE));
		Element cType = new Element("complexType", Namespace.getNamespace(SyncWSDL.XMLSCHEMA_NAMESPACE));
		inputType.setAttribute("name", method.getName());

		if (method.getInputs() != null) {
			for (int i = 0; i < method.getInputs().getInput().length; i++) {
				MethodTypeInputsInput param =  method.getInputs().getInput(i);
				Element sequence = new Element("sequence", Namespace.getNamespace(SyncWSDL.XMLSCHEMA_NAMESPACE));
				Element element = new Element("element", Namespace.getNamespace(SyncWSDL.XMLSCHEMA_NAMESPACE));
				element.setAttribute("name", param.getName());
				Namespace thisNamespace = null;
				// get the right namespace prefix for the type.....
				List namespaces = definitions.getAdditionalNamespaces();
				for (int nameIndex = 0; nameIndex < namespaces.size(); nameIndex++) {
					Namespace tempNS = (Namespace) namespaces.get(nameIndex);
					if (tempNS.getURI().equals(
							param.getNamespace())) {
						thisNamespace = tempNS;
					}
				}

				Namespace ns = null;
				if (thisNamespace == null) {
					// need to add this import and namespace to the
					// list.....
					ns = Namespace.getNamespace("ns"
							+ String.valueOf(this.namespaceCount++), param
							.getNamespace());
					this.definitions.addNamespaceDeclaration(ns);
					Element importEl = new Element("import", this.definitions
							.getNamespace());
					if (param.getLocation() != null) {
						importEl.setAttribute("location", param
								.getLocation());
					}
					importEl.setAttribute("namespace", param
							.getNamespace());
					this.definitions.addContent(0, importEl);
				}

			
					if ((param.getMaxOccurs() != null)
							&& (param.getMaxOccurs().equals(
									"unbounded") || Integer.parseInt(param.getMaxOccurs()) > 1)) {
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
															.getType()
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
															.getType()
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
									+ param.getType());
						} else {
							element.setAttribute("type", thisNamespace
									.getPrefix()
									+ ":" + param.getType());
						}
						if (param.getMinOccurs() != null) {
							element.setAttribute("minOccurs", param
									.getMinOccurs());
						}
						if (param.getMaxOccurs() != null) {
							element.setAttribute("maxOccurs", param
									.getMaxOccurs());
						}
					}
				
				sequence.addContent(element);
				cType.addContent(sequence);
			}
		}
		inputType.addContent(cType);
		return inputType;
	}

	private Element createOutputType(MethodType method) {
		Element outputType = new Element("element", Namespace.getNamespace(SyncWSDL.XMLSCHEMA_NAMESPACE));
		outputType.setAttribute("name", method.getName()
				+ "Response");
		Element cType = new Element("complexType", Namespace.getNamespace(SyncWSDL.XMLSCHEMA_NAMESPACE));
		Element sequence = new Element("sequence", Namespace.getNamespace(SyncWSDL.XMLSCHEMA_NAMESPACE));
		MethodTypeOutput output = method.getOutput();

		// if this methods return has a namespace and type
		if (!output.getClassName().equals("void")) {
			Element element = new Element("element", Namespace.getNamespace(SyncWSDL.XMLSCHEMA_NAMESPACE));
			element.setAttribute("name", "value");
			Namespace thisNamespace = null;
			// get the right namespace prefix for the type.....
			List namespaces = definitions.getAdditionalNamespaces();
			for (int nameIndex = 0; nameIndex < namespaces.size(); nameIndex++) {
				Namespace tempNS = (Namespace) namespaces.get(nameIndex);
				if (tempNS.getURI().equals(
						output.getNamespace())) {
					thisNamespace = tempNS;
				}
			}
			if (thisNamespace == null) {
				// need to add this import and namespace to the list.....
				Namespace ns = Namespace.getNamespace("ns"
						+ String.valueOf(this.namespaceCount++), output
						.getNamespace());
				this.definitions.addNamespaceDeclaration(ns);
				Element importEl = new Element("import", this.definitions
						.getNamespace());
				if (output.getLocation() != null) {
					importEl.setAttribute("location", output
							.getLocation());
				}
				importEl.setAttribute("namespace", output
						.getNamespace());
				this.definitions.addContent(0, importEl);
				element.setAttribute("type", ns.getPrefix() + ":"
						+ output.getType());
			} else {
				element.setAttribute("type", thisNamespace.getPrefix() + ":"
						+ output.getType());
			}
			if (output.getMinOccurs() != null) {
				element.setAttribute("minOccurs", output
						.getMinOccurs());
			}
			if (output.getMaxOccurs() != null) {
				element.setAttribute("maxOccurs", output
						.getMaxOccurs());
			}

			sequence.addContent(element);
		}

		cType.addContent(sequence);
		outputType.addContent(cType);
		return outputType;
	}

	private Element createFaultType(MethodTypeExceptionsException fault) {
		Element faultType = new Element("element", definitions
				.getNamespace(SyncWSDL.XMLSCHEMA_NAMESPACE_PREFIX));
		Element cType = new Element("complexType", definitions
				.getNamespace(SyncWSDL.XMLSCHEMA_NAMESPACE_PREFIX));
		Element cContent = new Element("complexContext", definitions
				.getNamespace(SyncWSDL.XMLSCHEMA_NAMESPACE_PREFIX));
		Element extension = new Element("extension", definitions
				.getNamespace(SyncWSDL.XMLSCHEMA_NAMESPACE_PREFIX));

		// set the name of the fault
		faultType.setAttribute("name", fault.getName());
		// set the base fault type...
		extension.setAttribute("base", SyncWSDL.FAULT_NAMESPACE_PREFIX
				+ ":BaseFaultType");

		cContent.addContent(extension);
		cType.addContent(cContent);
		faultType.addContent(cType);
		return faultType;
	}

	private Element createOperation(MethodType method) {
		Element operation = new Element("operation", this.definitions
				.getNamespace());
		operation.setAttribute("name", method.getName());
		Element input = new Element("input", this.definitions.getNamespace());
		input.setAttribute("message", "tns:" + method.getName()
				+ "InputMessage");
		Element output = new Element("output", this.definitions.getNamespace());
		output.setAttribute("message", "tns:"
				+ method.getName() + "OutputMessage");
		operation.addContent(input);
		operation.addContent(output);

		// process the faults for this method...
		MethodTypeExceptions exceptionsEl = method.getExceptions();
		if (exceptionsEl != null) {
			for (int i = 0; i < exceptionsEl.getException().length; i++) {
				MethodTypeExceptionsException fault = exceptionsEl.getException(i);
				Element faultEl = new Element("fault", this.definitions
						.getNamespace());
				faultEl.setAttribute("message", "tns:"
						+ fault.getName() + "FaultMessage");
				faultEl.setAttribute("name", fault.getName());
				operation.addContent(faultEl);
			}
		}

		return operation;
	}

	private void addMethods(List additions) {
		for (int i = 0; i < additions.size(); i++) {
			MethodType method = (MethodType) additions.get(i);
			this.addMethod(method);
		}
	}

	private void addMethod(MethodType method) {
		// process the faults for this method...
		MethodTypeExceptions exceptionsEl = method.getExceptions();
		if (exceptionsEl != null) {
			for (int i = 0; i < exceptionsEl.getException().length; i++) {
				MethodTypeExceptionsException child =exceptionsEl.getException(i);
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
