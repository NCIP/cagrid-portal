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
package gov.nih.nci.cagrid.portal.portlet.gss;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.DataServiceConstants;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

public class CalculatorHelper {

    private String url;
    private CQLQueryResults result;
    private Long resultNumber;
    private String strResult;

    public CalculatorHelper(String url, CQLQueryResults result) throws Exception {
        this.url = url;
        this.result = result;
        this.strResult = queryResultAsString(this.result);
        String s = getCounterStrFromFullAnswer(this.strResult);
        try {
            this.resultNumber = new Long(s);
        } catch (NumberFormatException e) {
            this.resultNumber = new Long(0);
        }
    }

    public String getUrl() {
        return url;
    }

    public Long getResultNumber() {
        return resultNumber;
    }

    public String getStrResult() {
        return strResult;
    }

    private String queryResultAsString(CQLQueryResults queryResult) throws Exception {
        StringWriter writer = new StringWriter();
        Utils.serializeObject(queryResult, DataServiceConstants.CQL_RESULT_SET_QNAME, writer);
        String out = writer.getBuffer().toString();
        out = out.replace(" ", "");
        out = out.replace("\n", "");
        out = out.replace("targetClassname", " targetClassname");
        out = out.replace("xmlns", " xmlns");
        out = out.replace("count", " count");
        return out;
    }

    private String getCounterStrFromFullAnswer(String answer) throws Exception {
        //
        // long l = result.getCountResult().getCount();
        //
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = documentBuilderFactory.newDocumentBuilder();
        InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(answer));
        Document document = builder.parse(is);

        Node firstChild = document.getFirstChild();
        Node firstGrandChild = firstChild.getFirstChild();

        NamedNodeMap nm = firstGrandChild.getAttributes();
        Node namedItem = nm.getNamedItem("count");

        String resStr = namedItem.getTextContent();

        return resStr;
    }

}
