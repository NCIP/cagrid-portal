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
