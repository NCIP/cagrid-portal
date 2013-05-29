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
package gov.nih.nci.cagrid.portal.domain.metadata.common;

import gov.nih.nci.cagrid.portal.domain.AbstractDomainObject;
import gov.nih.nci.cagrid.portal.domain.SemanticMetadataMapping;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 */
@Entity
@Table(name = "sem_meta")
@GenericGenerator(name = "id-generator", strategy = "native",
        parameters = {
        @Parameter(name = "sequence", value = "seq_sem_meta")
                }
)
public class SemanticMetadata extends AbstractDomainObject {

    private Integer oder;
    private String conceptName;
    private String conceptDefinition;
    private Integer orderLevel;
    private String conceptCode;
    private SemanticMetadataMapping semanticMetadataMapping;

    @Column(name = "sm_ccode")
    public String getConceptCode() {
        return conceptCode;
    }

    public void setConceptCode(String conceptCode) {
        this.conceptCode = conceptCode;
    }

    @Column(name = "sm_cdef", length = 4000)
    public String getConceptDefinition() {
        return conceptDefinition;
    }

    public void setConceptDefinition(String conceptDefinition) {
        this.conceptDefinition = conceptDefinition;
    }

    @Column(name = "sm_cname")
    public String getConceptName() {
        return conceptName;
    }

    public void setConceptName(String conceptName) {
        this.conceptName = conceptName;
    }

    @Column(name = "sm_order")
    public Integer getOder() {
        return oder;
    }

    public void setOder(Integer oder) {
        this.oder = oder;
    }

    @Column(name = "sm_order_level")
    public Integer getOrderLevel() {
        return orderLevel;
    }

    public void setOrderLevel(Integer orderLevel) {
        this.orderLevel = orderLevel;
    }


    @OneToOne(cascade = CascadeType.ALL, mappedBy = "semanticMetadata")
    public SemanticMetadataMapping getSemanticMetadataMapping() {
        return semanticMetadataMapping;
    }

    public void setSemanticMetadataMapping(SemanticMetadataMapping semanticMetadataMapping) {
        this.semanticMetadataMapping = semanticMetadataMapping;
    }
}
