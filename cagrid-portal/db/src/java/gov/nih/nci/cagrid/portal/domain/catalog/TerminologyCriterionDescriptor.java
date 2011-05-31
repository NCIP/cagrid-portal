package gov.nih.nci.cagrid.portal.domain.catalog;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("terminology")
public class TerminologyCriterionDescriptor extends CriterionDescriptor {

    
}