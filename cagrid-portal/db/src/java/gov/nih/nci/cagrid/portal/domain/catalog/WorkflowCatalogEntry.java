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

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("tool_workflow")

public class WorkflowCatalogEntry extends ToolCatalogEntry {

    public List<OutputDescriptor> outputs = new ArrayList<OutputDescriptor>();

    @OneToMany(cascade=CascadeType.ALL)
    @JoinColumn(name="output_id")
    public List<OutputDescriptor> getOutputs() {
        return outputs;
    }

    public void setOutputs(List<OutputDescriptor> outputs) {
        this.outputs = outputs;
    }
}