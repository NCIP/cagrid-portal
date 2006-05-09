package gov.nih.nci.cagrid.workflow.management.service;

import java.io.File;
import java.io.FileOutputStream;

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

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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

    public static void main(String[] args) throws Exception {
        File f = new File(args[0]);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder db;
        try {
            db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            System.err.println("Your JAXP is sick in the head.");
            throw e;
        }
        Document bpelDoc = db.parse(f);
        PDDGenerator g = new PDDGenerator(bpelDoc);
        Document pddDoc = g.generate();
        FileOutputStream out = new FileOutputStream(f.getName()+".pdd");
        Transformer trans = TransformerFactory.newInstance().newTransformer();
        trans.setOutputProperty(OutputKeys.INDENT, "yes");
        trans.transform(
                new DOMSource(pddDoc), 
                new StreamResult(out));
        out.close();
    }

    public PDDGenerator(Document bpel) {
        bpelDoc = bpel;
        parsePartnerLinks();
    }

    public Document generate() throws ParserConfigurationException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.newDocument();
        Element root = doc.createElementNS(XMLNS_PDD, "process");
        QName processQ = getBpelProcessName();
        root.setAttribute("name","bpelns:"+processQ.getLocalPart());
        // FIXME: for the record, I think Transformer should do this, but for
        // some obscure reason mine doesn't
        root.setAttribute("xmlns", XMLNS_PDD);
        root.setAttribute("xmlns:bpelns",processQ.getNamespaceURI());
        root.setAttribute("location","...");
        doc.appendChild(root);
        appendPartnerLinks(doc, root);
        appendWsdlReferences(doc, root);
        return doc;
    }

    public QName getBpelProcessName() {
        Element root = bpelDoc.getDocumentElement();
        String processName = root.getAttribute("name");
        String processNS = root.getAttribute("targetNamespace");
        return new QName(processNS, processName);
    }

    void appendPartnerLinks(final Document doc, final Node root) {
        Element plinksNode = doc.createElementNS(XMLNS_PDD, "partnerLinks");
        root.appendChild(plinksNode);
        for (Iterator it = partnerLinks.iterator(); it.hasNext(); ) {
            PartnerLink plink = (PartnerLink)it.next();
            Element pNode = doc.createElementNS(XMLNS_PDD, "partnerLink");
            plinksNode.appendChild(pNode);
            pNode.setAttribute("name", plink.name);
            if (!"".equals(plink.myRole)) {
                Element myRoleNode = doc.createElementNS(XMLNS_PDD, "myRole");
                myRoleNode.setAttribute("service","ProcessServiceNameHere");
                myRoleNode.setAttribute("allowedRoles","");
                myRoleNode.setAttribute("binding","MSG");
                pNode.appendChild(myRoleNode);
            } else if (!"".equals(plink.partnerRole)) {
                Element pRoleNode = doc.createElementNS(XMLNS_PDD, "partnerRole");
                pRoleNode.setAttribute("endpointReference","static");
                Element epRefNode = doc.createElementNS(XMLNS_WSA,"EndpointReference");
                Element epAddrNode = doc.createElementNS(XMLNS_WSA,"Address");
                Element epServNode = doc.createElementNS(XMLNS_WSA,"ServiceName");

                pNode.appendChild(pRoleNode);
                pRoleNode.appendChild(epRefNode);
                epRefNode.appendChild(epAddrNode);
                epRefNode.appendChild(epServNode);

                epRefNode.setPrefix("wsa");
                epRefNode.setAttribute("xmlns:wsa", XMLNS_WSA);
                epAddrNode.appendChild(doc.createTextNode(WSA_ANONYMOUS));
                epAddrNode.setPrefix("wsa");
                epAddrNode.setAttribute("xmlns:wsa", XMLNS_WSA);
                epServNode.setAttribute("PortName","");
                epServNode.setPrefix("wsa");
                epServNode.setAttribute("xmlns:wsa", XMLNS_WSA);
                epServNode.setAttribute("xmlns:ns0", 
                        plink.linkType.getNamespaceURI());
                epServNode.appendChild(doc.createTextNode("ns0:SoapPortHere"));
            } else {
                System.err.println("PartnerLink "+plink.name
                        +" should have either myRole or partnerRole attribute!");
                continue;
            }
        }
    }

    void appendWsdlReferences(final Document doc, final Node root) {
        Element wsdlRefsNode = doc.createElementNS(XMLNS_PDD,"wsdlReferences");
        root.appendChild(wsdlRefsNode);
        for (Iterator it = wsdlNamespaces.iterator(); it.hasNext(); ) {
            String ns = (String)it.next();
            Element wsdlRefNode = doc.createElementNS(XMLNS_PDD, "wsdl");
            wsdlRefNode.setAttribute("namespace",ns);
            wsdlRefNode.setAttribute("location","project:/...");
            wsdlRefsNode.appendChild(wsdlRefNode);
        }
    }

    void parsePartnerLinks() {
        NodeList partnerLinksNL = bpelDoc.getElementsByTagNameNS(
                XMLNS_BPEL, "partnerLink");
        partnerLinks = new ArrayList();
        wsdlNamespaces = new HashSet();

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
    }

    String resolveNS(final Node start, final String prefix) {
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



    private class PartnerLink
    {
        public String name;
        public QName linkType;
        public String myRole;
        public String partnerRole;
    }

    private Collection wsdlNamespaces;
    private Collection partnerLinks;
    private Document bpelDoc;
}

