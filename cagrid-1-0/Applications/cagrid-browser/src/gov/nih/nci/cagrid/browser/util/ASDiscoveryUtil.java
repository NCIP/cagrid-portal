package gov.nih.nci.cagrid.browser.util;

//~--- non-JDK imports --------------------------------------------------------

import gov.nih.nci.cagrid.analytical.client.AnalyticalServiceDiscoveryClient;
import gov.nih.nci.cagrid.browser.beans.IndexService;

import java.util.Collection;
import java.util.List;

//~--- classes ----------------------------------------------------------------

/**
 * Created by the caGrid Team
 * User: kherm
 * Date: Jul 27, 2005
 * Time: 10:19:33 AM
 * To change this template use File | Settings | File Templates.
 */
public class ASDiscoveryUtil {
    AnalyticalServiceDiscoveryClient analDisc;
    IndexService                     idx;

    //~--- constructors -------------------------------------------------------

    public ASDiscoveryUtil(IndexService idx) {
        this.idx = idx;
        analDisc = new AnalyticalServiceDiscoveryClient(this.idx.getUrl());
    }

    //~--- get methods --------------------------------------------------------

    public Collection getServicesFromEVSConcept(String keyword) {
        List returnList = analDisc.discoverServicesByConceptCode(keyword);

        return returnList;
    }

    public Collection getServicesFromKeyword(String keyword) {
        List returnList =
            analDisc.discoverServicesByAnalyticalSearchString(keyword);

        return returnList;
    }

    public Collection getServicesFromObjectClass(String keyword) {
        List returnList = analDisc.discoverServicesByMethodClass(keyword);

        return returnList;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
