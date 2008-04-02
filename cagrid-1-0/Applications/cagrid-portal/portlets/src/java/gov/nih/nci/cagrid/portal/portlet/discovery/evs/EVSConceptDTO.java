package gov.nih.nci.cagrid.portal.portlet.discovery.evs;

import gov.nih.nci.evs.domain.DescLogicConcept;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class EVSConceptDTO implements Comparable{

    private String name;
    private String code;
    private String definitation;

   public static final int MAX_DEFINITION_LENGTH =200;

    /**
     * Constructor to create DTO with DescLogicConcept class (see caCORE API)
     * @param descConcept
     */
    public EVSConceptDTO(DescLogicConcept descConcept){
        setName(descConcept.getName());
        setCode(descConcept.getCode());
    }

    /**
     * Constructor to create DTO with ResolvedConceptReference (see LexBIG API)
     * @param conceptReference
     */
    public EVSConceptDTO(ResolvedConceptReference conceptReference){
        setCode(conceptReference.getConceptCode());
        setName(conceptReference.getEntityDescription().getContent());
        if(conceptReference.getReferencedEntry().getDefinitionCount()>0)
        setDefinitation(conceptReference.getReferencedEntry().getDefinition(0).getText().getContent());
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
        if(definitation.length()> MAX_DEFINITION_LENGTH)
        definitation = definitation.substring(0, MAX_DEFINITION_LENGTH) + ".....";
        this.definitation = definitation;
    }

    public int compareTo(Object o) {
         return ((EVSConceptDTO)o).getName().compareTo(name);

    }
}
