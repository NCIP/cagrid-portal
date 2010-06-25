package gov.nih.nci.cagrid.portal.portlet.gss;

/*
 <ns1:CQLQueryResults targetClassname="gov.nih.nci.caarray.domain.array.Array " xmlns:ns1="http://CQL.caBIG/1/gov.nih.nci.cagrid.CQLResultSet">
 <ns1:CountResult count="67"/>
 </ns1:CQLQueryResults> 
 */
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.DataServiceConstants;
import gov.nih.nci.cagrid.data.client.DataServiceClient;
import gov.nih.nci.cagrid.portal.dao.catalog.GridServiceEndPointCatalogEntryDao;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.context.ApplicationContext;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

public class GridSummaryCalculator implements Runnable {

    private ApplicationContext appCtx;
    private Map<String, Long> summaryResults;

    public GridSummaryCalculator(ApplicationContext _appCtx, Map<String, Long> _summaryResults) {
        this.appCtx = _appCtx;
        this.summaryResults = _summaryResults;
    }

    @Override
    public void run() {
        synchronized (summaryResults) {
            System.out.println("********* RUNNING ********");

            GridServiceEndPointCatalogEntryDao dao = (GridServiceEndPointCatalogEntryDao) appCtx.getBean("gridServiceEndPointCatalogEntryDao");
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

            List<SummaryQueryWithLocations> qloc = new ArrayList<SummaryQueryWithLocations>();
            qloc
                    .add(new SummaryQueryWithLocations(
                            "countArrays",
                            "<ns1:CQLQuery xmlns:ns1=\"http://CQL.caBIG/1/gov.nih.nci.cagrid.CQLQuery\"><ns1:Target name=\"gov.nih.nci.caarray.domain.array.Array \"/><ns1:QueryModifier countOnly=\"true\"/></ns1:CQLQuery>",
                            "http://cagrid1.duhs.duke.edu:18080/wsrf/services/cagrid/CaArraySvc"));
            qloc
                    .add(new SummaryQueryWithLocations(
                            "countExperiments",
                            "<ns1:CQLQuery xmlns:ns1=\"http://CQL.caBIG/1/gov.nih.nci.cagrid.CQLQuery\"><ns1:Target name=\"gov.nih.nci.caarray.domain.project.Experiment\"/><ns1:QueryModifier countOnly=\"true\"/></ns1:CQLQuery>",
                            "http://cagrid1.duhs.duke.edu:18080/wsrf/services/cagrid/CaArraySvc"));
            qloc
                    .add(new SummaryQueryWithLocations(
                            "countSamples",
                            "<ns1:CQLQuery xmlns:ns1=\"http://CQL.caBIG/1/gov.nih.nci.cagrid.CQLQuery\"><ns1:Target name=\"gov.nih.nci.caarray.domain.sample.Sample\"/><ns1:QueryModifier countOnly=\"true\"/></ns1:CQLQuery>",
                            "http://cagrid1.duhs.duke.edu:18080/wsrf/services/cagrid/CaArraySvc"));

            for (SummaryQueryWithLocations currQueryLocs : qloc) {

                currQueryLocs.addUrls(dao.getByUmlClassNameAndPartialUrl(currQueryLocs.getPackage(), currQueryLocs.getShortClassName(), "%"));

                Iterator<String> i = currQueryLocs.getUrlsIterator();
                while (i.hasNext()) {
                    String currUrl = i.next();
                    try {
                        DataServiceClient client = new DataServiceClient(currUrl);

                        CQLQueryResults result = client.query(currQueryLocs.getCqlQuery());
                        // long l = result.getCountResult().getCount();
                        StringWriter writer = new StringWriter();
                        Utils.serializeObject(result, DataServiceConstants.CQL_RESULT_SET_QNAME, writer);
                        String out = writer.getBuffer().toString();
                        out = out.replace(" ", "");
                        out = out.replace("\n", "");
                        out = out.replace("targetClassname", " targetClassname");
                        out = out.replace("xmlns", " xmlns");
                        out = out.replace("count", " count");

                        System.out.println("vvvvvvvvvvvvvvv OUT vvvvvvvvvvvvvvvv");
                        System.out.println(out);

                        try {
                            DocumentBuilder builder = documentBuilderFactory.newDocumentBuilder();
                            System.out.println("builder == null ? " + (builder == null));

                            InputSource is = new InputSource();
                            System.out.println("is == null ? " + (is == null));

                            is.setCharacterStream(new StringReader(out));

                            Document document = builder.parse(is);
                            System.out.println("document == null ? " + (document == null));

                            Node firstChild = document.getFirstChild();
                            System.out.println("firstChild == null ? " + (firstChild == null));
                            System.out.println("firstChild name = " + firstChild.getNodeName());

                            Node firstGrandChild = firstChild.getFirstChild();
                            System.out.println("firstGrandChild == null ? " + (firstGrandChild == null));
                            System.out.println("firstGrandChild name = " + firstGrandChild.getNodeName());

                            NamedNodeMap nm = firstGrandChild.getAttributes();
                            System.out.println("nm == null ? " + (nm == null));

                            Node namedItem = nm.getNamedItem("count");
                            System.out.println("namedItem == null ? " + (namedItem == null));

                            String resStr = namedItem.getTextContent();
                            System.out.println("resStr = " + resStr);

                            currQueryLocs.setCounter(currUrl, resStr);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        System.out.println("^^^^^^^^^^^^^^^ OUT ^^^^^^^^^^^^^^^^");

                    } catch (Exception e) {
                        System.out.println("vvvvvvvvvvvvvvv ERROR vvvvvvvvvvvvvvvv");
                        System.out.println("querying for " + currQueryLocs.getShortClassName());
                        System.out.println("failed for url " + currUrl);
                        System.out.println("with error " + e.getMessage());
                        System.out.println("^^^^^^^^^^^^^^^ ERROR ^^^^^^^^^^^^^^^^");
                    }
                }
            }
            for (SummaryQueryWithLocations currQueryLocs : qloc) {
                summaryResults.put(currQueryLocs.getId(), currQueryLocs.getSum());
            }

        }

    }
}
