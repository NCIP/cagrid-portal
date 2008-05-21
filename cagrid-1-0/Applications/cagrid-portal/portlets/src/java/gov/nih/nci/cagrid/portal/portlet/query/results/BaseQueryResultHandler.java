/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.query.results;

import gov.nih.nci.cagrid.portal.portlet.util.Table;

import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public abstract class BaseQueryResultHandler extends DefaultHandler {

	protected Stack<ElementInfo> elementStack = new Stack<ElementInfo>();
	protected ResultType resultType;
	protected QueryType queryType;
	
	enum ResultType {
		EMPTY, OBJECT, ATTRIBUTE, COUNT;
	}

	enum QueryType {
		CQL, DCQL;
	}

	class ElementInfo {
		String uri;
		String localName;
		String qName;
		Attributes atts;

		ElementInfo(String uri, String localName, String qName, Attributes atts) {
			this.uri = uri;
			this.localName = localName;
			this.qName = qName;
			this.atts = atts;
		}
	}

}
