/*
 * Copyright (C) 2003 The University of Manchester 
 *
 * Modifications to the initial code base are copyright of their
 * respective authors, or their employers as appropriate.  Authorship
 * of the modifications may be determined from the ChangeLog placed at
 * the end of this file.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 * USA.
 *
 ****************************************************************
 * Source code information
 * -----------------------
 * Filename           $RCSfile: XMLSplitterSerialisationHelper.java,v $
 * Revision           $Revision: 1.1 $
 * Release status     $State: Exp $
 * Last modified on   $Date: 2008-08-05 20:47:49 $
 *               by   $Author: tanw $
 * Created on 16-May-2006
 *****************************************************************/
package net.sf.taverna.wsdl.xmlsplitter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import net.sf.taverna.wsdl.parser.ArrayTypeDescriptor;
import net.sf.taverna.wsdl.parser.BaseTypeDescriptor;
import net.sf.taverna.wsdl.parser.ComplexTypeDescriptor;
import net.sf.taverna.wsdl.parser.TypeDescriptor;

import org.apache.log4j.Logger;
import org.jdom.Element;
import org.jdom.Namespace;

/**
 * A helper class that supports the XMLOutputSplitter and XMLInputSplitter,
 * providing the ability for each to be serialised/deserialised to and from the
 * extensions XML for the ScuflModel XML when storing a workflow. This XML
 * describes the TypeDescriptor tree that the Splitter wraps.
 * 
 * @author Stuart Owen
 * 
 */

public class XMLSplitterSerialisationHelper {
	
	public final static Namespace XScuflNS = Namespace.getNamespace("s",
	"http://org.embl.ebi.escience/xscufl/0.1alpha");

	private static Logger logger = Logger
			.getLogger(XMLSplitterSerialisationHelper.class);

	/**
	 * Generates the extensions XML that describes the TypeDescriptor to allow
	 * an XMLInputSplitter or XMLOutputSplitter to be reconstructed using
	 * consumeXML.
	 */
	public static Element typeDescriptorToExtensionXML(TypeDescriptor descriptor) {
		Element result = new Element("extensions", XScuflNS);
		Element type = null;
		if (descriptor instanceof ComplexTypeDescriptor)
			type = constructElementForComplexType(
					(ComplexTypeDescriptor) descriptor, new ArrayList<String>());
		else if (descriptor instanceof ArrayTypeDescriptor)
			type = constructElementForArrayType(
					(ArrayTypeDescriptor) descriptor, new ArrayList<String>());
		result.addContent(type);
		return result;
	}

	/**
	 * Generates the TypeDescriptor structurefrom the extensions XML element
	 * provided. This assumes that the root of the structure is <complextype/>.
	 * This will be the same xml generated by provideXML.
	 */
	public static TypeDescriptor extensionXMLToTypeDescriptor(Element element) {
		Element child = (Element) element.getChildren().get(0);
		return buildTypeDescriptorFromElement(child, new HashMap<String,TypeDescriptor>());
	}

	private static Element constructElementForArrayType(
			ArrayTypeDescriptor descriptor, List<String> existingsTypes) {
		Element result = new Element("arraytype", XScuflNS);
		if (existingsTypes.contains(descriptor.getQname().toString())) {
			result.setAttribute("id", descriptor.getQname().toString());
			populateElement(result, descriptor);
			result.removeAttribute("qname");
		} else {
			existingsTypes.add(descriptor.getQname().toString());
			populateElement(result, descriptor);
			Element elementType = new Element("elementtype", XScuflNS);
			if (descriptor.getElementType() instanceof ComplexTypeDescriptor) {
				elementType.addContent(constructElementForComplexType(
						(ComplexTypeDescriptor) descriptor.getElementType(),
						existingsTypes));
			} else if (descriptor.getElementType() instanceof ArrayTypeDescriptor) {
				elementType.addContent(constructElementForArrayType(
						(ArrayTypeDescriptor) descriptor.getElementType(),
						existingsTypes));
			} else if (descriptor.getElementType() instanceof BaseTypeDescriptor) {
				Element element = new Element("basetype", XScuflNS);
				populateElement(element, descriptor.getElementType());
				elementType.addContent(element);
			}
			result.addContent(elementType);
		}
		return result;
	}

	private static Element constructElementForComplexType(
			ComplexTypeDescriptor descriptor, List<String> existingsTypes) {
		Element result = new Element("complextype", XScuflNS);
		if (existingsTypes.contains(descriptor.getQname().toString())) {
			result.setAttribute("id", descriptor.getQname().toString());
			populateElement(result, descriptor);
			result.removeAttribute("qname");
		} else {
			existingsTypes.add(descriptor.getQname().toString());
			populateElement(result, descriptor);
			Element elements = new Element("elements", XScuflNS);
			for (TypeDescriptor desc : descriptor.getElements()) {
				Element element = null;
				if (desc instanceof ComplexTypeDescriptor) {
					element = constructElementForComplexType(
							(ComplexTypeDescriptor) desc, existingsTypes);
				} else if (desc instanceof ArrayTypeDescriptor) {
					element = constructElementForArrayType(
							(ArrayTypeDescriptor) desc, existingsTypes);
				} else if (desc instanceof BaseTypeDescriptor) {
					element = new Element("basetype", XScuflNS);
					populateElement(element, desc);
				}
				if (element != null)
					elements.addContent(element);
			}
			result.addContent(elements);
		}
		return result;
	}

	private static void populateElement(Element element,
			TypeDescriptor descriptor) {
		element.setAttribute("optional", String
				.valueOf(descriptor.isOptional()));
		element.setAttribute("unbounded", String.valueOf(descriptor
				.isUnbounded()));
		if (descriptor instanceof ArrayTypeDescriptor) {
			element.setAttribute("wrapped",String.valueOf(((ArrayTypeDescriptor)descriptor).isWrapped()));
		}
		element.setAttribute("typename", descriptor.getType());
		element.setAttribute("name", descriptor.getName() == null ? ""
				: descriptor.getName());
		element.setAttribute("qname", descriptor.getQname().toString());
	}

	private static TypeDescriptor buildTypeDescriptorFromElement(
			Element element, HashMap<String,TypeDescriptor> existingsTypes) {
		TypeDescriptor result = null;
		if (element.getAttributeValue("id") != null) {
			TypeDescriptor stored = (TypeDescriptor) existingsTypes.get(element
					.getAttributeValue("id"));
			if (stored == null)
				logger.fatal("Missing reference to parent type with id="
						+ element.getAttributeValue("id"));
			else {
				result = createFromCache(stored, element);
			}
		}

		if (result == null) {
			if (element.getName().equalsIgnoreCase("complextype")) {
				result = new ComplexTypeDescriptor();
				populateDescriptor(element, result);
				existingsTypes.put(result.getQname().toString(), result);
				Element elements = element
						.getChild("elements", XScuflNS);
				for (Iterator<?> iterator = elements.getChildren().iterator(); iterator
						.hasNext();) {
					Element childElement = (Element) iterator.next();
					((ComplexTypeDescriptor) result).getElements().add(
							buildTypeDescriptorFromElement(childElement,
									existingsTypes));
				}

			} else if (element.getName().equalsIgnoreCase("arraytype")) {

				result = new ArrayTypeDescriptor();
				populateDescriptor(element, result);
				existingsTypes.put(result.getQname().toString(), result);
				Element elementType = element.getChild("elementtype",
						XScuflNS);
				((ArrayTypeDescriptor) result)
						.setElementType(buildTypeDescriptorFromElement(
								(Element) elementType.getChildren().get(0),
								existingsTypes));
				if (element.getAttribute("wrapped")!=null) {
					((ArrayTypeDescriptor)result).setWrapped(element.getAttributeValue("wrapped").equalsIgnoreCase("true"));
				}
				else {
					//prior to the addition of the wrapped attribute, in the majority of cases an array
					//would not be wrapped if it was flagged as unbounded.
					((ArrayTypeDescriptor)result).setWrapped(!result.isUnbounded());
				}

			} else if (element.getName().equalsIgnoreCase("basetype")) {
				result = new BaseTypeDescriptor();
				populateDescriptor(element, result);
			}
		}

		return result;
	}

	/**
	 * Performs a shallow copy of the descriptor stored, but updates its name,
	 * isbounded and optional This means that descriptors of the same type do
	 * not need to be repeated throught the stored XML but also takes into
	 * account parameters of the same type may have different name and
	 * attributes to that stored
	 * 
	 * @param descriptor
	 * @param element
	 * @return
	 */
	private static TypeDescriptor createFromCache(TypeDescriptor descriptor,
			Element element) {
		TypeDescriptor result = null;
		if (descriptor instanceof ArrayTypeDescriptor) {
			ArrayTypeDescriptor array = new ArrayTypeDescriptor();
			array.setQname(descriptor.getQname());
			array.setElementType(((ArrayTypeDescriptor) descriptor)
					.getElementType());
			array.setWrapped(((ArrayTypeDescriptor) descriptor).isWrapped());
			result = array;
		} else if (descriptor instanceof ComplexTypeDescriptor) {
			ComplexTypeDescriptor complex = new ComplexTypeDescriptor();
			complex.setQname(descriptor.getQname());
			complex.setElements(((ComplexTypeDescriptor) descriptor)
					.getElements());
			result = complex;
		}
		result.setType(descriptor.getType());

		String name = element.getAttributeValue("name");
		result.setName(name != null ? name : descriptor.getName());

		String optional = element.getAttributeValue("optional");
		if (optional != null) {
			result.setOptional(optional.equalsIgnoreCase("true"));
		} else {
			result.setOptional(descriptor.isOptional());
		}

		String unbounded = element.getAttributeValue("unbounded");
		if (unbounded != null) {
			result.setUnbounded(unbounded.equalsIgnoreCase("true"));
		} else {
			result.setUnbounded(descriptor.isUnbounded());
		}

		return result;
	}

	private static void populateDescriptor(Element element,
			TypeDescriptor result) {
		result.setName(element.getAttributeValue("name"));
		result.setType(element.getAttributeValue("typename"));
		result.setOptional(element.getAttributeValue("optional")
				.equalsIgnoreCase("true"));
		result.setUnbounded(element.getAttributeValue("unbounded")
				.equalsIgnoreCase("true"));

		// qname has been added since 1.3.2-RC1 so need to test if missing for
		// older workflows
		// if missing it is resolved to an empty namespace and typename:
		// {}typename
		String qname = element.getAttributeValue("qname");
		if (qname != null)
			result.setQnameFromString(qname);
	}

}