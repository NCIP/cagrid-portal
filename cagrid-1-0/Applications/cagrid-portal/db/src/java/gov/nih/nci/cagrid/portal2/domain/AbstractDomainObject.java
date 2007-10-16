/**
 * 
 */
package gov.nih.nci.cagrid.portal2.domain;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
@MappedSuperclass
public class AbstractDomainObject implements DomainObject {

	private Integer id;

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nih.nci.cagrid.portal2.domain.DomainObject#getId()
	 */
	@Id
	@GeneratedValue(generator = "id-generator")
	public Integer getId() {
		return this.id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nih.nci.cagrid.portal2.domain.DomainObject#setId(java.lang.Integer)
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	public int hashCode() {
		return toString().hashCode();
	}

	public boolean equals(Object obj) {
		boolean eq = false;
		if(obj != null){
			if(obj.getClass().getName().equals(getClass().getName())){
				eq = ((AbstractDomainObject)obj).hashCode() == hashCode();
			}
		}
		return eq;
	}

	public String toString() {
		return getClass().getName() + ":" + getId();
	}

}
