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
package gov.nih.nci.cagrid.portal.domain.metadata.common;

import gov.nih.nci.cagrid.portal.domain.AbstractDomainObject;
import gov.nih.nci.cagrid.portal.domain.Person;
import gov.nih.nci.cagrid.portal.domain.catalog.PointOfContactCatalogEntry;
import org.hibernate.annotations.ForceDiscriminator;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 */
@Entity
@Table(name = "pocs")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = {@Parameter(name = "sequence", value = "seq_pocs")})
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "poc_type", discriminatorType = DiscriminatorType.STRING)
@ForceDiscriminator
public abstract class PointOfContact extends AbstractDomainObject {

    private String affiliation;

    private String role;

    private Person person;

    private PointOfContactCatalogEntry catalog;


    public String getAffiliation() {
        return affiliation;
    }

    public void setAffiliation(String affiliation) {
        this.affiliation = affiliation;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @ManyToOne
    @JoinColumn(name = "person_id")
    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    @OneToOne(mappedBy = "about", cascade = CascadeType.REMOVE)
    public PointOfContactCatalogEntry getCatalog() {
        return catalog;
    }

    public void setCatalog(PointOfContactCatalogEntry catalog) {
        this.catalog = catalog;
    }
}
