package gov.nih.nci.cagrid.portal.portlet.query;

import gov.nih.nci.cagrid.portal.domain.dataservice.QueryInstance;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public interface QueryInstanceExecutor<T extends QueryInstance> {

    void setQueryInstance(T instance);

    T getQueryInstance();

    void start();

    boolean cancel();

    boolean timeout();


}
