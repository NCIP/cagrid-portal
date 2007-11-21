/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.query.cql;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlquery.QueryModifier;
import gov.nih.nci.cagrid.data.DataServiceConstants;
import gov.nih.nci.cagrid.portal.domain.metadata.dataservice.UMLClass;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
@Transactional
public class CQLQueryBean extends CriteriaBean {
	
	private QueryModifierType modifierType;
	private List<String> selectedAttributes = new ArrayList<String>();

	/**
	 * 
	 */
	public CQLQueryBean() {
		
	}
	
	public CQLQueryBean(UMLClass umlClass){
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
	
	public CQLQuery toCQLQuery(){
		CQLQuery query = new CQLQuery();

		QueryModifier modifier = null;
		if(QueryModifierType.COUNT_ONLY.equals(getModifierType())){
			modifier = new QueryModifier(null, true, null);
		}else if(QueryModifierType.DISTINCT_ATTRIBUTE.equals(getModifierType())){
			modifier = new QueryModifier(null, false, getSelectedAttributes().get(0));
		}else if(QueryModifierType.SELECTED_ATTRIBUTES.equals(getModifierType())){
			String[] atts = new String[getSelectedAttributes().size()];
			for(int i = 0; i < atts.length; i++){
				atts[i] = getSelectedAttributes().get(i);
			}
			modifier = new QueryModifier(atts, false, null);
		}else{
			//No modifier
		}
		if(modifier != null){
			query.setQueryModifier(modifier);
		}
		
		query.setTarget(toTarget());
		
		return query;
	}

	public String toXml() throws Exception {
		StringWriter sw = new StringWriter();
		Utils.serializeObject(toCQLQuery(), DataServiceConstants.CQL_QUERY_QNAME, sw);
		return sw.getBuffer().toString();
	}

}
