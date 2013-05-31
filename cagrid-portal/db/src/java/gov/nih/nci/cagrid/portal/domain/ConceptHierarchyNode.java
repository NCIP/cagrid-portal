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
/**
 *
 */
package gov.nih.nci.cagrid.portal.domain;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 */
@Entity
@Table(name = "c_hier_node")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = {
    @Parameter(name = "sequence", value = "seq_c_hier_node")})
public class ConceptHierarchyNode extends AbstractDomainObject {

    private String code;
    private String name;
    private String description;
    private ConceptHierarchy hierarchy;
    private ConceptHierarchyNode parent;
    private List<ConceptHierarchyNode> children = new ArrayList<ConceptHierarchyNode>();
    private List<ConceptHierarchyNode> ancestors = new ArrayList<ConceptHierarchyNode>();
    private List<ConceptHierarchyNode> descendants = new ArrayList<ConceptHierarchyNode>();
    private int level;
    private List<SemanticMetadataMapping> semanticMetadataMappings = new ArrayList<SemanticMetadataMapping>();

    /**
     *
     */
    public ConceptHierarchyNode() {

    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(length = 4000)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @ManyToOne
    @JoinColumn(name = "parent_id")
    public ConceptHierarchyNode getParent() {
        return parent;
    }

    public void setParent(ConceptHierarchyNode parent) {
        this.parent = parent;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "parent")
    public List<ConceptHierarchyNode> getChildren() {
        return children;
    }

    public void setChildren(List<ConceptHierarchyNode> children) {
        this.children = children;
    }


    @ManyToMany
    @JoinTable(
            name = "c_hier_desc",
            joinColumns =
            @JoinColumn(name = "descendant_id"),
            inverseJoinColumns =
            @JoinColumn(name = "ancestor_id"),
            uniqueConstraints =
            @UniqueConstraint(columnNames =
                    {"ancestor_id", "descendant_id"}))
    @OrderBy("level desc")
    public List<ConceptHierarchyNode> getAncestors() {
        return ancestors;
    }


    public void setAncestors(List<ConceptHierarchyNode> ancestors) {
        this.ancestors = ancestors;
    }

    @ManyToMany(
            cascade = CascadeType.ALL, mappedBy = "ancestors"
    )
    public List<ConceptHierarchyNode> getDescendants() {
        return descendants;
    }

    public void setDescendants(List<ConceptHierarchyNode> descendants) {
        this.descendants = descendants;
    }

    @ManyToOne
    @JoinColumn(name = "hierarchy_id")
    public ConceptHierarchy getHierarchy() {
        return hierarchy;
    }

    public void setHierarchy(ConceptHierarchy hierarchy) {
        this.hierarchy = hierarchy;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @OneToMany(mappedBy = "concept")
    public List<SemanticMetadataMapping> getSemanticMetadataMappings() {
        return semanticMetadataMappings;
    }

    public void setSemanticMetadataMappings(
            List<SemanticMetadataMapping> semanticMetadataMappings) {
        this.semanticMetadataMappings = semanticMetadataMappings;
    }

}
