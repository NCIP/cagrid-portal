package gov.nih.nci.cagrid.portal.domain.catalog;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("tabular")

public class TabularOutputDescriptor extends OutputDescriptor {

    public List<OutputColumnDescriptor> columns = new ArrayList<OutputColumnDescriptor>();

    @OneToMany
    public List<OutputColumnDescriptor> getColumns() {
        return columns;
    }

    public void setColumns(List<OutputColumnDescriptor> columns) {
        this.columns = columns;
    }
}