package gov.nih.nci.cagrid.browser.util;

//~--- non-JDK imports --------------------------------------------------------

import gov.nih.nci.cagrid.browser.beans.GSH;
import gov.nih.nci.cagrid.browser.beans.IndexService;
import gov.nih.nci.cagrid.common.client.DiscoveryClient;
import org.gridforum.ogsi.HandleType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

//~--- classes ----------------------------------------------------------------

/**
 * Created by the caGrid Team
 * User: kherm
 * Date: Jul 1, 2005
 * Time: 3:27:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class GenericDiscoveryUtil {
    DiscoveryClient discoveryClient;
    IndexService    idx;

    //~--- constructors -------------------------------------------------------

    public GenericDiscoveryUtil(IndexService idx) {
        this.idx = idx;
        discoveryClient     = new DiscoveryClient();

        discoveryClient.setRegistryURL(this.idx.getUrl());
    }

    //~--- get methods --------------------------------------------------------

    /**
     * Get GSH for a collection of URL's(Strings)
     * @param handleList
     * @return Collection of GSH
     */
    public static Collection getGSH(Collection handleList) {
        List     returnList  = new ArrayList();
        Object[] handleArray = handleList.toArray();

        for (int i = 0; i < handleArray.length; i++) {
            HandleType serviceHandleType = ((HandleType) handleArray[i]);
            GSH        serviceGSH        = new GSH(serviceHandleType);

            /** Add service to discovery result only if it is valid
             *
             */
            if(serviceGSH.isValidService())
            returnList.add(serviceGSH);
            else
            ApplicationCtx.logInfo("Service registry " + serviceGSH.getURL() + " is invalid. Ignored during discovery");
        }

        return returnList;
    }

    public Collection getServicesFromBiologyType(String bType) {
        List returnList = discoveryClient.discoverServicesByBiologyType(bType);

        return returnList;
    }

    /**
     * Google type search
     * Returns a list of unique GSH's
     *
     * @param keyword
     * @return HasSet of GSH
     */
    public Collection getServicesFromKeyword(String keyword) {
        List returnList = discoveryClient.discoverServicesBySearchString(keyword);

        return returnList;
    }

    public Collection getServicesFromRCInfo(String keyword) {
        List returnList = discoveryClient.discoverServicesByCancerCenter(keyword);

        return returnList;
    }

    public DiscoveryClient getDiscoveryClient() {
        return discoveryClient;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
