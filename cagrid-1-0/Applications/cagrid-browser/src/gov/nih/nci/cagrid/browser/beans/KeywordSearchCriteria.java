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

    List metaDataCategoriesSelected = new ArrayList();
    List metaDataCategoryItems = new ArrayList();

    //user sercted categories in view
    List serviceTypeCategoriesSelected = new ArrayList();
    //all categories
    List serviceTypeCategoryItems = new ArrayList();

    private DiscoveredServices discoveryResult;
    private IndexService idxService;


    private String keyword;

    public KeywordSearchCriteria() {

        metaDataCategoryItems.add(new SelectItem(META_DATA_ITEMS_EVS_CONCEPT));
        metaDataCategoryItems.add(new SelectItem(META_DATA_ITEMS_OBJECT_CLASS));
        metaDataCategoryItems.add(new SelectItem(META_DATA_ITEMS_RC_INFO));
        metaDataCategoriesSelected.add(META_DATA_ITEMS_EVS_CONCEPT);
        metaDataCategoriesSelected.add(META_DATA_ITEMS_OBJECT_CLASS);
        metaDataCategoriesSelected.add(META_DATA_ITEMS_RC_INFO);

        serviceTypeCategoryItems.add(new SelectItem(SERVICE_TYPE_ITEMS_DATA));
        serviceTypeCategoryItems.add(new SelectItem(SERVICE_TYPE_ITEMS_ANALYTICAL));
        serviceTypeCategoriesSelected.add(SERVICE_TYPE_ITEMS_DATA);
        serviceTypeCategoriesSelected.add(SERVICE_TYPE_ITEMS_ANALYTICAL);
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
            //have to do this everytime to keep discovery and index selection in sync
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
        try {
            if (metaDataCategoriesSelected.contains(META_DATA_ITEMS_OBJECT_CLASS)) {
                DiscoveryClient discClient = idxService.getDiscClient();
                discClient.discoverServicesByConceptCode(this.keyword);
            }
        } catch (Exception e) {
            throw new FacesException(e);
        }

        return "success";
    }


    public List getMetaDataCategoriesSelected() {
        return metaDataCategoriesSelected;
    }

    public void setMetaDataCategoriesSelected(List metaDataCategoriesSelected) {
        this.metaDataCategoriesSelected = metaDataCategoriesSelected;
    }

    public List getMetaDataCategoryItems() {
        return metaDataCategoryItems;
    }

    public void setMetaDataCategoryItems(List metaDataCategoryItems) {
        this.metaDataCategoryItems = metaDataCategoryItems;
    }

    public List getServiceTypeCategoriesSelected() {
        return serviceTypeCategoriesSelected;
    }

    public void setServiceTypeCategoriesSelected(List serviceTypeCategoriesSelected) {
        this.serviceTypeCategoriesSelected = serviceTypeCategoriesSelected;
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
