/**
 * 
 */
package gov.nih.nci.cagrid.portal2.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
@Entity
@DiscriminatorValue("GridDataService")
public class GridDataService extends GridService {

	private Long dataModelId;

	public Long getDataModelId() {
		return dataModelId;
	}

	public void setDataModelId(Long dataModelId) {
		this.dataModelId = dataModelId;
	}
	
}
