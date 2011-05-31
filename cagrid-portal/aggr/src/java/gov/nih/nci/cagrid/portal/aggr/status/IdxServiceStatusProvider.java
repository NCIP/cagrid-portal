package gov.nih.nci.cagrid.portal.aggr.status;

import gov.nih.nci.cagrid.portal.aggr.ServiceUrlProvider;
import gov.nih.nci.cagrid.portal.domain.ServiceStatus;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Set;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class IdxServiceStatusProvider implements ServiceStatusProvider {

    private final Log logger = LogFactory.getLog(IdxServiceStatusProvider.class);

    private String[] indexServiceUrls = new String[0];
    private ServiceUrlProvider dynamicServiceUrlProvider;
    private boolean strictIndexVerification;

    /*
        * (non-Javadoc)
    *
    * @see gov.nih.nci.cagrid.portal.aggr.status.ServiceStatusProvider#getStatus(java.lang.String)
    */
    public ServiceStatus getStatus(String serviceUrl) {
        ServiceStatus status = ServiceStatus.ACTIVE;

        if (strictIndexVerification) {
            //set it to Inactive first
            status = ServiceStatus.INACTIVE;
            for (String indexSvcUrl : indexServiceUrls) {
                Set<String> dynamicSvcUrls = null;
                try {
                    dynamicSvcUrls = getDynamicServiceUrlProvider().getUrls(
                            indexSvcUrl);
                    if (dynamicSvcUrls.contains(serviceUrl)) {
                        logger.debug("Service:" + serviceUrl + " found in index. All is well");
                        status = ServiceStatus.ACTIVE;
                        break;
                    }

                } catch (Exception ex) {
                    //Will happen during high server load
                    //catch and log exception. Status returned will be ACTIVE
                    logger.error(ex);
                    logger.warn("Error retrieving service urls from Index." +
                            " Will keep service:" + serviceUrl + " as active: ");
                    status = ServiceStatus.ACTIVE;
                }
            }
            if (status.equals(ServiceStatus.INACTIVE))
                logger.info("Active Service:" + serviceUrl + " is not in the Index. Marking Service as Inactive");
        }

        return status;
    }


    public boolean isStrictIndexVerification() {
        return strictIndexVerification;
    }

    public void setStrictIndexVerification(boolean strictIndexVerification) {
        this.strictIndexVerification = strictIndexVerification;
    }

    public ServiceUrlProvider getDynamicServiceUrlProvider() {
        return dynamicServiceUrlProvider;
    }

    public void setDynamicServiceUrlProvider(ServiceUrlProvider dynamicServiceUrlProvider) {
        this.dynamicServiceUrlProvider = dynamicServiceUrlProvider;
    }

    public String[] getIndexServiceUrls() {
        return indexServiceUrls;
    }

    public void setIndexServiceUrls(String[] indexServiceUrls) {
        this.indexServiceUrls = indexServiceUrls;
    }
}
