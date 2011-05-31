package gov.nih.nci.cagrid.portal.domain.catalog;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "cat_relations_shared_query")
@GenericGenerator(name = "id-generator", strategy = "native",
 parameters = {
        @Parameter(name="sequence", value="seq_cat_relations_shared_query")
    })
public class SharedQueryToolsRelationship extends AbstractRelationship {

    public SharedQueryCatalogEntry sharedQuery;
    public ToolDeploymentCatalogEntry tool;

    @ManyToOne
    @JoinColumn(name = "sharedQuery_id")
    public SharedQueryCatalogEntry getSharedQuery() {
        return sharedQuery;
    }

    public void setSharedQuery(SharedQueryCatalogEntry sharedQuery) {
        this.sharedQuery = sharedQuery;
    }

    @ManyToOne
    @JoinColumn(name = "tool_id")
    public ToolDeploymentCatalogEntry getTool() {
        return tool;
    }

    public void setTool(ToolDeploymentCatalogEntry tool) {
        this.tool = tool;
    }
}