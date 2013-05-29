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
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("LOVCriterion")

public class LOVCriterionDescriptor extends CriterionDescriptor {

    public List<LOVCriterionValues> values = new ArrayList<LOVCriterionValues>();

    @OneToMany(mappedBy="criterion")
    public List<LOVCriterionValues> getValues() {
        return values;
    }

    public void setValues(List<LOVCriterionValues> values) {
        this.values = values;
    }
}