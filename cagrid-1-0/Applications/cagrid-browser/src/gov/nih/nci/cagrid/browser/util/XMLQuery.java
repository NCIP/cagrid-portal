package gov.nih.nci.cagrid.browser.util;

//~--- non-JDK imports --------------------------------------------------------

import gov.nih.nci.cagrid.browser.beans.DiscoveredServices;
import org.jdom.Element;
import org.jdom.input.DOMBuilder;
import org.jdom.output.XMLOutputter;

//~--- classes ----------------------------------------------------------------

/**
 * Created by the caGrid Team
 * User: kherm
 * Date: Aug 18, 2005
 * Time: 3:00:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class XMLQuery {
    private String name;
    private String xmlQuery;

    //~--- constructors -------------------------------------------------------

    public XMLQuery(String name, org.w3c.dom.Element queryElement) {
        this.name = name;

        DOMBuilder domBld = new DOMBuilder();
        Element queryElem = domBld.build(queryElement);
        XMLOutputter xmlOut = new XMLOutputter();

        this.xmlQuery = xmlOut.outputString(queryElem);
    }

    //~--- methods ------------------------------------------------------------

    public void fillInQuery() {
        DiscoveredServices disc =
                (DiscoveredServices) AppUtils.getBean(
                        "#{discoveredServices}");


    }

    //~--- get methods --------------------------------------------------------

    public String getName() {
        return name;
    }

    public String getXmlQuery() {
        return xmlQuery;
    }

    //~--- set methods --------------------------------------------------------

    public void setName(String name) {
        this.name = name;
    }

    public void setXmlQuery(String xmlQuery) {
        this.xmlQuery = xmlQuery;
    }
}

//~ Formatted by Jindent --- http://www.jindent.com
