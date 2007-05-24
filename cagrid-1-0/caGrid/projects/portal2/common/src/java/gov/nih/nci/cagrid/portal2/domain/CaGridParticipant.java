/**
 * 
 */
package gov.nih.nci.cagrid.portal2.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
@Entity
@DiscriminatorValue("CaGridParticipant")
public class CaGridParticipant extends Person {

}
