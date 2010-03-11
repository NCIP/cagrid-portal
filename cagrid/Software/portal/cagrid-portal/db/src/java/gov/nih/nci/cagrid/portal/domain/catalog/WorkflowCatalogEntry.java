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