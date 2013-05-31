/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-portal/LICENSE.txt for details.
*============================================================================
**/
package gov.nih.nci.cagrid.portal.util;

import gov.nih.nci.cagrid.portal.aggr.ServiceUrlProvider;
import gov.nih.nci.cagrid.portal.aggr.status.ServiceStatusProvider;
import gov.nih.nci.cagrid.portal.dao.GridServiceDao;
import gov.nih.nci.cagrid.portal.domain.GridService;
import gov.nih.nci.cagrid.portal.domain.ServiceStatus;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Investigates service status in the Portal and in the index
 * Will provide the latest service status
 * <p/>
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class ServiceStatusInfo {

    public static void main(String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext(
                new String[]{
                        "classpath:applicationContext-db.xml",
                        "classpath:applicationContext-aggr-status-beans.xml"});


        String serviceUrl = args[0].trim();
        String indexUrl = args[1].trim();


        try {
            ServiceStatusProvider dbServiceStatusProvider = (ServiceStatusProvider) ctx.getBean("cachedServiceStatusProvider");
            System.out.println("Service is currently marked as " + dbServiceStatusProvider.getStatus(serviceUrl) + " in the Portal");

            GridServiceDao gridServiceDao = (GridServiceDao) ctx.getBean("gridServiceDao");
            GridService gService = gridServiceDao.getByUrl(serviceUrl);
            if (gService != null && gService.getServiceMetadata().getServiceDescription() == null) {
                System.out.println("Service metadata is null. Service will not show up in the Portal");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("PROBLEM: Couldn't find service " + serviceUrl + " in the Portal");

        }

        ServiceStatusProvider dynamicServiceStatusProvider = (ServiceStatusProvider) ctx.getBean("pingServiceStatusProvider");
        ServiceStatus _status = dynamicServiceStatusProvider.getStatus(serviceUrl);
        System.out.println("Dynamic Status is " + _status);

//        if(_status.equals(ServiceStatus.ACTIVE)){
        System.out.println("Will try and query metadata");
        try {
            MetadataUtils metadataUtils = new MetadataUtils();
            metadataUtils.getMetadata(serviceUrl, 20000);
            System.out.println("Metadata retreived Sucessfully");
        } catch (Exception e) {
            System.out.println("PROBLEM: Couldn't get Metadata" + e.getMessage());
        }

//        }

        System.out.println("Will look for service in the caGrid Index Service");
        ServiceUrlProvider serviceUrlProvider = (ServiceUrlProvider) ctx.getBean("dynamicServiceUrlProvider");
        if (!serviceUrlProvider.getUrls(indexUrl).contains(serviceUrl))
            System.out.println("PROBLEM: Service not found in index.");
        else
            System.out.println("Service found in the Index");
    }


}
