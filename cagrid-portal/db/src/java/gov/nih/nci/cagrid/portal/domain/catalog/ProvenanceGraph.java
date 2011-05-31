package gov.nih.nci.cagrid.portal.domain.catalog;

import gov.nih.nci.cagrid.portal.domain.AbstractDomainObject;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cat_provenance_graph")
@GenericGenerator(name = "id-generator", strategy = "native",
 parameters = {
        @Parameter(name="sequence", value="seq_cat_provenance_graph")
    })
public class ProvenanceGraph extends AbstractDomainObject implements Commentable {

    public DataSetCatalogEntry dataSet;
    private List<Comment> comments = new ArrayList<Comment>();

    @OneToOne(mappedBy = "provenance")
    public DataSetCatalogEntry getDataSet() {
        return dataSet;
    }

    public void setDataSet(DataSetCatalogEntry dataSet) {
        this.dataSet = dataSet;

    }


    @OneToMany(cascade = CascadeType.ALL)
    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}