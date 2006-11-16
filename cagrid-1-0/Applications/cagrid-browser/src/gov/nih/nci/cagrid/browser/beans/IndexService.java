package gov.nih.nci.cagrid.browser.beans;

//~--- JDK imports ------------------------------------------------------------

import gov.nih.nci.cagrid.browser.util.GridUtils;
import gov.nih.nci.cagrid.discovery.client.DiscoveryClient;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.types.URI;

import javax.faces.model.SelectItem;
import java.util.ArrayList;

//~--- classes ----------------------------------------------------------------

/**
 * Created by the caGrid Team
 * User: kherm
 * Date: Jun 23, 2005
 * Time: 10:59:51 AM
 * To change this template use File | Settings | File Templates.
 */
public class IndexService {
    private static SelectItem[] indexItems = null;

    /**
     * The default index *
     */
    private ArrayList indexList = new ArrayList();
    private EndpointReferenceType defaultEPR;
    private DiscoveryClient discClient;


    public SelectItem[] getIndexItems() throws Exception {
        indexItems = new SelectItem[indexList.size() + 1];

        for (int i = 0; i < indexList.size(); i++) {
            indexItems[i + 1] = new SelectItem(indexList.get(i));
        }

        return indexItems;
    }

    public ArrayList getIndexList() {
        return indexList;
    }

    public static void setIndexItems(SelectItem[] indexItems) {
        IndexService.indexItems = indexItems;
    }

    public void setIndexList(ArrayList indexList) throws Exception {
        this.indexList = indexList;
    }

    public EndpointReferenceType getDefaultEPR() throws URI.MalformedURIException {
        if (defaultEPR == null) {
            defaultEPR = GridUtils.getEPR((String) indexList.get(0));
        }
        return defaultEPR;
    }

    public void setDefaultEPR(EndpointReferenceType defaultEPR) {
        this.defaultEPR = defaultEPR;
    }

    public DiscoveryClient getDiscClient() throws Exception {
        if (discClient == null) {
            discClient = new DiscoveryClient(getDefaultEPR());
        }
        return discClient;
    }

    public void setDiscClient(DiscoveryClient discClient) {
        this.discClient = discClient;
    }

}


