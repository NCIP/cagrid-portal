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
/**
 *
 */
package gov.nih.nci.cagrid.portal.domain.metadata.dataservice;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.FetchType;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 */
@Entity
@DiscriminatorValue("SourceUMLAssociationEdge")
public class SourceUMLAssociationEdge extends UMLAssociationEdge {

    private UMLAssociation association;

    /**
     *
     */
    public SourceUMLAssociationEdge() {

    }

    @OneToOne(mappedBy = "source", fetch = FetchType.LAZY, optional = true, cascade = CascadeType.ALL)
    public UMLAssociation getAssociation() {
        return association;
    }

    public void setAssociation(UMLAssociation association) {
        this.association = association;
    }

}
