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
/**
 * 
 */
package model2.domain;

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nih.nci.cagrid.portal.domain.DomainObject#getId()
	 */
	@Id
	@GeneratedValue(generator = "id-generator")
	public Integer getId() {
		return this.id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nih.nci.cagrid.portal.domain.DomainObject#setId(java.lang.Integer)
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
