package gov.nih.nci.cagrid.browser.beans;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.List;

import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.log4j.Logger;

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
	
	
	private static Logger logger = Logger.getLogger(DiscoveredServices.class);
	
    private List list = new ArrayList();
    private String size;
    private CaGridService navigatedService;
    

	public DiscoveredServices() {
    }


    public void addDiscoveryResult(EndpointReferenceType[] eprs) {
        for (int i = 0; i < eprs.length; i++) {
            CaGridService svc = new CaGridService(eprs[i]);
            try{
            	svc.loadMetadata();
            	this.list.add(svc);
            }catch(Exception ex){
            	logger.error("Error loading basic metadata for '" + eprs[i] + "': " + ex.getMessage(), ex);
            }
        }
    }

    public void clear() {
        this.list.clear();
    }


    public List getList() {
        return list;
    }

    public CaGridService getNavigatedService() {
        return navigatedService;
    }

    public void setNavigatedService(CaGridService navigatedService) {
        this.navigatedService = navigatedService;
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

