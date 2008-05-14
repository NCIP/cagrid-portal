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
