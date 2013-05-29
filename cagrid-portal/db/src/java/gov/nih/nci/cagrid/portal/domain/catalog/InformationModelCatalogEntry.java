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
package gov.nih.nci.cagrid.portal.domain.catalog;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.UniqueConstraint;

@Entity
@DiscriminatorValue("information_model")

public class InformationModelCatalogEntry extends DataSetCatalogEntry {
	
	private String projectLongName;

	public String getProjectLongName() {
		return projectLongName;
	}

	public void setProjectLongName(String projectLongName) {
		this.projectLongName = projectLongName;
	}
}