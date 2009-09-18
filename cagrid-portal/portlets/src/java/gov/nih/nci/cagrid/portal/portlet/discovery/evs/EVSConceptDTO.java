package gov.nih.nci.cagrid.portal.portlet.discovery.evs;

import gov.nih.nci.cagrid.portal.domain.ConceptHierarchyNode;
import gov.nih.nci.cagrid.portal.util.StringUtils;
import gov.nih.nci.evs.domain.DescLogicConcept;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class EVSConceptDTO implements Comparable {

    private String name;
    private String code;
    private String definitation;

    public static final int MAX_DEFINITION_LENGTH = 200;

    public EVSConceptDTO() {
    }

    /**
     * Constructor to create DTO with DescLogicConcept class (see caCORE API)
     *
     * @param descConcept DescLogicConcept
     */
    public EVSConceptDTO(DescLogicConcept descConcept) {
        setName(descConcept.getName());
        setCode(descConcept.getCode());
    }

    /**
     * Constructor to create DTO with ResolvedConceptReference (see LexBIG API)
     *
     * @param conceptReference ResolvedConceptReference
     */
    public EVSConceptDTO(ResolvedConceptReference conceptReference) {
        setCode(conceptReference.getConceptCode());
        setName(conceptReference.getEntityDescription().getContent());
        if (conceptReference.getReferencedEntry().getDefinitionCount() > 0)
            setDefinitation(conceptReference.getReferencedEntry().getDefinition(0).getText().getContent());
    }

    /**
     * Constructor to create DTO from Portal domain object ConceptHierarchyNode
     *
     * @param node ConceptHierarchyNode
     */
    public EVSConceptDTO(ConceptHierarchyNode node) {
        setCode(StringUtils.getEmptyIfNull(node.getCode()));
        setName(node.getName());
        setDefinitation(StringUtils.getEmptyIfNull(node.getDescription()));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDefinitation() {
        return definitation;
    }

    public void setDefinitation(String definitation) {
        if (definitation.length() > MAX_DEFINITION_LENGTH)
            definitation = definitation.substring(0, MAX_DEFINITION_LENGTH) + ".....";
        this.definitation = definitation;
    }

    /**
     * For Sorting
     *
     * @param o
     * @return
     */
    public int compareTo(Object o) {
        return ((EVSConceptDTO) o).getName().compareTo(name);

    }

    @Override
    public boolean equals(Object o) {
        return ((EVSConceptDTO) o).getCode().equals(getCode());
    }

    @Override
    public int hashCode() {
        return getCode().hashCode();
    }
}
