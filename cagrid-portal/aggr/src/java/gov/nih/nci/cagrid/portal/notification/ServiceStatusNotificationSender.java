/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-core/LICENSE.txt for details.
*============================================================================
**/
package gov.nih.nci.cagrid.portal.notification;

import gov.nih.nci.cagrid.portal.PortalSystemException;
import gov.nih.nci.cagrid.portal.aggr.status.ServiceStatusChangeEvent;
import gov.nih.nci.cagrid.portal.domain.NotificationSubscriber;

import java.util.HashMap;
import java.util.Map;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class ServiceStatusNotificationSender extends BaseNotificationSender<ServiceStatusChangeEvent> {

    private String portalUrl;

    public void sendMessage(NotificationSubscriber sub, ServiceStatusChangeEvent e) throws PortalSystemException {

        Map<String, Object> _map = new HashMap<String, Object>();
        _map.put("oldStatus", e.getOldStatus().toString());
        _map.put("newStatus", e.getNewStatus().toString());
        _map.put("serviceUrl", e.getServiceUrl());
        _map.put("portalUrl", portalUrl);

        scheduleSend(getNotificationGenerator().getMessage(sub, _map));
    }

    public String getPortalUrl() {
        return portalUrl;
    }

    public void setPortalUrl(String portalUrl) {
        this.portalUrl = portalUrl;
    }
}