/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-portal/LICENSE.txt for details.
*============================================================================
**/
/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.query.results;

import java.util.ArrayList;
import java.util.List;
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
	private List<String> columnNames = new ArrayList<String>();
	
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

	public List<String> getColumnNames() {
		return columnNames;
	}

	public void setColumnNames(List<String> columnNames) {
		this.columnNames = columnNames;
	}

}
