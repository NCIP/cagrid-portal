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

@Entity
@Table(name = "cat_tool_applications")
@GenericGenerator(name = "id-generator", strategy = "native",
 parameters = {
        @Parameter(name="sequence", value="seq_cat_tool_applications")
    })
@DiscriminatorColumn(
        name = "tool_type",
        discriminatorType = DiscriminatorType.STRING
)
public class ToolApplications extends AbstractDomainObject {

    public String description;

    public ToolCatalogEntry tool;
    public Term term;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @ManyToOne
    @JoinColumn(name="tool_catalog_id")
    public ToolCatalogEntry getTool() {
        return tool;
    }

    public void setTool(ToolCatalogEntry tool) {
        this.tool = tool;
    }

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="term_id")
    public Term getTerm() {
        return term;
    }

    public void setTerm(Term term) {
        this.term = term;
    }
}