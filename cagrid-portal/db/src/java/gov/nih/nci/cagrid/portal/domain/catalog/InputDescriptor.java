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

import gov.nih.nci.cagrid.portal.domain.AbstractDomainObject;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cat_input_desc")
@GenericGenerator(name = "id-generator", strategy = "native",
 parameters = {
        @Parameter(name="sequence", value="seq_cat_input_desc")
    })
@DiscriminatorColumn(
        name = "input_desc_type",
        discriminatorType = DiscriminatorType.STRING
)
@DiscriminatorValue("InputDescriptor")

public class InputDescriptor extends AbstractDomainObject {

    public String label;

    public String description;

    public int orderLevel;
    
    private List<Comment> comments = new ArrayList<Comment>();


    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getOrderLevel() {
        return orderLevel;
    }

    public void setOrderLevel(int orderLevel) {
        this.orderLevel = orderLevel;
    }


    @OneToMany(cascade = CascadeType.ALL)
    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}