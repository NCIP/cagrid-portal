/**
 *
 */
package gov.nih.nci.cagrid.portal.domain.metadata.common;

import gov.nih.nci.cagrid.portal.domain.AbstractDomainObject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

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
}
