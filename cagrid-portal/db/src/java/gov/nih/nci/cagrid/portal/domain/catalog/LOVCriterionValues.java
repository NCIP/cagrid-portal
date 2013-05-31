/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-portal/LICENSE.txt for details.
*============================================================================
**/
package gov.nih.nci.cagrid.portal.domain.catalog;

import gov.nih.nci.cagrid.portal.domain.AbstractDomainObject;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "cat_lov_crit_values")
@GenericGenerator(name = "id-generator",strategy = "native",
 parameters = {
        @Parameter(name="sequence", value="seq_cat_lov_crit_values")
    })
public class LOVCriterionValues extends AbstractDomainObject {

    public String value;

    public String label;

    public String help;

    public LOVCriterionDescriptor criterion;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getHelp() {
        return help;
    }

    public void setHelp(String help) {
        this.help = help;
    }


    @ManyToOne
    @JoinColumn(name="descriptor_id")
    public LOVCriterionDescriptor getCriterion() {
        return criterion;
    }

    public void setCriterion(LOVCriterionDescriptor criterion) {
        this.criterion = criterion;
    }
}