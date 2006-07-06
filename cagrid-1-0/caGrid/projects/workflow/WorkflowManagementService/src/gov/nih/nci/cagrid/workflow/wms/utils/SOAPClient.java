package gov.nih.nci.cagrid.workflow.wms.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class SOAPClient {

    private static final String DUMMY_ROOT = "aeDummyRoot";

    /** Cache transformer factory. */
    private static TransformerFactory sTransformerFactory = null;


    /**
     * Added due to JBoss 4.03 expansion of JSP invocation into a static call.
     * 
     * @param url
     * @param message
     * 
     * @return Process or service output or error message.
     */
    public static String invokeStat(String url, String message) {
        SOAPClient sc = new SOAPClient();
        return sc.invoke(url, message);
    }

    /**
     * @param url
     * @param message
     * @return process or service output or error message
     */
    public String invoke(String url, String message) {
        if (message.equals(""))
            return "Please provide message content.";

        SOAPConnection conn = null;
        
        try {
            // Create and send SOAP message to service at URL
        	URL tempUrl = new URL(url);
        	url = tempUrl.toString();
            SOAPMessage requestMsg = createSOAPMessage(message);
            requestMsg.getMimeHeaders().addHeader("SOAPAction", "\"\"");

            conn = SOAPConnectionFactory.newInstance().createConnection();
            System.out.println("URL " + url);
            SOAPMessage responseMsg = conn.call(requestMsg, url);
            if (responseMsg != null) {
                // Return SOAP envelope containing process response
                Document d = responseMsg.getSOAPBody().getOwnerDocument();
                return documentToString((Node) d, true);
            } else {
                return "No message was returned.\r\nIf this is a one-way operation then this is to be expected.";
            }

        } catch (Exception e) {
            e.printStackTrace();
            String msg = e.getLocalizedMessage();
            StringWriter stackTrace = new StringWriter();
            e.printStackTrace(new PrintWriter(stackTrace));
            return "Error: " + (msg != null ? msg + ". " : "") + "\r\n" + stackTrace.toString();
        } finally {
            if (conn != null)
                try {
                    conn.close();
                } catch (SOAPException e) {
                }
        }
    }

    /**
     * @param message
     * @return SOAP message with body wrapping message content as XML element(s)
     */
    public SOAPMessage createSOAPMessage(String message) throws SOAPException,
            SAXException, IOException, ParserConfigurationException {

        MessageFactory msgFactory = MessageFactory.newInstance();
        SOAPMessage requestMsg = msgFactory.createMessage();
        requestMsg.getSOAPHeader().detachNode();
        Document doc = createDocument(message);
        createSOAPBody(doc, requestMsg);
        requestMsg.saveChanges();
        
        return requestMsg;
    }

    /**
     * @param message
     * @return Document containing message string (wrapped with DUMMY_ROOT
     *         element if has multiple root elements).
     */
    public Document createDocument(String message) throws SAXException,
            IOException, ParserConfigurationException {
        
        Document doc = null;
        try {
            doc = stringToDocument(message);
        } catch (SAXParseException spe) { // In case message contains multiple
            // root elements, wrap them with one
            String s = "<" + DUMMY_ROOT + ">\n" + message + "\n</" + DUMMY_ROOT
                    + ">";
            doc = stringToDocument(s);
        }
        return doc;
    }

    /**
     * Adds doc to env's SOAP body
     * 
     * @param doc
     * @param env
     * @throws SOAPException
     */
    public void createSOAPBody(Document doc, SOAPMessage message)
            throws SOAPException {

        Element rootElement = doc.getDocumentElement();
        String rootElementName = rootElement.getLocalName();
        if (rootElementName.equals(DUMMY_ROOT) || rootElementName.equals("Body")) {
            // Copy child elements to SOAP body element
            NodeList nl = rootElement.getChildNodes();
            for (int i = 0; i < nl.getLength(); i++) {
                Node node = nl.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Node e = message.getSOAPPart().importNode(node, true);
                    message.getSOAPBody().appendChild(e);
                }
            }
        } else
            message.getSOAPBody().addDocument(doc);
    }

    /**
     * @param xml
     * @return Document containing xml string
     */
    public Document stringToDocument(String xml) throws SAXException,
            IOException, ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        // Use the default (false) for now: factory.setValidating(true);
        factory.setNamespaceAware(true);
        return factory.newDocumentBuilder().parse(
                new InputSource(new StringReader(xml)));
    }

    /**
     * This method will return the String representation of a document.
     * 
     * @param aNode
     *            the node to be converted.
     * @param aIndentFlag
     *            true if you want pretty printing
     */
    private String documentToString(Node aNode, boolean aIndentFlag)
            throws TransformerConfigurationException, TransformerException {

        StringWriter writer = new StringWriter();
        Transformer t = getTransformerFactory().newTransformer();
        t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        t.setOutputProperty(OutputKeys.INDENT, aIndentFlag ? "yes" : "no");
        t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "3");
        t.transform(new DOMSource(aNode), new StreamResult(writer));

        return writer.toString();
    }

    /**
     * @return The cached transformer factory
     */
    private TransformerFactory getTransformerFactory() {
        if (sTransformerFactory == null)
            sTransformerFactory = TransformerFactory.newInstance();
        return sTransformerFactory;
    }
    
    public static void main(String args[]) {
    	String message = "<invoke xmlns=\"http://workflow.cagrid.nci.nih.gov/SampleService1\"><arg0><param>Hello</param></arg0></invoke>";
    	SOAPClient.invokeStat("http://localhost:8080/active-bpel/services/SimpleSecureService", message);
    }

}