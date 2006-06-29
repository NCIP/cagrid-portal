package gov.nih.nci.cagrid.portal.aggregator;

import gov.nih.nci.cagrid.discovery.client.DiscoveryClient;
import org.apache.axis.message.addressing.EndpointReferenceType;

/**
 * This class is Runnable thread that will retreive the
 * list of services from a given index service.
 * <p/>
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Jun 29, 2006
 * Time: 4:52:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class IndexAggregator implements Runnable {

    private DiscoveryClient discClient;
    private boolean metadataCompliance;

    /**
     * Initialize the runnable class with the
     * index service to aggregate from
     */
    public IndexAggregator(EndpointReferenceType indexServiceEPR, boolean metadataCompliance) {
        this.discClient = new DiscoveryClient(indexServiceEPR);
        this.metadataCompliance = metadataCompliance;
    }

    public void run() {

        try {
            EndpointReferenceType[] services = discClient.getAllServices(metadataCompliance);

        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


    }


}
