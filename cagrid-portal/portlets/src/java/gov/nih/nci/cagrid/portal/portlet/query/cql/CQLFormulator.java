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
package gov.nih.nci.cagrid.portal.portlet.query.cql;

import gov.nih.nci.cagrid.portal.portlet.query.QueryFormulator;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlquery.QueryModifier;
import gov.nih.nci.cagrid.fqp.common.DCQLConstants;
import gov.nih.nci.cagrid.data.DataServiceConstants;

import javax.xml.namespace.QName;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class CQLFormulator implements QueryFormulator<CQLQuery> {

    public static final QName qname = DataServiceConstants.CQL_QUERY_QNAME;

    public QName getQName() {
        return qname;
    }

    public CQLQuery toQuery(CQLQueryBean bean) {
        CQLQuery query = new CQLQuery();

        QueryModifier modifier = null;
        if (QueryModifierType.COUNT_ONLY.equals(bean.getModifierType())) {
            modifier = new QueryModifier(null, true, null);
        } else if (QueryModifierType.DISTINCT_ATTRIBUTE.equals(bean.getModifierType())) {
            modifier = new QueryModifier(null, false, bean.getSelectedAttributes().get(0));
        } else if (QueryModifierType.SELECTED_ATTRIBUTES.equals(bean.getModifierType())) {
            String[] atts = new String[bean.getSelectedAttributes().size()];
            for (int i = 0; i < atts.length; i++) {
                atts[i] = bean.getSelectedAttributes().get(i);
            }
            modifier = new QueryModifier(atts, false, null);
        } else {
            //No modifier
        }
        if (modifier != null) {
            query.setQueryModifier(modifier);
        }

        query.setTarget(bean.toTarget());

        return query;
    }
}
