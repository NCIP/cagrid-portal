package gov.nih.nci.cagrid.browser.beans;

//~--- non-JDK imports --------------------------------------------------------

import gov.nih.nci.cagrid.browser.util.ApplicationCtx;
import gov.nih.nci.cagrid.browser.util.XMLQuery;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

//~--- classes ----------------------------------------------------------------

/**
 * Created by the caGrid Team
 * User: kherm
 * Date: Aug 18, 2005
 * Time: 2:45:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class SampleXMLQueries {

    /**
     * Contains xml Queries to be used in the JSF page
     */
    private List xmlQueries = new ArrayList();
    private String queryFilename;

    //~--- constructors -------------------------------------------------------

    public SampleXMLQueries() {
    }

    //~--- get methods --------------------------------------------------------

    public String getQueryFilename() {
        return queryFilename;
    }

    public List getXmlQueries() {
        return xmlQueries;
    }

    //~--- set methods --------------------------------------------------------

    public void setQueryFilename(String queryFilename) {
        this.queryFilename = queryFilename;

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        NodeList queryList;
        Document document;

        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputStream stream =
                    ApplicationCtx.loadResourceAsStream(
                            queryFilename);

            document = builder.parse(stream);
            queryList = document.getElementsByTagName("caBIGXMLQuery");

            for (int i = 0; i < queryList.getLength(); i++) {
                Element queryElem = (Element) queryList.item(i);
                String name = queryElem.getAttribute("name")
                        + " Sample Query";
                XMLQuery query = new XMLQuery(name, queryElem);

                this.xmlQueries.add(query);
            }
        } catch (ParserConfigurationException e) {
            ApplicationCtx.getAppLogger().severe(
                    "Error parsing example queries file. Please check faces-config.xml");
        } catch (SAXException e) {
            ApplicationCtx.getAppLogger().severe(
                    "Error parsing example queries file. Please check faces-config.xml");
        } catch (IOException e) {
            ApplicationCtx.getAppLogger().severe(
                    "Error parsing example queries file. Please check faces-config.xml");
        }
    }

    public void setXmlQueries(List xmlQueries) {
        this.xmlQueries = xmlQueries;
    }
}

//~ Formatted by Jindent --- http://www.jindent.com
