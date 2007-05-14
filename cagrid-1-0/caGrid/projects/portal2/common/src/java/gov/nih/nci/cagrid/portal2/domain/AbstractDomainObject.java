/**
 * 
 */
package gov.nih.nci.cagrid.portal2.domain;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
@MappedSuperclass
public class AbstractDomainObject implements DomainObject {
	
	private Integer id;

	/* (non-Javadoc)
	 * @see gov.nih.nci.cagrid.portal2.domain.DomainObject#getId()
	 */
	@Id @GeneratedValue(generator = "id-generator")
	public Integer getId() {
		return this.id;
	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.cagrid.portal2.domain.DomainObject#setId(java.lang.Integer)
	 */
	public void setId(Integer id) {
		this.id = id;
	}

}
