/**
 * 
 */
package gov.nih.nci.cagrid.portal.domain.dataservice;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
@Entity
@DiscriminatorValue("CQLQuery")
public class CQLQuery extends Query {
	


	/**
	 * 
	 */
	public CQLQuery() {

	}

	

}
