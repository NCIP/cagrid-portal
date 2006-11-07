package gov.nih.nci.cagrid.browser.beans;

//~--- non-JDK imports --------------------------------------------------------

import gov.nih.nci.cagrid.browser.util.ASDiscoveryUtil;
import gov.nih.nci.cagrid.browser.util.ApplicationCtx;
import gov.nih.nci.cagrid.browser.util.DSDiscoveryUtil;
import gov.nih.nci.cagrid.browser.util.GenericDiscoveryUtil;
import gov.nih.nci.cagrid.common.client.DiscoveryClient;

import javax.faces.model.SelectItem;
import java.util.Calendar;
import java.util.LinkedHashSet;
import java.util.Set;

//~--- classes ----------------------------------------------------------------

/**
 * Created by the caGrid Team
 * User: kherm
 * Date: Jun 15, 2005
 * Time: 11:30:41 AM
 * To change this template use File | Settings | File Templates.
 */
public class KeywordSearchCriteria {
    private final static String META_DATA_ITEMS_ALL           = "All";
    private final static String META_DATA_ITEMS_OBJECT_CLASS  = "Object Class";
    private final static String META_DATA_ITEMS_EVS_CONCEPT   = "EVS Concept";
    private final static String META_DATA_ITEMS_RC_INFO       =
            "Research Center";
    private final static String SERVICE_TYPE_ITEMS_ALL        = "All";
    private final static String SERVICE_TYPE_ITEMS_DATA       =
            "Data Services";
    private final static String SERVICE_TYPE_ITEMS_ANALYTICAL =
            "Analytical Services";
    private static SelectItem[] metaDataItems =
            new SelectItem[] {
                    new SelectItem(KeywordSearchCriteria.META_DATA_ITEMS_ALL),
                    new SelectItem(KeywordSearchCriteria.META_DATA_ITEMS_EVS_CONCEPT),
                    new SelectItem(KeywordSearchCriteria.META_DATA_ITEMS_OBJECT_CLASS),
                    new SelectItem(KeywordSearchCriteria.META_DATA_ITEMS_RC_INFO), };
    private static SelectItem[] serviceTypeItems =
            new SelectItem[] {
                    new SelectItem(KeywordSearchCriteria.SERVICE_TYPE_ITEMS_ALL),
                    new SelectItem(KeywordSearchCriteria.SERVICE_TYPE_ITEMS_DATA),
                    new SelectItem(
                            KeywordSearchCriteria.SERVICE_TYPE_ITEMS_ANALYTICAL), };

    //~--- fields -------------------------------------------------------------

    private String[]           metaDataCategories    = null;
    private String[]           serviceTypeCategories = null;
    private BrowserConfig      browserConfig;
    private String             keyword;
    private DiscoveredServices services;

    //~--- constructors -------------------------------------------------------

    public KeywordSearchCriteria() {}

    //~--- methods ------------------------------------------------------------

    public Set discoverAS4Keyword() {

        /* do a new discovery */
        IndexService    index           = browserConfig.getIndexService();
        ASDiscoveryUtil analDiscUtil    = new ASDiscoveryUtil(index);
        LinkedHashSet             discoveryResult = new LinkedHashSet();

        /* If no metadata category selected then do a goodle type search */
        if (metaDataCategories.length < 1) {
            discoveryResult.addAll(
                    analDiscUtil.getServicesFromKeyword(this.keyword));
        }

        for (int i = 0; i < metaDataCategories.length; i++) {
            if (metaDataCategories[i].equals(
                    KeywordSearchCriteria.META_DATA_ITEMS_ALL)) {
                discoveryResult.addAll(
                        analDiscUtil.getServicesFromKeyword(this.keyword));
            } else if (metaDataCategories[i].equals(
                    KeywordSearchCriteria.META_DATA_ITEMS_OBJECT_CLASS)) {
                discoveryResult.addAll(
                        analDiscUtil.getServicesFromObjectClass(this.keyword));
            } else if (metaDataCategories[i].equals(
                    KeywordSearchCriteria.META_DATA_ITEMS_EVS_CONCEPT)) {
                discoveryResult.addAll(
                        analDiscUtil.getServicesFromEVSConcept(this.keyword));
            } else if (metaDataCategories[i].equals(
                    KeywordSearchCriteria.META_DATA_ITEMS_RC_INFO)) {}
        }    // end for metadata categories

        return discoveryResult;
    }

    private Set discoverAllServices4Keyword() {

        /* do a new discovery */
        IndexService         index                =
                browserConfig.getIndexService();
        GenericDiscoveryUtil genericDiscoveryUtil =
                new GenericDiscoveryUtil(index);
        DSDiscoveryUtil dataDiscUtil    = new DSDiscoveryUtil(index);
        LinkedHashSet             discoveryResult = new LinkedHashSet();

        /* If no metadata category selected then do a goodle type search */
        if (metaDataCategories.length < 1) {
            discoveryResult.addAll(
                    genericDiscoveryUtil.getServicesFromKeyword(this.keyword));
        }

        for (int i = 0; i < metaDataCategories.length; i++) {
            if (metaDataCategories[i].equals(
                    KeywordSearchCriteria.META_DATA_ITEMS_ALL)) {
                discoveryResult.addAll(
                        genericDiscoveryUtil.getServicesFromKeyword(this.keyword));
                        break;
            } else if (metaDataCategories[i].equals(
                    KeywordSearchCriteria.META_DATA_ITEMS_OBJECT_CLASS)) {
                discoveryResult.addAll(
                        dataDiscUtil.getServicesFromObjectClass(this.keyword));
            } else if (metaDataCategories[i].equals(
                    KeywordSearchCriteria.META_DATA_ITEMS_EVS_CONCEPT)) {
                discoveryResult.addAll(
                        dataDiscUtil.getServicesFromEVSConcept(this.keyword));
            } else if (metaDataCategories[i].equals(
                    KeywordSearchCriteria.META_DATA_ITEMS_RC_INFO)) {
                discoveryResult.addAll(
                        genericDiscoveryUtil.getServicesFromRCInfo(this.keyword));
            }
        }    // end for metadata categories

        return discoveryResult;
    }

    public Set discoverDS4Keyword() {

        /* do a new discovery */
        IndexService    index           = browserConfig.getIndexService();
        DSDiscoveryUtil dataDiscUtil    = new DSDiscoveryUtil(index);
        LinkedHashSet             discoveryResult = new LinkedHashSet();

        /* If no metadata category selected then do a goodle type search on ALL metadata categories */
        if (metaDataCategories.length < 1) {
            discoveryResult.addAll(
                    dataDiscUtil.getServicesFromKeyword(this.keyword));

        }


        for (int i = 0; i < metaDataCategories.length; i++) {
            if (metaDataCategories[i].equals(
                    KeywordSearchCriteria.META_DATA_ITEMS_ALL)) {
                discoveryResult.addAll(
                        dataDiscUtil.getServicesFromKeyword(this.keyword));
                    break;
            } else if (metaDataCategories[i].equals(
                    KeywordSearchCriteria.META_DATA_ITEMS_OBJECT_CLASS)) {
                discoveryResult.addAll(
                        dataDiscUtil.getServicesFromObjectClass(this.keyword));
            } else if (metaDataCategories[i].equals(
                    KeywordSearchCriteria.META_DATA_ITEMS_EVS_CONCEPT)) {
                discoveryResult.addAll(
                        dataDiscUtil.getServicesFromEVSConcept(this.keyword));
            } else if (metaDataCategories[i].equals(
                    KeywordSearchCriteria.META_DATA_ITEMS_RC_INFO)) {}
        }    // end for metadata categories

        return discoveryResult;
    }

    public String doDiscoveryAll(){

        /* clear the preivous resultset * */
        services.removeAll();

        LinkedHashSet discoveryResult = new LinkedHashSet();

        IndexService         index                =
                browserConfig.getIndexService();
        GenericDiscoveryUtil genericDiscoveryUtil =
                new GenericDiscoveryUtil(index);
        DiscoveryClient discClient = genericDiscoveryUtil.getDiscoveryClient();


        discoveryResult.addAll(discClient.getAllServices());

        if (discoveryResult == null) {
            ApplicationCtx.getAppLogger().info(
                    "Null discovery resultset from "
                            + browserConfig.getIndexService() + " at "
                            + Calendar.getInstance().getTime());
        } else {

            /** Only do this once for entire discovered resultset */
            services.addAll(GenericDiscoveryUtil.getGSH(discoveryResult));
        }

        return "success";
    }

    public String doDiscovery() {
        LinkedHashSet discoveryResult = new LinkedHashSet();

        /* clear the preivous resultset * */
        services.removeAll();

        /* Is no specific service type is selected then search ALL */
        if (serviceTypeCategories.length < 1) {
            discoveryResult.addAll(discoverAllServices4Keyword());
        }    // end if service type

        else {
            for (int j = 0; j < serviceTypeCategories.length; j++) {
                if (serviceTypeCategories[j].equals(
                        KeywordSearchCriteria.SERVICE_TYPE_ITEMS_ALL)) {
                    discoveryResult.addAll(discoverAllServices4Keyword());
                } else if (serviceTypeCategories[j].equals(
                        KeywordSearchCriteria.SERVICE_TYPE_ITEMS_DATA)) {
                    discoveryResult.addAll(discoverDS4Keyword());
                } else if (serviceTypeCategories[j].equals(
                        KeywordSearchCriteria.SERVICE_TYPE_ITEMS_ANALYTICAL)) {
                    discoveryResult.addAll(discoverAS4Keyword());
                }
            }    // end for service types
        }

        if (discoveryResult == null) {
            ApplicationCtx.getAppLogger().info(
                    "Null discovery resultset from "
                            + browserConfig.getIndexService() + " at "
                            + Calendar.getInstance().getTime());
        } else {

            /** Only do this once for entire discovered resultset */

            services.addAll(GenericDiscoveryUtil.getGSH(discoveryResult));
        }

        return "success";
    }

    //~--- get methods --------------------------------------------------------

    public BrowserConfig getBrowserConfig() {
        return browserConfig;
    }

    public String getKeyword() {
        return keyword;
    }

    public String[] getMetaDataCategories() {
        return metaDataCategories;
    }

    public SelectItem[] getMetaDataItems() {
        return metaDataItems;
    }

    public String[] getServiceTypeCategories() {
        return serviceTypeCategories;
    }

    public SelectItem[] getServiceTypeItems() {
        return serviceTypeItems;
    }

    public DiscoveredServices getServices() {
        return services;
    }

    //~--- set methods --------------------------------------------------------

    public void setBrowserConfig(BrowserConfig browserConfig) {
        this.browserConfig = browserConfig;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public void setMetaDataCategories(String[] metaDataCategories) {
        this.metaDataCategories = metaDataCategories;
    }

    public void setServiceTypeCategories(String[] serviceTypeCategories) {
        this.serviceTypeCategories = serviceTypeCategories;
    }

    public void setServices(DiscoveredServices services) {
        this.services = services;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
