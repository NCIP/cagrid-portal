package gov.nih.nci.cagrid.browser.beans;


import gov.nih.nci.cagrid.discovery.client.DiscoveryClient;

import javax.faces.FacesException;
import javax.faces.model.SelectItem;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


/**
 * Created by the caGrid Team
 * User: kherm
 * Date: Jun 15, 2005
 * Time: 11:30:41 AM
 * To change this template use File | Settings | File Templates.
 */
public class KeywordSearchCriteria {

    private String META_DATA_ITEMS_OBJECT_CLASS = "Object Class";
    private String META_DATA_ITEMS_EVS_CONCEPT = "EVS Concept";
    private String META_DATA_ITEMS_RC_INFO = "Research Center";


    private String SERVICE_TYPE_ITEMS_DATA = "Data Services";
    private String SERVICE_TYPE_ITEMS_ANALYTICAL = "Analytical Services";

    List metaDataCategories = new ArrayList();
    List metaDataCategoryItems = new ArrayList();


    List serviceTypeCategories = new ArrayList();
    List serviceTypeCategoryItems = new ArrayList();

    private DiscoveredServices discoveryResult;
    private IndexService idxService;


    private String keyword;

    public KeywordSearchCriteria() {

        metaDataCategoryItems.add(new SelectItem(META_DATA_ITEMS_EVS_CONCEPT));
        metaDataCategoryItems.add(new SelectItem(META_DATA_ITEMS_OBJECT_CLASS));
        metaDataCategoryItems.add(new SelectItem(META_DATA_ITEMS_RC_INFO));
        metaDataCategories.add(META_DATA_ITEMS_EVS_CONCEPT);
        metaDataCategories.add(META_DATA_ITEMS_OBJECT_CLASS);
        metaDataCategories.add(META_DATA_ITEMS_RC_INFO);

        serviceTypeCategoryItems.add(new SelectItem(SERVICE_TYPE_ITEMS_DATA));
        serviceTypeCategoryItems.add(new SelectItem(SERVICE_TYPE_ITEMS_ANALYTICAL));
        serviceTypeCategories.add(SERVICE_TYPE_ITEMS_DATA);
        serviceTypeCategories.add(SERVICE_TYPE_ITEMS_ANALYTICAL);
    }

    //~--- methods ------------------------------------------------------------

    public Set discoverAS4Keyword() {

        /* do a new discovery */

        return null;
    }

    /**
     * Discover all services based on a keyboard
     */
    private void doDiscovery4Keyword() throws Exception {

    }


    public String doDiscoveryAll() throws FacesException {
        try {
            DiscoveryClient discClient = idxService.getDiscClient();
            discoveryResult.clear();
            discoveryResult.addDiscoveryResult(discClient.getAllServices(true));
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            throw new FacesException(e);
        }
        return "success";
    }

    public String doDiscovery() {

        return "success";
    }


    public List getMetaDataCategories() {
        return metaDataCategories;
    }

    public void setMetaDataCategories(List metaDataCategories) {
        this.metaDataCategories = metaDataCategories;
    }

    public List getMetaDataCategoryItems() {
        return metaDataCategoryItems;
    }

    public void setMetaDataCategoryItems(List metaDataCategoryItems) {
        this.metaDataCategoryItems = metaDataCategoryItems;
    }

    public List getServiceTypeCategories() {
        return serviceTypeCategories;
    }

    public void setServiceTypeCategories(List serviceTypeCategories) {
        this.serviceTypeCategories = serviceTypeCategories;
    }

    public List getServiceTypeCategoryItems() {
        return serviceTypeCategoryItems;
    }

    public void setServiceTypeCategoryItems(List serviceTypeCategoryItems) {
        this.serviceTypeCategoryItems = serviceTypeCategoryItems;
    }

    public String getKeyword() {
        return keyword;
    }

    public IndexService getIdxService() {
        return idxService;
    }

    public void setIdxService(IndexService idxService) {
        this.idxService = idxService;
    }

    public DiscoveredServices getDiscoveryResult() {
        return discoveryResult;
    }

    public void setDiscoveryResult(DiscoveredServices discoveryResult) {
        this.discoveryResult = discoveryResult;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }


}

//~ Formatted by Jindent --- http://www.jindent.com
