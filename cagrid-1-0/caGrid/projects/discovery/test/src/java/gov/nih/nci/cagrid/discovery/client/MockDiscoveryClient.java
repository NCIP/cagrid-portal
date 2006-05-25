package gov.nih.nci.cagrid.discovery.client;

import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.types.URI.MalformedURIException;
import org.apache.xpath.XPathAPI;
import org.apache.xpath.objects.XNodeSet;
import org.apache.xpath.objects.XObject;
import org.globus.wsrf.encoding.ObjectDeserializer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * Acts as the discovery client, and replaces the actual call to the Index
 * Service with an execution of the XPath against the specified local file.
 * 
 * @author oster
 */
public class MockDiscoveryClient extends DiscoveryClient {
	private Node xml;


	/**
	 * @throws MalformedURIException
	 */
	public MockDiscoveryClient(Node xml) throws MalformedURIException {
		super();
		this.xml = xml;
	}


	protected EndpointReferenceType[] discoverByFilter(String xpathPredicate) throws Exception {
		XObject result = XPathAPI.eval(getRootNode(), translateXPath(xpathPredicate));
		EndpointReferenceType[] resultsList = null;

		if (result instanceof XNodeSet) {
			XNodeSet set = (XNodeSet) result;
			NodeList list = set.nodelist();
			resultsList = new EndpointReferenceType[list.getLength()];
			for (int i = 0; i < list.getLength(); i++) {
				Node node = list.item(i);
				if (node instanceof Document) {
					Object obj = ObjectDeserializer.toObject(((Document) node).getDocumentElement(),
						EndpointReferenceType.class);
					resultsList[i] = (EndpointReferenceType) obj;
				} else if (node instanceof Element) {
					Object obj = ObjectDeserializer.toObject((Element) node, EndpointReferenceType.class);
					resultsList[i] = (EndpointReferenceType) obj;
				} else {
					throw new Exception("Unexpected query result!");
				}
			}
		} else {
			throw new Exception("Unexpected query result!");
		}

		return resultsList;
	}


	public void setRootNode(Node node) {
		this.xml = node;
	}


	/**
	 * @return
	 */
	private Node getRootNode() {
		return xml;
	}

}
