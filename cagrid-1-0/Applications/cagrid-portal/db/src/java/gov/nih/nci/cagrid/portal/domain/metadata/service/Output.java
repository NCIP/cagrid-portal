/**
 * 
 */
package gov.nih.nci.cagrid.portal.domain.metadata.service;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
@Entity
@DiscriminatorValue("Output")
public class Output extends AbstractParameter {
	
	private Operation operation;
	
	@OneToOne
	public Operation getOperation() {
		return operation;
	}
	public void setOperation(Operation operation) {
		this.operation = operation;
	}
}
