/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-core/LICENSE.txt for details.
*============================================================================
**/
/**
 * 
 */
package gov.nih.nci.cagrid.xml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class SimpleChildDocumentHandler implements ChildDocumentHandler {

	private int nsPrefixCount = 0;
	private Map<String,String> nsPrefixMap = new HashMap<String,String>();
	private String nsPrefixBase = "ns";
	private Map<String,String> classToNs;

	public SimpleChildDocumentHandler(Map<String,String> classToNs) {
		this.classToNs = classToNs;
	}
	public SimpleChildDocumentHandler(Map<String,String> classToNs, String nsPrefixBase){
		this.classToNs = classToNs;
		this.nsPrefixBase = nsPrefixBase;
	}
	public int getNsPrefixCount() {
		return nsPrefixCount;
	}
	public void setNsPrefixCount(int nsPrefixCount) {
		this.nsPrefixCount = nsPrefixCount;
	}
	public Map<String, String> getNsPrefixMap() {
		return nsPrefixMap;
	}
	public void setNsPrefixMap(Map<String, String> nsPrefixMap) {
		this.nsPrefixMap = nsPrefixMap;
	}
	public String getNsPrefixBase() {
		return nsPrefixBase;
	}
	public void setNsPrefixBase(String nsPrefixBase) {
		this.nsPrefixBase = nsPrefixBase;
	}
	public void insert(String className, ObjectPath path, Document parent, Document child) {
		Collection<Document> children = new ArrayList<Document>();
		children.add(child);
		insert(className, path, parent, children);
		
	}
	public void insert(String className, ObjectPath path, Document parent,
			Collection<Document> children) {
		
		String elementName = "participation";
		String ns = classToNs.get(className);
		String qName = "{" + ns + "}" + elementName; 
		String nsPrefix = nsPrefixMap.get(qName);
		if(nsPrefix == null){
			nsPrefix = "ns" + nsPrefixCount++;
			nsPrefixMap.put(qName, nsPrefix);
		}
		Element el = parent.createElementNS(ns, nsPrefix + ":" + elementName);
		for(Document child : children){
			Node n = parent.adoptNode(child.getDocumentElement());
			el.appendChild(n);
		}
		parent.getDocumentElement().appendChild(el);
	}

}
