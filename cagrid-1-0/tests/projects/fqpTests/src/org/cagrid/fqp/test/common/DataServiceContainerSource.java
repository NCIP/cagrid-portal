package org.cagrid.fqp.test.common;

import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainer;

/** 
 *  DataServiceContainerSource
 *  Interface hides the actual means of getting a ServiceContainer
 * 
 * @author David Ervin
 * 
 * @created Jul 10, 2008 11:01:38 AM
 * @version $Id: DataServiceContainerSource.java,v 1.1 2008-07-10 15:05:37 dervin Exp $ 
 */
public interface DataServiceContainerSource {

    public ServiceContainer getDataServiceContainer();
}
