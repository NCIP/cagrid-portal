package gov.nih.nci.cagrid.workflow.wms.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;

import gov.nih.nci.cagrid.common.Utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.HashSet;
import java.util.StringTokenizer;

import javax.xml.namespace.QName;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.axis.utils.XMLUtils;
import org.globus.wsrf.utils.XmlUtils;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class PDDGenerator
{
	public static final String XMLNS_PDD =
        "http://www.active-endpoints.com/schemas/deploy/pdd.xsd";
    public static final String XMLNS_BPEL =
        "http://schemas.xmlsoap.org/ws/2003/03/business-process/";
    public static final String XMLNS_WSDL =
        "http://schemas.xmlsoap.org/wsdl/";
    public static final String XMLNS_PLINK =
        "http://schemas.xmlsoap.org/ws/2003/05/partner-link/";
    public static final String XMLNS_WSA =
        "http://schemas.xmlsoap.org/ws/2003/03/addressing";
    public static final String WSA_ANONYMOUS =
        "http://schemas.xmlsoap.org/ws/2004/08/addressing/role/anonymous";
    
	public static Document 
		generatePDD(String workflowName, Document bpelDoc) throws Exception {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.newDocument();
        Element root = doc.createElementNS(XMLNS_PDD, "process");
        QName processQ = getBpelProcessName(bpelDoc);
        root.setAttribute("name","bpelns:"+processQ.getLocalPart());
        root.setAttribute("xmlns", XMLNS_PDD);
        root.setAttribute("xmlns:bpelns",processQ.getNamespaceURI());
        root.setAttribute("location",workflowName + ".bpel");
        root.setAttribute("xmlns:wsa", XMLNS_WSA);
        doc.appendChild(root);
        // Add partnerLinks
        Collection partnerLinks = parsePartnerLinks(root,bpelDoc, doc);
        Collection wsdlNamespaces = null;
        appendPartnerLinks(doc, root, partnerLinks);
		return doc;
	}

	public static QName 
	getBpelProcessName(Document bpelDoc) {
		Element root = bpelDoc.getDocumentElement();
		String processName = root.getAttribute("name");
		String processNS = root.getAttribute("targetNamespace");
		return new QName(processNS, processName);
	}

	 public static void appendPartnerLinks(Document doc,  Node root, Collection partnerLinks) {
	        Element plinksNode = doc.createElementNS(XMLNS_PDD, "partnerLinks");
	        root.appendChild(plinksNode);
	        for (Iterator it = partnerLinks.iterator(); it.hasNext(); ) {
	            PartnerLink plink = (PartnerLink)it.next();
	            Element pNode = doc.createElementNS(XMLNS_PDD, "partnerLink");
	            plinksNode.appendChild(pNode);
	            pNode.setAttribute("name", plink.name);
	            if (!"".equals(plink.myRole)) {
	                Element myRoleNode = doc.createElementNS(XMLNS_PDD, "myRole");
	                myRoleNode.setAttribute("service",plink.getName() + "Service");
	                myRoleNode.setAttribute("allowedRoles","");
	                myRoleNode.setAttribute("binding","MSG");
	                pNode.appendChild(myRoleNode);
	            } 
	            if (!"".equals(plink.partnerRole)) {
	                Element pRoleNode = doc.createElementNS(XMLNS_PDD, "partnerRole");
	                pRoleNode.setAttribute("endpointReference","static");
	                Element epRefNode = doc.createElement("wsa:EndpointReference");
	                Element epAddrNode = doc.createElement("wsa:Address");
	               // Element epServNode = doc.createElementNS(XMLNS_WSA,"ServiceName");

	                pNode.appendChild(pRoleNode);
	                pRoleNode.appendChild(epRefNode);
	                epRefNode.appendChild(epAddrNode);
//	                epRefNode.appendChild(epServNode);

//	                epRefNode.setPrefix("wsa");
//	                epRefNode.setAttribute("xmlns:wsa", XMLNS_WSA);
	                epAddrNode.appendChild(doc.createTextNode(plink.linkType.getNamespaceURI()));
//	                epAddrNode.setPrefix("wsa");
//	                epAddrNode.setAttribute("xmlns:wsa", XMLNS_WSA);
//	                epServNode.setAttribute("PortName","");
//	                epServNode.setPrefix("wsa");
//	                epServNode.setAttribute("xmlns:wsa", XMLNS_WSA);
//	                epServNode.setAttribute("xmlns:ns0", 
//	                        plink.linkType.getNamespaceURI());
//	                epServNode.appendChild(doc.createTextNode("ns0:SoapPortHere"));
	            } else {
	                System.err.println("PartnerLink "+plink.name
	                        +" should have either myRole or partnerRole attribute!");
	                continue;
	            }
	        }
	    }
	 private static void appendWsdlReferences(Document doc, Node root, Collection wsdlNamespaces, Collection locations) {
	        Element wsdlRefsNode = doc.createElementNS(XMLNS_PDD,"wsdlReferences");
	        root.appendChild(wsdlRefsNode);
	        for (Iterator it = wsdlNamespaces.iterator(); it.hasNext(); ) {
	            String ns = (String)it.next();
	            Element wsdlRefNode = doc.createElementNS(XMLNS_PDD, "wsdl");
	            wsdlRefNode.setAttribute("namespace",ns);
	            wsdlRefNode.setAttribute("location","http://localhost:8080/wsrf/share/schema/SampleService1/SampleService1_flattened.wsdl");
	            wsdlRefsNode.appendChild(wsdlRefNode);
	        }
	    }

	 private static ArrayList parsePartnerLinks(Node root,Document bpelDoc, Document pdd) {
	        NodeList partnerLinksNL = bpelDoc.getElementsByTagNameNS(
	                XMLNS_BPEL, "partnerLink");
	        ArrayList partnerLinks = new ArrayList();
	        Collection wsdlNamespaces = new HashSet();

	        int len = partnerLinksNL.getLength();
	        for (int i = 0; i < len; i++) {
	            PartnerLink pl = new PartnerLink();
	            Element el = (Element)partnerLinksNL.item(i);
	            pl.myRole = el.getAttribute("myRole");
	            pl.partnerRole = el.getAttribute("partnerRole");
	            pl.name = el.getAttribute("name");
	            String plt = el.getAttribute("partnerLinkType");
	            StringTokenizer st = new StringTokenizer(plt, ":", false);
	            String prefix = st.nextToken();
	            String local = st.nextToken();
	            String nsuri = resolveNS(el, prefix);
	            if (null == nsuri) {
	                System.err.println(
	                		"Unable to resolve the partnerLinkType namespace for "+prefix);
	                continue;
	            }
	            pl.linkType = new QName(nsuri, local, prefix);
	            wsdlNamespaces.add(nsuri);
	            partnerLinks.add(pl);
	        }
	        appendWsdlReferences(pdd, root, wsdlNamespaces, null);
	        return partnerLinks;
	    }
	
	 private  static String resolveNS(final Node start, final String prefix) {
	        if (null == prefix) {
	            throw new IllegalArgumentException("Starting Node is required");
	        }
	        if (null == start) {
	            return null;
	        }
	        Element el;
	        try {
	            el = (Element)start;
	        } catch (ClassCastException e) {
	            return null;
	        }
	        String nsuri;
	        if ("".equals(prefix)) {
	            nsuri = el.getAttribute("xmlns");
	        } else {
	            nsuri = el.getAttribute("xmlns:"+prefix);
	        }
	        if ("".equals(nsuri)) {
	            return resolveNS(start.getParentNode(), prefix);
	        }
	        return nsuri;
	    }
	
	 public static String createPDD(String workflowName, String bpelFile) throws Exception {
		 File f = new File(bpelFile);
		 DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		 dbf.setNamespaceAware(true);
		 DocumentBuilder db;
		 try {
			 db = dbf.newDocumentBuilder();
		 } catch (ParserConfigurationException e) {
			 throw e;
		 }
		 Document bpelDoc = db.parse(f);
		 
		 Document pdd = PDDGenerator.generatePDD(workflowName, bpelDoc);
		 File pddFile = new File(System.getProperty("java.io.tmpdir")+ workflowName + ".pdd");
		 Writer writer = new BufferedWriter(new FileWriter(pddFile));
		 XMLUtils.PrettyDocumentToWriter(pdd, writer);
		 writer.close();
		 System.out.println(pddFile.getAbsolutePath());
		 return pddFile.getAbsolutePath();
		 
	 }
	 public static void main(String args[]) throws Exception {
		 
		 String workflowName = args[0];
		 File f = new File(args[1]);
	        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	        dbf.setNamespaceAware(true);
	        DocumentBuilder db;
	        try {
	            db = dbf.newDocumentBuilder();
	        } catch (ParserConfigurationException e) {
	            throw e;
	        }
	     Document bpelDoc = db.parse(f);
	     
		 Document pdd = PDDGenerator.generatePDD(workflowName, bpelDoc);
		 System.out.println(XMLUtils.PrettyDocumentToString(pdd));
		
	 }
}
