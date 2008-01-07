package gov.nih.nci.cagrid.sdkquery4.processor;

import gov.nih.nci.system.applicationservice.ApplicationService;
import gov.nih.nci.system.client.proxy.ProxyHelperImpl;

import org.apache.log4j.Logger;

/** 
 *  PojoProxyHelperImpl
 *  Proxy helper implementation to simply return POJOs instead
 *  of Spring backed proxy domain objects
 * 
 * @author David Ervin
 * 
 * @created Jan 7, 2008 2:59:54 PM
 * @version $Id: PojoProxyHelperImpl.java,v 1.1 2008-01-07 20:02:24 dervin Exp $ 
 */
public class PojoProxyHelperImpl extends ProxyHelperImpl {
    
    private static Logger LOG = Logger.getLogger(PojoProxyHelperImpl.class);

    public Object convertToProxy(ApplicationService as, Object obj) {
        LOG.debug(obj.getClass().getName() + " was NOT proxied");
        return obj;
    }
}
