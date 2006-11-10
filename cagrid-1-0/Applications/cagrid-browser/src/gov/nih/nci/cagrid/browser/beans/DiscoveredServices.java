package gov.nih.nci.cagrid.browser.beans;

//~--- JDK imports ------------------------------------------------------------

import org.apache.axis.message.addressing.EndpointReferenceType;

import java.util.ArrayList;
import java.util.List;

//~--- classes ----------------------------------------------------------------

/**
 * Represents the
 * current set of  "discovered services"
 * Created by the caGrid Team
 * User: kherm
 * Date: Jun 15, 2005
 * Time: 11:04:48 AM
 * To change this template use File | Settings | File Templates.
 */
public class DiscoveredServices {
    private List list = new ArrayList();
    private String size;

    public DiscoveredServices() {
    }


    public void addDiscoveryResult(EndpointReferenceType[] eprs) {
        for (int i = 0; i < eprs.length; i++) {
            CaGridService discoveredServiceTemp = new CaGridService(eprs[i]);
            list.add(discoveredServiceTemp);
        }
    }

    public void clear() {
        this.list.clear();
    }


    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

}

