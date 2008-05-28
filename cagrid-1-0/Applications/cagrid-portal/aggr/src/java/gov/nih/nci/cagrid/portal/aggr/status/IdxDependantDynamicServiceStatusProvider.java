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
public class IdxDependantDynamicServiceStatusProvider extends DynamicServiceStatusProvider {

    private final Log logger = LogFactory.getLog(IdxDependantDynamicServiceStatusProvider.class);

    private String[] indexServiceUrls = new String[0];
    private ServiceUrlProvider dynamicServiceUrlProvider;
    private boolean strictIndexVerification;

    /*
        * (non-Javadoc)
    *
    * @see gov.nih.nci.cagrid.portal.aggr.status.ServiceStatusProvider#getStatus(java.lang.String)
    */
    @Override
    public ServiceStatus getStatus(String serviceUrl) {
        ServiceStatus status = super.getStatus(serviceUrl);    //To change body of overridden methods use File | Settings | File Templates.

        if (strictIndexVerification) {

            if (status.equals(ServiceStatus.ACTIVE)) {
                logger.debug("Service is Active. Checking Index.");

                //set it to inactive and check indexes
                status = ServiceStatus.INACTIVE;

                for (String indexSvcUrl : indexServiceUrls) {
                    Set<String> dynamicSvcUrls = null;
                    try {
                        dynamicSvcUrls = getDynamicServiceUrlProvider().getUrls(
                                indexSvcUrl);

                        for (String svcUrl : dynamicSvcUrls) {
                            if (svcUrl.equals(serviceUrl)) {
                                status = ServiceStatus.ACTIVE;
                                break;
                            }
                        }
                    } catch (Exception ex) {
                        //catch and log exception. Status returned will be ACTIVE
                        logger.warn("Error retrieving dynamic service url." +
                                " Will not check index for service status for service "
                                + serviceUrl);
                        logger.error(ex);

                    }
                }
                if (status.equals(ServiceStatus.INACTIVE))
                    logger.info("Active service is not in the index any more. Marking " + serviceUrl + " inactive");
            }
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
