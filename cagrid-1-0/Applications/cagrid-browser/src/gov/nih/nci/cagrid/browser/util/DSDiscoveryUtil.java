package gov.nih.nci.cagrid.browser.util;

//~--- non-JDK imports --------------------------------------------------------

import gov.nih.nci.cagrid.browser.beans.BrowserConfig;
import gov.nih.nci.cagrid.browser.beans.IndexService;
import gov.nih.nci.cagrid.browser.exception.EVSDiscoveryException;
import gov.nih.nci.cagrid.data.client.DataServiceDiscoveryClient;
import gov.nih.nci.evs.query.EVSQueryImpl;
import gov.nih.nci.system.applicationservice.ApplicationService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

//~--- classes ----------------------------------------------------------------

/**
 * Created by the caGrid Team
 * User: kherm
 * Date: Jul 27, 2005
 * Time: 10:19:49 AM
 * To change this template use File | Settings | File Templates.
 */
public class DSDiscoveryUtil {
    DataServiceDiscoveryClient dataDisc;

    //~--- constructors -------------------------------------------------------

    public DSDiscoveryUtil(IndexService idx) {
        this.dataDisc = new DataServiceDiscoveryClient();

        dataDisc.setRegistryURL(idx.getUrl());
    }

    //~--- get methods --------------------------------------------------------

    public Collection getAllDataServices() {
        List returnList = dataDisc.getAllDataServices();

        return returnList;
    }

    public Collection getServicesFromEVSConcept(String keyword) {
        List returnList = new ArrayList();

        /* Use discovery client to do a search in the metadata first */
        List metaList = dataDisc.discoverServicesByConceptCode(keyword);

        if (metaList != null) {
            returnList.addAll(metaList);
        }

        /** Don't search EVS if keyword is null */
        if (keyword.length() > 0) {
            BrowserConfig browserCfg =
                (BrowserConfig) ApplicationCtx.getBean(
                    "#{browserConfig}");
            String cvsAppServerURL = browserCfg.getCaCOREServiceURL();

            /*
             *  Do a EVS search for extended search results. This allows searching
             *       for evs concepts not just codes
             */
            EVSQueryImpl evsQuery = new EVSQueryImpl();

            evsQuery.getConceptCodeByName("NCI_Thesaurus", keyword);

            ApplicationService evsService =
                ApplicationService.getRemoteInstance(cvsAppServerURL);
            List conceptList = null;

            try {
                conceptList = evsService.evsSearch(evsQuery);
            } catch (Exception e) {
                throw new EVSDiscoveryException("Problem discovering concept"
                                                + keyword, e);
            }

            for (Iterator iter = conceptList.iterator(); iter.hasNext(); ) {
                List evsList = dataDisc.discoverServicesByConceptCode(
                                   (String) iter.next());

                if (evsList != null) {
                    returnList.addAll(evsList);
                }
            }
        }    // end If statement

        return returnList;
    }

    public Collection getServicesFromKeyword(String keyword) {
       List returnList = new ArrayList();
        if(keyword.length() <= 0){
           return getAllDataServices();
        }

        else{
            returnList.addAll(dataDisc.discoverServicesByModelSearchString(keyword));
        }
        return returnList;
    }

    public Collection getServicesFromObjectClass(String keyword) {
        List returnList = new ArrayList();
        returnList.addAll(dataDisc.discoverServicesByObjectName(keyword));

        return returnList;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
