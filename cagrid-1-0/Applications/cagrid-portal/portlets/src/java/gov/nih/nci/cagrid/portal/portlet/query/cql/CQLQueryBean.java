/**
 *
 */
package gov.nih.nci.cagrid.portal.portlet.query.cql;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.cqlquery.*;
import gov.nih.nci.cagrid.data.DataServiceConstants;
import gov.nih.nci.cagrid.portal.domain.metadata.dataservice.UMLClass;
import gov.nih.nci.cagrid.portal.portlet.query.QueryFormulator;
import gov.nih.nci.cagrid.portal.portlet.query.builder.ForeignTargetsProvider;
import gov.nih.nci.cagrid.fqp.common.DCQLConstants;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import javax.xml.namespace.QName;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 */
@Transactional
public class CQLQueryBean extends CriteriaBean {

    private QueryModifierType modifierType = QueryModifierType.COUNT_ONLY;
    private List<String> selectedAttributes = new ArrayList<String>();

    private Map<QName, QueryFormulator> formulators;

    /**
     *
     */
    public CQLQueryBean() {

    }

    public CQLQueryBean(UMLClass umlClass) {
        setUmlClass(umlClass);
    }

    public QueryModifierType getModifierType() {
        return modifierType;
    }

    public void setModifierType(QueryModifierType modifierType) {
        this.modifierType = modifierType;
    }

    public List<String> getSelectedAttributes() {
        return selectedAttributes;
    }

    public void setSelectedAttributes(List<String> selectedAttributes) {
        this.selectedAttributes = selectedAttributes;
    }

    public Map<QName, QueryFormulator> getFormulators() {
        return formulators;
    }

    public void setFormulators(Map<QName, QueryFormulator> formulators) {
        this.formulators = formulators;
    }

    public boolean isDCQLQuery() {
        for (AssociationBean assocBean : getAssociations()) {
            if (assocBean.getRoleName().startsWith(ForeignTargetsProvider.FOREIGN_TARGETS_CLASS_PREFIX)) {
                return true;
            }
        }
        return getAggregateTargets() != null && getAggregateTargets().getSelected().size() > 1;
    }


    public String toXml() throws Exception {
        StringWriter sw = new StringWriter();
        QueryFormulator queryFormulator;

        queryFormulator = isDCQLQuery() ? formulators.get(DCQLConstants.DCQL_QUERY_QNAME) : formulators.get(DataServiceConstants.CQL_QUERY_QNAME);
        Utils.serializeObject(queryFormulator.toQuery(this), queryFormulator.getQName(), sw);
        return sw.getBuffer().toString();
    }

}
