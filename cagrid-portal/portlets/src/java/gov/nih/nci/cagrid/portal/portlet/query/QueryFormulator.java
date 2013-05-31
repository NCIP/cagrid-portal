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
package gov.nih.nci.cagrid.portal.portlet.query;

import gov.nih.nci.cagrid.portal.portlet.query.cql.CriteriaBean;
import gov.nih.nci.cagrid.portal.portlet.query.cql.CQLQueryBean;

import javax.xml.namespace.QName;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public interface QueryFormulator<T> {

    public T toQuery(CQLQueryBean bean);

    public QName getQName();
}
